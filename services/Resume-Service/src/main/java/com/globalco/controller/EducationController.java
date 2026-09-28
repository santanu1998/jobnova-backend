package com.globalco.controller;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.EducationResponse;
import com.globalco.payload.AddEducationRequest;
import com.globalco.services.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/education")
public class EducationController {
    private final EducationService educationService;
    @PostMapping("/{resumeId}/add")
    public ResponseEntity<EducationResponse> addEducation(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddEducationRequest educationRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(educationService.addEducation(resumeId, candidateId, educationRequest));
    }
    @GetMapping("/{resumeId}/all")
    public ResponseEntity<List<EducationResponse>> getAllEducations(
            @PathVariable Long resumeId
    ) {
        return ResponseEntity.ok(educationService.getEducations(resumeId));
    }
    @PutMapping("/{resumeId}/update/{educationId}")
    public ResponseEntity<EducationResponse> updateEducation(
            @PathVariable Long educationId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddEducationRequest educationRequest
    ) {
        return ResponseEntity.ok(educationService.updateEducation(educationId, resumeId, candidateId, educationRequest));
    }
    @DeleteMapping("/{resumeId}/delete/{educationId}")
    public ResponseEntity<ApiResponse> deleteEducation(
            @PathVariable Long educationId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        educationService.deleteEducation(educationId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Education deleted successfully", true));
    }
}
