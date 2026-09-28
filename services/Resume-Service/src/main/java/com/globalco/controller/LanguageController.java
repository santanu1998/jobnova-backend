package com.globalco.controller;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.LanguageResponse;
import com.globalco.payload.AddLanguageRequest;
import com.globalco.services.LanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/languages")
public class LanguageController {
    private final LanguageService languageService;
    @PostMapping("/{resumeId}/add")
    public ResponseEntity<LanguageResponse> addLanguage(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddLanguageRequest languageRequest
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(languageService.addLanguage(resumeId, candidateId, languageRequest));
    }
    @GetMapping("/{resumeId}/all")
    public ResponseEntity<List<LanguageResponse>> getAllLanguages(
            @PathVariable Long resumeId
            ) {
        return ResponseEntity.ok(languageService.getAllLanguages(resumeId));
    }
    @PutMapping("/{resumeId}/update/{languageId}")
    public ResponseEntity<LanguageResponse> updateLanguage(
            @PathVariable Long languageId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddLanguageRequest languageRequest
    ) {
        return ResponseEntity.ok(languageService.updateLanguage(resumeId, candidateId, languageId, languageRequest));
    }
    @DeleteMapping("/{resumeId}/delete/{languageId}")
    public ResponseEntity<ApiResponse> deleteLanguage(
            @PathVariable Long languageId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        languageService.deleteLanguage(resumeId, candidateId, languageId);
        return ResponseEntity.ok(new ApiResponse("Language deleted successfully", true));
    }
}
