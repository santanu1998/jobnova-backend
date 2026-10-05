package com.globalco.controllers;

import com.globalco.dto.response.ResumeResponse;

import com.globalco.dto.response.ApplicationScreeningResponse;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.ApplicationResponse;
import com.globalco.payload.CompanyApplicationFilterRequest;
import com.globalco.payload.CreateApplicationRequest;
import com.globalco.payload.UpdateApplicationStatusRequest;
import com.globalco.payload.WithdrawApplicationRequest;
import com.globalco.services.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    @PostMapping("/create")
    public ResponseEntity<ApplicationResponse> createApplication(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid CreateApplicationRequest createApplicationRequest
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.createApplication(candidateId, createApplicationRequest));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getApplicationById(
            @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<ApplicationResponse>> getAllApplications(
            @RequestHeader("X-User-Id") Long candidateId) {
        return ResponseEntity.ok(applicationService.getAllApplications(candidateId));
    }
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForJob(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(jobId));
    }
    @GetMapping("/company")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForCompany(
            @RequestHeader("X-User-Id") Long userId,
            @ModelAttribute CompanyApplicationFilterRequest filter) throws Exception {
        return ResponseEntity.ok(applicationService.getApplicationsForCompany(
                userId, filter));
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId,
            @RequestBody @Valid UpdateApplicationStatusRequest req)
            throws Exception {
        return ResponseEntity.ok(applicationService.updateStatus(id,
                employerId,
                req.getStatus())
        );
    }
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<ApplicationResponse> withdraw(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody WithdrawApplicationRequest req)
            throws Exception {
        return ResponseEntity.ok(applicationService.withdraw(id, candidateId, req));
    }
    @PatchMapping("/{id}/star")
    public ResponseEntity<ApplicationResponse> toggleStar(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId)
            throws Exception {
        return ResponseEntity.ok(applicationService.toggleStar(id, employerId));
    }
    // Re-runs AI screening for an application (hiring employer only)
    @PostMapping("/{id}/screen")
    public ResponseEntity<ApplicationScreeningResponse> screen(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId) {
        return ResponseEntity.ok(applicationService.screenApplication(id, employerId));
    }

    // Resume attached to an application (hiring employer or the candidate)
    @GetMapping("/{id}/resume")
    public ResponseEntity<ResumeResponse> getResume(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(applicationService.getApplicationResume(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteApplication(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long candidateId)
            throws Exception {
        applicationService.deleteApplication(id, candidateId);
        return ResponseEntity.ok(
                new ApiResponse("Application deleted successfully", true));
    }
}
