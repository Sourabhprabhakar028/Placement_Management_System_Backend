package com.placement.placement.repository;

import com.placement.placement.entity.OfferLetter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfferLetterRepository extends JpaRepository<OfferLetter, Long> {

    List<OfferLetter> findByStudentId(Long studentId);
    Optional<OfferLetter> findByStudentIdAndCompanyId(Long studentId, Long companyId);
    boolean existsByStudentIdAndCompanyId(Long studentId, Long companyId);
}