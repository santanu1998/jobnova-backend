package com.globalco.services.impl;

import com.globalco.dto.response.EducationResponse;
import com.globalco.mapper.ResumeMapper;
import com.globalco.models.Education;
import com.globalco.models.Resume;
import com.globalco.payload.AddEducationRequest;
import com.globalco.repositories.EducationRepository;
import com.globalco.services.EducationService;
import com.globalco.services.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {
    private final ResumeService resumeService;
    private final EducationRepository educationRepository;

    @Override
    public EducationResponse addEducation(Long resumeId, Long candidateId, AddEducationRequest educationRequest) {
        Resume resume = resumeService.getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        Education education = Education.builder()
                .resume(resume)
                .institutionName(educationRequest.getInstitutionName())
                .degree(educationRequest.getDegree())
                .fieldOfStudy(educationRequest.getFieldOfStudy())
                .grade(educationRequest.getGrade())
                .startDate(educationRequest.getStartDate().atStartOfDay())
                .endDate(educationRequest.getEndDate().atStartOfDay())
                .isCurrentlyStudying(Boolean.TRUE.equals(educationRequest.getIsCurrentlyStudying()))
                .description(educationRequest.getDescription())
                .displayOrder(educationRequest.getDisplayOrder() != null ? educationRequest.getDisplayOrder() : 0)
                .build();
        Education savedEducation = educationRepository.save(education);
        return ResumeMapper.toEducationResponse(savedEducation);
    }

    private void assertOwner(Resume resume, Long candidateId) {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new IllegalArgumentException("Candidate with ID " + candidateId + " is not the owner of resume with ID " + resume.getId());
        }
    }

    @Override
    public List<EducationResponse> getEducations(Long resumeId) {
        return educationRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toEducationResponse)
                .toList();
    }

    @Override
    public EducationResponse updateEducation(Long educationId, Long resumeId, Long candidateId, AddEducationRequest educationRequest) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new IllegalArgumentException("Education with ID " + educationId + " not found"));
        assertOwner(education.getResume(), candidateId);
        // Update the education fields
        education.setInstitutionName(educationRequest.getInstitutionName());
        education.setDegree(educationRequest.getDegree());
        education.setFieldOfStudy(educationRequest.getFieldOfStudy());
        education.setGrade(educationRequest.getGrade());
        education.setStartDate(educationRequest.getStartDate().atStartOfDay());
        education.setEndDate(educationRequest.getEndDate().atStartOfDay());
        education.setIsCurrentlyStudying(Boolean.TRUE.equals(educationRequest.getIsCurrentlyStudying()));
        education.setDescription(educationRequest.getDescription());
        education.setDisplayOrder(educationRequest.getDisplayOrder() != null ? educationRequest.getDisplayOrder() : 0);
        Education updatedEducation = educationRepository.save(education);
        return ResumeMapper.toEducationResponse(updatedEducation);
    }

    @Override
    public void deleteEducation(Long educationId, Long resumeId, Long candidateId) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new IllegalArgumentException("Education with ID " + educationId + " not found"));
        assertOwner(education.getResume(), candidateId);
        educationRepository.deleteById(educationId);
    }
}
