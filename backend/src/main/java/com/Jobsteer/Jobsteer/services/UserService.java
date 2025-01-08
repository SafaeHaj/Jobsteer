package com.Jobsteer.Jobsteer.services;

import com.Jobsteer.Jobsteer.dto.RegistrationRequest;
import com.Jobsteer.Jobsteer.dto.UserUpdateDTO;
import com.Jobsteer.Jobsteer.entities.JobSeeker;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.entities.User;
import com.Jobsteer.Jobsteer.repositories.JobSeekerRepository;
import com.Jobsteer.Jobsteer.repositories.RecruiterRepository;
import com.Jobsteer.Jobsteer.utils.UserFactory;

import jakarta.transaction.Transactional;

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

    @Transactional
    public User updateUser(Long id, UserUpdateDTO updateDTO) {
        // First try to find as JobSeeker
        JobSeeker jobSeeker = jobseekerRepository.findById(id).orElse(null);
        if (jobSeeker != null) {
            return updateJobSeeker(jobSeeker, updateDTO);
        }

        // If not JobSeeker, try Recruiter
        Recruiter recruiter = recruiterRepository.findById(id).orElse(null);
        if (recruiter != null) {
            return updateRecruiter(recruiter, updateDTO);
        }

        throw new RuntimeException("User not found with id: " + id);
    }

    private JobSeeker updateJobSeeker(JobSeeker jobSeeker, UserUpdateDTO updateDTO) {
        if (updateDTO.getEmail() != null) {
            jobSeeker.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getFirstName() != null) {
            jobSeeker.setFirstName(updateDTO.getFirstName());
        }
        if (updateDTO.getLastName() != null) {
            jobSeeker.setLastName(updateDTO.getLastName());
        }
        if (updateDTO.getLocation() != null) {
            jobSeeker.setLocation(updateDTO.getLocation());
        }
        return jobseekerRepository.save(jobSeeker);
    }

    private Recruiter updateRecruiter(Recruiter recruiter, UserUpdateDTO updateDTO) {
        if (updateDTO.getEmail() != null) {
            recruiter.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getFirstName() != null) {
            recruiter.setFirstName(updateDTO.getFirstName());
        }
        if (updateDTO.getLastName() != null) {
            recruiter.setLastName(updateDTO.getLastName());
        }
        if (updateDTO.getCompanyName() != null) {
            recruiter.setCompanyName(updateDTO.getCompanyName());
        }
        return recruiterRepository.save(recruiter);
    }
}

