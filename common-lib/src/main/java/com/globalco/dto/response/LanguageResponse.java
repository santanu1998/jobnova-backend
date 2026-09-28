package com.globalco.dto.response;

import com.globalco.domain.LanguageProficiency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageResponse {
    private Long id;
    private String languageName;
    private LanguageProficiency proficiencyLevel;
    private Integer displayOrder;
}
