package com.Jobsteer.Jobsteer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Jobsteer.Jobsteer.entities.JobPost;

public interface JobPostRepository extends JpaRepository<JobPost, Integer> {
}
