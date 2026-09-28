package com.globalco.services;

import com.globalco.dto.response.CertificationResponse;
import com.globalco.payload.AddCertificationRequest;

import java.util.List;

public interface CertificationService {
    CertificationResponse addCertification(Long resumeId, Long candidateId, AddCertificationRequest certificationRequest);
    List<CertificationResponse> getAllCertifications(Long resumeId);
    CertificationResponse updateCertification(
            Long resumeId, Long candidateId, Long certificationId, AddCertificationRequest certificationRequest);
    void deleteCertification(Long resumeId, Long candidateId, Long certificationId);
}
