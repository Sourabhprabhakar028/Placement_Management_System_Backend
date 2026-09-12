package com.placement.placement.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureService {
    String uploadProfilePicture(Long studentId, MultipartFile file);
    Resource viewProfilePicture(Long studentId);
    void deleteProfilePicture(Long studentId);
}