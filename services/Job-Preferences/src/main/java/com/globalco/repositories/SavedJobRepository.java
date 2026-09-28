package com.globalco.repositories;

import com.globalco.models.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findByCandidateId(Long candidateId);
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);
}
