package com.placement.placement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company name cannot be empty")
    private String name;

    @NotBlank(message = "Location cannot be empty")
    private String location;

    @NotNull(message = "Package cannot be null")
    @Min(value = 1, message = "Package must be greater than 0")
    private Double packageOffered;

    @Min(value = 0, message = "Minimum percentage must be >= 0")
    private Double minPercentage = 0.0;

    private String eligibleBranches; // e.g. "CSE,IT,ECE" or "ALL"

    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> jobApplications = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Placement> placements = new ArrayList<>();

    public Company() {}

    public Company(String name, String location, Double packageOffered) {
        this.name = name;
        this.location = location;
        this.packageOffered = packageOffered;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getPackageOffered() { return packageOffered; }
    public void setPackageOffered(Double packageOffered) { this.packageOffered = packageOffered; }

    public Double getMinPercentage() { return minPercentage; }
    public void setMinPercentage(Double minPercentage) { this.minPercentage = minPercentage; }

    public String getEligibleBranches() { return eligibleBranches; }
    public void setEligibleBranches(String eligibleBranches) { this.eligibleBranches = eligibleBranches; }

    public List<JobApplication> getJobApplications() { return jobApplications; }
    public void setJobApplications(List<JobApplication> jobApplications) { this.jobApplications = jobApplications; }

    public List<Placement> getPlacements() { return placements; }
    public void setPlacements(List<Placement> placements) { this.placements = placements; }
}
