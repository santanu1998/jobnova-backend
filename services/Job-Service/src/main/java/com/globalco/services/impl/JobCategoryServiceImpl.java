package com.globalco.services.impl;

import com.globalco.dto.response.JobCategoryResponse;
import com.globalco.mapper.JobCategoryMapper;
import com.globalco.models.JobCategory;
import com.globalco.payload.JobCategoryRequest;
import com.globalco.repositories.JobCategoryRepository;
import com.globalco.services.JobCategoryService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobCategoryServiceImpl implements JobCategoryService {
    private final JobCategoryRepository jobCategoryRepository;

    @Override
    public JobCategoryResponse createJobCategory(JobCategoryRequest request) {
        if (jobCategoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Job category with the same name already exists.");
        }
        JobCategory parent = null;
        if (request.getParentId() != null) {
            parent = getJobCategoryEntityById(request.getParentId());
        }
        String slug = generateUniqueSlug(request.getName());
        JobCategory jobCategory = JobCategory.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .iconUrl(request.getIconUrl())
                .parentCategory(parent)
                .active(true)
                .build();
        JobCategory savedJobCategory = jobCategoryRepository.save(jobCategory);
        return JobCategoryMapper.toJobCategoryResponse(savedJobCategory, true);
    }

    private String generateUniqueSlug(@NotBlank(message = "Job category name is required") String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]+", "")
                .trim()
                .replaceAll("[\\s-]+", "-");
        if (!jobCategoryRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while (jobCategoryRepository.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }

    @Override
    public List<JobCategoryResponse> getAllJobCategories() {
        return jobCategoryRepository.findByActiveTrue().stream()
                .map(category -> JobCategoryMapper.
                        toJobCategoryResponse(category, false))
                .toList();
    }

    @Override
    public JobCategoryResponse getJobCategoryById(Long categoryId) {
        JobCategory jobCategory = getJobCategoryEntityById(categoryId);
        return JobCategoryMapper.toJobCategoryResponse(jobCategory, true);
    }

    @Override
    public JobCategoryResponse updateJobCategory(Long categoryId, JobCategoryRequest request) {
        JobCategory jobCategory = getJobCategoryEntityById(categoryId);
        if (!jobCategory.getName().equals(request.getName())
                && jobCategoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Job category with the same name already exists.");
        }
        JobCategory parent = null;
        if (request.getParentId() != null) {
            if (request.getParentId().equals(categoryId)) {
                throw new IllegalArgumentException("A job category cannot be its own parent.");
            }
            parent = getJobCategoryEntityById(request.getParentId());
        }
        jobCategory.setName(request.getName());
        jobCategory.setDescription(request.getDescription());
        jobCategory.setIconUrl(request.getIconUrl());
        jobCategory.setParentCategory(parent);
        JobCategory updatedJobCategory = jobCategoryRepository.save(jobCategory);
        return JobCategoryMapper.toJobCategoryResponse(updatedJobCategory, true);
    }

    @Override
    public void deleteJobCategory(Long categoryId) {
        JobCategory jobCategory = getJobCategoryEntityById(categoryId);
        jobCategory.setActive(false);
        jobCategoryRepository.save(jobCategory);
    }

    @Override
    public JobCategory getJobCategoryEntityById(Long categoryId) {
        return jobCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Job category not found with id: " + categoryId));
    }
}
