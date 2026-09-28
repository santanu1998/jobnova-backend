package com.globalco.mapper;

import com.globalco.dto.response.CompanyResponse;
import com.globalco.dto.response.JobResponse;
import com.globalco.dto.response.JobSkillResponse;
import com.globalco.dto.response.JobTagResponse;
import com.globalco.models.Job;
import com.globalco.models.embeddable.JobLocation;
import com.globalco.models.embeddable.SalaryRange;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class JobMapper {
    public static JobResponse toResponse(Job job, CompanyResponse companyResponse) {
        JobLocation loc = job.getLocation();
        SalaryRange sal = job.getSalaryRange();
        Set<JobSkillResponse> skillResponses = job.getSkills() == null ? Collections.emptySet()
                : job.getSkills().stream()
                .map(JobSkillMapper :: toJobSkillResponse)
                .collect(Collectors.toSet());
        Set<JobTagResponse> tagResponses = job.getTags() == null ? Collections.emptySet()
                : job.getTags().stream()
                .map(JobTagMapper :: toJobTagResponse)
                .collect(Collectors.toSet());

        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .responsibilities(job.getResponsibilities())
                .benefits(job.getBenefits())
                .employerId(job.getEmployerId())
                .company(companyResponse)
                .employerId(job.getEmployerId())
                .category(JobCategoryMapper.toJobCategoryResponse(job.getCategory(), false))
                .skills(skillResponses)
                .tags(tagResponses)
                .address(loc != null ? loc.getAddress() : null)
                .city(loc != null ? loc.getCity() : null)
                .state(loc != null ? loc.getState() : null)
                .country(loc != null ? loc.getCountry() : null)
                .postalCode(loc != null ? loc.getPostalCode() : null)
                .minSalary(sal != null ? sal.getMinSalary() : null)
                .maxSalary(sal != null ? sal.getMaxSalary() : null)
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .experienceLevel(job.getExperienceLevel())
                .status(job.getStatus())
                .openings(job.getOpenings())
                .applicationDeadline(job.getApplicationDeadline())
                .expiresAt(job.getExpiresAt())
                .isActive(job.getIsActive())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .publishedAt(job.getPublishedAt())
                .closedAt(job.getClosedAt())
                .build();
    }
}
