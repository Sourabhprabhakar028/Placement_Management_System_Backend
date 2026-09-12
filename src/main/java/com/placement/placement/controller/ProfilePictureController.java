package com.placement.placement.controller;

import com.placement.placement.service.ProfilePictureService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profiles")
public class ProfilePictureController {

    private final ProfilePictureService profilePictureService;

    public ProfilePictureController(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }

    @PostMapping(value = "/student/{studentId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfilePicture(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(
                profilePictureService.uploadProfilePicture(studentId, file));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Resource> viewProfilePicture(
            @PathVariable Long studentId) {
        Resource resource = profilePictureService.viewProfilePicture(studentId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<String> deleteProfilePicture(
            @PathVariable Long studentId) {
        profilePictureService.deleteProfilePicture(studentId);
        return ResponseEntity.ok("Profile picture deleted successfully!");
    }
}