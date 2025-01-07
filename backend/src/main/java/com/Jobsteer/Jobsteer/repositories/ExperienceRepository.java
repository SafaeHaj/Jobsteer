package com.Jobsteer.Jobsteer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Jobsteer.Jobsteer.entities.Experience;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByResumeId(Long resumeId);
    void deleteByResumeId(Long resumeId);
}