package com.placement.placement.controller;

import com.placement.placement.dto.request.InterviewRoundRequest;
import com.placement.placement.dto.response.InterviewRoundResponse;
import com.placement.placement.service.InterviewRoundService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interviews")
public class InterviewRoundController {

    private final InterviewRoundService interviewRoundService;

    public InterviewRoundController(InterviewRoundService interviewRoundService) {
        this.interviewRoundService = interviewRoundService;
    }

    // ✅ Schedule new round
    @PostMapping("/application/{applicationId}")
    public ResponseEntity<InterviewRoundResponse> scheduleRound(
            @PathVariable Long applicationId,
            @Valid @RequestBody InterviewRoundRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(interviewRoundService.scheduleRound(applicationId, request));
    }

    // ✅ Get all rounds
    @GetMapping
    public ResponseEntity<List<InterviewRoundResponse>> getAllRounds() {
        return ResponseEntity.ok(interviewRoundService.getAllRounds());
    }

    // ✅ Get round by id
    @GetMapping("/{id}")
    public ResponseEntity<InterviewRoundResponse> getRoundById(
            @PathVariable Long id) {
        return ResponseEntity.ok(interviewRoundService.getRoundById(id));
    }

    // ✅ Get rounds by application
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<InterviewRoundResponse>> getRoundsByApplication(
            @PathVariable Long applicationId) {
        return ResponseEntity.ok(
                interviewRoundService.getRoundsByApplication(applicationId));
    }

    // ✅ Get rounds by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InterviewRoundResponse>> getRoundsByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(
                interviewRoundService.getRoundsByStatus(status));
    }

    // ✅ Update round (ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<InterviewRoundResponse> updateRound(
            @PathVariable Long id,
            @Valid @RequestBody InterviewRoundRequest request) {
        return ResponseEntity.ok(interviewRoundService.updateRound(id, request));
    }

    // ✅ Delete round (ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRound(@PathVariable Long id) {
        interviewRoundService.deleteRound(id);
        return ResponseEntity.ok("Interview round deleted successfully!");
    }
}