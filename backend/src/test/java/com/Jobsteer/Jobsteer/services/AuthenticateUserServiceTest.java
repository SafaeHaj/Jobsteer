package com.Jobsteer.Jobsteer.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import com.Jobsteer.Jobsteer.entities.JobSeeker;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.repositories.JobSeekerRepository;
import com.Jobsteer.Jobsteer.repositories.RecruiterRepository;

public class AuthenticateUserServiceTest {

    private AuthenticateUserService authenticateUserService;
    private JobSeekerRepository jobSeekerRepository;
    private RecruiterRepository recruiterRepository;

    @BeforeEach
    void setUp() {
        jobSeekerRepository = mock(JobSeekerRepository.class);
        recruiterRepository = mock(RecruiterRepository.class);
        authenticateUserService = new AuthenticateUserService();
        
        ReflectionTestUtils.setField(authenticateUserService, "jobseekerRepository", jobSeekerRepository);
        ReflectionTestUtils.setField(authenticateUserService, "recruiterRepository", recruiterRepository);
    }

    @Test
    void loadUserByUsername_WithJobSeeker_ReturnsUserDetails() {
        
        String email = "jobseeker@test.com";
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setEmail(email);
        jobSeeker.setPassword("password");
        
        when(jobSeekerRepository.findByEmail(email)).thenReturn(Optional.of(jobSeeker));

        
        UserDetails userDetails = authenticateUserService.loadUserByUsername(email);

        
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("jobseeker", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_WithRecruiter_ReturnsUserDetails() {
        
        String email = "recruiter@test.com";
        Recruiter recruiter = new Recruiter();
        recruiter.setEmail(email);
        recruiter.setPassword("password");
        
        when(jobSeekerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(recruiterRepository.findByEmail(email)).thenReturn(Optional.of(recruiter));

        
        UserDetails userDetails = authenticateUserService.loadUserByUsername(email);

        
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("recruiter", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_WithNonExistentUser_ThrowsException() {
       
        String email = "nonexistent@test.com";
        when(jobSeekerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(recruiterRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, 
            () -> authenticateUserService.loadUserByUsername(email));
    }
}