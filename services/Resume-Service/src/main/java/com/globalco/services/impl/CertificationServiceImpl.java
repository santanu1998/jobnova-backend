package com.globalco.services.impl;

import com.globalco.dto.response.CertificationResponse;
import com.globalco.mapper.ResumeMapper;
import com.globalco.models.Certification;
import com.globalco.models.Resume;
import com.globalco.payload.AddCertificationRequest;
import com.globalco.repositories.CertificationRepository;
import com.globalco.services.CertificationService;
import com.globalco.services.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {
    private final ResumeService resumeService;
    private final CertificationRepository certificationRepository;

    @Override
    public CertificationResponse addCertification(Long resumeId, Long candidateId, AddCertificationRequest certificationRequest) {
        Resume resume = resumeService.getResumeEntityById(resumeId);
        assertOwner(resume, candidateId);
        Certification certification = Certification.builder()
                .resume(resume)
                .certificationName(certificationRequest.getCertificationName())
                .issuingOrganization(certificationRequest.getIssuingOrganization())
                .issueDate(certificationRequest.getIssueDate())
                .expirationDate(certificationRequest.getExpirationDate())
                .credentialId(certificationRequest.getCredentialId())
                .credentialUrl(certificationRequest.getCredentialUrl())
                .displayOrder(certificationRequest.getDisplayOrder() != null ? certificationRequest.getDisplayOrder() : 0)
                .build();
        Certification savedCertification = certificationRepository.save(certification);
        return ResumeMapper.toCertificationResponse(savedCertification);
    }
    private void assertOwner(Resume resume, Long candidateId) {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new IllegalArgumentException("Candidate with ID " + candidateId + " is not the owner of resume with ID " + resume.getId());
        }
    }
    @Override
    public List<CertificationResponse> getAllCertifications(Long resumeId) {
        return certificationRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toCertificationResponse)
                .toList();
    }

    @Override
    public CertificationResponse updateCertification(Long resumeId, Long candidateId, Long certificationId, AddCertificationRequest certificationRequest) {
        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new IllegalArgumentException("Certification with ID " + certificationId + " not found"));
        assertOwner(certification.getResume(), candidateId);
        certification.setCertificationName(certificationRequest.getCertificationName());
        certification.setIssuingOrganization(certificationRequest.getIssuingOrganization());
        certification.setIssueDate(certificationRequest.getIssueDate());
        certification.setExpirationDate(certificationRequest.getExpirationDate());
        certification.setCredentialId(certificationRequest.getCredentialId());
        certification.setCredentialUrl(certificationRequest.getCredentialUrl());
        if (certificationRequest.getDisplayOrder() != null) {
            certification.setDisplayOrder(certificationRequest.getDisplayOrder());
        }
        Certification updatedCertification = certificationRepository.save(certification);
        return ResumeMapper.toCertificationResponse(updatedCertification);
    }

    @Override
    public void deleteCertification(Long resumeId, Long candidateId, Long certificationId) {
        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new IllegalArgumentException("Certification with ID " + certificationId + " not found"));
        assertOwner(certification.getResume(), candidateId);
        certificationRepository.delete(certification);
    }
}
