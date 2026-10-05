package com.globalco.services.impl;

import java.time.LocalDateTime;

import com.globalco.exception.ResourceNotFoundException;

import com.globalco.domain.CompanyStatus;
import com.globalco.domain.CompanyType;
import com.globalco.domain.IndustryType;
import com.globalco.dto.request.CompanyRequest;
import com.globalco.dto.response.CompanyResponse;
import com.globalco.dto.response.SocialLinkResponse;
import com.globalco.mapper.CompanyMapper;
import com.globalco.models.Company;
import com.globalco.models.SocialLink;
import com.globalco.repositories.CompanyRepository;
import com.globalco.services.CompanyService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public CompanyResponse createCompany(Long ownerId, CompanyRequest request) {
        if (companyRepository.existsByOwnerId(ownerId)) {
            throw new IllegalArgumentException("You already have a company registered. "
                    + "Only one company per user is allowed.");
        }
        if (companyRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Company name already exists. Please choose a different name.");
        }
        if (request.getRegistrationNumber() != null &&
                companyRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new IllegalArgumentException("Company registration number already exists. " +
                    "Please provide a unique registration number.");
        }
        String slug = generateUniqueSlug(request.getName());
        Company company = Company.builder()
                .name(request.getName())
                .slug(slug)
                .tagline(request.getTagline())
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .coverImageUrl(request.getCoverImageUrl())
                .website(request.getWebsite())
                .email(request.getEmail())
                .phone(request.getPhone())
                .foundedYear(request.getFoundedYear())
                .companySize(request.getCompanySize())
                .companyType(request.getCompanyType())
                .industryType(request.getIndustryType())
                .registrationNumber(request.getRegistrationNumber())
                .ownerId(ownerId)
                .socialLinks(mapSocialLinks(request.getSocialLinks()))
                .isVerified(false)
                .status(CompanyStatus.PENDING_VERIFICATION)
                .active(true)
                .build();
        Company savedCompany = companyRepository.save(company);
        return CompanyMapper.toResponse(savedCompany);
    }

    private List<SocialLink> mapSocialLinks(List<SocialLinkResponse> socialLinks) {
        if (socialLinks == null || socialLinks.isEmpty()) {
            return new ArrayList<SocialLink>();
        }
        return socialLinks.stream()
                .map(
                        link -> SocialLink.builder()
                                .platform(link.getPlatform())
                                .url(link.getUrl())
                                .build())
                .collect(Collectors.toList());

    }

    private String generateUniqueSlug(String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]+", "")
                .trim()
                .replaceAll("[\\s-]+", "-");
        if (!companyRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while (companyRepository.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }

    @Override
    public CompanyResponse getCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));
        return CompanyMapper.toResponse(company);
    }

    @Override
    public CompanyResponse getMyCompany(Long ownerId) {
        Company company = companyRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("You have not created a company yet."));
        return CompanyMapper.toResponse(company);
    }

    @Override
    public List<CompanyResponse> getAllCompanies(CompanyType companyType, IndustryType industryType, CompanyStatus status) {
        return companyRepository.findByFilters(
                        companyType,
                        industryType,
                        status
                ).stream()
                .map(CompanyMapper::toResponse)
                .collect(Collectors.toList()
                );
    }

    @Override
    public CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest request) {
        Company company = getCompanyEntityById(companyId);
        if (!company.getName().equals(request.getName())
                && companyRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException
                    ("Company name already exists. Please choose a different name.");
        }
        if (request.getRegistrationNumber() != null
                && !request.getRegistrationNumber().equals(company.getRegistrationNumber())
                && companyRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new IllegalArgumentException("Company registration number already exists. " +
                    "Please provide a unique registration number.");
        }
        company.setName(request.getName());
        company.setTagline(request.getTagline());
        company.setDescription(request.getDescription());
        company.setLogoUrl(request.getLogoUrl());
        company.setCoverImageUrl(request.getCoverImageUrl());
        company.setWebsite(request.getWebsite());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());
        company.setFoundedYear(request.getFoundedYear());
        company.setCompanySize(request.getCompanySize());
        company.setCompanyType(request.getCompanyType());
        company.setIndustryType(request.getIndustryType());
        company.setRegistrationNumber(request.getRegistrationNumber());
        company.setSocialLinks(mapSocialLinks(request.getSocialLinks()));
        Company updatedCompany = companyRepository.save(company);
        return CompanyMapper.toResponse(updatedCompany);
    }

    @Override
    public CompanyResponse verifyCompany(Long companyId) {
        Company company = getCompanyEntityById(companyId);
        company.setStatus(CompanyStatus.ACTIVE);
        company.setVerified(true);
        company.setActive(true);
        company.setVerifiedAt(LocalDateTime.now());
        Company updatedCompany = companyRepository.save(company);
        return CompanyMapper.toResponse(updatedCompany);
    }

    @Override
    public void deleteCompany(Long companyId, Long ownerId, boolean isAdmin) {
        Company company = getCompanyEntityById(companyId);
        if (!isAdmin) {
            assertOwner(company, ownerId);
        }
        companyRepository.delete(company);
    }

    private void assertOwner(Company company, Long ownerId) {
        if (!company.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("You are not the owner of this company.");
        }

    }

    @Override
    public CompanyResponse deactivateCompany(Long companyId) {
        Company company = getCompanyEntityById(companyId);
        company.setStatus(CompanyStatus.SUSPENDED);
        company.setVerified(false);
        company.setActive(false);
        company.setVerifiedAt(null);
        Company updatedCompany = companyRepository.save(company);
        return CompanyMapper.toResponse(updatedCompany);
    }

    @Override
    public Company getCompanyEntityById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));
    }
}
