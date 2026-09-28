package com.globalco.services;

import com.globalco.dto.response.AwardResponse;
import com.globalco.payload.AddAwardRequest;

import java.util.List;

public interface AwardService {
    AwardResponse addAward(Long resumeId, Long candidateId, AddAwardRequest awardRequest);
    List<AwardResponse> getAllAwards(Long resumeId);
    AwardResponse updateAward(
            Long resumeId, Long candidateId, Long awardId, AddAwardRequest awardRequest);
    void deleteAward(Long resumeId, Long candidateId, Long awardId);
}
