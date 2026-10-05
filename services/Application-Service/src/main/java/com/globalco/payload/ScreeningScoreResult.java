package com.globalco.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

// Mirrors AI-Service's ScreeningScoreResponse
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScreeningScoreResult {
    private int score;
    private int skillsMatchScore;
    private int experienceMatchScore;
    private int educationMatchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<String> strengths;
    private List<String> concerns;
    private String summary;
}
