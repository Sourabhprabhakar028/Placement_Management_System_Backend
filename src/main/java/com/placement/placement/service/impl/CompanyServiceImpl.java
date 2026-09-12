package com.placement.placement.service.impl;

import com.placement.placement.dto.request.CompanyRequest;
import com.placement.placement.dto.response.CompanyResponse;
import com.placement.placement.entity.Company;
import com.placement.placement.exception.CompanyNotFoundException;
import com.placement.placement.repository.CompanyRepository;
import com.placement.placement.repository.JobApplicationRepository;
import com.placement.placement.repository.PlacementRepository;
import com.placement.placement.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final PlacementRepository placementRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository,
                              JobApplicationRepository jobApplicationRepository,
                              PlacementRepository placementRepository) {
        this.companyRepository = companyRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.placementRepository = placementRepository;
    }

    @Override
    public CompanyResponse addCompany(CompanyRequest request) {
        log.info("Creating company: {}", request.getName());

        if (companyRepository.existsByName(request.getName())) {
            throw new IllegalStateException(
                    "Company already exists: " + request.getName());
        }

        Company company = new Company(
                request.getName(),
                request.getLocation(),
                request.getPackageOffered()
        );
        company.setMinPercentage(
                request.getMinPercentage() != null ? request.getMinPercentage() : 0.0);
        company.setEligibleBranches(request.getEligibleBranches());

        Company saved = companyRepository.save(company);
        log.info("Company created with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public List<CompanyResponse> getAllCompanies() {
        log.info("Fetching all companies");
        return companyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CompanyResponse> getAllCompaniesPaginated(Pageable pageable) {
        log.info("Fetching companies paginated");
        return companyRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public CompanyResponse getCompanyById(Long id) {
        log.info("Fetching company by id: {}", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));
        return toResponse(company);
    }

    @Override
    public CompanyResponse updateCompany(Long id, CompanyRequest request) {
        log.info("Updating company id: {}", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));

        // Check duplicate name only if name is changing
        if (!company.getName().equalsIgnoreCase(request.getName()) &&
                companyRepository.existsByName(request.getName())) {
            throw new IllegalStateException(
                    "Company name already in use: " + request.getName());
        }

        company.setName(request.getName());
        company.setLocation(request.getLocation());
        company.setPackageOffered(request.getPackageOffered());
        if (request.getMinPercentage() != null) {
            company.setMinPercentage(request.getMinPercentage());
        }
        company.setEligibleBranches(request.getEligibleBranches());

        Company updated = companyRepository.save(company);
        log.info("Company updated: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    public void deleteCompany(Long id) {
        log.info("Deleting company id: {}", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));

        // ✅ FIX — Check for active applications before deleting!
        List<String> activeStatuses = Arrays.asList(
                "APPLIED", "SHORTLISTED", "INTERVIEW");

        boolean hasActiveApplications = jobApplicationRepository
                .findByCompanyId(id)
                .stream()
                .anyMatch(app -> activeStatuses.contains(app.getStatus()));

        if (hasActiveApplications) {
            throw new IllegalStateException(
                    "Cannot delete company '" + company.getName() +
                            "' — it has active job applications (APPLIED/SHORTLISTED/INTERVIEW). " +
                            "Please reject or close all applications first.");
        }

        companyRepository.delete(company);
        log.info("Company deleted: {}", id);
    }

    @Override
    public List<CompanyResponse> searchByName(String name) {
        log.info("Searching companies by name: {}", name);
        return companyRepository.searchByName(name)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyResponse> filterCompanies(String location,
                                                 Double minPackage,
                                                 Double maxPackage) {
        log.info("Filtering companies - location: {}, minPkg: {}, maxPkg: {}",
                location, minPackage, maxPackage);
        return companyRepository.filterCompanies(location, minPackage, maxPackage)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Helper — entity to response
    private CompanyResponse toResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        response.setId(company.getId());
        response.setName(company.getName());
        response.setLocation(company.getLocation());
        response.setPackageOffered(company.getPackageOffered());
        response.setMinPercentage(company.getMinPercentage());
        response.setEligibleBranches(company.getEligibleBranches());
        return response;
    }
}