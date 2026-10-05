package com.globalco.services;

import com.globalco.client.AiClient;
import com.globalco.client.JobClient;
import com.globalco.client.ResumeClient;
import com.globalco.domain.AiShortListStatus;
import com.globalco.dto.response.ApplicationScreeningResponse;
import com.globalco.dto.response.JobResponse;
import com.globalco.dto.response.JobSkillResponse;
import com.globalco.dto.response.ResumeResponse;
import com.globalco.dto.response.ResumeSkillResponse;
import com.globalco.dto.response.WorkExperienceResponse;
import com.globalco.models.Application;
import com.globalco.payload.ScreeningScoreRequest;
import com.globalco.payload.ScreeningScoreResult;
import com.globalco.repositories.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Scores an application against its job with AI-Service and stores the
 * result (aiScore + aishortListStatus) on the application.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationScreeningService {

    private final ApplicationRepository applicationRepository;
    private final JobClient jobClient;
    private final ResumeClient resumeClient;
    private final AiClient aiClient;

    /** Fire-and-forget screening right after a candidate applies. */
    @Async("screeningExecutor")
    public void screenInBackground(Long applicationId) {
        try {
            applicationRepository.findById(applicationId).ifPresent(this::screen);
        } catch (Exception e) {
            // the application stays NOT_SCREENED; employers can re-run screening later
            log.warn("AI screening failed for application {}: {}", applicationId, e.getMessage());
        }
    }

    /** Scores the application, saves the score and returns the full screening result. */
    public ApplicationScreeningResponse screen(Application application) {
        JobResponse job = jobClient.getJobById(application.getJobId());
        ResumeResponse resume = resumeClient.getResumeById(application.getResumeId(), application.getCandidateId());

        ScreeningScoreResult result = aiClient.scoreCandidate(buildRequest(application, job, resume));
        AiShortListStatus status = toShortListStatus(result.getScore());

        application.setAiScore(result.getScore());
        application.setAishortListStatus(status);
        applicationRepository.save(application);

        return ApplicationScreeningResponse.builder()
                .id(application.getId())
                .overallScore(result.getScore())
                .skillsMatchScore(result.getSkillsMatchScore())
                .experienceMatchScore(result.getExperienceMatchScore())
                .educationMatchScore(result.getEducationMatchScore())
                .shortlistStatus(status)
                .matchedSkills(result.getMatchedSkills())
                .missingSkills(result.getMissingSkills())
                .strengths(result.getStrengths())
                .concerns(result.getConcerns())
                .summary(result.getSummary())
                .screenedAt(LocalDateTime.now())
                .build();
    }

    public static AiShortListStatus toShortListStatus(int score) {
        if (score >= 80) return AiShortListStatus.AUTO_SHORTLISTED;
        if (score >= 60) return AiShortListStatus.REVIEW_RECOMMENDED;
        if (score >= 40) return AiShortListStatus.PENDING_REVIEW;
        return AiShortListStatus.LOW_MATCH;
    }

    private ScreeningScoreRequest buildRequest(Application application, JobResponse job, ResumeResponse resume) {
        List<String> requiredSkills = job.getSkills() == null ? List.of()
                : job.getSkills().stream().map(JobSkillResponse::getName).toList();

        List<String> candidateSkills = resume.getSkills() == null ? List.of()
                : resume.getSkills().stream().map(ResumeSkillResponse::getSkillName).toList();

        List<String> candidateExperience = resume.getWorkExperiences() == null ? List.of()
                : resume.getWorkExperiences().stream().map(this::describe).toList();

        String summary = Stream.of(
                        resume.getPersonalInfo() != null ? resume.getPersonalInfo().getHeadline() : null,
                        resume.getSummary(),
                        application.getCoverLetter())
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining("\n\n"));

        return ScreeningScoreRequest.builder()
                .jobTitle(job.getTitle())
                .experienceLevel(job.getExperienceLevel() != null ? job.getExperienceLevel().name() : null)
                .requiredSkills(requiredSkills)
                .responsibilities(Stream.of(job.getResponsibilities(), job.getRequirements())
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining("\n")))
                .candidateSummary(summary)
                .candidateSkills(candidateSkills)
                .candidateExperience(candidateExperience)
                .build();
    }

    private String describe(WorkExperienceResponse e) {
        String period = (e.getStartDate() != null ? e.getStartDate().toString() : "?") + " - "
                + (Boolean.TRUE.equals(e.getIsCurrentlyWorking()) ? "present"
                : e.getEndDate() != null ? e.getEndDate().toString() : "?");
        return e.getJobTitle() + " at " + e.getCompanyName() + " (" + period + ")";
    }
}
