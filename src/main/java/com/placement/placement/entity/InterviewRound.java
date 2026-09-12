package com.placement.placement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "interview_rounds")
public class InterviewRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication application;

    @NotNull(message = "Round number cannot be null")
    private Integer roundNumber;

    @NotBlank(message = "Round type cannot be empty")
    private String roundType;

    @NotNull(message = "Interview date cannot be null")
    private LocalDate interviewDate;

    @NotNull(message = "Interview time cannot be null")
    private LocalTime interviewTime;

    private String venue;
    private String status;
    private String remarks;

    public InterviewRound() {}

    public InterviewRound(JobApplication application, Integer roundNumber,
                          String roundType, LocalDate interviewDate,
                          LocalTime interviewTime, String venue,
                          String status, String remarks) {
        this.application = application;
        this.roundNumber = roundNumber;
        this.roundType = roundType;
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
        this.venue = venue;
        this.status = status;
        this.remarks = remarks;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public JobApplication getApplication() { return application; }
    public void setApplication(JobApplication application) { this.application = application; }
    public Integer getRoundNumber() { return roundNumber; }
    public void setRoundNumber(Integer roundNumber) { this.roundNumber = roundNumber; }
    public String getRoundType() { return roundType; }
    public void setRoundType(String roundType) { this.roundType = roundType; }
    public LocalDate getInterviewDate() { return interviewDate; }
    public void setInterviewDate(LocalDate interviewDate) { this.interviewDate = interviewDate; }
    public LocalTime getInterviewTime() { return interviewTime; }
    public void setInterviewTime(LocalTime interviewTime) { this.interviewTime = interviewTime; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}