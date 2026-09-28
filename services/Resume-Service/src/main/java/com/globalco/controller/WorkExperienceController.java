package com.globalco.controller;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.WorkExperienceResponse;
import com.globalco.payload.AddWorkExperienceRequest;
import com.globalco.services.WorkExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-experiences")
@RequiredArgsConstructor
public class WorkExperienceController {
    private final WorkExperienceService workExperienceService;
    @PostMapping("/{resumeId}/add")
    public ResponseEntity<WorkExperienceResponse> addWorkExperience(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddWorkExperienceRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workExperienceService.addWorkExperience(resumeId, candidateId, request));
    }
    @GetMapping("/{resumeId}/all")
    public ResponseEntity<List<WorkExperienceResponse>> getWorkExperiences(
            @PathVariable Long resumeId
    ) {
        return ResponseEntity.ok(workExperienceService.getWorkExperiences(resumeId));
    }
    @PutMapping("/{resumeId}/{workExperienceId}/update")
    public ResponseEntity<WorkExperienceResponse> updateWorkExperience(
            @PathVariable Long resumeId,
            @PathVariable Long workExperienceId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddWorkExperienceRequest request
    ) {
        return ResponseEntity.ok(workExperienceService.updateWorkExperience(resumeId, candidateId, workExperienceId, request));
    }
    @DeleteMapping("/{resumeId}/{workExperienceId}/delete")
    public ResponseEntity<ApiResponse> deleteWorkExperience(
            @PathVariable Long resumeId,
            @PathVariable Long workExperienceId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        workExperienceService.deleteWorkExperience(resumeId, workExperienceId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Work experience deleted successfully", true));
    }
}
