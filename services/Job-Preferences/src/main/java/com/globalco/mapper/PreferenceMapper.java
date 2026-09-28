package com.globalco.mapper;

import com.globalco.dto.response.SavedJobResponse;
import com.globalco.models.SavedJob;

public class PreferenceMapper {
    public static SavedJobResponse toSavedJobResponse(SavedJob savedJob) {
        return SavedJobResponse.builder()
                .id(savedJob.getId())
                .candidateId(savedJob.getCandidateId())
                .jobId(savedJob.getJobId())
                .savedAt(savedJob.getSavedAt())
                .build();
    }
}
