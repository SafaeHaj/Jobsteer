package com.Jobsteer.Jobsteer.utils;

import org.springframework.stereotype.Component;

import com.Jobsteer.Jobsteer.dto.RegistrationRequest;
import com.Jobsteer.Jobsteer.entities.JobSeeker;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.entities.User;

@Component
public class UserFactory {
    public User createUser(RegistrationRequest request, String encodedPassword) {
        switch (request.getUserType().toLowerCase()) {
            case "jobseeker":
                JobSeeker jobseeker = new JobSeeker();
                jobseeker.setEmail(request.getEmail());
                jobseeker.setPassword(encodedPassword);
                jobseeker.setFirstName(request.getFirstName());
                jobseeker.setLastName(request.getLastName());
                jobseeker.setLocation(request.getLocation());
                return jobseeker;
            case "recruiter":
                Recruiter recruiter = new Recruiter();
                recruiter.setEmail(request.getEmail());
                recruiter.setPassword(encodedPassword);
                recruiter.setFirstName(request.getFirstName());
                recruiter.setLastName(request.getLastName());
                recruiter.setCompanyName(request.getCompany());
                return recruiter;
            default:
                throw new IllegalArgumentException("Unknown user type: " + request.getUserType());
        }
    }
}
