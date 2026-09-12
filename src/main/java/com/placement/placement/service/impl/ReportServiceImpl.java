package com.placement.placement.service.impl;

import com.placement.placement.dto.response.CompanyResponse;
import com.placement.placement.dto.response.ReportResponse;
import com.placement.placement.repository.CompanyRepository;
import com.placement.placement.repository.PlacementRepository;
import com.placement.placement.repository.StudentRepository;
import com.placement.placement.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final PlacementRepository placementRepository;

    public ReportServiceImpl(StudentRepository studentRepository,
                             CompanyRepository companyRepository,
                             PlacementRepository placementRepository) {
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.placementRepository = placementRepository;
    }

    @Override
    public ReportResponse getFullReport() {
        log.info("Generating full placement report");

        ReportResponse report = new ReportResponse();

        // ✅ Basic counts
        long totalStudents = studentRepository.count();
        long totalCompanies = companyRepository.count();
        long totalPlacements = placementRepository.count();

        report.setTotalStudents(totalStudents);
        report.setTotalCompanies(totalCompanies);
        report.setTotalPlacements(totalPlacements);

        // ✅ Placement percentage
        double placementPercentage = totalStudents > 0
                ? Math.round(((double) totalPlacements / totalStudents) * 100.0 * 10) / 10.0
                : 0.0;
        report.setPlacementPercentage(placementPercentage);

        // ✅ Average CTC
        Double avgCtc = placementRepository.findAverageCtc();
        report.setAverageCtc(avgCtc != null
                ? Math.round(avgCtc * 10.0) / 10.0
                : 0.0);

        // ✅ Branch wise students
        Map<String, Long> branchWiseStudents = new LinkedHashMap<>();
        studentRepository.countStudentsByBranch()
                .forEach(row -> branchWiseStudents.put(
                        (String) row[0], (Long) row[1]));
        report.setBranchWiseStudents(branchWiseStudents);

        // ✅ Branch wise placements
        Map<String, Long> branchWisePlacements = new LinkedHashMap<>();
        placementRepository.countPlacementsByBranch()
                .forEach(row -> branchWisePlacements.put(
                        (String) row[0], (Long) row[1]));
        report.setBranchWisePlacements(branchWisePlacements);

        // ✅ FIXED - Top 5 companies by package (6 params now)
        List<CompanyResponse> topCompanies = companyRepository
                .findTop5ByOrderByPackageOfferedDesc()
                .stream()
                .map(c -> new CompanyResponse(
                        c.getId(),
                        c.getName(),
                        c.getLocation(),
                        c.getPackageOffered(),
                        c.getMinPercentage(),       // ✅ NEW
                        c.getEligibleBranches()))   // ✅ NEW
                .collect(Collectors.toList());
        report.setTopCompaniesByPackage(topCompanies);

        // ✅ Monthly placements
        Map<String, Long> monthlyPlacements = new LinkedHashMap<>();
        placementRepository.countPlacementsByMonth()
                .forEach(row -> monthlyPlacements.put(
                        (String) row[0], (Long) row[1]));
        report.setMonthlyPlacements(monthlyPlacements);

        // ✅ Status wise placements
        Map<String, Long> placementsByStatus = new LinkedHashMap<>();
        placementRepository.countPlacementsByStatus()
                .forEach(row -> placementsByStatus.put(
                        (String) row[0], (Long) row[1]));
        report.setPlacementsByStatus(placementsByStatus);

        log.info("Report generated successfully");
        return report;
    }
}