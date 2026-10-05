package com.globalco.repositories;

import com.globalco.domain.AiShortListStatus;
import com.globalco.domain.ApplicationStatus;
import com.globalco.models.Application;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ApplicationSpecification {
    public static Specification<Application> forCompanyWithFilters(
            Long companyId,
            Long jobId,
            ApplicationStatus status,
            Boolean isStarred,
            AiShortListStatus aiShortListStatus,
            Integer minAiScore
    ) {
        return (root, query, cb)->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("companyId"),companyId));
            if(jobId != null) predicates.add(cb.equal(root.get("jobId"),jobId));
            if(status != null) predicates.add(cb.equal(root.get("status"), status));
            if(Boolean.TRUE.equals(isStarred)) predicates.add(cb.isTrue(root.get("isStarred")));
            if(aiShortListStatus != null) predicates.add(cb.equal(
                    root.get("aishortListStatus"), aiShortListStatus));
            if(minAiScore != null) predicates.add(cb.greaterThanOrEqualTo(
                    root.get("aiScore"),minAiScore
            ));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}