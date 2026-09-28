package com.globalco.payload;

import com.globalco.domain.ExperienceLevel;
import com.globalco.domain.JobStatus;
import com.globalco.domain.JobType;
import com.globalco.domain.WorkMode;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSearchRequest {
    private String keyword;
    private Long categoryId;
    private List<Long> skillIds;
    private List<Long> tagIds;
    private Long companyId;
    private String location;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private JobType jobType;
    private WorkMode workMode;
    private ExperienceLevel experienceLevel;
    private JobStatus status;
    private Integer minOpenings;
    private Integer maxOpenings;
}
