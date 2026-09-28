package com.globalco.mapper;

import com.globalco.dto.PersonalInfoResponse;
import com.globalco.dto.response.*;
import com.globalco.models.PersonalInfo;
import com.globalco.models.Resume;
import com.globalco.models.ResumeSkill;
import com.globalco.models.Education;
import com.globalco.models.Project;
import com.globalco.models.Language;
import com.globalco.models.Award;
import com.globalco.models.Certification;

import java.util.List;

public class ResumeMapper {
    public static PersonalInfoResponse toPersonalInfoResponse(PersonalInfo personalInfo) {
        if (personalInfo == null) {
            return null;
        }
        return PersonalInfoResponse.builder()
                .firstName(personalInfo.getFirstName())
                .lastName(personalInfo.getLastName())
                .headline(personalInfo.getHeadline())
                .email(personalInfo.getEmail())
                .phoneNumber(personalInfo.getPhoneNumber())
                .city(personalInfo.getCity())
                .country(personalInfo.getCountry())
                .linkedinUrl(personalInfo.getLinkedinUrl())
                .githubUrl(personalInfo.getGithubUrl())
                .portfolioUrl(personalInfo.getPortfolioUrl())
                .websiteUrl(personalInfo.getWebsiteUrl())
                .build();
    }
    public static ResumeResponse toResumeResponse(Resume resume,
                                                  List<WorkExperienceResponse> workExperienceResponses,
                                                  List<EducationResponse> educationResponses,
                                                  List<ResumeSkillResponse> skillResponses,
                                                  List<ProjectResponse> projectResponses,
                                                  List<CertificationResponse> certificationResponses,
                                                  List<AwardResponse> awardResponses,
                                                  List<LanguageResponse> languageResponses) {
        if (resume == null) {
            return null;
        }
        return ResumeResponse.builder()
                .id(resume.getId())
                .candidateId(resume.getCandidateId())
                .title(resume.getTitle())
                .template(resume.getTemplate())
                .visibility(resume.getVisibility())
                .isDefault(resume.getIsDefault())
                .personalInfo(ResumeMapper.toPersonalInfoResponse(resume.getPersonalInfo()))
                .summary(resume.getSummary())
                .completionScore(resume.getCompletionScore())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .workExperiences(workExperienceResponses)
                .educations(educationResponses)
                .skills(skillResponses)
                .projects(projectResponses)
                .certifications(certificationResponses)
                .awards(awardResponses)
                .languages(languageResponses)
                .build();
    }
    public static ResumeSkillResponse toResumeSkillResponse(ResumeSkill resumeSkill) {
        if (resumeSkill == null) {
            return null;
        }
        return ResumeSkillResponse.builder()
                .id(resumeSkill.getId())
                .skillName(resumeSkill.getSkillName())
                .proficiencyLevel(resumeSkill.getProficiencyLevel())
                .yearsOfExperience(resumeSkill.getYearsOfExperience())
                .displayOrder(resumeSkill.getDisplayOrder())
                .build();
    }
    public static EducationResponse toEducationResponse(Education education) {
        if (education == null) {
            return null;
        }
        return EducationResponse.builder()
                .id(education.getId())
                .institutionName(education.getInstitutionName())
                .degree(education.getDegree())
                .fieldOfStudy(education.getFieldOfStudy())
                .grade(education.getGrade())
                .startDate(education.getStartDate().toLocalDate())
                .endDate(education.getEndDate() != null ? education.getEndDate().toLocalDate() : null)
                .isCurrentlyStudying(education.getIsCurrentlyStudying())
                .description(education.getDescription())
                .displayOrder(education.getDisplayOrder())
                .build();
    }
    public static ProjectResponse toProjectResponse(Project project) {
        if (project == null) {
            return null;
        }
        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .technologies(project.getTechnologies())
                .projectUrl(project.getProjectUrl())
                .sourceCodeUrl(project.getSourceCodeUrl())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .isOngoing(project.getIsOngoing())
                .displayOrder(project.getDisplayOrder())
                .build();
    }
    public static LanguageResponse toLanguageResponse(Language language) {
        if (language == null) {
            return null;
        }
        return LanguageResponse.builder()
                .id(language.getId())
                .languageName(language.getLanguageName())
                .proficiencyLevel(language.getProficiencyLevel())
                .displayOrder(language.getDisplayOrder())
                .build();
    }
    public static AwardResponse toAwardResponse(Award award) {
        if (award == null) {
            return null;
        }
        return AwardResponse.builder()
                .id(award.getId())
                .title(award.getTitle())
                .issuer(award.getIssuer())
                .issuedDate(award.getIssuedDate())
                .description(award.getDescription())
                .displayOrder(award.getDisplayOrder())
                .scope(award.getScope())
                .build();
    }
    public static CertificationResponse toCertificationResponse(Certification certification) {
        if (certification == null) {
            return null;
        }
        return CertificationResponse.builder()
                .id(certification.getId())
                .certificationName(certification.getCertificationName())
                .issuingOrganization(certification.getIssuingOrganization())
                .issueDate(certification.getIssueDate())
                .expirationDate(certification.getExpirationDate())
                .credentialId(certification.getCredentialId())
                .credentialUrl(certification.getCredentialUrl())
                .displayOrder(certification.getDisplayOrder())
                .build();
    }
}
