package com.globalco.services;

import com.globalco.dto.response.JobTagResponse;
import com.globalco.models.JobTag;
import com.globalco.payload.JobTagRequest;

import java.util.List;
import java.util.Set;

public interface JobTagService {
    JobTagResponse createJobTag(JobTagRequest jobTagRequest);
    List<JobTagResponse> getAllJobTags();
    JobTagResponse getJobTagById(Long id);
    JobTagResponse updateJobTag(Long id, JobTagRequest jobTagRequest);
    void deleteJobTag(Long id);
    JobTag getJobTagEntityById(Long id);
    Set<JobTag> getJobTagEntitiesByIds(Set<Long> ids);
}
