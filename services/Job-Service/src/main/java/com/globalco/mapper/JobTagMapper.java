package com.globalco.mapper;

import com.globalco.dto.response.JobTagResponse;
import com.globalco.models.JobTag;

public class JobTagMapper {
    public static JobTagResponse toJobTagResponse(JobTag tag) {
        return JobTagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .slug(tag.getSlug())
                .build();
    }
}
