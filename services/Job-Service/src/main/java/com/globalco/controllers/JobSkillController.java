package com.globalco.controllers;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.JobSkillResponse;
import com.globalco.payload.JobSkillRequest;
import com.globalco.services.JobSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-skills")
@RequiredArgsConstructor
public class JobSkillController {
    private final JobSkillService jobSkillService;
    @PostMapping("/create")
    public ResponseEntity<JobSkillResponse> createJobSkill(
            @RequestBody @Valid JobSkillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobSkillService.createJobSkill(request));
    }
    @GetMapping("/all")
    public ResponseEntity<List<JobSkillResponse>> getAllJobSkills() {
        return ResponseEntity.ok(jobSkillService.getAllJobSkills());
    }
    @GetMapping("/{id}")
    public ResponseEntity<JobSkillResponse> getJobSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(jobSkillService.getJobSkillById(id));
    }
    @PutMapping("update/{id}")
    public ResponseEntity<JobSkillResponse> updateJobSkill(@PathVariable Long id,
            @RequestBody @Valid JobSkillRequest request) {
        return ResponseEntity.ok(jobSkillService.updateJobSkill(id, request));
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteJobSkill(@PathVariable Long id) {
        jobSkillService.deleteJobSkill(id);
        return ResponseEntity.ok(new ApiResponse("Job skill deleted successfully", true));
    }
}
