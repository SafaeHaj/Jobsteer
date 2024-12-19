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


@Controller
public class JobPostController {
	@Autowired
	private JobPostRepository jobpostRepo;
	
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
