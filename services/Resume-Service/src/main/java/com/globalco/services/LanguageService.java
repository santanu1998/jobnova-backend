package com.globalco.services;

import com.globalco.dto.response.LanguageResponse;
import com.globalco.payload.AddLanguageRequest;

import java.util.List;

public interface LanguageService {
    LanguageResponse addLanguage(Long resumeId, Long candidateId, AddLanguageRequest languageRequest);
    List<LanguageResponse> getAllLanguages(Long resumeId);
    LanguageResponse updateLanguage(
            Long resumeId, Long candidateId, Long languageId, AddLanguageRequest languageRequest);
    void deleteLanguage(Long resumeId, Long candidateId, Long languageId);
}
