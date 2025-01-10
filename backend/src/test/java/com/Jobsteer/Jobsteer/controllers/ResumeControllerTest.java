package com.Jobsteer.Jobsteer.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import com.Jobsteer.Jobsteer.entities.Resume;
import com.Jobsteer.Jobsteer.services.ResumeService;

@SpringBootTest
@AutoConfigureMockMvc
public class ResumeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResumeService resumeService;

    @Test
    @WithMockUser(roles = "JOBSEEKER")
    public void uploadResume_Success() throws Exception {
        // Arrange
        Long jobSeekerId = 1L;
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "resume.pdf",
            MediaType.APPLICATION_PDF_VALUE,
            "PDF content".getBytes()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("resumeId", 1);
        doReturn(ResponseEntity.ok(response))
        .when(resumeService)
        .uploadResume(eq(jobSeekerId), any());


        // Act & Assert
        mockMvc.perform(multipart("/api/resume/upload/{jobSeekerId}", jobSeekerId)
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.resumeId").exists());
    }

    @Test
    @WithMockUser(roles = "JOBSEEKER")
    public void getResumeById_Success() throws Exception {
        // Arrange
        Integer resumeId = 1;
        Resume resume = new Resume();
        resume.setId(resumeId);

        doReturn(ResponseEntity.ok(resume))
            .when(resumeService)
            .getResumeById(any());  

        // Act & Assert
        mockMvc.perform(get("/api/resume/{id}", resumeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "JOBSEEKER")
    public void getResumeById_NotFound() throws Exception {
        // Arrange
        Integer resumeId = 999;
        doReturn(ResponseEntity.notFound().build())
            .when(resumeService)
            .getResumeById(any());  

        // Act & Assert
        mockMvc.perform(get("/api/resume/{id}", resumeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "JOBSEEKER")
    public void deleteResume_Success() throws Exception {
        // Arrange
        Integer resumeId = 1;
        doReturn(ResponseEntity.ok("Resume successfully deleted"))
            .when(resumeService)
            .deleteResume(any()); 

        // Act & Assert
        mockMvc.perform(post("/api/resume/delete/{id}", resumeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Resume successfully deleted"));
    }
}