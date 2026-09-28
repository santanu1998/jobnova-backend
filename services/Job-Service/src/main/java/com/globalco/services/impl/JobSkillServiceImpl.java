package com.globalco.services.impl;

import com.globalco.domain.SkillCategory;
import com.globalco.dto.response.JobSkillResponse;
import com.globalco.mapper.JobSkillMapper;
import com.globalco.models.JobSkill;
import com.globalco.payload.JobSkillRequest;
import com.globalco.repositories.JobSkillRepository;
import com.globalco.services.JobSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobSkillServiceImpl implements JobSkillService {
    private final JobSkillRepository jobSkillRepository;

    @Override
    public JobSkillResponse createJobSkill(JobSkillRequest request) {
        if (jobSkillRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Job skill with the same name already exists.");
        }
        String slug = generateUniqueSlug(request.getName());
        SkillCategory category = convertToSkillCategory(request.getCategory());
        JobSkill jobSkill = JobSkill.builder()
                .name(request.getName())
                .slug(slug)
                .category(category)
                .active(true)
                .build();
        JobSkill savedJobSkill = jobSkillRepository.save(jobSkill);
        return JobSkillMapper.toJobSkillResponse(savedJobSkill);
    }

    private String generateUniqueSlug(String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]+", "")
                .trim()
                .replaceAll("[\\s-]+", "-");
        if (!jobSkillRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while (jobSkillRepository.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }

    @Override
    public List<JobSkillResponse> getAllJobSkills() {
        return jobSkillRepository.findByActiveTrue().stream()
                .map(JobSkillMapper::toJobSkillResponse)
                .toList();
    }

    @Override
    public JobSkillResponse getJobSkillById(Long id) {
        JobSkill jobSkill = jobSkillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job skill not found with id: " + id));
        return JobSkillMapper.toJobSkillResponse(jobSkill);
    }

    @Override
    public JobSkillResponse updateJobSkill(Long id, JobSkillRequest request) {
        JobSkill jobSkill = jobSkillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job skill not found with id: " + id));
        if (!jobSkill.getName().equals(request.getName())
        && jobSkillRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Job skill with the same name already exists.");
        }
        jobSkill.setName(request.getName());
        SkillCategory category = convertToSkillCategory(request.getCategory());
        jobSkill.setCategory(category);
        JobSkill updatedJobSkill = jobSkillRepository.save(jobSkill);
        return JobSkillMapper.toJobSkillResponse(updatedJobSkill);
    }

    @Override
    public void deleteJobSkill(Long id) {
        JobSkill jobSkill = jobSkillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job skill not found with id: " + id));
        jobSkill.setActive(false);
        jobSkillRepository.save(jobSkill);
    }

    @Override
    public Set<JobSkill> getJobSkillEntitiesByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        // Return only the skills that exist, skip missing ones
        return new HashSet<>(jobSkillRepository.findAllById(ids));
    }

    /**
     * Convert string category value to SkillCategory enum.
     * Handles exact matches and common variations.
     */
    private SkillCategory convertToSkillCategory(String categoryStr) {
        if (categoryStr == null || categoryStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Skill category cannot be null or empty");
        }

        String normalized = categoryStr.toUpperCase().trim().replace(" ", "_").replace("/", "_");

        // Try exact match first
        try {
            return SkillCategory.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            // Handle common variations
            return switch (normalized) {
                case "PROGRAMMING_LANGUAGE" -> SkillCategory.PROGRAMMING_LANGUAGES;
                case "PROG_LANGUAGE", "PROG_LANGUAGES" -> SkillCategory.PROGRAMMING_LANGUAGES;
                case "FRONT_END", "FRONTEND" -> SkillCategory.FRONTEND_DEVELOPMENT;
                case "BACK_END", "BACKEND" -> SkillCategory.BACKEND_DEVELOPMENT;
                case "CLOUD" -> SkillCategory.CLOUD_PLATFORMS;
                case "UI_UX", "UXUI" -> SkillCategory.UI_UX_DESIGN;
                case "AI", "ARTIFICIAL_INT" -> SkillCategory.ARTIFICIAL_INTELLIGENCE;
                case "ML", "MACHINE_LEARN" -> SkillCategory.MACHINE_LEARNING;
                case "QA" -> SkillCategory.QUALITY_ASSURANCE;
                default -> throw new IllegalArgumentException("Invalid SkillCategory: '" + categoryStr + "'. " +
                        "Accepted values are: PROGRAMMING_LANGUAGES, FRAMEWORKS, DATABASES, CLOUD_PLATFORMS, " +
                        "DEVOPS, MOBILE_DEVELOPMENT, FRONTEND_DEVELOPMENT, BACKEND_DEVELOPMENT, DATA_SCIENCE, " +
                        "MACHINE_LEARNING, ARTIFICIAL_INTELLIGENCE, CYBER_SECURITY, NETWORKING, UI_UX_DESIGN, " +
                        "PROJECT_MANAGEMENT, BUSINESS_ANALYSIS, QUALITY_ASSURANCE, DESIGN, SOFT_SKILLS, TOOLS, " +
                        "LANGUAGE, OTHER. " +
                        "Common variations supported: PROGRAMMING_LANGUAGE→PROGRAMMING_LANGUAGES, FRONTEND→FRONTEND_DEVELOPMENT, " +
                        "BACKEND→BACKEND_DEVELOPMENT, CLOUD→CLOUD_PLATFORMS, UI/UX→UI_UX_DESIGN, AI→ARTIFICIAL_INTELLIGENCE, " +
                        "ML→MACHINE_LEARNING, QA→QUALITY_ASSURANCE");
            };
        }
    }
}
