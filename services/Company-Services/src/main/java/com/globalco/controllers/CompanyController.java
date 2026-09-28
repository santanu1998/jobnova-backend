package com.globalco.controllers;

import com.globalco.domain.CompanyStatus;
import com.globalco.domain.CompanyType;
import com.globalco.domain.IndustryType;
import com.globalco.dto.request.CompanyRequest;
// ... ApiResponse no longer used in this controller
import com.globalco.dto.response.ApiResponse;
import com.globalco.dto.response.CompanyResponse;
import com.globalco.services.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/create")
    public ResponseEntity<CompanyResponse> createCompany(
            @RequestHeader("X-User-Id") Long ownerId,
            @RequestBody @Valid CompanyRequest request
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(ownerId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<CompanyResponse> getMyCompany(@RequestHeader("X-User-Id") Long ownerId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getMyCompany(ownerId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CompanyResponse>> getAllCompanies(
            @RequestParam(required = false) CompanyType companyType,
            @RequestParam(required = false) IndustryType industryType,
            @RequestParam(required = false) CompanyStatus status
    ) {
        return ResponseEntity.status(HttpStatus.OK).
                body(companyService.getAllCompanies(companyType, industryType, status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable("id") Long companyId,
            @RequestHeader("X-User-Id") Long ownerId,
            @RequestBody @Valid CompanyRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.updateCompany(companyId, ownerId, request));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<CompanyResponse> verifyCompany(@PathVariable("id") Long companyId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.verifyCompany(companyId));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CompanyResponse> deactivateCompany(@PathVariable("id") Long companyId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.deactivateCompany(companyId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCompany(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long ownerId
    ) {
        companyService.deleteCompany(id, ownerId);
        return ResponseEntity.ok(new ApiResponse("Company deleted successfully", true));
    }
}
