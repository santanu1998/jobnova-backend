package com.globalco.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequest {
    @NotBlank(message = "Job title is required")
    private String title;
    @NotBlank(message = "Job description is required")
    private String description;
    private String requirements;
    private String responsibilities;
    private String benefits;
    @NotNull(message = "Company is required")
    private Long companyId;
    @NotNull(message = "Category is required")
    private Long categoryId;
    private Set<Long> skillIds;
    private Set<Long> tagIds;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum salary must be greater than 0")
    private BigDecimal minSalary;
    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum salary must be greater than 0")
    private BigDecimal maxSalary;
//    private String currency;
//    private SalaryPeriod salaryPeriod;
//    private Boolean salaryNegotiable;
//    private Boolean salaryDisclosed;
    @NotNull(message = "Job type is required")
    private String jobType;
    @NotNull(message = "Work mode is required")
    private String workMode;
    @NotNull(message = "Experience level is required")
    private String experienceLevel;
    @Min(value = 1, message = "Openings must be at least 1")
    private Integer openings = 1;
    private String applicationDeadline;
    private String expiresAt;
}
