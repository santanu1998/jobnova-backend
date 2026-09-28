package com.globalco.services.impl;

import com.globalco.dto.response.ResumeSkillResponse;
import com.globalco.mapper.ResumeMapper;
import com.globalco.models.Resume;
import com.globalco.models.ResumeSkill;
import com.globalco.payload.AddResumeSkillRequest;
import com.globalco.repositories.ResumeSkillRepository;
import com.globalco.services.ResumeService;
import com.globalco.services.ResumeSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeSkillServiceImpl implements ResumeSkillService {
    private final ResumeService resumeService;
    private final ResumeSkillRepository resumeSkillRepository;

    @Override
    public ResumeSkillResponse addResumeSkill(Long resumeId, Long candidateId, AddResumeSkillRequest request) {
        Resume resume = resumeService.getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        ResumeSkill resumeSkill = ResumeSkill.builder()
                .resume(resume)
                .skillName(request.getSkillName())
                .proficiencyLevel(request.getProficiencyLevel())
                .yearsOfExperience(request.getYearsOfExperience())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();
        ResumeSkill savedResumeSkill = resumeSkillRepository.save(resumeSkill);
        return ResumeMapper.toResumeSkillResponse(savedResumeSkill);
    }

    private void assertOwner(Resume resume, Long candidateId) {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new IllegalArgumentException("Resume does not belong to the specified candidate");
        }
    }

    @Override
    public List<ResumeSkillResponse> getResumeSkills(Long resumeId) {
        return resumeSkillRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId).stream()
                .map(ResumeMapper::toResumeSkillResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResumeSkillResponse updateResumeSkill(Long skillId, Long resumeId, Long candidateId, AddResumeSkillRequest request) {
        ResumeSkill resumeSkill = resumeSkillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Resume skill not found"));
        assertOwner(resumeSkill.getResume(), candidateId);
        resumeSkill.setSkillName(request.getSkillName());
        resumeSkill.setProficiencyLevel(request.getProficiencyLevel());
        resumeSkill.setYearsOfExperience(request.getYearsOfExperience());
        if (request.getDisplayOrder() != null) {
            resumeSkill.setDisplayOrder(request.getDisplayOrder());
        }
        ResumeSkill updatedResumeSkill = resumeSkillRepository.save(resumeSkill);
        return ResumeMapper.toResumeSkillResponse(updatedResumeSkill);
    }

    @Override
    public void deleteResumeSkill(Long skillId, Long resumeId, Long candidateId) {
        ResumeSkill resumeSkill = resumeSkillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Resume skill not found"));
        assertOwner(resumeSkill.getResume(), candidateId);
        resumeSkillRepository.delete(resumeSkill);
    }
}
