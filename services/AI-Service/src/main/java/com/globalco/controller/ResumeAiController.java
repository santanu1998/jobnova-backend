package com.globalco.controller;

import com.globalco.payload.*;
import com.globalco.services.ResumeAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/resume")
public class ResumeAiController {

    private final ResumeAiService resumeAiService;

    @PostMapping("/summary")
    public ResponseEntity<AiTextResponse> generateSummary(
            @RequestBody ResumeSummaryRequest resumeSummaryRequest
            ) throws Exception {
        AiTextResponse response=resumeAiService.generateProfessionalSummary(resumeSummaryRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/experience-bullets")
    public ResponseEntity<WorkExperienceBulletsResponse> generateBullets(
            @Valid @RequestBody WorkExperienceBulletRequest request) throws Exception {
        WorkExperienceBulletsResponse response = resumeAiService.generateWorkExperienceBullets(request);
        return ResponseEntity.ok( response);
    }

    @PostMapping("/improvements")
    public ResponseEntity<ResumeImprovementResponse> getImprovements(
            @Valid @RequestBody ResumeImprovementRequest request) throws Exception {
        ResumeImprovementResponse response = resumeAiService.getResumeImprovementTips(request);
        return ResponseEntity.ok( response);
    }

    @PostMapping("/career-feedback")
    public ResponseEntity<CareerFeedbackResponse> getImprovements(
            @Valid @RequestBody CareerFeedbackRequest request) throws Exception {
        CareerFeedbackResponse response = resumeAiService.getCareerFeedback(request);
        return ResponseEntity.ok( response);
    }

}
