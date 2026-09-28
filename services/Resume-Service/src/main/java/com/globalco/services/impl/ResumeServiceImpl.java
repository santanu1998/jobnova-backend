package com.globalco.services.impl;

import com.globalco.dto.PersonalInfoResponse;
import com.globalco.domain.ResumeTemplate;
import com.globalco.domain.ResumeVisibility;
import com.globalco.dto.response.*;
import com.globalco.mapper.ResumeMapper;
import com.globalco.mapper.WorkExperienceMapper;
import com.globalco.models.PersonalInfo;
import com.globalco.models.Resume;
import com.globalco.payload.CreateResumeRequest;
import com.globalco.repositories.*;
import com.globalco.services.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final EducationRepository educationRepository;
    private final ResumeSkillRepository resumeSkillRepository;
    private final ProjectRepository projectRepository;
    private final CertificationRepository certificationRepository;
    private final AwardRepository awardRepository;
    private final LanguageRepository languageRepository;

    @Override
    public ResumeResponse createResume(Long candidateId, CreateResumeRequest resumeRequest) {
        if (Boolean.TRUE.equals(resumeRequest.getIsDefault())) {
            resumeRepository.findByCandidateIdAndIsDefaultTrue(candidateId)
                    .ifPresent(existing -> {
                        existing.setIsDefault(false);
                        resumeRepository.save(existing);
                    });
        }
        // determine title: prefer explicit title, then resumeName, then fallback
        String title = resumeRequest.getTitle() != null && !resumeRequest.getTitle().isBlank()
                ? resumeRequest.getTitle()
                : (resumeRequest.getResumeName() != null && !resumeRequest.getResumeName().isBlank()
                ? resumeRequest.getResumeName() : "Untitled Resume");
        // default template/visibility if not provided
        ResumeTemplate template = resumeRequest.getTemplate() != null ? resumeRequest.getTemplate() : ResumeTemplate.PROFESSIONAL;
        ResumeVisibility visibility = resumeRequest.getVisibility() != null ? resumeRequest.getVisibility() : ResumeVisibility.PUBLIC;
        Resume resume = Resume.builder()
                .candidateId(candidateId)
                .title(title)
                .template(template)
                .visibility(visibility)
                .isDefault(Boolean.TRUE.equals(resumeRequest.getIsDefault()))
                .isActive(true)
                .summary(resumeRequest.getSummary())
                .build();
        Resume savedResume = resumeRepository.save(resume);
        return buildFullResponse(savedResume);
    }

    private ResumeResponse buildFullResponse(Resume resume) {
        Long resumeId = resume.getId();
        List<WorkExperienceResponse> workExperienceResponses = workExperienceRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(WorkExperienceMapper::toWorkExperienceResponse)
                .toList();
        List<EducationResponse> educationResponses = educationRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toEducationResponse)
                .toList();
        List<ResumeSkillResponse> skillResponses = resumeSkillRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toResumeSkillResponse)
                .toList();
        List<ProjectResponse> projectResponses = projectRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toProjectResponse)
                .toList();
        List<CertificationResponse> certificationResponses = certificationRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toCertificationResponse)
                .toList();
        List<AwardResponse> awardResponses = awardRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toAwardResponse)
                .toList();
        List<LanguageResponse> languageResponses = languageRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toLanguageResponse)
                .toList();
        return ResumeMapper.toResumeResponse(resume, workExperienceResponses, educationResponses, skillResponses, projectResponses, certificationResponses, awardResponses, languageResponses);
    }

    @Override
    public ResumeResponse getResumeById(Long resumeId, Long candidateId) {
        Resume resume = getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        return buildFullResponse(resume);
    }

    private void assertOwner(Resume resume, Long candidateId) {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new RuntimeException("Unauthorized access to resume");
        }
    }

    @Override
    public List<ResumeResponse> getAllResumesByCandidateId(Long candidateId) {
        if (candidateId == null) {
            return List.of();
        }
        return resumeRepository.findByCandidateIdAndIsActiveTrue(candidateId)
                .stream()
                .map(this::buildFullResponse)
                .toList();
    }

    @Override
    public ResumeResponse updateResumesById(Long resumeId, Long candidateId, PersonalInfoResponse personalInfo) {
        Resume resume = getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        PersonalInfo info = resume.getPersonalInfo();
        if (info == null) {
            info = new PersonalInfo();
        }
        if (personalInfo.getFirstName() != null) {
            info.setFirstName(personalInfo.getFirstName());
        }
        if (personalInfo.getLastName() != null) {
            info.setLastName(personalInfo.getLastName());
        }
        if (personalInfo.getHeadline() != null) {
            info.setHeadline(personalInfo.getHeadline());
        }
        if (personalInfo.getEmail() != null) {
            info.setEmail(personalInfo.getEmail());
        }
        if (personalInfo.getPhoneNumber() != null) {
            info.setPhoneNumber(personalInfo.getPhoneNumber());
        }
        if (personalInfo.getCity() != null) {
            info.setCity(personalInfo.getCity());
        }
        if (personalInfo.getCountry() != null) {
            info.setCountry(personalInfo.getCountry());
        }
        if (personalInfo.getLinkedinUrl() != null) {
            info.setLinkedinUrl(personalInfo.getLinkedinUrl());
        }
        if (personalInfo.getGithubUrl() != null) {
            info.setGithubUrl(personalInfo.getGithubUrl());
        }
        if (personalInfo.getPortfolioUrl() != null) {
            info.setPortfolioUrl(personalInfo.getPortfolioUrl());
        }
        if (personalInfo.getWebsiteUrl() != null) {
            info.setWebsiteUrl(personalInfo.getWebsiteUrl());
        }
        resume.setPersonalInfo(info);
        Resume updatedResume = resumeRepository.save(resume);
        return buildFullResponse(updatedResume);
    }

    @Override
    public ResumeResponse updateSummary(Long resumeId, Long candidateId, String summary) {
        Resume resume = getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        resume.setSummary(summary);
        Resume updatedResume = resumeRepository.save(resume);
        return buildFullResponse(updatedResume);
    }

    @Override
    public ResumeResponse setResumeAsDefault(Long resumeId, Long candidateId) {
        Resume resume = getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        resumeRepository.findByCandidateIdAndIsDefaultTrue(candidateId)
                .ifPresent(existing -> {
                    existing.setIsDefault(false);
                    resumeRepository.save(existing);
                });
        resume.setIsDefault(true);
        Resume updatedResume = resumeRepository.save(resume);
        return buildFullResponse(updatedResume);
    }

    @Override
    public void deleteResume(Long resumeId, Long candidateId) {
        Resume resume = getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        resume.setIsActive(false);
        resume.setIsDefault(false);
        resumeRepository.save(resume);
    }

    @Override
    public Resume getResumeEntityById(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
    }
}
