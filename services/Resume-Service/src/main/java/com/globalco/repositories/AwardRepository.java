package com.globalco.repositories;

import com.globalco.models.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
    List<Award> findByResume_IdOrderByDisplayOrderAsc(Long resumeId);
}
