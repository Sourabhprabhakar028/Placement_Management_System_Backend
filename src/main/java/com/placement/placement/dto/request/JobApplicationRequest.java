package com.placement.placement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class JobApplicationRequest {

    @NotBlank(message = "Status cannot be empty")
    private String status;

    @NotNull(message = "Applied date cannot be null")
    private LocalDate appliedDate;

    private String remarks;

    public JobApplicationRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}