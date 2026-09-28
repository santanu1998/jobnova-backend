package com.globalco.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class AddAwardRequest {
    @NotBlank(message = "Award title is required")
    @Size(max = 100, message = "Award title cannot exceed {max} characters")
    private String title;
    @NotBlank(message = "Issuer organization is required")
    @Size(max = 150, message = "Issuer name cannot exceed {max} characters")
    private String issuer;
    @NotNull(message = "Issue date is required")
    private LocalDate issuedDate; // Changed to String to accept date in "yyyy-MM-dd" format
    @Size(max = 500, message = "Description cannot exceed {max} characters")
    private String description;
    private Integer displayOrder;
    @Size(max = 50, message = "Scope cannot exceed {max} characters")
    private String scope;
}
