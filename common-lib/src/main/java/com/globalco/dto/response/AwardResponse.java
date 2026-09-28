package com.globalco.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwardResponse {
    private Long id;
    private String title;
    private String issuer;
    private LocalDate issuedDate;
    private String description;
    private Integer displayOrder;
    private String scope;
}
