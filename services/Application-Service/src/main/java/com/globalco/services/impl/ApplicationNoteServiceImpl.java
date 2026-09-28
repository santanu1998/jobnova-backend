package com.globalco.services.impl;

import com.globalco.dto.response.ApplicationNoteResponse;
import com.globalco.mapper.ApplicationMapper;
import com.globalco.models.Application;
import com.globalco.models.ApplicationNote;
import com.globalco.payload.AddApplicationNoteRequest;
import com.globalco.repositories.ApplicationNoteRepository;
import com.globalco.services.ApplicationNoteService;
import com.globalco.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationNoteServiceImpl implements ApplicationNoteService {
    private final ApplicationService applicationService;
    private final ApplicationNoteRepository applicationNoteRepository;

    @Override
    public ApplicationNoteResponse addNote(Long applicationId, Long employerId, AddApplicationNoteRequest request) {
        Application application = applicationService.getApplicationEntity(applicationId);
        assertEmployer(application,employerId);
        ApplicationNote applicationNote=ApplicationNote.builder()
                .application(application)
                .addedByUserId(employerId)
                .content(request.getContent())
                .build();
        ApplicationNote savedNote=applicationNoteRepository.save(applicationNote);
        return ApplicationMapper.toNoteResponse(savedNote);
    }

    private void assertEmployer(Application application, Long employerId) {
        if (!application.getEmployerId().equals(employerId)) {
            throw new IllegalArgumentException("Employer not authorized to modify this application");
        }
    }

    @Override
    public List<ApplicationNoteResponse> getNotesByApplication(Long applicationId, Long employerId) {
        return applicationNoteRepository.findByApplicationId(applicationId)
                .stream().map(ApplicationMapper::toNoteResponse).toList();
    }

    @Override
    public void deleteNote(Long applicationId, Long noteId, Long employerId) {
        Application application = applicationService.getApplicationEntity(applicationId);
        assertEmployer(application, employerId);
        ApplicationNote note=applicationNoteRepository.findById(noteId)
                .orElseThrow(()-> new RuntimeException("Note does not belong to application")
        );
        applicationNoteRepository.deleteById(noteId);
    }
}