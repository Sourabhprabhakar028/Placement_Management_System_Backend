package com.placement.placement.service;

import com.placement.placement.dto.request.PlacementRequest;
import com.placement.placement.dto.response.PlacementResponse;
import com.placement.placement.entity.Company;
import com.placement.placement.entity.Placement;
import com.placement.placement.entity.Student;
import com.placement.placement.exception.CompanyNotFoundException;
import com.placement.placement.exception.PlacementNotFoundException;
import com.placement.placement.exception.StudentNotFoundException;
import com.placement.placement.repository.CompanyRepository;
import com.placement.placement.repository.PlacementRepository;
import com.placement.placement.repository.StudentRepository;
import com.placement.placement.service.impl.PlacementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlacementServiceTest {

    @Mock
    private PlacementRepository placementRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private PlacementServiceImpl placementService;

    private Student student;
    private Company company;
    private Placement placement;
    private PlacementRequest placementRequest;

    @BeforeEach
    void setUp() {
        student = new Student("Sourabh", "sourabh@gmail.com", "CSE", 85.5);
        student.setId(1L);

        company = new Company("TCS", "Pune", 7.5);
        company.setId(1L);

        placement = new Placement(student, company, "PLACED",
                LocalDate.of(2024, 6, 15), 7.5);
        placement.setId(1L);

        placementRequest = new PlacementRequest();
        placementRequest.setStatus("PLACED");
        placementRequest.setPlacementDate(LocalDate.of(2024, 6, 15));
        placementRequest.setCtc(7.5);
    }

    @Test
    void addPlacement_Success() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));
        when(placementRepository.existsByStudentIdAndCompanyId(1L, 1L))
                .thenReturn(false);
        when(placementRepository.save(any(Placement.class)))
                .thenReturn(placement);

        PlacementResponse response = placementService
                .addPlacement(1L, 1L, placementRequest);

        assertNotNull(response);
        assertEquals("PLACED", response.getStatus());
        assertEquals(7.5, response.getCtc());
        assertEquals("Sourabh", response.getStudent().getName());
        assertEquals("TCS", response.getCompany().getName());
        verify(placementRepository, times(1)).save(any(Placement.class));
        System.out.println("✅ addPlacement_Success passed!");
    }

    @Test
    void addPlacement_StudentNotFound_ThrowsException() {
        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> placementService.addPlacement(99L, 1L, placementRequest)
        );

        verify(placementRepository, never()).save(any(Placement.class));
        System.out.println("✅ addPlacement_StudentNotFound passed!");
    }

    @Test
    void addPlacement_CompanyNotFound_ThrowsException() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(companyRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                CompanyNotFoundException.class,
                () -> placementService.addPlacement(1L, 99L, placementRequest)
        );

        verify(placementRepository, never()).save(any(Placement.class));
        System.out.println("✅ addPlacement_CompanyNotFound passed!");
    }

    @Test
    void addPlacement_Duplicate_ThrowsException() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));
        when(placementRepository.existsByStudentIdAndCompanyId(1L, 1L))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> placementService.addPlacement(1L, 1L, placementRequest)
        );

        assertEquals("Student is already placed in this company",
                exception.getMessage());
        System.out.println("✅ addPlacement_Duplicate passed!");
    }

    @Test
    void getAllPlacements_Success() {
        when(placementRepository.findAll())
                .thenReturn(List.of(placement));

        List<PlacementResponse> responses = placementService.getAllPlacements();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("PLACED", responses.get(0).getStatus());
        System.out.println("✅ getAllPlacements_Success passed!");
    }

    @Test
    void getPlacementById_Success() {
        when(placementRepository.findById(1L))
                .thenReturn(Optional.of(placement));

        PlacementResponse response = placementService.getPlacementById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("PLACED", response.getStatus());
        System.out.println("✅ getPlacementById_Success passed!");
    }

    @Test
    void getPlacementById_NotFound_ThrowsException() {
        when(placementRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                PlacementNotFoundException.class,
                () -> placementService.getPlacementById(99L)
        );

        System.out.println("✅ getPlacementById_NotFound passed!");
    }

    @Test
    void deletePlacement_Success() {
        when(placementRepository.findById(1L))
                .thenReturn(Optional.of(placement));
        doNothing().when(placementRepository).delete(any(Placement.class));

        placementService.deletePlacement(1L);

        verify(placementRepository, times(1)).delete(any(Placement.class));
        System.out.println("✅ deletePlacement_Success passed!");
    }
}