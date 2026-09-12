package com.placement.placement.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {
    String uploadResume(Long studentId, MultipartFile file);
    Resource downloadResume(Long studentId);
    String getResumePath(Long studentId);
    void deleteResume(Long studentId);
}