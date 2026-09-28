package com.globalco.services;

import com.globalco.dto.response.EducationResponse;
import com.globalco.payload.AddEducationRequest;

import java.util.List;

public interface EducationService {
    EducationResponse addEducation(Long resumeId, Long candidateId, AddEducationRequest educationRequest);
    List<EducationResponse> getEducations(Long resumeId);
    EducationResponse updateEducation(
            Long educationId, Long resumeId, Long candidateId, AddEducationRequest educationRequest);
    void deleteEducation(Long educationId, Long resumeId, Long candidateId);

}
