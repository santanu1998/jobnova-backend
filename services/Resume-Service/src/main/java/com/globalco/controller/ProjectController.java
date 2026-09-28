package com.globalco.controller;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.ProjectResponse;
import com.globalco.payload.AddProjectRequest;
import com.globalco.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/projects")
public class ProjectController {
    private final ProjectService projectService;
    @PostMapping("/{resumeId}/add")
    public ResponseEntity<ProjectResponse> addProject(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddProjectRequest projectRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.addProject(resumeId, candidateId, projectRequest));
    }
    @GetMapping("/{resumeId}/all")
    public ResponseEntity<List<ProjectResponse>> getAllProjects(
            @PathVariable Long resumeId
    ) {
        return ResponseEntity.ok(projectService.getAllProjects(resumeId));
    }
    @PutMapping("/{resumeId}/update/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long projectId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddProjectRequest projectRequest
    ) {
        return ResponseEntity.ok(projectService.updateProject(projectId, resumeId, candidateId, projectRequest));
    }
    @DeleteMapping("/{resumeId}/delete/{projectId}")
    public ResponseEntity<ApiResponse> deleteProject(
            @PathVariable Long projectId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        projectService.deleteProject(projectId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Project deleted successfully", true));
    }
}
