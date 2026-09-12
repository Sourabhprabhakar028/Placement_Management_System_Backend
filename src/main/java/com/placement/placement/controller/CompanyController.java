package com.placement.placement.controller;

import com.placement.placement.dto.request.CompanyRequest;
import com.placement.placement.dto.response.CompanyResponse;
import com.placement.placement.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> addCompany(
            @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyService.addCompany(request));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<CompanyResponse>> getAllCompaniesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(
                companyService.getAllCompaniesPaginated(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(
            @PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.updateCompany(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok("Company deleted successfully");
    }

    // ✅ Search by name
    @GetMapping("/search")
    public ResponseEntity<List<CompanyResponse>> searchCompanies(
            @RequestParam String name) {
        return ResponseEntity.ok(companyService.searchByName(name));
    }

    // ✅ Filter companies
    @GetMapping("/filter")
    public ResponseEntity<List<CompanyResponse>> filterCompanies(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPackage,
            @RequestParam(required = false) Double maxPackage) {
        return ResponseEntity.ok(
                companyService.filterCompanies(
                        location, minPackage, maxPackage));
    }
}
