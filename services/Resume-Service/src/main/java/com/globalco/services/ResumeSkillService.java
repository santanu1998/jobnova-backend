package com.globalco.services;

import com.globalco.dto.response.ResumeSkillResponse;
import com.globalco.payload.AddResumeSkillRequest;

import java.util.List;

public interface ResumeSkillService {
    ResumeSkillResponse addResumeSkill(Long resumeId, Long candidateId, AddResumeSkillRequest request);
    List<ResumeSkillResponse> getResumeSkills(Long resumeId);
    ResumeSkillResponse updateResumeSkill(
            Long skillId, Long resumeId, Long candidateId, AddResumeSkillRequest request);
    void deleteResumeSkill(Long skillId, Long resumeId, Long candidateId);
}
