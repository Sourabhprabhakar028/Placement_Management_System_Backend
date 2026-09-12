package com.placement.placement.service;

import com.placement.placement.dto.request.InterviewRoundRequest;
import com.placement.placement.dto.response.InterviewRoundResponse;

import java.util.List;

public interface InterviewRoundService {
    InterviewRoundResponse scheduleRound(Long applicationId,
                                         InterviewRoundRequest request);
    List<InterviewRoundResponse> getAllRounds();
    InterviewRoundResponse getRoundById(Long id);
    List<InterviewRoundResponse> getRoundsByApplication(Long applicationId);
    List<InterviewRoundResponse> getRoundsByStatus(String status);
    InterviewRoundResponse updateRound(Long id, InterviewRoundRequest request);
    void deleteRound(Long id);
}