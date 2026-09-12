package com.placement.placement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class InterviewRoundRequest {

    @NotNull(message = "Round number cannot be null")
    private Integer roundNumber;

    @NotBlank(message = "Round type cannot be empty")
    private String roundType;

    @NotNull(message = "Interview date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate interviewDate;

    @NotNull(message = "Interview time cannot be null")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime interviewTime;

    private String venue;
    private String status = "SCHEDULED";
    private String remarks;

    public InterviewRoundRequest() {}

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