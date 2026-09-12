package com.placement.placement.service;

import com.placement.placement.dto.request.CompanyRequest;
import com.placement.placement.dto.response.CompanyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {
    CompanyResponse addCompany(CompanyRequest request);
    List<CompanyResponse> getAllCompanies();
    Page<CompanyResponse> getAllCompaniesPaginated(Pageable pageable);
    CompanyResponse getCompanyById(Long id);
    CompanyResponse updateCompany(Long id, CompanyRequest request);
    void deleteCompany(Long id);

    // ✅ NEW - Search + Filter
    List<CompanyResponse> searchByName(String name);
    List<CompanyResponse> filterCompanies(String location,
                                          Double minPackage,
                                          Double maxPackage);
}