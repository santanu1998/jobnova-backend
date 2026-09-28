package com.globalco.mapper;

import com.globalco.dto.response.JobCategoryResponse;
import com.globalco.models.JobCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class JobCategoryMapper {
    public static JobCategoryResponse toJobCategoryResponse(JobCategory jobCategory, boolean includeChildren) {
        List<JobCategoryResponse> subCategories = null;
        if (includeChildren && (jobCategory.getSubCategories() != null && !jobCategory.getSubCategories().isEmpty())) {
            subCategories = jobCategory.getSubCategories().stream()
                    .map(subCategory ->
                            JobCategoryMapper.toJobCategoryResponse(subCategory,
                                    true))
                    .toList();
        }
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        if (jobCategory.getCreatedAt() != null) {
            createdAt = jobCategory.getCreatedAt().atStartOfDay();
        }
        if (jobCategory.getUpdatedAt() != null) {
            updatedAt = jobCategory.getUpdatedAt().atStartOfDay();
        }

        return JobCategoryResponse.builder()
                .id(jobCategory.getId())
                .name(jobCategory.getName())
                .slug(jobCategory.getSlug())
                .description(jobCategory.getDescription())
                .iconUrl(jobCategory.getIconUrl())
                .active(jobCategory.getActive())
                .parentId(jobCategory.getParentCategory() != null ? jobCategory.getParentCategory().getId() : null)
                .parentName(jobCategory.getParentCategory() != null ? jobCategory.getParentCategory().getName() : null)
                .subCategories(subCategories)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
