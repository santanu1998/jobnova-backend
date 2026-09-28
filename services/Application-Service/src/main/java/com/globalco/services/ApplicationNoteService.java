package com.globalco.services;

import com.globalco.dto.response.ApplicationNoteResponse;
import com.globalco.payload.AddApplicationNoteRequest;

import java.util.List;

public interface ApplicationNoteService {
    ApplicationNoteResponse addNote(
            Long applicationId, Long employerId, AddApplicationNoteRequest request
    );

    List<ApplicationNoteResponse> getNotesByApplication(
            Long applicationId, Long employerId
    );

    void deleteNote(Long applicationId, Long noteId, Long employerId);
}
