package com.globalco.controller;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.AwardResponse;
import com.globalco.payload.AddAwardRequest;
import com.globalco.services.AwardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/awards")
public class AwardController {
    private final AwardService awardService;
    @PostMapping("/add")
    public ResponseEntity<AwardResponse> addAward(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddAwardRequest awardRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(awardService.addAward(resumeId, candidateId, awardRequest));
    }
    @GetMapping("/all")
    public ResponseEntity<List<AwardResponse>> getAllAwards(
            @PathVariable Long resumeId
    ) {
        return ResponseEntity.ok(awardService.getAllAwards(resumeId));
    }
    @PutMapping("/update/{awardId}")
    public ResponseEntity<AwardResponse> updateAward(
            @PathVariable Long awardId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddAwardRequest awardRequest
    ) {
        return ResponseEntity.ok(awardService.updateAward(resumeId, candidateId, awardId, awardRequest));
    }
    @DeleteMapping("/delete/{awardId}")
    public ResponseEntity<ApiResponse> deleteAward(
            @PathVariable Long awardId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        awardService.deleteAward(resumeId, candidateId, awardId);
        return ResponseEntity.ok(new ApiResponse("Award deleted successfully", true));
    }
}
