package com.placement.placement.repository;

import com.placement.placement.entity.InterviewRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRoundRepository
        extends JpaRepository<InterviewRound, Long> {

    List<InterviewRound> findByApplicationId(Long applicationId);
    List<InterviewRound> findByStatus(String status);
    List<InterviewRound> findByApplicationIdOrderByRoundNumberAsc(Long applicationId);
}