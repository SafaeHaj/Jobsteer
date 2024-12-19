package com.Jobsteer.Jobsteer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Jobsteer.Jobsteer.entities.Requirement;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Integer> {
}
