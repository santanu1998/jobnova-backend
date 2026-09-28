package com.globalco.dto.response;

import com.globalco.domain.AiShortListStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Result of an AI-driven resume/application screening pass
 * (produced by Application-Service, backed by AI-Service).
 */
@Data
@Builder
public class ApplicationScreeningResponse {

    private Long id;
    private int overallScore;
    private int skillsMatchScore;
    private int experienceMatchScore;
    private int educationMatchScore;
    private AiShortListStatus shortlistStatus;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<String> strengths;
    private List<String> concerns;
    private String summary;
    private LocalDateTime screenedAt;
}
