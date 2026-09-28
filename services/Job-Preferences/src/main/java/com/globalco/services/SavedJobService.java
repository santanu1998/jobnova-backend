package com.globalco.services;

import com.globalco.dto.response.SavedJobResponse;
import com.globalco.payload.SaveJobRequest;

import java.util.List;

public interface SavedJobService {
    SavedJobResponse saveJob(Long candidateId, SaveJobRequest req);
    void unsaveJob(Long candidateId,Long savedJobId);
    List<SavedJobResponse> getSavedJob(Long candidateId);
    boolean isSaved(Long candidateId,Long jobId);
}
