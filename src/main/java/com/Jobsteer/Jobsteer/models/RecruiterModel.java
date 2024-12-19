package com.Jobsteer.Jobsteer.models;

import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.repositories.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecruiterModel {

    @Autowired
    private RecruiterRepository recruiterRepository;

    public Recruiter saveRecruiter(Recruiter recruiter) {
        return recruiterRepository.save(recruiter);
    }

    // Find recruiter by ID
    public Optional<Recruiter> findRecruiterById(Long id) {
        return recruiterRepository.findById(id);
    }

    // Get all recruiters
    public List<Recruiter> findAllRecruiters() {
        return recruiterRepository.findAll();
    }

    // Post a new job
    public void postJob(Long recruiterId, JobPost jobPosting) {
        Optional<Recruiter> recruiterOptional = recruiterRepository.findById(recruiterId);
        recruiterOptional.ifPresent(recruiter -> {
            recruiter.postJob(jobPosting);
            recruiterRepository.save(recruiter);
        });
    }

    // Update a job description
    public void updateJobDescription(Long recruiterId, JobPost jobPosting, String updatedDescription) {
        Optional<Recruiter> recruiterOptional = recruiterRepository.findById(recruiterId);
        recruiterOptional.ifPresent(recruiter -> {
            recruiter.updateJob(jobPosting, updatedDescription);
            recruiterRepository.save(recruiter);
        });
    }

    // Delete a job
    public void deleteJob(Long recruiterId, JobPost jobPosting) {
        Optional<Recruiter> recruiterOptional = recruiterRepository.findById(recruiterId);
        recruiterOptional.ifPresent(recruiter -> {
            recruiter.deleteJob(jobPosting);
            recruiterRepository.save(recruiter);
        });
    }
}
