package com.globalco.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobCategoryRequest {
    @NotBlank(message = "Job category name is required")
    private String name;
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    private String iconUrl;
    private Long parentId;
}
