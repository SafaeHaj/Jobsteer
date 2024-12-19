package com.Jobsteer.Jobsteer.controllers;

import com.Jobsteer.Jobsteer.dto.JobPostDTO;
import com.Jobsteer.Jobsteer.dto.JobPostRequest;
import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.entities.Requirement;
import com.Jobsteer.Jobsteer.entities.RequirementType;
import com.Jobsteer.Jobsteer.repositories.JobPostRepository;
import com.Jobsteer.Jobsteer.repositories.RequirementRepository;  // Add this import
import com.Jobsteer.Jobsteer.services.JobPostMapper;
import com.Jobsteer.Jobsteer.services.JobPostService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/jobpost/internal")
public class JobPostInternController {

	    @Autowired
	    private JobPostService jobPostService;

	    @PostMapping
	    public ResponseEntity<?> createJobPost(@RequestBody JobPostRequest request) {
	        // Hardcode recruiter_id for now (replace with authentication logic)
	        Long recruiterId = 1L;

	        JobPost savedJobPost = jobPostService.createJobPost(request, recruiterId);
	        return ResponseEntity.ok(savedJobPost);
	    }
	}
