package com.placement.placement.dto.request;

import jakarta.validation.constraints.*;

public class CompanyRequest {

    @NotBlank(message = "Company name cannot be empty")
    private String name;

    @NotBlank(message = "Location cannot be empty")
    private String location;

    @NotNull(message = "Package cannot be null")
    @Min(value = 1, message = "Package must be greater than 0")
    private Double packageOffered;

    // ✅ NEW - Eligibility Fields
    @Min(value = 0, message = "Minimum percentage must be >= 0")
    private Double minPercentage = 0.0;

    private String eligibleBranches = "ALL"; // ALL or "CSE,IT,ECE"

    public CompanyRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getPackageOffered() { return packageOffered; }
    public void setPackageOffered(Double packageOffered) { this.packageOffered = packageOffered; }

    // ✅ NEW Getters Setters
    public Double getMinPercentage() { return minPercentage; }
    public void setMinPercentage(Double minPercentage) { this.minPercentage = minPercentage; }

    public String getEligibleBranches() { return eligibleBranches; }
    public void setEligibleBranches(String eligibleBranches) { this.eligibleBranches = eligibleBranches; }
}