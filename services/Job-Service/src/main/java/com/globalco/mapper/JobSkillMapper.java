package com.globalco.mapper;

import com.globalco.dto.response.JobSkillResponse;
import com.globalco.models.JobSkill;

public class JobSkillMapper {
    public static JobSkillResponse toJobSkillResponse(JobSkill skill) {
        return JobSkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .slug(skill.getSlug())
                .category(skill.getCategory())
                .active(skill.getActive())
                .build();
    }
}
