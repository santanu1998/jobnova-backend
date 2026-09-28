package com.globalco.controllers;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.SavedJobResponse;
import com.globalco.payload.SaveJobRequest;
import com.globalco.services.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
@RequiredArgsConstructor
public class SavedJobController {
    private final SavedJobService savedJobService;
    @PostMapping("/save")
    public ResponseEntity<SavedJobResponse> saveJob(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody SaveJobRequest req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedJobService.saveJob(candidateId, req));
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<SavedJobResponse>> getAllSavedJobs(
            @RequestHeader("X-User-Id") Long candidateId) {
        return ResponseEntity.ok(savedJobService.getSavedJob(candidateId));
    }
    @GetMapping("/check")
    public ResponseEntity<Boolean> isSaved(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestParam Long jobId) {
        return ResponseEntity.ok(savedJobService.isSaved(candidateId, jobId));
    }
    @DeleteMapping("/unsave/{savedJobId}")
    public ResponseEntity<ApiResponse> unsaveJob(
            @PathVariable Long savedJobId,
            @RequestHeader("X-User-Id") Long candidateId) throws Exception {
        savedJobService.unsaveJob(candidateId, savedJobId);
        return ResponseEntity.ok(new ApiResponse("Job removed from saved list", true));
    }
}
