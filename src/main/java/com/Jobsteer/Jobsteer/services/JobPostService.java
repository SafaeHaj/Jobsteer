package com.Jobsteer.Jobsteer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Jobsteer.Jobsteer.dto.JobPostRequest;
import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.entities.Requirement;
import com.Jobsteer.Jobsteer.entities.RequirementType;
import com.Jobsteer.Jobsteer.repositories.JobPostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    public JobPost createJobPost(JobPostRequest request, Long recruiterId) {
        // 1. Create and populate JobPost
        JobPost jobPost = new JobPost();
        jobPost.setTitle(request.getTitle());
        jobPost.setLocation(request.getLocation());
        jobPost.setDescription(request.getDescription());
        jobPost.setToApply(request.getToApply());

        // Initialize Recruiter and set ID
        Recruiter recruiter = new Recruiter();
        recruiter.setId(recruiterId);
        jobPost.setRecruiter(recruiter); // Link Recruiter to JobPost

        jobPost.setSource(null); // Optional: null for now

        // 2. Convert Requirements and link them
        ArrayList<Requirement> requirements = (ArrayList<Requirement>) request.getRequirements().stream()
                .map(req -> {
                    Requirement requirement = new Requirement();
                    requirement.setDescription(req.getDescription());
                    requirement.setType(RequirementType.valueOf(req.getType()));
                    requirement.setJobPost(jobPost);
                    return requirement;
                }).collect(Collectors.toList());

        jobPost.setRequirements(requirements);

        // 3. Save JobPost (cascades to requirements)
        return jobPostRepository.save(jobPost);
    }

}

