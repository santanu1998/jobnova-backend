package com.globalco.services.impl;

import com.globalco.dto.response.WorkExperienceResponse;
import com.globalco.mapper.WorkExperienceMapper;
import com.globalco.models.Resume;
import com.globalco.models.WorkExperience;
import com.globalco.payload.AddWorkExperienceRequest;
import com.globalco.repositories.WorkExperienceRepository;
import com.globalco.services.ResumeService;
import com.globalco.services.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkExperienceServiceImpl implements WorkExperienceService {
    private final ResumeService resumeService;
    private final WorkExperienceRepository workExperienceRepository;

    @Override
    public WorkExperienceResponse addWorkExperience(Long resumeId, Long candidateId, AddWorkExperienceRequest request) {
        Resume resume = resumeService.getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        WorkExperience workExperience = WorkExperience.builder()
                .resume(resume)
                .companyName(request.getCompanyName())
                .companyLogoUrl(request.getCompanyLogoUrl())
                .jobTitle(request.getJobTitle())
                .employmentType(request.getEmploymentType())
                .location(request.getLocation())
                .startDate(request.getStartDate() != null ? LocalDate.parse(request.getStartDate()) : null)
                .endDate(request.getEndDate() != null ? LocalDate.parse(request.getEndDate()) : null)
                .isCurrentlyWorking(request.getIsCurrentlyWorking())
                .isCurrentlyWorking(Boolean.TRUE.equals(request.getIsCurrentlyWorking()))
                .jobDescription(request.getJobDescription())
                .technologies(request.getTechnologies() != null ? request.getTechnologies() : List.of())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();
        WorkExperience savedWorkExperience = workExperienceRepository.save(workExperience);
        return WorkExperienceMapper.toWorkExperienceResponse(savedWorkExperience);
    }

    private void assertOwner(Resume resume, Long candidateId) {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new IllegalArgumentException("Candidate with ID " + candidateId + " is not the owner of resume with ID " + resume.getId());
        }
    }

    @Override
    public List<WorkExperienceResponse> getWorkExperiences(Long resumeId) {
        return workExperienceRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(WorkExperienceMapper::toWorkExperienceResponse)
                .toList();
    }

    @Override
    public WorkExperienceResponse updateWorkExperience(Long resumeId, Long candidateId, Long workExperienceId, AddWorkExperienceRequest request) {
        WorkExperience workExperience = getWorkExperienceEntityById(workExperienceId);
        assertOwner(workExperience.getResume(), candidateId);
         workExperience.setCompanyName(request.getCompanyName());
         workExperience.setCompanyLogoUrl(request.getCompanyLogoUrl());
         workExperience.setJobTitle(request.getJobTitle());
         workExperience.setEmploymentType(request.getEmploymentType());
         workExperience.setLocation(request.getLocation());
         workExperience.setStartDate(request.getStartDate() != null ? LocalDate.parse(request.getStartDate()) : null);
         workExperience.setEndDate(request.getEndDate() != null ? LocalDate.parse(request.getEndDate()) : null);
         workExperience.setIsCurrentlyWorking(Boolean.TRUE.equals(request.getIsCurrentlyWorking()));
        workExperience.setJobDescription(request.getJobDescription());
        if (request.getTechnologies() != null) {
            workExperience.setTechnologies(request.getTechnologies());
        }
        if (request.getDisplayOrder() != null) {
            workExperience.setDisplayOrder(request.getDisplayOrder());
        }
//        workExperience.setTechnologies(request.getTechnologies() != null ? request.getTechnologies() : List.of());
//        workExperience.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        WorkExperience updatedWorkExperience = workExperienceRepository.save(workExperience);
        return WorkExperienceMapper.toWorkExperienceResponse(updatedWorkExperience);
    }

    @Override
    public void deleteWorkExperience(Long resumeId, Long workExperienceId, Long candidateId) {
        WorkExperience workExperience = getWorkExperienceEntityById(workExperienceId);
        assertOwner(workExperience.getResume(), candidateId);
        workExperienceRepository.delete(workExperience);
    }

    @Override
    public WorkExperience getWorkExperienceEntityById(Long workExperienceId) {
        return workExperienceRepository.findById(workExperienceId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("Work experience with ID " + workExperienceId + " not found"));
    }
}
