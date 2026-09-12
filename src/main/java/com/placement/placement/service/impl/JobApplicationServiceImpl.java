package com.placement.placement.service.impl;

import com.placement.placement.dto.request.JobApplicationRequest;
import com.placement.placement.dto.request.JobApplicationStatusRequest;
import com.placement.placement.dto.response.JobApplicationResponse;
import com.placement.placement.entity.Company;
import com.placement.placement.entity.JobApplication;
import com.placement.placement.entity.Student;
import com.placement.placement.exception.CompanyNotFoundException;
import com.placement.placement.exception.ResourceNotFoundException;
import com.placement.placement.exception.StudentNotFoundException;
import com.placement.placement.repository.CompanyRepository;
import com.placement.placement.repository.JobApplicationRepository;
import com.placement.placement.repository.StudentRepository;
import com.placement.placement.service.EmailService;
import com.placement.placement.service.JobApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private static final Logger log = LoggerFactory.getLogger(JobApplicationServiceImpl.class);

    private final JobApplicationRepository jobApplicationRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final EmailService emailService;

    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository,
                                     StudentRepository studentRepository,
                                     CompanyRepository companyRepository,
                                     EmailService emailService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.emailService = emailService;
    }

    @Override
    public JobApplicationResponse apply(Long studentId, Long companyId,
                                        JobApplicationRequest request) {
        log.info("Student {} applying to company {}", studentId, companyId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyId));

        if (jobApplicationRepository.existsByStudentIdAndCompanyId(studentId, companyId)) {
            throw new IllegalStateException("Student has already applied to this company!");
        }

        JobApplication application = new JobApplication(
                student, company, "APPLIED", LocalDate.now(), request.getRemarks());

        JobApplication saved = jobApplicationRepository.save(application);
        log.info("Application saved with id: {}", saved.getId());

        // ✅ Send application confirmation email
        try {
            emailService.sendApplicationEmail(
                    student.getEmail(), student.getName(), company.getName());
            log.info("Application confirmation email sent to: {}", student.getEmail());
        } catch (Exception e) {
            log.warn("Failed to send application email: {}", e.getMessage());
        }

        return toResponse(saved);
    }

    @Override
    public List<JobApplicationResponse> getAllApplications() {
        return jobApplicationRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public JobApplicationResponse getApplicationById(Long id) {
        JobApplication app = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication not found with id: " + id));
        return toResponse(app);
    }

    @Override
    public List<JobApplicationResponse> getApplicationsByStudent(Long studentId) {
        return jobApplicationRepository.findByStudentId(studentId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<JobApplicationResponse> getApplicationsByCompany(Long companyId) {
        return jobApplicationRepository.findByCompanyId(companyId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<JobApplicationResponse> getApplicationsByStatus(String status) {
        return jobApplicationRepository.findByStatus(status).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public JobApplicationResponse updateStatus(Long id, JobApplicationStatusRequest request) {
        log.info("Updating status for application id: {}", id);

        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication not found with id: " + id));

        String newStatus = request.getStatus().toUpperCase();
        application.setStatus(newStatus);

        if (request.getRemarks() != null && !request.getRemarks().isBlank()) {
            application.setRemarks(request.getRemarks());
        }

        JobApplication updated = jobApplicationRepository.save(application);
        log.info("Application {} status updated to: {}", id, newStatus);

        // ✅ Send email on SELECTED or REJECTED
        String studentEmail = application.getStudent().getEmail();
        String studentName = application.getStudent().getName();
        String companyName = application.getCompany().getName();

        try {
            if ("SELECTED".equals(newStatus)) {
                emailService.sendSelectionEmail(studentEmail, studentName, companyName, null);
                log.info("Selection email sent to: {}", studentEmail);
            } else if ("REJECTED".equals(newStatus)) {
                emailService.sendRejectionEmail(studentEmail, studentName, companyName);
                log.info("Rejection email sent to: {}", studentEmail);
            }
        } catch (Exception e) {
            log.warn("Failed to send status change email: {}", e.getMessage());
        }

        return toResponse(updated);
    }

    @Override
    public void deleteApplication(Long id) {
        log.info("Deleting application id: {}", id);
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication not found with id: " + id));
        jobApplicationRepository.delete(application);
        log.info("Application deleted: {}", id);
    }

    private JobApplicationResponse toResponse(JobApplication app) {
        // Build nested StudentResponse
        com.placement.placement.dto.response.StudentResponse studentResponse =
                new com.placement.placement.dto.response.StudentResponse();
        studentResponse.setId(app.getStudent().getId());
        studentResponse.setName(app.getStudent().getName());
        studentResponse.setEmail(app.getStudent().getEmail());
        studentResponse.setBranch(app.getStudent().getBranch());
        studentResponse.setPercentage(app.getStudent().getPercentage());

        // Build nested CompanyResponse
        com.placement.placement.dto.response.CompanyResponse companyResponse =
                new com.placement.placement.dto.response.CompanyResponse();
        companyResponse.setId(app.getCompany().getId());
        companyResponse.setName(app.getCompany().getName());
        companyResponse.setLocation(app.getCompany().getLocation());
        companyResponse.setPackageOffered(app.getCompany().getPackageOffered());

        JobApplicationResponse response = new JobApplicationResponse();
        response.setId(app.getId());
        response.setStudent(studentResponse);
        response.setCompany(companyResponse);
        response.setStatus(app.getStatus());
        response.setAppliedDate(app.getAppliedDate());
        response.setRemarks(app.getRemarks());
        return response;
    }
}