package com.globalco.services.impl;

import feign.FeignException;

import com.globalco.client.CompanyClient;
import com.globalco.domain.ExperienceLevel;
import com.globalco.domain.JobStatus;
import com.globalco.domain.JobType;
import com.globalco.domain.WorkMode;
import com.globalco.dto.request.JobRequest;
import com.globalco.dto.response.CompanyResponse;
import com.globalco.dto.response.JobResponse;
import com.globalco.mapper.JobMapper;
import com.globalco.models.Job;
import com.globalco.models.JobCategory;
import com.globalco.models.JobSkill;
import com.globalco.models.JobTag;
import com.globalco.models.embeddable.JobLocation;
import com.globalco.models.embeddable.SalaryRange;
import com.globalco.payload.JobSearchRequest;
import com.globalco.repositories.JobRepository;
import com.globalco.repositories.JobSpecification;
import com.globalco.services.JobCategoryService;
import com.globalco.services.JobService;
import com.globalco.services.JobSkillService;
import com.globalco.services.JobTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final JobCategoryService jobCategoryService;
    private final JobSkillService jobSkillService;
    private final JobTagService jobTagService;
    private final CompanyClient companyClient;

    @Override
    @Transactional
    public JobResponse createJob(Long employerId, JobRequest jobRequest) {
        JobCategory jobCategory = jobCategoryService.getJobCategoryEntityById(jobRequest.getCategoryId());
        Set<JobSkill> jobSkills = jobRequest.getSkillIds() != null ?
                jobSkillService.getJobSkillEntitiesByIds(jobRequest.getSkillIds()) : Collections.emptySet();
        Set<JobTag> jobTags = jobRequest.getTagIds() != null ?
                jobTagService.getJobTagEntitiesByIds(jobRequest.getTagIds()) : Collections.emptySet();
        // use companyId provided by client (added to JobRequest) and ensure nullable fields have safe defaults
        CompanyResponse company = companyClient.getMyCompany(employerId);
        Long companyId = company.getId();
        Job job = Job.builder()
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .requirements(jobRequest.getRequirements() != null ? jobRequest.getRequirements() : "")
                .responsibilities(jobRequest.getResponsibilities() != null ? jobRequest.getResponsibilities() : "")
                .benefits(jobRequest.getBenefits() != null ? jobRequest.getBenefits() : "")
                .companyId(companyId)
                .employerId(employerId)
                .category(jobCategory)
                .skills(jobSkills)
                .tags(jobTags)
                .location(buildLocation(jobRequest))
                .salaryRange(buildSalaryRange(jobRequest))
                .jobType(JobType.valueOf(jobRequest.getJobType()))
                .workMode(WorkMode.valueOf(jobRequest.getWorkMode()))
                .experienceLevel(ExperienceLevel.valueOf(jobRequest.getExperienceLevel()))
                .status(JobStatus.DRAFT)
                .openings(jobRequest.getOpenings() != null ? jobRequest.getOpenings() : 1)
                // parse dates only when provided to avoid NPE/DateTimeParseException
                .applicationDeadline(parseDate(jobRequest.getApplicationDeadline()))
                .expiresAt(parseDate(jobRequest.getExpiresAt()))
                .isActive(true)
                .build();
        Job savedJob = jobRepository.save(job);
        return convertToResponse(savedJob);
    }

    private JobResponse convertToResponse(Job savedJob) {
        // a deleted company must not break job listings: return the job without company details
        CompanyResponse companyResponse = null;
        try {
            companyResponse = companyClient.getCompanyById(savedJob.getCompanyId());
        } catch (FeignException.NotFound e) {
            // company no longer exists
        }
        return JobMapper.toResponse(savedJob, companyResponse);
    }

    // optional "yyyy-MM-dd" dates: blank/null -> null
    private LocalDate parseDate(String value) {
        return value != null && !value.isBlank() ? LocalDate.parse(value) : null;
    }

    private SalaryRange buildSalaryRange(JobRequest jobRequest) {
        return SalaryRange.builder()
                .minSalary(jobRequest.getMinSalary())
                .maxSalary(jobRequest.getMaxSalary())
                .build();
    }

    private JobLocation buildLocation(JobRequest jobRequest) {
        return JobLocation.builder()
                .address(jobRequest.getAddress())
                .city(jobRequest.getCity())
                .state(jobRequest.getState())
                .country(jobRequest.getCountry())
                .postalCode(jobRequest.getPostalCode())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new RuntimeException("Job not found with id: " + jobId)
        );
        return convertToResponse(job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs(JobSearchRequest jobSearchRequest) {
        List<Job> jobs = jobRepository.findAll(JobSpecification.build(jobSearchRequest));
        // public search: hide jobs whose company has been deleted
        return jobs.stream().
                map(this::convertToResponse).
                filter(job -> job.getCompany() != null).
                collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> getJobsByCompany(Long companyId) {
        List<Job> jobs = jobRepository.findByCompanyId(companyId);
        return jobs.stream().
                map(this::convertToResponse).
                collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobResponse updateJob(Long jobId, Long employerId, JobRequest jobRequest) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new RuntimeException("Job not found with id: " + jobId)
        );
        JobCategory jobCategory = jobCategoryService.getJobCategoryEntityById(jobRequest.getCategoryId());
        Set<JobSkill> jobSkills = jobRequest.getSkillIds() != null ?
                jobSkillService.getJobSkillEntitiesByIds(jobRequest.getSkillIds()) : Collections.emptySet();
        Set<JobTag> jobTags = jobRequest.getTagIds() != null ?
                jobTagService.getJobTagEntitiesByIds(jobRequest.getTagIds()) : Collections.emptySet();
        assertEmployer(job, employerId);
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        // same defaults as createJob: these columns are NOT NULL
        job.setRequirements(jobRequest.getRequirements() != null ? jobRequest.getRequirements() : "");
        job.setResponsibilities(jobRequest.getResponsibilities() != null ? jobRequest.getResponsibilities() : "");
        job.setBenefits(jobRequest.getBenefits() != null ? jobRequest.getBenefits() : "");
        job.setCategory(jobCategory);
        job.setSkills(jobSkills);
        job.setTags(jobTags);
        job.setLocation(buildLocation(jobRequest));
        job.setSalaryRange(buildSalaryRange(jobRequest));
        job.setJobType(JobType.valueOf(jobRequest.getJobType()));
        job.setWorkMode(WorkMode.valueOf(jobRequest.getWorkMode()));
        job.setExperienceLevel(ExperienceLevel.valueOf(jobRequest.getExperienceLevel()));
        job.setOpenings(jobRequest.getOpenings() != null ? jobRequest.getOpenings() : 1);
        job.setApplicationDeadline(parseDate(jobRequest.getApplicationDeadline()));
        job.setExpiresAt(parseDate(jobRequest.getExpiresAt()));
        return convertToResponse(jobRepository.save(job));
    }

    @Override
    @Transactional
    public JobResponse publishJob(Long jobId, Long employerId) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new RuntimeException("Job not found with id: " + jobId)
        );
        assertEmployer(job, employerId);
        if (job.getStatus() == JobStatus.CLOSED || job.getStatus() == JobStatus.EXPIRED) {
            throw new RuntimeException("Cannot publish a closed or expired job");
        }
        job.setStatus(JobStatus.OPEN);
        job.setPublishedAt(LocalDate.now());
        job.setIsActive(true);
        return convertToResponse(jobRepository.save(job));
    }

    private void assertEmployer(Job job, Long employerId) {
        if (!job.getEmployerId().equals(employerId)) {
            throw new RuntimeException("Employer is not authorized to publish this job");
        }
    }


    @Override
    @Transactional
    public JobResponse closeJob(Long jobId, Long employerId) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new RuntimeException("Job not found with id: " + jobId)
        );
        assertEmployer(job, employerId);
        job.setStatus(JobStatus.CLOSED);
        job.setClosedAt(LocalDate.now());
        job.setIsActive(false);
        return convertToResponse(jobRepository.save(job));
    }

    @Override
    @Transactional
    public void deleteJob(Long jobId, Long employerId) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new RuntimeException("Job not found with id: " + jobId)
        );
        assertEmployer(job, employerId);
        jobRepository.delete(job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobsAdmin(JobSearchRequest jobSearchRequest) {
        return jobRepository.findAll(JobSpecification.build(jobSearchRequest)).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}
