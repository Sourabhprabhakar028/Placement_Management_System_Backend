package com.placement.placement.controller;

import com.placement.placement.dto.response.OfferLetterResponse;
import com.placement.placement.service.OfferLetterService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferLetterController {

    private final OfferLetterService offerLetterService;

    public OfferLetterController(OfferLetterService offerLetterService) {
        this.offerLetterService = offerLetterService;
    }

    @PostMapping(value = "/student/{studentId}/company/{companyId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OfferLetterResponse> uploadOfferLetter(
            @PathVariable Long studentId,
            @PathVariable Long companyId,
            @RequestParam("ctc") Double ctc,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(
                offerLetterService.uploadOfferLetter(studentId, companyId, ctc, file));
    }

    @GetMapping("/student/{studentId}/company/{companyId}/download")
    public ResponseEntity<Resource> downloadOfferLetter(
            @PathVariable Long studentId,
            @PathVariable Long companyId) {
        Resource resource = offerLetterService.downloadOfferLetter(studentId, companyId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<OfferLetterResponse>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                offerLetterService.getOfferLettersByStudent(studentId));
    }

    @DeleteMapping("/student/{studentId}/company/{companyId}")
    public ResponseEntity<String> deleteOfferLetter(
            @PathVariable Long studentId,
            @PathVariable Long companyId) {
        offerLetterService.deleteOfferLetter(studentId, companyId);
        return ResponseEntity.ok("Offer letter deleted successfully!");
    }
}