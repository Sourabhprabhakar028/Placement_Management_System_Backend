package com.placement.placement.service.impl;

import com.placement.placement.dto.request.InterviewRoundRequest;
import com.placement.placement.dto.response.InterviewRoundResponse;
import com.placement.placement.entity.InterviewRound;
import com.placement.placement.entity.JobApplication;
import com.placement.placement.exception.ResourceNotFoundException;
import com.placement.placement.repository.InterviewRoundRepository;
import com.placement.placement.repository.JobApplicationRepository;
import com.placement.placement.service.EmailService;
import com.placement.placement.service.InterviewRoundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewRoundServiceImpl implements InterviewRoundService {

    private static final Logger log = LoggerFactory.getLogger(InterviewRoundServiceImpl.class);

    private final InterviewRoundRepository interviewRoundRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final EmailService emailService;

    public InterviewRoundServiceImpl(InterviewRoundRepository interviewRoundRepository,
                                     JobApplicationRepository jobApplicationRepository,
                                     EmailService emailService) {
        this.interviewRoundRepository = interviewRoundRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.emailService = emailService;
    }

    @Override
    public InterviewRoundResponse scheduleRound(Long applicationId,
                                                InterviewRoundRequest request) {
        log.info("Scheduling round {} for application {}", request.getRoundNumber(), applicationId);

        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "JobApplication not found with id: " + applicationId));

        InterviewRound round = new InterviewRound(
                application,
                request.getRoundNumber(),
                request.getRoundType(),
                request.getInterviewDate(),
                request.getInterviewTime(),
                request.getVenue(),
                request.getStatus() != null ? request.getStatus() : "SCHEDULED",
                request.getRemarks()
        );

        InterviewRound saved = interviewRoundRepository.save(round);
        log.info("Interview round scheduled with id: {}", saved.getId());

        // ✅ Send interview scheduled email with all required fields
        try {
            String studentEmail = application.getStudent().getEmail();
            String studentName  = application.getStudent().getName();
            String companyName  = application.getCompany().getName();
            // Convert date/time/venue to String safely
            String date  = request.getInterviewDate() != null
                    ? request.getInterviewDate().toString() : "TBD";
            String time  = request.getInterviewTime() != null
                    ? request.getInterviewTime().toString() : "TBD";
            String venue = request.getVenue() != null
                    ? request.getVenue() : "TBD";

            emailService.sendInterviewEmail(
                    studentEmail, studentName, companyName, date, time, venue);
            log.info("Interview notification email sent to: {}", studentEmail);
        } catch (Exception e) {
            log.warn("Failed to send interview email: {}", e.getMessage());
        }

        return toResponse(saved);
    }

    @Override
    public List<InterviewRoundResponse> getAllRounds() {
        return interviewRoundRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public InterviewRoundResponse getRoundById(Long id) {
        InterviewRound round = interviewRoundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InterviewRound not found with id: " + id));
        return toResponse(round);
    }

    @Override
    public List<InterviewRoundResponse> getRoundsByApplication(Long applicationId) {
        return interviewRoundRepository.findByApplicationId(applicationId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<InterviewRoundResponse> getRoundsByStatus(String status) {
        return interviewRoundRepository.findByStatus(status)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public InterviewRoundResponse updateRound(Long id, InterviewRoundRequest request) {
        log.info("Updating round id: {}", id);
        InterviewRound round = interviewRoundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InterviewRound not found with id: " + id));

        round.setRoundNumber(request.getRoundNumber());
        round.setRoundType(request.getRoundType());
        round.setInterviewDate(request.getInterviewDate());
        round.setInterviewTime(request.getInterviewTime());
        round.setVenue(request.getVenue());
        if (request.getStatus() != null) round.setStatus(request.getStatus());
        if (request.getRemarks() != null) round.setRemarks(request.getRemarks());

        InterviewRound updated = interviewRoundRepository.save(round);
        log.info("Interview round updated: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    public void deleteRound(Long id) {
        log.info("Deleting round id: {}", id);
        InterviewRound round = interviewRoundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InterviewRound not found with id: " + id));
        interviewRoundRepository.delete(round);
        log.info("Interview round deleted: {}", id);
    }

    private InterviewRoundResponse toResponse(InterviewRound round) {
        InterviewRoundResponse response = new InterviewRoundResponse();
        response.setId(round.getId());
        response.setApplicationId(round.getApplication().getId());
        response.setStudentName(round.getApplication().getStudent().getName());
        response.setCompanyName(round.getApplication().getCompany().getName());
        response.setRoundNumber(round.getRoundNumber());
        response.setRoundType(round.getRoundType());
        response.setInterviewDate(round.getInterviewDate());
        response.setInterviewTime(round.getInterviewTime());
        response.setVenue(round.getVenue());
        response.setStatus(round.getStatus());
        response.setRemarks(round.getRemarks());
        return response;
    }
}