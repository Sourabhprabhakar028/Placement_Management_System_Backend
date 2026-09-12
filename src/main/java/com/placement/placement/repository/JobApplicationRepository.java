package com.placement.placement.repository;

import com.placement.placement.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByStudentId(Long studentId);
    List<JobApplication> findByCompanyId(Long companyId);
    List<JobApplication> findByStatus(String status);
    boolean existsByStudentIdAndCompanyId(Long studentId, Long companyId);
}