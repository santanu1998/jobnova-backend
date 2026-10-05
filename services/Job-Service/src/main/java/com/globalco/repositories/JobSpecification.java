package com.globalco.repositories;

import jakarta.persistence.criteria.JoinType;

import jakarta.persistence.criteria.Join;

import com.globalco.domain.JobStatus;
import com.globalco.models.Job;
import com.globalco.payload.JobSearchRequest;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {
    public JobSpecification(){}
    public static Specification<Job> build(JobSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isTrue(root.get("isActive")));
            // Only filter by status if provided in the request. Otherwise return jobs of any status.
            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                // every word must appear in the title, description, requirements or category name
                Join<Object, Object> category = root.join("category", JoinType.LEFT);
                for (String word : request.getKeyword().toLowerCase().trim().split("\\s+")) {
                    String pattern = "%" + word + "%";
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("requirements")), pattern),
                            criteriaBuilder.like(criteriaBuilder.lower(category.get("name")), pattern)
                    ));
                }
            }
            if (request.getSkillIds() != null && !request.getSkillIds().isEmpty()) {
                predicates.add(root.join("skills").get("id").in(request.getSkillIds()));
                query.distinct(true);
            }
            if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
                predicates.add(root.join("tags").get("id").in(request.getTagIds()));
                query.distinct(true);
            }
            if (request.getJobType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("jobType"), request.getJobType()));
            }
            if (request.getWorkMode() != null) {
                predicates.add(criteriaBuilder.equal(root.get("workMode"), request.getWorkMode()));
            }
            if (request.getExperienceLevel() != null) {
                predicates.add(criteriaBuilder.equal(root.get("experienceLevel"), request.getExperienceLevel()));
            }
            if (request.getCompanyId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("companyId"), request.getCompanyId()));
            }
            if (request.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), request.getCategoryId()));
            }
            if (request.getLocation() != null && !request.getLocation().isBlank()) {
                String pattern = "%" + request.getLocation().toLowerCase() + "%";
                Path<String> city = root.get("location").get("city");
                Path<String> state = root.get("location").get("state");
                Path<String> country = root.get("location").get("country");
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(city), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(state), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(country), pattern)
                ));
            }
            if (request.getMinSalary() != null) {
                predicates.add(criteriaBuilder.
                        greaterThanOrEqualTo(root.get("salaryRange").get("minSalary"), request.getMinSalary()));
            }
            if (request.getMaxSalary() != null) {
                predicates.add(criteriaBuilder.
                        lessThanOrEqualTo(root.get("salaryRange").get("maxSalary"), request.getMaxSalary()));
            }
            if (request.getMinOpenings() != null) {
                predicates.add(criteriaBuilder.
                        greaterThanOrEqualTo(root.get("openings"), request.getMinOpenings()));
            }
            if (request.getMaxOpenings() != null) {
                predicates.add(criteriaBuilder.
                        lessThanOrEqualTo(root.get("openings"), request.getMaxOpenings()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
