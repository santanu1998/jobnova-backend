package com.globalco.repositories;

import com.globalco.models.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    List<JobCategory> findByActiveTrue();
}
