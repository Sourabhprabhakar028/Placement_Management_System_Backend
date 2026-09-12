package com.placement.placement.controller;

import com.placement.placement.dto.request.JobApplicationRequest;
import com.placement.placement.dto.request.JobApplicationStatusRequest;
import com.placement.placement.dto.response.JobApplicationResponse;
import com.placement.placement.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    // ✅ Student applies to company
    @PostMapping("/student/{studentId}/company/{companyId}")
    public ResponseEntity<JobApplicationResponse> apply(
            @PathVariable Long studentId,
            @PathVariable Long companyId,
            @Valid @RequestBody JobApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobApplicationService.apply(studentId, companyId, request));
    }

    // ✅ Get all applications
    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAllApplications() {
        return ResponseEntity.ok(jobApplicationService.getAllApplications());
    }

    // ✅ Get application by id
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getApplicationById(
            @PathVariable Long id) {
        return ResponseEntity.ok(jobApplicationService.getApplicationById(id));
    }

    // ✅ Get applications by student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<JobApplicationResponse>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                jobApplicationService.getApplicationsByStudent(studentId));
    }

    // ✅ Get applications by company
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobApplicationResponse>> getByCompany(
            @PathVariable Long companyId) {
        return ResponseEntity.ok(
                jobApplicationService.getApplicationsByCompany(companyId));
    }

    // ✅ Get applications by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobApplicationResponse>> getByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(
                jobApplicationService.getApplicationsByStatus(status));
    }

    // ✅ Update status (ADMIN only)
    @PutMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationStatusRequest request) {
        return ResponseEntity.ok(
                jobApplicationService.updateStatus(id, request));
    }

    // ✅ Delete application (ADMIN only)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
        jobApplicationService.deleteApplication(id);
        return ResponseEntity.ok("Application deleted successfully!");
    }
}