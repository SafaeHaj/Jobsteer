package com.Jobsteer.Jobsteer.services;

import com.Jobsteer.Jobsteer.dto.RegistrationRequest;
import com.Jobsteer.Jobsteer.entities.JobSeeker;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.entities.User;
import com.Jobsteer.Jobsteer.repositories.JobSeekerRepository;
import com.Jobsteer.Jobsteer.repositories.RecruiterRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private JobSeekerRepository jobseekerRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(RegistrationRequest registrationRequest) {
        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());

        User user = null;
        if ("jobseeker".equalsIgnoreCase(registrationRequest.getUserType())) {
            JobSeeker jobseeker = new JobSeeker();
            jobseeker.setEmail(registrationRequest.getEmail());
            jobseeker.setPassword(encodedPassword);
            jobseeker.setFirstName(registrationRequest.getFirstName());
            jobseeker.setLastName(registrationRequest.getLastName());
            jobseeker.setLocation(registrationRequest.getLocation());
            user = jobseekerRepository.save(jobseeker);
            
        } else if ("recruiter".equalsIgnoreCase(registrationRequest.getUserType())) {
            Recruiter recruiter = new Recruiter();
            recruiter.setEmail(registrationRequest.getEmail());
            recruiter.setPassword(encodedPassword);
            recruiter.setFirstName(registrationRequest.getFirstName());
            recruiter.setLastName(registrationRequest.getLastName());
            recruiter.setCompanyName(registrationRequest.getCompany());
            user = recruiterRepository.save(recruiter);
        }

        return user;
    }
    
    public User getUserByEmail(String email) {
        Optional<JobSeeker> jobSeeker = jobseekerRepository.findByEmail(email);
        if (jobSeeker.isPresent()) {
            return jobSeeker.get();
        }

        Optional<Recruiter> recruiter = recruiterRepository.findByEmail(email);
        if (recruiter.isPresent()) {
            return recruiter.get();
        }

        throw new UsernameNotFoundException("User not found");
    }
}
