package com.Jobsteer.Jobsteer.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Jobsteer.Jobsteer.entities.JobSeeker;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.entities.User;
import com.Jobsteer.Jobsteer.repositories.JobSeekerRepository;
import com.Jobsteer.Jobsteer.repositories.RecruiterRepository;
import java.util.Collections;

@Service
public class AuthenticateUserService implements UserDetailsService {

    @Autowired
    private JobSeekerRepository jobseekerRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<JobSeeker> jobseeker = jobseekerRepository.findByEmail(email);
        if (jobseeker.isPresent()) {
            return createUserDetails(jobseeker.get(), "jobseeker");
        }

        Optional<Recruiter> recruiter = recruiterRepository.findByEmail(email);
        if (recruiter.isPresent()) {
            return createUserDetails(recruiter.get(), "recruiter");
        }

        throw new UsernameNotFoundException("User not found");
    }

    private UserDetails createUserDetails(User user, String role) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(role))
        );
    }
}
