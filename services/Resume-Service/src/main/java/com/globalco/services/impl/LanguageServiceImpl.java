package com.globalco.services.impl;

import com.globalco.dto.response.LanguageResponse;
import com.globalco.mapper.ResumeMapper;
import com.globalco.models.Language;
import com.globalco.models.Resume;
import com.globalco.payload.AddLanguageRequest;
import com.globalco.repositories.LanguageRepository;
import com.globalco.services.LanguageService;
import com.globalco.services.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final ResumeService resumeService;
    private final LanguageRepository languageRepository;

    @Override
    public LanguageResponse addLanguage(Long resumeId, Long candidateId, AddLanguageRequest languageRequest) {
        Resume resume = resumeService.getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        Language language = Language.builder()
                .resume(resume)
                .languageName(languageRequest.getLanguageName())
                .proficiencyLevel(languageRequest.getProficiencyLevel())
                .displayOrder(languageRequest.getDisplayOrder() != null ? languageRequest.getDisplayOrder() : 0)
                .build();
        Language savedLanguage = languageRepository.save(language);
        return ResumeMapper.toLanguageResponse(savedLanguage);
    }

    private void assertOwner(Resume resume, Long candidateId) {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new IllegalArgumentException("Candidate with ID " + candidateId + " is not the owner of resume with ID " + resume.getId());
        }
    }

    @Override
    public List<LanguageResponse> getAllLanguages(Long resumeId) {
        return languageRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toLanguageResponse)
                .toList();
    }

    @Override
    public LanguageResponse updateLanguage(Long resumeId, Long candidateId, Long languageId, AddLanguageRequest languageRequest) {
        Language language = languageRepository.findById(languageId)
                .orElseThrow(() -> new IllegalArgumentException("Language with ID " + languageId + " not found"));
        assertOwner(language.getResume(), candidateId);
        language.setLanguageName(languageRequest.getLanguageName());
        language.setProficiencyLevel(languageRequest.getProficiencyLevel());
        if (languageRequest.getDisplayOrder() != null) {
            language.setDisplayOrder(languageRequest.getDisplayOrder());
        }
        Language updatedLanguage = languageRepository.save(language);
        return ResumeMapper.toLanguageResponse(updatedLanguage);
    }

    @Override
    public void deleteLanguage(Long resumeId, Long candidateId, Long languageId) {
        Language language = languageRepository.findById(languageId)
                .orElseThrow(() -> new IllegalArgumentException("Language with ID " + languageId + " not found"));
        assertOwner(language.getResume(), candidateId);
        languageRepository.delete(language);
    }
}
