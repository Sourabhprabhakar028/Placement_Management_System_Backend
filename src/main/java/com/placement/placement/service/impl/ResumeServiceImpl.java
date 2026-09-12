package com.placement.placement.service.impl;

import com.placement.placement.entity.Student;
import com.placement.placement.exception.ResourceNotFoundException;
import com.placement.placement.exception.StudentNotFoundException;
import com.placement.placement.repository.StudentRepository;
import com.placement.placement.service.ResumeService;
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

@Service
public class ResumeServiceImpl implements ResumeService {

    private static final Logger log =
            LoggerFactory.getLogger(ResumeServiceImpl.class);

    @Value("${file.upload.dir}")
    private String resumeUploadDir;

    private final StudentRepository studentRepository;

    public ResumeServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public String uploadResume(Long studentId, MultipartFile file) {
        log.info("Uploading resume for studentId: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id " + studentId));

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null ||
                !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed!");
        }

        try {
            Path uploadPath = Paths.get(resumeUploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // ✅ Delete old resume if exists
            if (student.getResumePath() != null) {
                Path oldFile = Paths.get(student.getResumePath());
                Files.deleteIfExists(oldFile);
                log.info("Old resume deleted for student: {}", studentId);
            }

            // ✅ Create unique filename
            String fileName = "resume_student" + studentId
                    + "_" + System.currentTimeMillis() + ".pdf";
            Path filePath = uploadPath.resolve(fileName);

            // ✅ Save file
            Files.copy(file.getInputStream(), filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            // ✅ Update student record
            student.setResumePath(filePath.toString());
            studentRepository.save(student);

            log.info("Resume uploaded for studentId: {}", studentId);
            return "Resume uploaded successfully: " + fileName;

        } catch (IOException e) {
            log.error("Failed to upload resume: {}", e.getMessage());
            throw new RuntimeException("Failed to upload resume: "
                    + e.getMessage());
        }
    }

    @Override
    public Resource downloadResume(Long studentId) {
        log.info("Downloading resume for studentId: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id " + studentId));

        if (student.getResumePath() == null) {
            throw new ResourceNotFoundException(
                    "No resume found for student: " + studentId);
        }

        try {
            Path filePath = Paths.get(student.getResumePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException(
                        "Resume file not found for student: " + studentId);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error reading resume file!");
        }
    }

    @Override
    public String getResumePath(Long studentId) {
        log.info("Getting resume path for studentId: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id " + studentId));

        return student.getResumePath(); // returns null if no resume uploaded
    }

    @Override
    public void deleteResume(Long studentId) {
        log.info("Deleting resume for studentId: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id " + studentId));

        if (student.getResumePath() == null) {
            throw new ResourceNotFoundException(
                    "No resume found for student: " + studentId);
        }

        try {
            Path filePath = Paths.get(student.getResumePath());
            Files.deleteIfExists(filePath);
            student.setResumePath(null);
            studentRepository.save(student);
            log.info("Resume deleted for studentId: {}", studentId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete resume!");
        }
    }
}