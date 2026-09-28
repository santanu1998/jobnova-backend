package com.globalco.controllers;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.JobCategoryResponse;
import com.globalco.payload.JobCategoryRequest;
import com.globalco.services.JobCategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job-categories")
@RequiredArgsConstructor
public class JobCategoryController {
    private final JobCategoryService jobCategoryService;
    @PostMapping("/create")
    public ResponseEntity<JobCategoryResponse> createJobCategory(@RequestBody @Valid JobCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobCategoryService.createJobCategory(request));
    }
    @GetMapping("/all")
    public ResponseEntity<List<JobCategoryResponse>> getAllJobCategories() {
        return ResponseEntity.ok(jobCategoryService.getAllJobCategories());
    }
    @GetMapping("{id}")
    public ResponseEntity<JobCategoryResponse> getJobCategoryById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(jobCategoryService.getJobCategoryById(id));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<JobCategoryResponse> updateJobCategory(
            @PathVariable Long id,
            @RequestBody @Valid JobCategoryRequest request
    ) {
        return ResponseEntity.ok(jobCategoryService.updateJobCategory(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteJobCategory(
            @PathVariable Long id
    ) {
        jobCategoryService.deleteJobCategory(id);
        return ResponseEntity.ok(new ApiResponse("Category deleted successfully", true));
    }
}
