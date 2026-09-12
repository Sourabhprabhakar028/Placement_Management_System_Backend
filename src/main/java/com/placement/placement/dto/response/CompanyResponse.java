package com.placement.placement.dto.response;

public class CompanyResponse {

    private Long id;
    private String name;
    private String location;
    private Double packageOffered;
    private Double minPercentage;
    private String eligibleBranches;

    public CompanyResponse() {}

    public CompanyResponse(Long id, String name, String location,
                           Double packageOffered) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.packageOffered = packageOffered;
    }

    // ✅ 6-arg constructor used by PlacementServiceImpl and ReportServiceImpl
    public CompanyResponse(Long id, String name, String location,
                           Double packageOffered, Double minPercentage,
                           String eligibleBranches) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.packageOffered = packageOffered;
        this.minPercentage = minPercentage;
        this.eligibleBranches = eligibleBranches;
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
}