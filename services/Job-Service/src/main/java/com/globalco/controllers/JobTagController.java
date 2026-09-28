package com.globalco.controllers;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.JobTagResponse;
import com.globalco.payload.JobTagRequest;
import com.globalco.services.JobTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-tags")
@RequiredArgsConstructor
public class JobTagController {
    private final JobTagService jobTagService;
    @PostMapping("/create")
    public ResponseEntity<JobTagResponse> createJobTag(@RequestBody @Valid JobTagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobTagService.createJobTag(request));
    }
    @GetMapping("/all")
    public ResponseEntity<List<JobTagResponse>> getAllJobTags() {
        return ResponseEntity.ok(jobTagService.getAllJobTags());
    }
    @GetMapping("/{id}")
    public ResponseEntity<JobTagResponse> getJobTagById(@PathVariable Long id) {
        return ResponseEntity.ok(jobTagService.getJobTagById(id));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<JobTagResponse> updateJobTag(@PathVariable Long id,
            @RequestBody @Valid JobTagRequest request) {
        return ResponseEntity.ok(jobTagService.updateJobTag(id, request));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteJobTag(@PathVariable Long id) {
        jobTagService.deleteJobTag(id);
        return ResponseEntity.ok(new ApiResponse("Job tag deleted successfully", true));
    }
}
