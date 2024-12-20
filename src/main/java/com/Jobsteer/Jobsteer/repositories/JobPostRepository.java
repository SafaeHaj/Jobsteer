package com.Jobsteer.Jobsteer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Jobsteer.Jobsteer.entities.JobPost;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Integer> {
    List<JobPost> findByRecruiterId(Long recruiterId);
    void deleteById(Long jobPostId);
    boolean existsById(Long id);
    void deleteBySource(String source);

}
