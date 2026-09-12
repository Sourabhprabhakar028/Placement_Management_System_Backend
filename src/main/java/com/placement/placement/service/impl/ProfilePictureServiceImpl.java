package com.placement.placement.service.impl;

import com.placement.placement.entity.Student;
import com.placement.placement.exception.ResourceNotFoundException;
import com.placement.placement.exception.StudentNotFoundException;
import com.placement.placement.repository.StudentRepository;
import com.placement.placement.service.ProfilePictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {

    private static final Logger log =
            LoggerFactory.getLogger(ProfilePictureServiceImpl.class);

    // ✅ Allowed image extensions
    private static final List<String> ALLOWED_TYPES = List.of(
            ".jpg", ".jpeg", ".png", ".gif"
    );

    // ✅ Dedicated property for profile picture directory
    @Value("${file.upload.profilepicture.dir}")
    private String profilePictureUploadDir;

    private final StudentRepository studentRepository;

    public ProfilePictureServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ✅ SITUATION 1: Upload profile picture
    // Handles: student not found, null file, empty file,
    //          invalid extension, no extension, directory creation,
    //          old picture replacement, DB update
    @Override
    public String uploadProfilePicture(Long studentId, MultipartFile file) {
        log.info("Uploading profile picture for studentId: {}", studentId);

        // ✅ Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.error("Student not found: {}", studentId);
                    return new StudentNotFoundException(
                            "Student not found with id " + studentId);
                });

        // ✅ Validate file not null or empty
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty!");
        }

        String originalFilename = file.getOriginalFilename();

        // ✅ Validate filename not null
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Invalid file name!");
        }

        // ✅ Validate file has an extension
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new IllegalArgumentException(
                    "File must have an extension!");
        }

        // ✅ Validate file extension is allowed
        String extension = originalFilename
                .substring(dotIndex).toLowerCase();
        if (!ALLOWED_TYPES.contains(extension)) {
            throw new IllegalArgumentException(
                    "Only JPG, JPEG, PNG, GIF files are allowed! " +
                            "Got: " + extension);
        }

        try {
            // ✅ Create directory if not exists
            Path uploadPath = Paths.get(profilePictureUploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created profile picture directory: {}",
                        profilePictureUploadDir);
            }

            // ✅ Delete old profile picture if exists
            if (student.getProfilePicturePath() != null) {
                Path oldFile = Paths.get(student.getProfilePicturePath());
                boolean deleted = Files.deleteIfExists(oldFile);
                if (deleted) {
                    log.info("Old profile picture deleted for student: {}",
                            studentId);
                } else {
                    log.warn("Old profile picture file was already " +
                            "missing for student: {}", studentId);
                }
            }

            // ✅ Generate unique filename
            String fileName = "profile_student" + studentId
                    + "_" + System.currentTimeMillis() + extension;
            Path filePath = uploadPath.resolve(fileName);

            // ✅ Save file to disk
            Files.copy(file.getInputStream(), filePath,
                    StandardCopyOption.REPLACE_EXISTING);
            log.info("Profile picture saved: {}", fileName);

            // ✅ Update student record in DB
            student.setProfilePicturePath(filePath.toString());
            studentRepository.save(student);
            log.info("Profile picture path updated for student: {}",
                    studentId);

            return "Profile picture uploaded successfully: " + fileName;

        } catch (IOException e) {
            log.error("Failed to upload profile picture: {}",
                    e.getMessage());
            throw new RuntimeException(
                    "Failed to upload profile picture: " + e.getMessage());
        }
    }

    // ✅ SITUATION 2: View profile picture
    // Handles: student not found, no picture uploaded,
    //          file missing on disk, unreadable file, malformed URL
    @Override
    public Resource viewProfilePicture(Long studentId) {
        log.info("Viewing profile picture for studentId: {}", studentId);

        // ✅ Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.error("Student not found: {}", studentId);
                    return new StudentNotFoundException(
                            "Student not found with id " + studentId);
                });

        // ✅ Check if student has a profile picture
        if (student.getProfilePicturePath() == null) {
            log.warn("No profile picture for student: {}", studentId);
            throw new ResourceNotFoundException(
                    "No profile picture uploaded for student: " + studentId);
        }

        try {
            Path filePath = Paths.get(student.getProfilePicturePath());

            // ✅ Check file physically exists on disk
            if (!Files.exists(filePath)) {
                log.error("Profile picture file missing on disk: {}",
                        filePath);
                // ✅ Clean up stale DB record
                student.setProfilePicturePath(null);
                studentRepository.save(student);
                throw new ResourceNotFoundException(
                        "Profile picture file not found on server " +
                                "for student: " + studentId);
            }

            Resource resource = new UrlResource(filePath.toUri());

            // ✅ Check file is readable
            if (!resource.isReadable()) {
                log.error("Profile picture not readable: {}", filePath);
                throw new ResourceNotFoundException(
                        "Profile picture file cannot be read!");
            }

            log.info("Profile picture ready for student: {}", studentId);
            return resource;

        } catch (MalformedURLException e) {
            log.error("Malformed URL for profile picture: {}",
                    e.getMessage());
            throw new RuntimeException(
                    "Error processing profile picture file path!");
        }
    }

    // ✅ SITUATION 3: Delete profile picture
    // Handles: student not found, no picture to delete,
    //          file already missing on disk, DB cleanup
    @Override
    public void deleteProfilePicture(Long studentId) {
        log.info("Deleting profile picture for studentId: {}", studentId);

        // ✅ Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.error("Student not found: {}", studentId);
                    return new StudentNotFoundException(
                            "Student not found with id " + studentId);
                });

        // ✅ Check if student has a profile picture to delete
        if (student.getProfilePicturePath() == null) {
            log.warn("No profile picture to delete for student: {}",
                    studentId);
            throw new ResourceNotFoundException(
                    "No profile picture found for student: " + studentId);
        }

        try {
            Path filePath = Paths.get(student.getProfilePicturePath());

            // ✅ Delete file (won't throw if already missing)
            boolean fileDeleted = Files.deleteIfExists(filePath);
            if (fileDeleted) {
                log.info("Profile picture file deleted for student: {}",
                        studentId);
            } else {
                log.warn("Profile picture file was already missing " +
                        "on disk for student: {}", studentId);
            }

            // ✅ Always clean up DB record regardless of file existence
            student.setProfilePicturePath(null);
            studentRepository.save(student);
            log.info("Profile picture record cleared for student: {}",
                    studentId);

        } catch (IOException e) {
            log.error("Failed to delete profile picture: {}",
                    e.getMessage());
            throw new RuntimeException(
                    "Failed to delete profile picture: " + e.getMessage());
        }
    }
}