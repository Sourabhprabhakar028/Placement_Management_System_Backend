package com.placement.placement.service;

import com.placement.placement.dto.request.PlacementRequest;
import com.placement.placement.dto.response.PlacementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlacementService {
    PlacementResponse addPlacement(Long studentId, Long companyId, PlacementRequest request);
    List<PlacementResponse> getAllPlacements();
    Page<PlacementResponse> getAllPlacementsPaginated(Pageable pageable); // ✅ NEW
    PlacementResponse getPlacementById(Long id);
    List<PlacementResponse> getPlacementsByStudent(Long studentId);
    List<PlacementResponse> getPlacementsByCompany(Long companyId);
    List<PlacementResponse> getPlacementsByStatus(String status);
    PlacementResponse updatePlacement(Long id, PlacementRequest request);
    void deletePlacement(Long id);
}