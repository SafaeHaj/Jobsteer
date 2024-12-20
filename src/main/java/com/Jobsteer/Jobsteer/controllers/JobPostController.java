package com.Jobsteer.Jobsteer.controllers;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.models.JobPostModel;
import com.Jobsteer.Jobsteer.repositories.JobPostRepository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Jobsteer.Jobsteer.services.JobPostService;
import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.models.JobPostModel;
import com.Jobsteer.Jobsteer.repositories.JobPostRepository;



@RestController
@RequestMapping("/api/jobposts")
public class JobPostController {
	@Autowired
	private JobPostRepository jobpostRepo;
    @Autowired
    private JobPostService jobPostService;
	
    @PostMapping("/fetch-and-add")
    public String fetchAndAddJobPosts() {
        return jobPostService.fetchAndSaveJobs();
    }
    
    @GetMapping("/fetch-jobs")
    public String fetchJobPosts() {
        jobPostService.fetchJobPostsFromApi();
        return "Job posts fetched and saved!";
    }
      
	@GetMapping("/jobposts")
	public String getJobPosts(Model model) {
		List<JobPost> jobposts = jobpostRepo.findAll(); 
        model.addAttribute("jobposts", jobposts); 
		return "jobposts";
	}
	
	
	@PostMapping("/api/jobpost")
	public Map<String, List<JobPost>> listJobPosts() {
		HashMap<String, List<JobPost>> result = new HashMap<>();
		JobPostModel jobpostModel = new JobPostModel();
		result.put("jobposts", jobpostModel.getAll());
		return result;
	}
}
