package com.globalco.services;

import com.globalco.domain.ApplicationStatus;
import com.globalco.dto.response.ApplicationResponse;
import com.globalco.models.Application;
import com.globalco.payload.CompanyApplicationFilterRequest;
import com.globalco.payload.CreateApplicationRequest;
import com.globalco.payload.WithdrawApplicationRequest;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse createApplication(Long candidateId, CreateApplicationRequest req);
    ApplicationResponse getApplicationById(Long id);
    List<ApplicationResponse> getAllApplications(Long candidateId);
    List<ApplicationResponse> getApplicationsForJob(Long jobId);
    List<ApplicationResponse> getApplicationsForCompany(Long userId, CompanyApplicationFilterRequest request);
    ApplicationResponse updateStatus(Long applicationId, Long employerId, ApplicationStatus status);
    ApplicationResponse withdraw(Long applicationId, Long candidateId, WithdrawApplicationRequest req);
    ApplicationResponse toggleStar(Long applicationId, Long employerId);
    void deleteApplication(Long applicationId, Long candidateId);
    Application getApplicationEntity(Long id);
}
