package com.Jobsteer.Jobsteer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Jobsteer.Jobsteer.entities.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}