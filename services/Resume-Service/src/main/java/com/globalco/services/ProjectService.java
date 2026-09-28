package com.globalco.services;

import com.globalco.dto.response.ProjectResponse;
import com.globalco.payload.AddProjectRequest;

import java.util.List;

public interface ProjectService {
    ProjectResponse addProject(Long resumeId, Long candidateId, AddProjectRequest projectRequest);
    List<ProjectResponse> getAllProjects(Long resumeId);
    ProjectResponse updateProject(Long projectId, Long resumeId, Long candidateId, AddProjectRequest projectRequest);
    void deleteProject(Long projectId, Long resumeId, Long candidateId);
}
