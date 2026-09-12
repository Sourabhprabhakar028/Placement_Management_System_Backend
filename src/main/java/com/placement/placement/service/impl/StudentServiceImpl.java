package com.placement.placement.service.impl;

import com.placement.placement.dto.request.StudentRequest;
import com.placement.placement.dto.response.StudentResponse;
import com.placement.placement.entity.Company;
import com.placement.placement.entity.Student;
import com.placement.placement.exception.CompanyNotFoundException;
import com.placement.placement.exception.StudentNotFoundException;
import com.placement.placement.repository.CompanyRepository;
import com.placement.placement.repository.StudentRepository;
import com.placement.placement.service.EmailService;
import com.placement.placement.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final EmailService emailService;

    public StudentServiceImpl(StudentRepository studentRepository,
                              CompanyRepository companyRepository,
                              EmailService emailService) {
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.emailService = emailService;
    }

    @Override
    public StudentResponse saveStudent(StudentRequest request) {
        log.info("Creating student: {}", request.getEmail());

        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException(
                    "Student with email already exists: " + request.getEmail());
        }

        Student student = new Student(
                request.getName(),
                request.getEmail(),
                request.getBranch(),
                request.getPercentage()
        );

        Student saved = studentRepository.save(student);
        log.info("Student created with id: {}", saved.getId());

        // ✅ FIX — Send welcome email when student is created
        try {
            emailService.sendWelcomeEmail(saved.getEmail(), saved.getName());
            log.info("Welcome email sent to: {}", saved.getEmail());
        } catch (Exception e) {
            // Don't fail student creation if email fails
            log.warn("Failed to send welcome email to {}: {}", saved.getEmail(), e.getMessage());
        }

        return toResponse(saved);
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<StudentResponse> getAllStudentsPaginated(Pageable pageable) {
        log.info("Fetching students paginated");
        return studentRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        log.info("Fetching student by id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
        return toResponse(student);
    }

    @Override
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        log.info("Updating student id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        // Check duplicate email only if email is changing
        if (!student.getEmail().equalsIgnoreCase(request.getEmail()) &&
                studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException(
                    "Email already in use: " + request.getEmail());
        }

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setBranch(request.getBranch());
        student.setPercentage(request.getPercentage());

        Student updated = studentRepository.save(student);
        log.info("Student updated: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    public void deleteStudent(Long id) {
        log.info("Deleting student id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
        log.info("Student deleted: {}", id);
    }

    @Override
    public List<StudentResponse> getEligibleStudents(Long companyId) {
        log.info("Fetching eligible students for company: {}", companyId);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyId));

        Double minPercentage = company.getMinPercentage() != null
                ? company.getMinPercentage() : 0.0;
        String eligibleBranches = company.getEligibleBranches();

        List<Student> eligible;

        // If "ALL" or null → only filter by percentage
        if (eligibleBranches == null
                || eligibleBranches.isBlank()
                || eligibleBranches.equalsIgnoreCase("ALL")) {
            eligible = studentRepository.findEligibleByPercentage(minPercentage);
        } else {
            // Parse comma-separated branches
            List<String> branches = Arrays.stream(eligibleBranches.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            eligible = studentRepository
                    .findEligibleByPercentageAndBranch(minPercentage, branches);
        }

        log.info("Found {} eligible students for company: {}", eligible.size(), companyId);
        return eligible.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<StudentResponse> searchByName(String name) {
        log.info("Searching students by name: {}", name);
        return studentRepository.searchByName(name)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponse> filterStudents(String branch,
                                                Double minPercentage,
                                                Double maxPercentage) {
        log.info("Filtering students - branch: {}, min: {}, max: {}",
                branch, minPercentage, maxPercentage);
        return studentRepository.filterStudents(branch, minPercentage, maxPercentage)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Helper — entity to response
    private StudentResponse toResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setName(student.getName());
        response.setEmail(student.getEmail());
        response.setBranch(student.getBranch());
        response.setPercentage(student.getPercentage());
        response.setResumePath(student.getResumePath());
        response.setProfilePicturePath(student.getProfilePicturePath());
        return response;
    }
}