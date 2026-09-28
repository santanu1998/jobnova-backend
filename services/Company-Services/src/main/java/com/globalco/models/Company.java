package com.globalco.models;

import com.globalco.domain.CompanySize;
import com.globalco.domain.CompanyStatus;
import com.globalco.domain.CompanyType;
import com.globalco.domain.IndustryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(unique = true)
    private String slug;
    private String tagline;
    private String description;
    private String logoUrl;
    private String coverImageUrl;
    private String website;
    private String email;
    private String phone;
    private Integer foundedYear;
    // Stored in DB as smallint (ordinal) to match existing schema
    @Enumerated(EnumType.STRING)
    private CompanySize companySize;
    // Store enums as ordinals (smallint) to match existing DB schema
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;
    @Enumerated(EnumType.STRING)
    private IndustryType industryType;
    @Enumerated(EnumType.STRING)
    private CompanyStatus status;
    private boolean isVerified = false;
    @Column(unique = true)
    private String registrationNumber;
    @Column(unique = true, nullable = false)
    private Long ownerId;
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<SocialLink> socialLinks = new ArrayList<>();
    private Boolean active = true;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}