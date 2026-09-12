package com.placement.placement.service.impl;

import com.placement.placement.dto.response.OfferLetterResponse;
import com.placement.placement.entity.Company;
import com.placement.placement.entity.OfferLetter;
import com.placement.placement.entity.Student;
import com.placement.placement.exception.CompanyNotFoundException;
import com.placement.placement.exception.ResourceNotFoundException;
import com.placement.placement.exception.StudentNotFoundException;
import com.placement.placement.repository.CompanyRepository;
import com.placement.placement.repository.OfferLetterRepository;
import com.placement.placement.repository.StudentRepository;
import com.placement.placement.service.OfferLetterService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferLetterServiceImpl implements OfferLetterService {

    private static final Logger log =
            LoggerFactory.getLogger(OfferLetterServiceImpl.class);

    // ✅ Dedicated property for offer letter directory
    @Value("${file.upload.offerletter.dir}")
    private String offerLetterUploadDir;

    private final OfferLetterRepository offerLetterRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;

    public OfferLetterServiceImpl(
            OfferLetterRepository offerLetterRepository,
            StudentRepository studentRepository,
            CompanyRepository companyRepository) {
        this.offerLetterRepository = offerLetterRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
    }

    // ✅ SITUATION 1: Upload offer letter
    // Handles: student not found, company not found, invalid file type,
    //          directory creation, old file replacement, DB save
    @Override
    public OfferLetterResponse uploadOfferLetter(Long studentId,
                                                 Long companyId,
                                                 Double ctc,
                                                 MultipartFile file) {
        log.info("Uploading offer letter for student: {} company: {}",
                studentId, companyId);

        // ✅ Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.error("Student not found: {}", studentId);
                    return new StudentNotFoundException(
                            "Student not found with id " + studentId);
                });

        // ✅ Validate company exists
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> {
                    log.error("Company not found: {}", companyId);
                    return new CompanyNotFoundException(
                            "Company not found with id " + companyId);
                });

        // ✅ Validate file is not null or empty
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty!");
        }

        // ✅ Validate file type — only PDF allowed
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null ||
                !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException(
                    "Only PDF files are allowed for offer letters!");
        }

        // ✅ Validate CTC
        if (ctc == null || ctc <= 0) {
            throw new IllegalArgumentException(
                    "CTC must be greater than 0!");
        }

        try {
            // ✅ Create directory if not exists
            Path uploadPath = Paths.get(offerLetterUploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created offer letter directory: {}",
                        offerLetterUploadDir);
            }

            // ✅ Check duplicate — 409 if already exists
            if (offerLetterRepository.existsByStudentIdAndCompanyId(
                    studentId, companyId)) {
                log.warn("Offer letter already exists for student: {}" +
                        " company: {}", studentId, companyId);
                throw new IllegalStateException(
                        "Offer letter already exists for this student " +
                                "and company!");
            }

            // ✅ Generate unique filename
            String fileName = "offer_student" + studentId
                    + "_company" + companyId + "_"
                    + System.currentTimeMillis() + ".pdf";
            Path filePath = uploadPath.resolve(fileName);

            // ✅ Save file to disk
            Files.copy(file.getInputStream(), filePath,
                    StandardCopyOption.REPLACE_EXISTING);
            log.info("Offer letter file saved: {}", fileName);

            // ✅ Save record to DB
            OfferLetter offerLetter = new OfferLetter(
                    student, company,
                    filePath.toString(),
                    LocalDate.now(),
                    ctc
            );

            OfferLetter saved = offerLetterRepository.save(offerLetter);
            log.info("Offer letter record saved with id: {}", saved.getId());
            return toResponse(saved);

        } catch (IOException e) {
            log.error("Failed to upload offer letter: {}", e.getMessage());
            throw new RuntimeException(
                    "Failed to upload offer letter: " + e.getMessage());
        }
    }

    // ✅ SITUATION 2: Download offer letter
    // Handles: offer letter not found in DB, file missing on disk,
    //          malformed URL, unreadable file
    @Override
    public Resource downloadOfferLetter(Long studentId, Long companyId) {
        log.info("Downloading offer letter for student: {} company: {}",
                studentId, companyId);

        // ✅ Validate student exists
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Student not found with id " + studentId);
        }

        // ✅ Validate company exists
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(
                    "Company not found with id " + companyId);
        }

        // ✅ Find offer letter record — 404 if not found
        OfferLetter offerLetter = offerLetterRepository
                .findByStudentIdAndCompanyId(studentId, companyId)
                .orElseThrow(() -> {
                    log.error("Offer letter not found for student: {}" +
                            " company: {}", studentId, companyId);
                    return new ResourceNotFoundException(
                            "Offer letter not found for student "
                                    + studentId + " and company " + companyId);
                });

        try {
            Path filePath = Paths.get(offerLetter.getFilePath());

            // ✅ Check if file physically exists on disk
            if (!Files.exists(filePath)) {
                log.error("Offer letter file missing on disk: {}",
                        filePath);
                throw new ResourceNotFoundException(
                        "Offer letter file not found on server!");
            }

            Resource resource = new UrlResource(filePath.toUri());

            // ✅ Check if file is readable
            if (!resource.isReadable()) {
                log.error("Offer letter file not readable: {}", filePath);
                throw new ResourceNotFoundException(
                        "Offer letter file cannot be read!");
            }

            log.info("Offer letter ready for download: {}",
                    filePath.getFileName());
            return resource;

        } catch (MalformedURLException e) {
            log.error("Malformed URL for offer letter: {}", e.getMessage());
            throw new RuntimeException(
                    "Error processing offer letter file path!");
        }
    }

    // ✅ SITUATION 3: Get all offer letters by student
    // Handles: student not found, empty list (returns empty array)
    @Override
    public List<OfferLetterResponse> getOfferLettersByStudent(
            Long studentId) {
        log.info("Fetching offer letters for student: {}", studentId);

        // ✅ Validate student exists first
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Student not found with id " + studentId);
        }

        List<OfferLetterResponse> results = offerLetterRepository
                .findByStudentId(studentId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());

        log.info("Found {} offer letters for student: {}",
                results.size(), studentId);
        return results; // ✅ Returns empty list [] if none — not 404
    }

    // ✅ SITUATION 4: Delete offer letter
    // Handles: student not found, company not found,
    //          offer letter not found, file deletion failure
    @Override
    public void deleteOfferLetter(Long studentId, Long companyId) {
        log.info("Deleting offer letter for student: {} company: {}",
                studentId, companyId);

        // ✅ Validate student exists
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Student not found with id " + studentId);
        }

        // ✅ Validate company exists
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(
                    "Company not found with id " + companyId);
        }

        // ✅ Find offer letter — 404 if not found
        OfferLetter offerLetter = offerLetterRepository
                .findByStudentIdAndCompanyId(studentId, companyId)
                .orElseThrow(() -> {
                    log.error("Offer letter not found for student: {}" +
                            " company: {}", studentId, companyId);
                    return new ResourceNotFoundException(
                            "Offer letter not found for student "
                                    + studentId + " and company " + companyId);
                });

        try {
            // ✅ Delete file from disk (won't throw if already missing)
            Path filePath = Paths.get(offerLetter.getFilePath());
            boolean fileDeleted = Files.deleteIfExists(filePath);
            if (fileDeleted) {
                log.info("Offer letter file deleted from disk: {}", filePath);
            } else {
                log.warn("Offer letter file was already missing on disk: {}",
                        filePath);
            }

            // ✅ Delete record from DB
            offerLetterRepository.delete(offerLetter);
            log.info("Offer letter record deleted for student: {}" +
                    " company: {}", studentId, companyId);

        } catch (IOException e) {
            log.error("Failed to delete offer letter file: {}",
                    e.getMessage());
            throw new RuntimeException(
                    "Failed to delete offer letter file: " + e.getMessage());
        }
    }

    // ✅ Helper — convert entity to response DTO
    private OfferLetterResponse toResponse(OfferLetter offerLetter) {
        return new OfferLetterResponse(
                offerLetter.getId(),
                offerLetter.getStudent().getName(),
                offerLetter.getCompany().getName(),
                offerLetter.getFilePath(),
                offerLetter.getUploadedDate(),
                offerLetter.getCtc()
        );
    }
}