package com.Jobsteer.Jobsteer.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import com.Jobsteer.Jobsteer.dto.JobPostRequest;
import com.Jobsteer.Jobsteer.dto.JobPostRequest.RequirementRequest;
import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.entities.Recruiter;
import com.Jobsteer.Jobsteer.exceptions.EntityNotFoundException;
import com.Jobsteer.Jobsteer.repositories.JobPostRepository;
import com.Jobsteer.Jobsteer.repositories.RecruiterRepository;
import com.Jobsteer.Jobsteer.repositories.RequirementRepository;
import org.springframework.test.util.ReflectionTestUtils;

public class JobPostServiceTest {

    private JobPostService jobPostService;
    private JobPostRepository jobPostRepository;
    private RecruiterRepository recruiterRepository;
    private RequirementRepository requirementRepository;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        jobPostRepository = mock(JobPostRepository.class);
        recruiterRepository = mock(RecruiterRepository.class);
        requirementRepository = mock(RequirementRepository.class);
        restTemplate = mock(RestTemplate.class);

        jobPostService = new JobPostService();
        ReflectionTestUtils.setField(jobPostService, "jobPostRepository", jobPostRepository);
        ReflectionTestUtils.setField(jobPostService, "recruiterRepository", recruiterRepository);
        ReflectionTestUtils.setField(jobPostService, "requirementRepository", requirementRepository);
        ReflectionTestUtils.setField(jobPostService, "restTemplate", restTemplate);
    }

    @Test
    void createJobPost_Success() {
        // Arrange
        JobPostRequest request = new JobPostRequest() {
            @Override
            public Long getRecruiterId() {
                return 1L;
            }
        };
        request.setTitle("Software Engineer");
        request.setLocation("New York");
        request.setDescription("Job Description");
        request.setToApply("Apply at website");
        
        // Create and set requirements
        List<RequirementRequest> requirements = new ArrayList<>();
        RequirementRequest requirement = new RequirementRequest();
        requirement.setType("SKILL");
        requirement.setDescription("Java Programming");
        requirements.add(requirement);
        request.setRequirements(requirements);
        
        Recruiter recruiter = new Recruiter();
        recruiter.setId(1L);
        
        when(recruiterRepository.findById(1L)).thenReturn(Optional.of(recruiter));
        when(jobPostRepository.save(any(JobPost.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        JobPost result = jobPostService.createJobPost(request);

        // Assert
        assertNotNull(result);
        assertEquals("Software Engineer", result.getTitle());
        assertEquals("New York", result.getLocation());
        assertEquals(recruiter, result.getRecruiter());
        assertNotNull(result.getRequirement());
        assertFalse(result.getRequirement().isEmpty());
    }

    @Test
    void findByRecruiterId_ReturnsJobPosts() {
        // Arrange
        Long recruiterId = 1L;
        List<JobPost> expectedJobPosts = Arrays.asList(
            new JobPost("Job 1", "Description 1", "Location 1", "apply1", null),
            new JobPost("Job 2", "Description 2", "Location 2", "apply2", null)
        );
        
        when(jobPostRepository.findByRecruiterId(recruiterId)).thenReturn(expectedJobPosts);

        // Act
        List<JobPost> result = jobPostService.findByRecruiterId(recruiterId);

        // Assert
        assertEquals(expectedJobPosts.size(), result.size());
        assertEquals(expectedJobPosts.get(0).getTitle(), result.get(0).getTitle());
    }

    @Test
    void deleteJobPost_Success() {
        // Arrange
        Long jobPostId = 1L;
        when(jobPostRepository.existsById(jobPostId)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> jobPostService.deleteJobPost(jobPostId));
        verify(jobPostRepository).deleteById(jobPostId);
    }

    @Test
    void deleteJobPost_NonExistentId_ThrowsException() {
        // Arrange
        final Long jobPostId = 1L;
        when(jobPostRepository.existsById(jobPostId)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            jobPostService.deleteJobPost(jobPostId);
        });
    }
}