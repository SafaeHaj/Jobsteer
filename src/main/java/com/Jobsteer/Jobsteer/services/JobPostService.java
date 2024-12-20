package com.Jobsteer.Jobsteer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Jobsteer.Jobsteer.dto.JobPostRequest;
import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.entities.Requirement;
import com.Jobsteer.Jobsteer.entities.RequirementType;
import com.Jobsteer.Jobsteer.exceptions.EntityNotFoundException;
import com.Jobsteer.Jobsteer.repositories.JobPostRepository;
import com.Jobsteer.Jobsteer.repositories.RecruiterRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;
    
    @Autowired
    private RecruiterRepository recruiterRepository;

    public JobPost createJobPost(JobPostRequest request) {
        JobPost jobPost = new JobPost();
        jobPost.setTitle(request.getTitle());
        jobPost.setLocation(request.getLocation());
        jobPost.setDescription(request.getDescription());
        jobPost.setToApply(request.getToApply());

        Long recruiterId = request.getRecruiterId();
        Recruiter recruiter = recruiterRepository.findById(recruiterId).get();
        
        jobPost.setRecruiter(recruiter); 
        jobPost.setSource(null); 

        ArrayList<Requirement> requirements = (ArrayList<Requirement>) request.getRequirements().stream()
                .map(req -> {
                    Requirement requirement = new Requirement();
                    requirement.setDescription(req.getDescription());
                    requirement.setType(RequirementType.valueOf(req.getType()));
                    requirement.setJobPost(jobPost);
                    return requirement;
                }).collect(Collectors.toList());
        
        jobPost.setRequirements(requirements);

        return jobPostRepository.save(jobPost);
    }

    public List<JobPost> findByRecruiterId(Long recruiterId) {
        return jobPostRepository.findByRecruiterId(recruiterId);
    }
    
    @Transactional
    public void deleteJobPost(Long jobPostId) {
        if (jobPostRepository.existsById(jobPostId)) {
            jobPostRepository.deleteById(jobPostId); 
        } else {
            throw new EntityNotFoundException("Job post not found with id: " + jobPostId);
        }
    }

}
