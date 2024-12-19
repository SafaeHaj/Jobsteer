package com.Jobsteer.Jobsteer.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Jobsteer.Jobsteer.entities.Recruiter;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {

	Optional<Recruiter> findByEmail(String email);
}
