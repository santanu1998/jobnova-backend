package com.globalco.models;

import com.globalco.domain.ExperienceLevel;
import com.globalco.domain.JobStatus;
import com.globalco.domain.JobType;
import com.globalco.domain.WorkMode;
import com.globalco.models.embeddable.JobLocation;
import com.globalco.models.embeddable.SalaryRange;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String requirements;
    @Column(nullable = false)
    private String responsibilities;
    @Column(nullable = false)
    private String benefits;
    @Column(nullable = false)
    private Long companyId;
    @Column(nullable = false)
    private Long employerId;
    @ManyToOne
    private JobCategory category;
    @ManyToMany
    private Set<JobSkill> skills;
    @ManyToMany
    private Set<JobTag> tags;
    @Embedded
    private JobLocation location;
    @Embedded
    private SalaryRange salaryRange;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkMode workMode;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperienceLevel experienceLevel;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status= JobStatus.DRAFT;
    private Integer openings = 1;
    private LocalDate applicationDeadline;
    private LocalDate expiresAt;
    private Boolean isActive = true;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDate updatedAt;
    private LocalDate publishedAt;
    private LocalDate closedAt;
}
