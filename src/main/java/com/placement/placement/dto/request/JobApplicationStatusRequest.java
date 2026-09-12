package com.placement.placement.dto.request;

import jakarta.validation.constraints.NotBlank;

public class JobApplicationStatusRequest {

    @NotBlank(message = "Status cannot be empty")
    private String status;

    private String remarks;

    public JobApplicationStatusRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}