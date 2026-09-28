package com.globalco.controller;

import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.CertificationResponse;
import com.globalco.payload.AddCertificationRequest;
import com.globalco.services.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/certifications")
public class CertificationController {
    private final CertificationService certificationService;
    @PostMapping("/add")
    public ResponseEntity<CertificationResponse> addCertification(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddCertificationRequest certificationRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(certificationService.addCertification(resumeId, candidateId, certificationRequest));
    }
    @GetMapping("/all")
    public ResponseEntity<List<CertificationResponse>> getAllCertifications(
            @PathVariable Long resumeId
    ) {
        return ResponseEntity.ok(certificationService.getAllCertifications(resumeId));
    }
    @PutMapping("/update/{certificationId}")
    public ResponseEntity<CertificationResponse> updateCertification(
            @PathVariable Long certificationId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddCertificationRequest certificationRequest
    ) {
        return ResponseEntity.ok(certificationService.updateCertification(resumeId, candidateId, certificationId, certificationRequest));
    }
    @DeleteMapping("/delete/{certificationId}")
    public ResponseEntity<ApiResponse> deleteCertification(
            @PathVariable Long certificationId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        certificationService.deleteCertification(resumeId, candidateId, certificationId);
        return ResponseEntity.ok(new ApiResponse("Certification deleted successfully", true));
    }
}