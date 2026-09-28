package com.globalco.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "awards")
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Resume resume;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String issuer;
    @Column(nullable = false)
    private LocalDate issuedDate;
    private String description;
    @Column(nullable = false)
    private Integer displayOrder = 0;
    private String scope;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
