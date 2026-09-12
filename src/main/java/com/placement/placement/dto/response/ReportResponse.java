package com.placement.placement.dto.response;

import java.util.List;
import java.util.Map;

public class ReportResponse {

    private long totalStudents;
    private long totalCompanies;
    private long totalPlacements;
    private double averageCtc;
    private double placementPercentage;
    private Map<String, Long> branchWiseStudents;
    private Map<String, Long> branchWisePlacements;
    private List<CompanyResponse> topCompaniesByPackage;
    private Map<String, Long> monthlyPlacements;
    private Map<String, Long> placementsByStatus;

    public ReportResponse() {}

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }
    public long getTotalCompanies() { return totalCompanies; }
    public void setTotalCompanies(long totalCompanies) { this.totalCompanies = totalCompanies; }
    public long getTotalPlacements() { return totalPlacements; }
    public void setTotalPlacements(long totalPlacements) { this.totalPlacements = totalPlacements; }
    public double getAverageCtc() { return averageCtc; }
    public void setAverageCtc(double averageCtc) { this.averageCtc = averageCtc; }
    public double getPlacementPercentage() { return placementPercentage; }
    public void setPlacementPercentage(double placementPercentage) { this.placementPercentage = placementPercentage; }
    public Map<String, Long> getBranchWiseStudents() { return branchWiseStudents; }
    public void setBranchWiseStudents(Map<String, Long> branchWiseStudents) { this.branchWiseStudents = branchWiseStudents; }
    public Map<String, Long> getBranchWisePlacements() { return branchWisePlacements; }
    public void setBranchWisePlacements(Map<String, Long> branchWisePlacements) { this.branchWisePlacements = branchWisePlacements; }
    public List<CompanyResponse> getTopCompaniesByPackage() { return topCompaniesByPackage; }
    public void setTopCompaniesByPackage(List<CompanyResponse> topCompaniesByPackage) { this.topCompaniesByPackage = topCompaniesByPackage; }
    public Map<String, Long> getMonthlyPlacements() { return monthlyPlacements; }
    public void setMonthlyPlacements(Map<String, Long> monthlyPlacements) { this.monthlyPlacements = monthlyPlacements; }
    public Map<String, Long> getPlacementsByStatus() { return placementsByStatus; }
    public void setPlacementsByStatus(Map<String, Long> placementsByStatus) { this.placementsByStatus = placementsByStatus; }
}