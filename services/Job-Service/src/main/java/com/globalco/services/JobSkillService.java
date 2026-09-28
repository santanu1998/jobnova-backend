package com.globalco.services;

import com.globalco.dto.response.JobSkillResponse;
import com.globalco.models.JobSkill;
import com.globalco.payload.JobSkillRequest;

import java.util.List;
import java.util.Set;

public interface JobSkillService {
    JobSkillResponse createJobSkill(JobSkillRequest request);
    List<JobSkillResponse> getAllJobSkills();
    JobSkillResponse getJobSkillById(Long id);
    JobSkillResponse updateJobSkill(Long id, JobSkillRequest request);
    void deleteJobSkill(Long id);
    Set<JobSkill> getJobSkillEntitiesByIds(Set<Long> ids);
}
