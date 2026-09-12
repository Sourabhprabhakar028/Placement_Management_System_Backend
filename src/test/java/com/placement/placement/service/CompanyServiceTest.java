package com.placement.placement.service;

import com.placement.placement.dto.request.CompanyRequest;
import com.placement.placement.dto.response.CompanyResponse;
import com.placement.placement.entity.Company;
import com.placement.placement.exception.CompanyNotFoundException;
import com.placement.placement.repository.CompanyRepository;
import com.placement.placement.service.impl.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company;
    private CompanyRequest companyRequest;

    @BeforeEach
    void setUp() {
        company = new Company("TCS", "Pune", 7.5);
        company.setId(1L);

        // ✅ NEW - set eligibility fields
        company.setMinPercentage(60.0);
        company.setEligibleBranches("ALL");

        companyRequest = new CompanyRequest();
        companyRequest.setName("TCS");
        companyRequest.setLocation("Pune");
        companyRequest.setPackageOffered(7.5);
        companyRequest.setMinPercentage(60.0);
        companyRequest.setEligibleBranches("ALL");
    }

    @Test
    void addCompany_Success() {
        when(companyRepository.existsByName(companyRequest.getName()))
                .thenReturn(false);
        when(companyRepository.save(any(Company.class)))
                .thenReturn(company);

        CompanyResponse response = companyService.addCompany(companyRequest);

        assertNotNull(response);
        assertEquals("TCS", response.getName());
        assertEquals("Pune", response.getLocation());
        assertEquals(7.5, response.getPackageOffered());
        assertEquals(60.0, response.getMinPercentage());
        assertEquals("ALL", response.getEligibleBranches());
        verify(companyRepository, times(1)).save(any(Company.class));
        System.out.println("✅ addCompany_Success passed!");
    }

    @Test
    void addCompany_DuplicateName_ThrowsException() {
        when(companyRepository.existsByName(companyRequest.getName()))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> companyService.addCompany(companyRequest)
        );

        assertEquals("Company already exists: TCS", exception.getMessage());
        verify(companyRepository, never()).save(any(Company.class));
        System.out.println("✅ addCompany_DuplicateName passed!");
    }

    @Test
    void getAllCompanies_Success() {
        Company company2 = new Company("Google", "Bangalore", 25.0);
        company2.setId(2L);
        company2.setMinPercentage(70.0);
        company2.setEligibleBranches("CSE,IT");

        when(companyRepository.findAll())
                .thenReturn(List.of(company, company2));

        List<CompanyResponse> responses = companyService.getAllCompanies();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("TCS", responses.get(0).getName());
        assertEquals("Google", responses.get(1).getName());
        System.out.println("✅ getAllCompanies_Success passed!");
    }

    @Test
    void getCompanyById_Success() {
        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        CompanyResponse response = companyService.getCompanyById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("TCS", response.getName());
        System.out.println("✅ getCompanyById_Success passed!");
    }

    @Test
    void getCompanyById_NotFound_ThrowsException() {
        when(companyRepository.findById(99L))
                .thenReturn(Optional.empty());

        CompanyNotFoundException exception = assertThrows(
                CompanyNotFoundException.class,
                () -> companyService.getCompanyById(99L)
        );

        assertEquals("Company not found with id 99", exception.getMessage());
        System.out.println("✅ getCompanyById_NotFound passed!");
    }

    @Test
    void deleteCompany_Success() {
        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));
        doNothing().when(companyRepository).delete(any(Company.class));

        companyService.deleteCompany(1L);

        verify(companyRepository, times(1)).delete(any(Company.class));
        System.out.println("✅ deleteCompany_Success passed!");
    }

    @Test
    void deleteCompany_NotFound_ThrowsException() {
        when(companyRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                CompanyNotFoundException.class,
                () -> companyService.deleteCompany(99L)
        );

        verify(companyRepository, never()).delete(any(Company.class));
        System.out.println("✅ deleteCompany_NotFound passed!");
    }
}