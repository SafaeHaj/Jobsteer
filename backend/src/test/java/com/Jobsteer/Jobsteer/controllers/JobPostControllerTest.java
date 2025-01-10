package com.Jobsteer.Jobsteer.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.Jobsteer.Jobsteer.dto.JobPostRequest;
import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.exceptions.EntityNotFoundException;
import com.Jobsteer.Jobsteer.services.JobPostService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(JobPostController.class)
public class JobPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobPostService jobPostService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createJobPost_Success() throws Exception {
        JobPostRequest request = new JobPostRequest();
        // Set required fields
        request.setTitle("Software Engineer");
        request.setLocation("New York");
        request.setDescription("Job Description");
        
        JobPost savedJobPost = new JobPost("Software Engineer", "Job Description", "New York", "apply", null);

        when(jobPostService.createJobPost(any(JobPostRequest.class)))
            .thenReturn(savedJobPost);

        mockMvc.perform(post("/api/jobpost/internal")
                .with(csrf())
                .with(user("test@example.com").roles("RECRUITER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    public void getJobPostsByRecruiter_Success() throws Exception {
        // Arrange
        Long recruiterId = 1L;
        List<JobPost> jobPosts = Arrays.asList(
            new JobPost("Job 1", "Description 1", "Location 1", "apply1", null),
            new JobPost("Job 2", "Description 2", "Location 2", "apply2", null)
        );

        when(jobPostService.findByRecruiterId(recruiterId))
            .thenReturn(jobPosts);

        mockMvc.perform(get("/api/jobpost/recruiter/{recruiterId}", recruiterId)
                .with(user("test@example.com").roles("RECRUITER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Job 1"))
                .andExpect(jsonPath("$[1].title").value("Job 2"));
    }

    @Test
    public void deleteJobPost_Success() throws Exception {
        Long jobPostId = 1L;
        doNothing().when(jobPostService).deleteJobPost(jobPostId);

        mockMvc.perform(delete("/api/jobpost/{jobPostId}", jobPostId)
                .with(csrf())
                .with(user("test@example.com").roles("RECRUITER")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteJobPost_NotFound() throws Exception {
        // Arrange
        Long jobPostId = 1L;
        doThrow(new EntityNotFoundException("Job post not found"))
            .when(jobPostService).deleteJobPost(jobPostId);

        mockMvc.perform(delete("/api/jobpost/{jobPostId}", jobPostId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test@example.com").roles("RECRUITER")))
                .andExpect(status().is(403)); 
    }

}