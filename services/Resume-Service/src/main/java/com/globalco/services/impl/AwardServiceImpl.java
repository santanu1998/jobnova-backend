package com.globalco.services.impl;

import com.globalco.dto.response.AwardResponse;
import com.globalco.mapper.ResumeMapper;
import com.globalco.models.Award;
import com.globalco.models.Resume;
import com.globalco.payload.AddAwardRequest;
import com.globalco.repositories.AwardRepository;
import com.globalco.services.AwardService;
import com.globalco.services.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {
    private final ResumeService resumeService;
    private final AwardRepository awardRepository;

    @Override
    public AwardResponse addAward(Long resumeId, Long candidateId, AddAwardRequest awardRequest) {
        Resume resume = resumeService.getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        Award award = Award.builder()
                .resume(resume)
                .title(awardRequest.getTitle())
                .issuer(awardRequest.getIssuer())
                .issuedDate(awardRequest.getIssuedDate())
                .description(awardRequest.getDescription())
                .displayOrder(awardRequest.getDisplayOrder() != null ? awardRequest.getDisplayOrder() : 0)
                .scope(awardRequest.getScope())
                .build();
        Award savedAward = awardRepository.save(award);
        return ResumeMapper.toAwardResponse(savedAward);
    }
    private void assertOwner(Resume resume, Long candidateId) {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new IllegalArgumentException("Candidate with ID " + candidateId + " is not the owner of resume with ID " + resume.getId());
        }
    }

    @Override
    public List<AwardResponse> getAllAwards(Long resumeId) {
        return awardRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toAwardResponse)
                .toList();
    }

    @Override
    public AwardResponse updateAward(Long resumeId, Long candidateId, Long awardId, AddAwardRequest awardRequest) {
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new IllegalArgumentException("Award with ID " + awardId + " not found"));
        assertOwner(award.getResume(), candidateId);
        award.setTitle(awardRequest.getTitle());
        award.setIssuer(awardRequest.getIssuer());
        award.setIssuedDate(awardRequest.getIssuedDate());
        award.setDescription(awardRequest.getDescription());
        if (awardRequest.getDisplayOrder() != null) {
            award.setDisplayOrder(awardRequest.getDisplayOrder());
        }
        award.setScope(awardRequest.getScope());
        Award updatedAward = awardRepository.save(award);
        return ResumeMapper.toAwardResponse(updatedAward);
    }

    @Override
    public void deleteAward(Long resumeId, Long candidateId, Long awardId) {
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new IllegalArgumentException("Award with ID " + awardId + " not found"));
        assertOwner(award.getResume(), candidateId);
        awardRepository.delete(award);
    }
}
