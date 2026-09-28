package com.globalco.controller;

import com.globalco.dto.PersonalInfoResponse;
import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.ResumeResponse;
import com.globalco.payload.CreateResumeRequest;
import com.globalco.services.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    @PostMapping("/create")
    public ResponseEntity<ResumeResponse> createResume(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid CreateResumeRequest resumeRequest
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resumeService.createResume(candidateId, resumeRequest));
    }
    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeResponse> getResumeById(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        return ResponseEntity.ok(resumeService.getResumeById(resumeId, candidateId));
    }
    @GetMapping("/all")
    public ResponseEntity<List<ResumeResponse>> getAllResumes(
            @RequestHeader(value = "X-User-Id", required = false) Long candidateId
    ) {
        return ResponseEntity.ok(resumeService.getAllResumesByCandidateId(candidateId));
    }
    @PutMapping("/{resumeId}/update/personal-info")
    public ResponseEntity<ResumeResponse> updatePersonalInfo(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid PersonalInfoResponse personalInfo
    ) {
        return ResponseEntity.ok(resumeService.updateResumesById(resumeId, candidateId, personalInfo));
    }
    @PatchMapping("/{resumeId}/update/summary")
    public ResponseEntity<ResumeResponse> updateSummary(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestParam String summary
    ) {
        return ResponseEntity.ok(resumeService.updateSummary(resumeId, candidateId, summary));
    }
    @PatchMapping("/{resumeId}/set-default")
    public ResponseEntity<ResumeResponse> setResumeAsDefault(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        return ResponseEntity.ok(resumeService.setResumeAsDefault(resumeId, candidateId));
    }
    @DeleteMapping("/{resumeId}/delete")
    public ResponseEntity<ApiResponse> deleteResume(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        resumeService.deleteResume(resumeId, candidateId);
        ApiResponse response = new ApiResponse();
        response.setMessage("Resume deleted successfully");
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }
}
