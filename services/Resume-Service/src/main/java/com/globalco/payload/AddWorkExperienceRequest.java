package com.globalco.payload;

import com.globalco.domain.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddWorkExperienceRequest {
    @NotBlank(message = "Company name is required")
    private String companyName;
    private String companyLogoUrl;
    @NotBlank(message = "Job title is required")
    private String jobTitle;
    private JobType employmentType;
    private String location;
    @NotNull(message = "Start date is required")
    private String startDate; // Use String to accept date in "yyyy-MM-dd" format
    private String endDate; // Use String to accept date in "yyyy-MM-dd" format
    private Boolean isCurrentlyWorking = false;
    private String jobDescription;
    private List<String> technologies;
    private Integer displayOrder;
}
