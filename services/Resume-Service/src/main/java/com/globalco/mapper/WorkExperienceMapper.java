package com.globalco.mapper;

import com.globalco.dto.response.WorkExperienceResponse;
import com.globalco.models.WorkExperience;
import com.globalco.payload.AddWorkExperienceRequest;

public class WorkExperienceMapper {
    public static WorkExperienceResponse toWorkExperienceResponse(WorkExperience workExperience) {
        if (workExperience == null) {
            return null;
        }
        return WorkExperienceResponse.builder()
                .id(workExperience.getId())
                .companyName(workExperience.getCompanyName())
                .companyLogoUrl(workExperience.getCompanyLogoUrl())
                .jobTitle(workExperience.getJobTitle())
                .employmentType(workExperience.getEmploymentType())
                .location(workExperience.getLocation())
                .startDate(workExperience.getStartDate())
                .endDate(workExperience.getEndDate())
                .isCurrentlyWorking(workExperience.getIsCurrentlyWorking())
                .jobDescription(workExperience.getJobDescription())
                .technologies(workExperience.getTechnologies())
                .displayOrder(workExperience.getDisplayOrder())
                .build();
    }
}
