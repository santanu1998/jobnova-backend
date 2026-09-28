package com.globalco.services.impl;

import com.globalco.dto.response.SavedJobResponse;
import com.globalco.mapper.PreferenceMapper;
import com.globalco.models.SavedJob;
import com.globalco.payload.SaveJobRequest;
import com.globalco.repositories.SavedJobRepository;
import com.globalco.services.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {
    private final SavedJobRepository savedJobRepository;

    @Override
    public SavedJobResponse saveJob(Long candidateId, SaveJobRequest req) {
        if(isSaved(candidateId,req.getJobId())){
            throw new RuntimeException("job already saved");
        }
        SavedJob savedJob = SavedJob.builder()
                .candidateId(candidateId)
                .jobId(req.getJobId())
                .build();
        savedJob=savedJobRepository.save(savedJob);
        return PreferenceMapper.toSavedJobResponse(savedJob);
    }

    @Override
    public void unsaveJob(Long candidateId, Long savedJobId) {
        SavedJob savedJob = savedJobRepository.findById(savedJobId)
                .orElseThrow(() -> new RuntimeException("Saved job not found"));
        if (!savedJob.getCandidateId().equals(candidateId)) {
            throw new RuntimeException("You are not authorized to unsave this job");
        }
        savedJobRepository.delete(savedJob);
    }

    @Override
    public List<SavedJobResponse> getSavedJob(Long candidateId) {
        return savedJobRepository.findByCandidateId(candidateId)
                .stream().map(PreferenceMapper::toSavedJobResponse).toList();
    }

    @Override
    public boolean isSaved(Long candidateId, Long jobId) {
        return savedJobRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }
}
