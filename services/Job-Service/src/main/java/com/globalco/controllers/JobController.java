package com.globalco.controllers;

import com.globalco.dto.request.JobRequest;
import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.JobResponse;
import com.globalco.payload.JobSearchRequest;
import com.globalco.services.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.util.MultiValueMap;
import java.util.Arrays;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;
    @PostMapping("/create")
    public ResponseEntity<JobResponse> createJob(
            @RequestHeader("X-User-Id") Long employerId,
            @RequestBody @Valid JobRequest jobRequest) {
        // Implement the logic to create a job
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobService.createJob(employerId, jobRequest));
    }
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<JobResponse>> getJobs(@RequestParam MultiValueMap<String, String> params) {
        // Build JobSearchRequest manually to avoid binding exceptions from invalid enum/number values
        JobSearchRequest req = new JobSearchRequest();
        try {
            if (params.containsKey("keyword")) req.setKeyword(params.getFirst("keyword"));
            if (params.containsKey("categoryId")) req.setCategoryId(parseLongSafe(params.getFirst("categoryId")));
            if (params.containsKey("companyId")) req.setCompanyId(parseLongSafe(params.getFirst("companyId")));
            if (params.containsKey("skillIds")) req.setSkillIds(parseLongList(params.getFirst("skillIds")));
            if (params.containsKey("tagIds")) req.setTagIds(parseLongList(params.getFirst("tagIds")));
            if (params.containsKey("location")) req.setLocation(params.getFirst("location"));
            if (params.containsKey("minSalary")) req.setMinSalary(new BigDecimal(params.getFirst("minSalary")));
            if (params.containsKey("maxSalary")) req.setMaxSalary(new BigDecimal(params.getFirst("maxSalary")));
            if (params.containsKey("jobType")) {
                try {
                    String v = params.getFirst("jobType");
                    if (v != null) req.setJobType(com.globalco.domain.JobType.valueOf(v.toUpperCase()));
                } catch (Exception ignored) {}
            }
            if (params.containsKey("workMode")) {
                try {
                    String v = params.getFirst("workMode");
                    if (v != null) req.setWorkMode(com.globalco.domain.WorkMode.valueOf(v.toUpperCase()));
                } catch (Exception ignored) {}
            }
            if (params.containsKey("experienceLevel")) {
                try {
                    String v = params.getFirst("experienceLevel");
                    if (v != null) req.setExperienceLevel(com.globalco.domain.ExperienceLevel.valueOf(v.toUpperCase()));
                } catch (Exception ignored) {}
            }
            if (params.containsKey("status")) {
                try {
                    String v = params.getFirst("status");
                    if (v != null) req.setStatus(com.globalco.domain.JobStatus.valueOf(v.toUpperCase()));
                } catch (Exception ignored) {}
            }
            if (params.containsKey("minOpenings")) req.setMinOpenings(parseIntSafe(params.getFirst("minOpenings")));
            if (params.containsKey("maxOpenings")) req.setMaxOpenings(parseIntSafe(params.getFirst("maxOpenings")));
        } catch (Exception e) {
            // ignore invalid params - return as if not provided
        }
        List<JobResponse> jobs = jobService.getAllJobs(req);
        return ResponseEntity.ok(jobs);
    }

    // parsing helpers
    private Long parseLongSafe(String s) {
        try { return s == null ? null : Long.parseLong(s); } catch (Exception e) { return null; }
    }
    private Integer parseIntSafe(String s) {
        try { return s == null ? null : Integer.parseInt(s); } catch (Exception e) { return null; }
    }
    private java.util.List<Long> parseLongList(String s) {
        if (s == null || s.isBlank()) return null;
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(str -> {
                    try { return Long.parseLong(str); } catch (Exception e) { return null; }
                })
                .filter(v -> v != null)
                .collect(Collectors.toList());
    }
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobResponse>> getJobsByCompany(@PathVariable Long companyId) {
        List<JobResponse> jobs = jobService.getJobsByCompany(companyId);
        return ResponseEntity.ok(jobs);
    }
    @GetMapping("/admin")
    public ResponseEntity<List<JobResponse>> getAllJobsAdmin(@ModelAttribute JobSearchRequest jobSearchRequest) {
        return ResponseEntity.ok(jobService.getAllJobsAdmin(jobSearchRequest));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId,
            @RequestBody @Valid JobRequest jobRequest) {
        JobResponse updatedJob = jobService.updateJob(id, employerId, jobRequest);
        return ResponseEntity.ok(updatedJob);
    }
    @PatchMapping("/publish/{id}")
    public ResponseEntity<JobResponse> publishJob(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId) {
        JobResponse publishedJob = jobService.publishJob(id, employerId);
        return ResponseEntity.ok(publishedJob);
    }
    @PatchMapping("/close/{id}")
    public ResponseEntity<JobResponse> closeJob(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId) {
        JobResponse closedJob = jobService.closeJob(id, employerId);
        return ResponseEntity.ok(closedJob);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteJob(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId) {
        jobService.deleteJob(id, employerId);
        return ResponseEntity.ok(new ApiResponse("Job deleted successfully", true));
    }
}
