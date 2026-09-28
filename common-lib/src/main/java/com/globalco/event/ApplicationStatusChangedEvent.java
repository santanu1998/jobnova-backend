package com.globalco.event;

import com.globalco.domain.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Kafka event payload published by Application-Service whenever an
 * application's status changes, and consumed by Notification-Service
 * to trigger the corresponding candidate email.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusChangedEvent {

    private Long applicationId;
    private Long candidateId;
    private String candidateEmail;
    private String candidateName;
    private ApplicationStatus oldStatus;
    private ApplicationStatus newStatus;
    private String note;
    private String jobTitle;
    private String companyName;
    private LocalDateTime changedAt;
}
