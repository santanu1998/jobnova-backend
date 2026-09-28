package com.globalco.services.impl;

import com.globalco.client.CompanyClient;
import com.globalco.client.JobClient;
import com.globalco.client.ResumeClient;
import com.globalco.client.UserClient;
import com.globalco.domain.ApplicationStatus;
import com.globalco.dto.response.*;
import com.globalco.exception.UnauthorizedActionException;
import com.globalco.mapper.ApplicationMapper;
import com.globalco.models.Application;
import com.globalco.models.ApplicationNote;
import com.globalco.payload.CompanyApplicationFilterRequest;
import com.globalco.payload.CreateApplicationRequest;
import com.globalco.payload.WithdrawApplicationRequest;
import com.globalco.repositories.ApplicationNoteRepository;
import com.globalco.repositories.ApplicationRepository;
import com.globalco.repositories.ApplicationSpecification;
import com.globalco.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationNoteRepository applicationNoteRepository;
    private final JobClient jobClient;
    private final ResumeClient resumeClient;
    private final CompanyClient companyClient;
    private final UserClient userClient;
//    private final JobClient jobClient;

    @Override
    public ApplicationResponse createApplication(Long candidateId, CreateApplicationRequest req) {
        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, req.getJobId())) {
            throw new IllegalArgumentException("Application already exists for this candidate and job.");
        }
        // use employerId and companyId provided by client (added to request)
        JobResponse job = jobClient.getJobById(req.getJobId());
        Long companyId = job.getCompany().getId();
        Long employerId = job.getEmployerId();
        ResumeResponse resume = resumeClient.getResumeById(req.getResumeId(), candidateId);
        Application application = ApplicationMapper.toEntity(
                req,
                candidateId,
                companyId,
                employerId
        );
        Application savedApplication = applicationRepository.save(application);
        return buildFullResponse(savedApplication);
    }

    public ApplicationResponse buildFullResponse(Application application) {
        JobResponse job = jobClient.getJobById(application.getJobId());
        CompanyResponse company = companyClient.getCompanyById(application.getCompanyId());
        UserResponse candidate = userClient.getUserById(application.getCandidateId()); // Replace with actual call to userClient
//        List<ApplicationNote> notes = List.of(); // Replace with actual call to applicationNoteRepository
//        ApplicationScreening screening = ApplicationScreening.builder().build(); // Replace with actual call to applicationScreeningRepository
        List<ApplicationNote> notes = applicationNoteRepository
                .findByApplicationId(application.getId());
        return ApplicationMapper.toResponse(
                application,
                notes,
                job,
                company,
                candidate
        );
    }

    @Override
    public ApplicationResponse getApplicationById(Long id) {
        Application application = getApplicationEntity(id);
        return buildFullResponse(application);
    }

    @Override
    public List<ApplicationResponse> getAllApplications(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId)
                .stream()
                .map(this::buildFullResponse)
                .toList();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobId(jobId)
                .stream()
                .map(this::buildFullResponse)
                .toList();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForCompany(Long userId, CompanyApplicationFilterRequest request) {
        Long companyId = companyClient.getMyCompany(userId).getId();
        Sort sort = buildSort(request.getSortBy());
        return applicationRepository.findAll(ApplicationSpecification
                        .forCompanyWithFilters(
                                companyId,
                                request.getJobId(),
                                request.getStatus(),
                                request.getIsStarred(),
                                request.getAiShortListStatus(),
                                request.getMinAiScore()
                        ), sort)
                .stream()
                .map(this::buildFullResponse)
                .toList();
    }

    private Sort buildSort(String sortBy) {
        if("AI_SCORE_DESC".equals(sortBy)){
            return Sort.by(Sort.Order.desc("aiScore").with(Sort.NullHandling.NULLS_LAST));
        }
        else if("AI_SCORE_ASC".equals(sortBy)){
            return Sort.by(Sort.Order.asc("aiScore").with(Sort.NullHandling.NULLS_LAST));
        }
        return Sort.by(Sort.Direction.DESC, "appliedAt");
    }

    @Override
    public ApplicationResponse updateStatus(Long applicationId, Long employerId, ApplicationStatus status) {
        Application application=getApplicationEntity(applicationId);
//        ApplicationStatus oldStatus=application.getStatus();
        assertEmployer(application,employerId);
        if(application.getStatus()==ApplicationStatus.WITHDRAWN){
            throw new IllegalArgumentException("candidate have already withdrawn");
        }
        application.setStatus(status);
        Application savedApplication= applicationRepository.save(application);
//        applicationEventPublisher.publishStatusChange(application,
//                oldStatus,status,
//                "your application status get changed");
        return buildFullResponse(savedApplication);
    }

    private void assertEmployer(Application application, Long employerId) {
        if (application.getEmployerId() == null || !application.getEmployerId().equals(employerId)) {
            throw new UnauthorizedActionException("You are not the employer for this application.");
        }
    }

    @Override
    public ApplicationResponse withdraw(Long applicationId, Long candidateId, WithdrawApplicationRequest req) {
        Application application=getApplicationEntity(applicationId);
        assertCandidate(application,candidateId);
        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setWithdrawnReason(req.getReason());
        Application savedApplication= applicationRepository.save(application);
        return buildFullResponse(savedApplication);
    }

    private void assertCandidate(Application application, Long candidateId) {
        if(!application.getCandidateId().equals(candidateId)) {
            throw new IllegalArgumentException("You are not the candidate for this application.");
        }
    }

    @Override
    public ApplicationResponse toggleStar(Long applicationId, Long employerId) {
        Application application=getApplicationEntity(applicationId);
        assertEmployer(application,employerId);
        if(application.getIsStarred()==null){
            application.setIsStarred(true);
        }
        application.setIsStarred(!application.getIsStarred());
        Application savedApplication= applicationRepository.save(application);
        return buildFullResponse(savedApplication);
    }

    @Override
    public void deleteApplication(Long applicationId, Long candidateId) {
        Application application=getApplicationEntity(applicationId);
        assertCandidate(application,candidateId);
        applicationRepository.delete(application);
    }

    @Override
    public Application getApplicationEntity(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + id));
    }
}
