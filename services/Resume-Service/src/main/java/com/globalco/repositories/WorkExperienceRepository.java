package com.globalco.repositories;

import com.globalco.models.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
    List<WorkExperience> findByResume_IdOrderByDisplayOrderAsc(Long resumeId);
}
