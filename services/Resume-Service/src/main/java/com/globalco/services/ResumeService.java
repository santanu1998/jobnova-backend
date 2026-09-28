package com.globalco.services;

import com.globalco.dto.PersonalInfoResponse;
import com.globalco.models.Resume;
import com.globalco.payload.CreateResumeRequest;
import com.globalco.dto.response.ResumeResponse;

import java.util.List;

public interface ResumeService {
    ResumeResponse createResume(Long candidateId, CreateResumeRequest resumeRequest);
    ResumeResponse getResumeById(Long resumeId, Long candidateId);
    List<ResumeResponse> getAllResumesByCandidateId(Long candidateId);
    ResumeResponse updateResumesById(Long resumeId, Long candidateId, PersonalInfoResponse personalInfo);
    ResumeResponse updateSummary(Long resumeId, Long candidateId, String summary);
    ResumeResponse setResumeAsDefault(Long resumeId, Long candidateId);
    void deleteResume(Long resumeId, Long candidateId);
    Resume getResumeEntityById(Long resumeId);
}
