package com.Jobsteer.Jobsteer.models;
//package com.Jobsteer.Jobsteer.services;
import com.Jobsteer.Jobsteer.entities.JobSeeker;
import com.Jobsteer.Jobsteer.entities.Resume;
import com.Jobsteer.Jobsteer.repositories.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobSeekerModel {

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    public List<JobSeeker> getAllJobSeekers() {
        return jobSeekerRepository.findAll();
    }


    public JobSeeker saveJobSeeker(JobSeeker jobSeeker) {
        return jobSeekerRepository.save(jobSeeker);
    }
    
    
    public Optional<JobSeeker> findJobSeekerById(Long id) {
        return jobSeekerRepository.findById(id);
    }
    
    
    public Optional<JobSeeker> findJobSeekerByEmail(String email) {
        return jobSeekerRepository.findByEmail(email);
    }


    // Upload a resume
    public void uploadResume(Long jobSeekerId, Resume resume) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findById(jobSeekerId);
        jobSeekerOptional.ifPresent(jobSeeker -> {
            jobSeeker.uploadCv(resume);
            jobSeekerRepository.save(jobSeeker);
        });
    }

    // Delete a resume
    public void deleteResume(Long jobSeekerId) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findById(jobSeekerId);
        jobSeekerOptional.ifPresent(jobSeeker -> {
            jobSeeker.deleteCv();
            jobSeekerRepository.save(jobSeeker);
        });
    }
}
