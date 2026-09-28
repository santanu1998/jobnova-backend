package com.globalco.services;

import com.globalco.dto.response.JobCategoryResponse;
import com.globalco.models.JobCategory;
import com.globalco.payload.JobCategoryRequest;

import java.util.List;

public interface JobCategoryService {
    JobCategoryResponse createJobCategory(JobCategoryRequest request);
    List<JobCategoryResponse> getAllJobCategories();
    JobCategoryResponse getJobCategoryById(Long categoryId);
    JobCategoryResponse updateJobCategory(Long categoryId, JobCategoryRequest request);
    void deleteJobCategory(Long categoryId);
    JobCategory getJobCategoryEntityById(Long categoryId);
}
