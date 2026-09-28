package com.globalco.dto.response;


import com.globalco.domain.ExperienceLevel;
import com.globalco.domain.JobStatus;
import com.globalco.domain.JobType;
import com.globalco.domain.WorkMode;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String requirements;
    private String responsibilities;
    private String benefits;
    private CompanyResponse company;
    private Long employerId;
    private JobCategoryResponse category;
    private Set<JobSkillResponse> skills;
    private Set<JobTagResponse> tags;
    // Location
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    // Salary Range
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
//    private String currency;
//    private SalaryPeriod salaryPeriod;
//    private Boolean salaryNegotiable;
//    private Boolean salaryDisclosed;
    // Classifications
    private JobType jobType;
    private WorkMode workMode;
    private ExperienceLevel experienceLevel;
    private JobStatus status;
    // Posting Details
    private Integer openings;
    private LocalDate applicationDeadline;
    private LocalDate expiresAt;
    private Boolean isActive;
    // Analytics
//    private Long viewCount;
//    private Long applicationCount;
    // Timestamps
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate publishedAt;
    private LocalDate closedAt;
}
