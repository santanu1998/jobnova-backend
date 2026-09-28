package com.globalco.payload;

import com.globalco.domain.ResumeTemplate;
import com.globalco.domain.ResumeVisibility;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateResumeRequest {
    // Accept either `title` or `resumeName` from clients; validation/defaulting is performed in service
    private String title;
    private String resumeName;
    private ResumeTemplate template;
    private ResumeVisibility visibility;
    // whether to mark this resume as default for the candidate
    private Boolean isDefault;
    // optional free-text summary
    private String summary;
}
