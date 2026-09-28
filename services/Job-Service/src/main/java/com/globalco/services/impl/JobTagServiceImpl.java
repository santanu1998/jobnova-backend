package com.globalco.services.impl;

import com.globalco.dto.response.JobTagResponse;
import com.globalco.mapper.JobTagMapper;
import com.globalco.models.JobTag;
import com.globalco.payload.JobTagRequest;
import com.globalco.repositories.JobTagRepository;
import com.globalco.services.JobTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobTagServiceImpl implements JobTagService {
    private final JobTagRepository jobTagRepository;

    @Override
    public JobTagResponse createJobTag(JobTagRequest jobTagRequest) {
        if (jobTagRepository.existsByName(jobTagRequest.getName())) {
            throw new IllegalArgumentException("Job tag with the same name already exists.");
        }
        String slug = generateUniqueSlug(jobTagRequest.getName());
        JobTag jobTag = JobTag.builder()
                .name(jobTagRequest.getName())
                .slug(slug)
                .build();
        JobTag savedJobTag = jobTagRepository.save(jobTag);
        return JobTagMapper.toJobTagResponse(savedJobTag);
    }

    private String generateUniqueSlug(String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]+", "")
                .trim()
                .replaceAll("[\\s-]+", "-");
        if (!jobTagRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while (jobTagRepository.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }

    @Override
    public List<JobTagResponse> getAllJobTags() {
        return jobTagRepository.findAll().stream()
                .map(JobTagMapper::toJobTagResponse)
                .toList();
    }

    @Override
    public JobTagResponse getJobTagById(Long id) {
        JobTag jobTag = getJobTagEntityById(id);
        return JobTagMapper.toJobTagResponse(jobTag);
    }

    @Override
    public JobTagResponse updateJobTag(Long id, JobTagRequest jobTagRequest) {
        JobTag jobTag = getJobTagEntityById(id);
        if (!jobTag.getName().equals(jobTagRequest.getName()) &&
                jobTagRepository.existsByName(jobTagRequest.getName())) {
            throw new IllegalArgumentException("Job tag with the same name already exists.");
        }
        jobTag.setName(jobTagRequest.getName());
        jobTag.setSlug(generateUniqueSlug(jobTagRequest.getName()));
        JobTag updatedJobTag = jobTagRepository.save(jobTag);
        return JobTagMapper.toJobTagResponse(updatedJobTag);
    }

    @Override
    public void deleteJobTag(Long id) {
        JobTag jobTag = getJobTagEntityById(id);
        jobTagRepository.delete(jobTag);
    }

    @Override
    public JobTag getJobTagEntityById(Long id) {
        return jobTagRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Job tag not found"));
    }

    @Override
    public Set<JobTag> getJobTagEntitiesByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        // Return only the tags that exist, skip missing ones
        return new HashSet<>(jobTagRepository.findAllById(ids));
    }
}
