package com.globalco.services;


import com.globalco.domain.CompanyStatus;
import com.globalco.domain.CompanyType;
import com.globalco.domain.IndustryType;
import com.globalco.dto.request.CompanyRequest;
import com.globalco.dto.response.CompanyResponse;
import com.globalco.models.Company;

import java.util.List;

public interface CompanyService {
    CompanyResponse createCompany(Long ownerId, CompanyRequest request);
    CompanyResponse getCompanyById(Long companyId);
    CompanyResponse getMyCompany(Long ownerId);
    List<CompanyResponse> getAllCompanies(
            CompanyType companyType,
            IndustryType industryType,
            CompanyStatus status
    );
    CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest request);
    CompanyResponse verifyCompany(Long companyId);
    void deleteCompany(Long companyId, Long ownerId);
    CompanyResponse deactivateCompany(Long companyId);
    Company getCompanyEntityById(Long companyId);
}
