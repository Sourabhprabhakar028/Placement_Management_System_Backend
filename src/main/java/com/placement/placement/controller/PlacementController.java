package com.placement.placement.controller;

import com.placement.placement.dto.request.PlacementRequest;
import com.placement.placement.dto.response.PlacementResponse;
import com.placement.placement.service.PlacementService;
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
@RequestMapping("/placements")
public class PlacementController {

    private final PlacementService placementService;

    public PlacementController(PlacementService placementService) {
        this.placementService = placementService;
    }

    @PostMapping("/student/{studentId}/company/{companyId}")
    public ResponseEntity<PlacementResponse> addPlacement(
            @PathVariable Long studentId,
            @PathVariable Long companyId,
            @Valid @RequestBody PlacementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(placementService.addPlacement(studentId, companyId, request));
    }

    @GetMapping
    public ResponseEntity<List<PlacementResponse>> getAllPlacements() {
        return ResponseEntity.ok(placementService.getAllPlacements());
    }

    // ✅ NEW Paginated endpoint
    @GetMapping("/paginated")
    public ResponseEntity<Page<PlacementResponse>> getAllPlacementsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(placementService.getAllPlacementsPaginated(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlacementResponse> getPlacementById(@PathVariable Long id) {
        return ResponseEntity.ok(placementService.getPlacementById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<PlacementResponse>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(placementService.getPlacementsByStudent(studentId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<PlacementResponse>> getByCompany(
            @PathVariable Long companyId) {
        return ResponseEntity.ok(placementService.getPlacementsByCompany(companyId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PlacementResponse>> getByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(placementService.getPlacementsByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlacementResponse> updatePlacement(
            @PathVariable Long id,
            @Valid @RequestBody PlacementRequest request) {
        return ResponseEntity.ok(placementService.updatePlacement(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlacement(@PathVariable Long id) {
        placementService.deletePlacement(id);
        return ResponseEntity.ok("Placement deleted successfully");
    }
}
