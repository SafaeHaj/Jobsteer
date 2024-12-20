package com.Jobsteer.Jobsteer.services;

import com.Jobsteer.Jobsteer.dto.RegistrationRequest;
import com.Jobsteer.Jobsteer.entities.JobSeeker;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.entities.User;
import com.Jobsteer.Jobsteer.repositories.JobSeekerRepository;
import com.Jobsteer.Jobsteer.repositories.RecruiterRepository;
import com.Jobsteer.Jobsteer.utils.UserFactory;

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
    
    @Autowired
    private UserFactory userFactory;

    public User registerUser(RegistrationRequest registrationRequest) {
        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        User user = userFactory.createUser(registrationRequest, encodedPassword);
        if (user instanceof JobSeeker) {
            return jobseekerRepository.save((JobSeeker) user);
        } else if (user instanceof Recruiter) {
            return recruiterRepository.save((Recruiter) user);
        }
        throw new IllegalArgumentException("Unsupported user type");
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
