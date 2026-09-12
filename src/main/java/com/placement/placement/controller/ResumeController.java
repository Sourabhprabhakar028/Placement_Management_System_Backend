package com.placement.placement.controller;

import com.placement.placement.service.ResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    private static final Logger log =
            LoggerFactory.getLogger(ResumeController.class);

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value = "/student/{studentId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadResume(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file) {
        log.info("Resume upload request for studentId: {}", studentId);
        String message = resumeService.uploadResume(studentId, file);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/student/{studentId}/download")
    public ResponseEntity<Resource> downloadResume(
            @PathVariable Long studentId) {
        log.info("Resume download request for studentId: {}", studentId);
        Resource resource = resumeService.downloadResume(studentId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<String> getResumeInfo(
            @PathVariable Long studentId) {
        log.info("Resume info request for studentId: {}", studentId);
        String path = resumeService.getResumePath(studentId);
        if (path == null) {
            return ResponseEntity.ok("No resume uploaded yet!");
        }
        return ResponseEntity.ok("Resume path: " + path);
    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<String> deleteResume(
            @PathVariable Long studentId) {
        log.info("Resume delete request for studentId: {}", studentId);
        resumeService.deleteResume(studentId);
        return ResponseEntity.ok("Resume deleted successfully!");
    }
}