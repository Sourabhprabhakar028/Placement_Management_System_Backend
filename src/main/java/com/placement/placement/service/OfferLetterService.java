package com.placement.placement.service;

import com.placement.placement.dto.response.OfferLetterResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OfferLetterService {
    OfferLetterResponse uploadOfferLetter(Long studentId,
                                          Long companyId,
                                          Double ctc,
                                          MultipartFile file);
    Resource downloadOfferLetter(Long studentId, Long companyId);
    List<OfferLetterResponse> getOfferLettersByStudent(Long studentId);
    void deleteOfferLetter(Long studentId, Long companyId);
}