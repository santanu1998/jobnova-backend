package com.globalco.client;

import com.globalco.dto.response.CompanyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "COMPANY-SERVICES")
public interface CompanyClient {

    @GetMapping("/api/companies/{id}")
    CompanyResponse getCompanyById(
            @PathVariable Long id
    );

    @GetMapping("/api/companies/my")
    CompanyResponse getMyCompany(
            @RequestHeader("X-User-Id") Long ownerId);
}
