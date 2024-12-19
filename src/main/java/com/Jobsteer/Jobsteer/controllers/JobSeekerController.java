package com.Jobsteer.Jobsteer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Jobsteer.Jobsteer.entities.JobSeeker;
import com.Jobsteer.Jobsteer.models.JobSeekerModel;

@RestController
@RequestMapping("/job-seekers")
public class JobSeekerController {
	@Autowired
    private JobSeekerModel jobSeekerModel;

	@PostMapping("/create")
    public JobSeeker createJobSeeker(@RequestBody JobSeeker jobSeeker) {
        return jobSeekerModel.saveJobSeeker(jobSeeker);
    }
    
    @GetMapping("/jobseeker/dashboard")
    @Secured("ROLE_JOBSEEKER")
    public String getJobSeekerDashboard() {
        return "Welcome Job Seeker!";
    }
}
