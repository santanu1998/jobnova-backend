package com.globalco.services;

import com.globalco.dto.response.WorkExperienceResponse;
import com.globalco.models.WorkExperience;
import com.globalco.payload.AddWorkExperienceRequest;

import java.util.List;

public interface WorkExperienceService {
    WorkExperienceResponse addWorkExperience(Long resumeId, Long candidateId, AddWorkExperienceRequest request);
    List<WorkExperienceResponse> getWorkExperiences(Long resumeId);
    WorkExperienceResponse updateWorkExperience
            (Long resumeId, Long candidateId, Long workExperienceId, AddWorkExperienceRequest request);
    void deleteWorkExperience(Long resumeId, Long workExperienceId, Long candidateId);
    WorkExperience getWorkExperienceEntityById(Long workExperienceId);
}
