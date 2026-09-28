package com.globalco.services;

import com.globalco.dto.request.JobRequest;
import com.globalco.dto.response.JobResponse;
import com.globalco.payload.JobSearchRequest;

import java.util.List;

public interface JobService {
    JobResponse createJob(Long employerId, JobRequest jobRequest);
    JobResponse getJobById(Long jobId);
    List<JobResponse> getAllJobs(JobSearchRequest jobSearchRequest);
    List<JobResponse> getJobsByCompany(Long companyId);
    JobResponse updateJob(Long jobId, Long employerId, JobRequest jobRequest);
    JobResponse publishJob(Long jobId, Long employerId);
    JobResponse closeJob(Long jobId, Long employerId);
    void deleteJob(Long jobId, Long employerId);
//    void incrementApplicationCount(Long jobId);
    List<JobResponse> getAllJobsAdmin(JobSearchRequest jobSearchRequest);
}
