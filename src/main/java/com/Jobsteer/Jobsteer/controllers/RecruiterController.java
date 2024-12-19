package com.Jobsteer.Jobsteer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.models.RecruiterModel;

@RestController
@RequestMapping("/recruiters")
public class RecruiterController {
	@Autowired
	private RecruiterModel recruiterModel;
	
	@PostMapping("/create")
	public Recruiter createRecruiter(@RequestBody Recruiter recruiter) {
		return recruiterModel.saveRecruiter(recruiter);
	}
    @GetMapping("/recruiter/dashboard")
    @Secured("ROLE_RECRUITER")
    public String getRecruiterDashboard() {
        return "Welcome Recruiter!";
    }
}
