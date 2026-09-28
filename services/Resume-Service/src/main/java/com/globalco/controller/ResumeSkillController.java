package com.globalco.controller;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.ResumeSkillResponse;
import com.globalco.payload.AddResumeSkillRequest;
import com.globalco.services.ResumeSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume-skills")
@RequiredArgsConstructor
public class ResumeSkillController {
    private final ResumeSkillService resumeSkillService;
    @PostMapping("/{resumeId}/add")
    public ResponseEntity<ResumeSkillResponse> addResumeSkill(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddResumeSkillRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resumeSkillService.addResumeSkill(resumeId, candidateId, request));
    }
    @GetMapping("/{resumeId}")
    public ResponseEntity<List<ResumeSkillResponse>> getResumeSkills(
            @PathVariable Long resumeId
    ) {
        return ResponseEntity.ok(resumeSkillService.getResumeSkills(resumeId));
    }
    @PutMapping("/{resumeId}/{skillId}/update")
    public ResponseEntity<ResumeSkillResponse> updateResumeSkill(
            @PathVariable Long resumeId,
            @PathVariable Long skillId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddResumeSkillRequest request
    ) {
        return ResponseEntity.ok(resumeSkillService.updateResumeSkill(skillId, resumeId, candidateId, request));
    }
    @DeleteMapping("/{resumeId}/{skillId}/delete")
    public ResponseEntity<ApiResponse> deleteResumeSkill(
            @PathVariable Long resumeId,
            @PathVariable Long skillId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        resumeSkillService.deleteResumeSkill(skillId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Resume skill deleted successfully", true));
    }
}
