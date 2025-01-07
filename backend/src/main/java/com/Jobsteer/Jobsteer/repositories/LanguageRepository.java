package com.Jobsteer.Jobsteer.repositories;

import com.Jobsteer.Jobsteer.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Integer> {
    List<Language> findByResumeId(int resumeId);
}
