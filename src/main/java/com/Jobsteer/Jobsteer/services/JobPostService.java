package com.Jobsteer.Jobsteer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.Jobsteer.Jobsteer.dto.JobPostRequest;
import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.entities.Requirement;
import com.Jobsteer.Jobsteer.entities.RequirementType;
import com.Jobsteer.Jobsteer.exceptions.EntityNotFoundException;
import com.Jobsteer.Jobsteer.repositories.JobPostRepository;
import com.Jobsteer.Jobsteer.repositories.RecruiterRepository;
import com.Jobsteer.Jobsteer.repositories.RequirementRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JobPostService {
	
	

    @Autowired
    private JobPostRepository jobPostRepository;
    
    @Autowired
    private RecruiterRepository recruiterRepository;
    
    @Autowired
    private RequirementRepository requirementRepository;
    
    
    
    @Autowired
    private RestTemplate restTemplate;
    
    
    @Value("${python.getexternaljobs.service.url}")
    private static final String PYTHON_API_URL = "http://localhost:5002/get-jobs";
    
    
    public List<Map<String, String>> fetchJobPostsFromApi() {
        System.out.println("Fetching job posts from API...");
        List<Map<String, String>> jobPosts = restTemplate.getForObject(PYTHON_API_URL, List.class);

        System.out.println("Received data: " + jobPosts);
        for (Map<String, String> jobData : jobPosts) {
        	String t= "api";
            JobPost jobPost = new JobPost();
            jobPost.setTitle(jobData.get("title"));
            jobPost.setLocation(jobData.get("location"));
            jobPost.setDescription(jobData.get("DESCRIPTION"));
            jobPost.setRecruiter(null);
            jobPost.setSource(t);
            jobPost.setToApply(jobData.get("final_url"));

            jobPostRepository.save(jobPost);

        	
            addRequirement(jobPost, RequirementType.EXPERIENCE, jobData.get("EXPERIENCE"));

            addRequirement(jobPost, RequirementType.EDUCATION, jobData.get("EDUCATION"));
            addRequirement(jobPost, RequirementType.SKILL, jobData.get("SKILL"));
            addRequirement(jobPost, RequirementType.LANGUAGE, jobData.get("LANGUAGE"));

            
//            jobPostRepository.save(jobPost);
        }
        return jobPosts;
    }
    

    
    private void addRequirement(JobPost jobPost, RequirementType type, String description) {
        if (description != null && !description.isEmpty()) {
            Requirement requirement = new Requirement();
            requirement.setType(type);
            requirement.setDescription(description);
            requirement.setJobPost(jobPost); // Establish bidirectional relationship

            jobPost.addRequirement(requirement);
            requirementRepository.save(requirement);
        }
    }
    
    
    public String fetchAndSaveJobs() {
        fetchJobPostsFromApi();
        return "Jobs fetched and saved.";
    }
    
    
    
    
    
    @Scheduled(fixedRateString = "${job.sync.interval}")
    @Transactional
    public void syncJobs() {
        // Remove old jobs with source='api'
    	
        try {
            jobPostRepository.deleteBySource("api");
            fetchJobPostsFromApi();
            System.out.println("Job synchronization completed.");
        } catch (Exception e) {
            System.err.println("Error during job synchronization: " + e.getMessage());
        }
    	
    	
  //      jobPostRepository.deleteBySource("api");
        
        // Fetch and save new jobs
    //    fetchJobPostsFromApi();
        
    //    System.out.println("Job synchronization completed.");
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    

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
