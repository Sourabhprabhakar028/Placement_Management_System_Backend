package com.placement.placement.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class InterviewRoundResponse {

    private Long id;
    private Long applicationId;
    private String studentName;
    private String companyName;
    private Integer roundNumber;
    private String roundType;
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private String venue;
    private String status;
    private String remarks;

    public InterviewRoundResponse() {}

    public InterviewRoundResponse(Long id, Long applicationId,
                                  String studentName, String companyName,
                                  Integer roundNumber, String roundType,
                                  LocalDate interviewDate, LocalTime interviewTime,
                                  String venue, String status, String remarks) {
        this.id = id;
        this.applicationId = applicationId;
        this.studentName = studentName;
        this.companyName = companyName;
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

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

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