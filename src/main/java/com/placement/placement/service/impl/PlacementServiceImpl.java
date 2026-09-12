package com.placement.placement.service.impl;

import com.placement.placement.dto.request.PlacementRequest;
import com.placement.placement.dto.response.CompanyResponse;
import com.placement.placement.dto.response.PlacementResponse;
import com.placement.placement.dto.response.StudentResponse;
import com.placement.placement.entity.Company;
import com.placement.placement.entity.Placement;
import com.placement.placement.entity.Student;
import com.placement.placement.exception.CompanyNotFoundException;
import com.placement.placement.exception.PlacementNotFoundException;
import com.placement.placement.exception.StudentNotFoundException;
import com.placement.placement.repository.CompanyRepository;
import com.placement.placement.repository.PlacementRepository;
import com.placement.placement.repository.StudentRepository;
import com.placement.placement.service.PlacementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlacementServiceImpl implements PlacementService {

    private static final Logger log =
            LoggerFactory.getLogger(PlacementServiceImpl.class);

    private final PlacementRepository placementRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;

    public PlacementServiceImpl(PlacementRepository placementRepository,
                                StudentRepository studentRepository,
                                CompanyRepository companyRepository) {
        this.placementRepository = placementRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public PlacementResponse addPlacement(Long studentId, Long companyId,
                                          PlacementRequest request) {
        log.info("Adding placement for student: {} company: {}",
                studentId, companyId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.error("Student not found: {}", studentId);
                    return new StudentNotFoundException(
                            "Student not found with id " + studentId);
                });

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> {
                    log.error("Company not found: {}", companyId);
                    return new CompanyNotFoundException(
                            "Company not found with id " + companyId);
                });

        // ✅ Duplicate placement check
        if (placementRepository.existsByStudentIdAndCompanyId(
                studentId, companyId)) {
            log.warn("Placement already exists for student: {} company: {}",
                    studentId, companyId);
            throw new IllegalArgumentException(
                    "Placement already exists for this student and company!");
        }

        Placement placement = new Placement(
                student,
                company,
                request.getStatus().toUpperCase(),
                request.getPlacementDate(),
                request.getCtc()
        );

        Placement saved = placementRepository.save(placement);
        log.info("Placement saved with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public List<PlacementResponse> getAllPlacements() {
        log.info("Fetching all placements");
        return placementRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Page<PlacementResponse> getAllPlacementsPaginated(Pageable pageable) {
        log.info("Fetching paginated placements");
        return placementRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public PlacementResponse getPlacementById(Long id) {
        log.info("Fetching placement with id: {}", id);
        return toResponse(findById(id));
    }

    @Override
    public List<PlacementResponse> getPlacementsByStudent(Long studentId) {
        log.info("Fetching placements for student: {}", studentId);
        return placementRepository.findByStudentId(studentId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<PlacementResponse> getPlacementsByCompany(Long companyId) {
        log.info("Fetching placements for company: {}", companyId);
        return placementRepository.findByCompanyId(companyId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<PlacementResponse> getPlacementsByStatus(String status) {
        log.info("Fetching placements with status: {}", status);
        return placementRepository.findByStatus(status.toUpperCase())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public PlacementResponse updatePlacement(Long id,
                                             PlacementRequest request) {
        log.info("Updating placement with id: {}", id);
        Placement placement = findById(id);
        placement.setStatus(request.getStatus().toUpperCase());
        placement.setPlacementDate(request.getPlacementDate());
        placement.setCtc(request.getCtc());
        Placement updated = placementRepository.save(placement);
        log.info("Placement updated: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    public void deletePlacement(Long id) {
        log.info("Deleting placement with id: {}", id);
        placementRepository.delete(findById(id));
        log.info("Placement deleted: {}", id);
    }

    // ✅ FIXED — Uses PlacementNotFoundException → returns 404
    private Placement findById(Long id) {
        return placementRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Placement not found: {}", id);
                    return new PlacementNotFoundException(
                            "Placement not found with id " + id);
                });
    }

    private PlacementResponse toResponse(Placement placement) {
        StudentResponse studentResponse = new StudentResponse(
                placement.getStudent().getId(),
                placement.getStudent().getName(),
                placement.getStudent().getEmail(),
                placement.getStudent().getBranch(),
                placement.getStudent().getPercentage(),
                placement.getStudent().getResumePath(),
                placement.getStudent().getProfilePicturePath()
        );

        CompanyResponse companyResponse = new CompanyResponse(
                placement.getCompany().getId(),
                placement.getCompany().getName(),
                placement.getCompany().getLocation(),
                placement.getCompany().getPackageOffered(),
                placement.getCompany().getMinPercentage(),
                placement.getCompany().getEligibleBranches()
        );

        return new PlacementResponse(
                placement.getId(),
                studentResponse,
                companyResponse,
                placement.getStatus(),
                placement.getPlacementDate(),
                placement.getCtc()
        );
    }
}