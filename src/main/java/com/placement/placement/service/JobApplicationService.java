package com.placement.placement.service;

import com.placement.placement.dto.request.JobApplicationRequest;
import com.placement.placement.dto.request.JobApplicationStatusRequest;
import com.placement.placement.dto.response.JobApplicationResponse;

import java.util.List;

public interface JobApplicationService {
    JobApplicationResponse apply(Long studentId, Long companyId,
                                 JobApplicationRequest request);
    List<JobApplicationResponse> getAllApplications();
    JobApplicationResponse getApplicationById(Long id);
    List<JobApplicationResponse> getApplicationsByStudent(Long studentId);
    List<JobApplicationResponse> getApplicationsByCompany(Long companyId);
    List<JobApplicationResponse> getApplicationsByStatus(String status);
    JobApplicationResponse updateStatus(Long id,
                                        JobApplicationStatusRequest request);
    void deleteApplication(Long id);
}