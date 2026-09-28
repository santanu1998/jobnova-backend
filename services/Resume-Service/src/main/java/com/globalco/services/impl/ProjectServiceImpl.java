package com.globalco.services.impl;

import com.globalco.dto.response.ProjectResponse;
import com.globalco.mapper.ResumeMapper;
import com.globalco.models.Project;
import com.globalco.models.Resume;
import com.globalco.payload.AddProjectRequest;
import com.globalco.repositories.ProjectRepository;
import com.globalco.services.ProjectService;
import com.globalco.services.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ResumeService resumeService;
    private final ProjectRepository projectRepository;

    @Override
    public ProjectResponse addProject(Long resumeId, Long candidateId, AddProjectRequest projectRequest) {
        Resume resume = resumeService.getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        Project project = Project.builder()
                .resume(resume)
                .title(projectRequest.getTitle())
                .description(projectRequest.getDescription())
                .technologies(projectRequest.getTechnologies() != null ? projectRequest.getTechnologies() : List.of())
                .projectUrl(projectRequest.getProjectUrl())
                .sourceCodeUrl(projectRequest.getSourceCodeUrl())
                .startDate(projectRequest.getStartDate())
                .endDate(projectRequest.getEndDate())
                .isOngoing(Boolean.TRUE.equals(projectRequest.getIsOngoing()))
                .displayOrder(projectRequest.getDisplayOrder() != null ? projectRequest.getDisplayOrder() : 0)
                .build();
        Project savedProject = projectRepository.save(project);
        return ResumeMapper.toProjectResponse(savedProject);
    }

    private void assertOwner(Resume resume, Long candidateId) {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new IllegalArgumentException("Candidate with ID " + candidateId + " is not the owner of resume with ID " + resume.getId());
        }
    }

    @Override
    public List<ProjectResponse> getAllProjects(Long resumeId) {
        return projectRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toProjectResponse)
                .toList();
    }

    @Override
    public ProjectResponse updateProject(Long projectId, Long resumeId, Long candidateId, AddProjectRequest projectRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " not found"));
        assertOwner(project.getResume(), candidateId);
        project.setTitle(projectRequest.getTitle());
        project.setDescription(projectRequest.getDescription());
        project.setTechnologies(projectRequest.getTechnologies() != null ? projectRequest.getTechnologies() : List.of());
        project.setProjectUrl(projectRequest.getProjectUrl());
        project.setSourceCodeUrl(projectRequest.getSourceCodeUrl());
        project.setStartDate(projectRequest.getStartDate());
        project.setEndDate(projectRequest.getEndDate());
        project.setIsOngoing(Boolean.TRUE.equals(projectRequest.getIsOngoing()));
        project.setDisplayOrder(projectRequest.getDisplayOrder() != null ? projectRequest.getDisplayOrder() : 0);
        Project updatedProject = projectRepository.save(project);
        return ResumeMapper.toProjectResponse(updatedProject);
    }

    @Override
    public void deleteProject(Long projectId, Long resumeId, Long candidateId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " not found"));
        assertOwner(project.getResume(), candidateId);
        projectRepository.delete(project);
    }
}
