package com.placement.placement.dto.request;

import jakarta.validation.constraints.*;

public class StudentRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Branch cannot be empty")
    private String branch;

    @NotNull(message = "Percentage is required")
    @Min(value = 0, message = "Percentage must be >= 0")
    @Max(value = 100, message = "Percentage must be <= 100")
    private Double percentage;

    public StudentRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
}