package com.globalco.controllers;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.ApplicationNoteResponse;
import com.globalco.payload.AddApplicationNoteRequest;
import com.globalco.services.ApplicationNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/application-notes")
@RequiredArgsConstructor
public class ApplicationNoteController {
    private final ApplicationNoteService applicationNoteService;
    @PostMapping("/{applicationId}/add")
    public ResponseEntity<ApplicationNoteResponse> addNote(
            @PathVariable Long applicationId,
            @RequestHeader("X-User-Id") Long employerId,
            @RequestBody @Valid AddApplicationNoteRequest req)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationNoteService.addNote(applicationId, employerId, req));
    }
    @GetMapping("/{applicationId}/all")
    public ResponseEntity<List<ApplicationNoteResponse>> getNotes(
            @PathVariable Long applicationId,
            @RequestHeader("X-User-Id") Long employerId)
    {
        return ResponseEntity.ok(applicationNoteService.getNotesByApplication(applicationId, employerId));
    }
    @DeleteMapping("/{applicationId}/delete/{noteId}")
    public ResponseEntity<ApiResponse> deleteNote(
            @PathVariable Long applicationId,
            @PathVariable Long noteId,
            @RequestHeader("X-User-Id") Long employerId)
            throws Exception {
        applicationNoteService.deleteNote(applicationId, noteId, employerId);
        return ResponseEntity.ok(new ApiResponse("Note deleted successfully", true));
    }
}
