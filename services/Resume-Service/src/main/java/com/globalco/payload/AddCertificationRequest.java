package com.globalco.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCertificationRequest {
    @NotBlank(message = "Certification name is required")
    @Size(max = 150, message = "Certification name cannot exceed {max} characters")
    private String certificationName;
    @NotBlank(message = "Issuing organization is required")
    @Size(max = 150, message = "Issuing organization name cannot exceed {max} characters")
    private String issuingOrganization;
    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;
    private LocalDate expirationDate;
    @Size(max = 100, message = "Credential ID cannot exceed {max} characters")
    private String credentialId;
    @Size(max = 255, message = "Credential URL cannot exceed {max} characters")
    @Pattern(regexp = "^(https?://).*", message = "Credential URL must be valid.")
    private String credentialUrl;
    private Integer displayOrder;
}
