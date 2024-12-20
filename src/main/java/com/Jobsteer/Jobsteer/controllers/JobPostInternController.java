package com.Jobsteer.Jobsteer.controllers;

import com.Jobsteer.Jobsteer.dto.JobPostRequest;
import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.services.JobPostService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobpost")
public class JobPostInternController {

    @Autowired
    private JobPostService jobPostService;

    @PostMapping("/internal")
    public ResponseEntity<?> createJobPost(@RequestBody JobPostRequest request) {
    	System.out.print(request);
        JobPost savedJobPost = jobPostService.createJobPost(request);
        return ResponseEntity.ok(savedJobPost);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<JobPost>> getJobPostsByRecruiter(@PathVariable Long recruiterId) {
        List<JobPost> jobPosts = jobPostService.findByRecruiterId(recruiterId);
        return ResponseEntity.ok(jobPosts);
    }
    
    @DeleteMapping("/{jobPostId}")
    public ResponseEntity<Void> deleteJobPost(@PathVariable("jobPostId") Long jobPostId) {
        try {
            jobPostService.deleteJobPost(jobPostId);
            return ResponseEntity.noContent().build(); 
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build(); 
        }
    }
}