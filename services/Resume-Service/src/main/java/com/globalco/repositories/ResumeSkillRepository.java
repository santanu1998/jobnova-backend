package com.globalco.repositories;

import com.globalco.models.ResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeSkillRepository extends JpaRepository<ResumeSkill, Long> {
    List<ResumeSkill> findByResume_IdOrderByDisplayOrderAsc(Long resumeId);
}
