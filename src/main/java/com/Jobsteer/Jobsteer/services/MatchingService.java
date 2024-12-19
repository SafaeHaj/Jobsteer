package com.Jobsteer.Jobsteer.services;

import com.Jobsteer.Jobsteer.entities.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

@Service
public class MatchingService {
    
    @Value("${python.matching.service.url}")
    private String matchingServiceUrl;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public MatchingService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    private Map<String, Object> convertResumeToMap(Resume resume) {
        Map<String, Object> resumeMap = new HashMap<>();
        resumeMap.put("id", resume.getId());
        
        // Convert experiences
        List<Map<String, Object>> experiencesList = new ArrayList<>();
        for (Experience exp : resume.getExperiences()) {
            Map<String, Object> expMap = new HashMap<>();
            expMap.put("type", exp.getType().toString());
            expMap.put("description", exp.getDescription());
            experiencesList.add(expMap);
        }
        resumeMap.put("experiences", experiencesList);
        
        // Convert languages
        List<Map<String, Object>> languagesList = new ArrayList<>();
        for (Language lang : resume.getLanguages()) {
            Map<String, Object> langMap = new HashMap<>();
            langMap.put("language", lang.getLanguage());
            languagesList.add(langMap);
        }
        resumeMap.put("languages", languagesList);
        
        // Convert JobSeeker
        Map<String, Object> jobSeekerMap = new HashMap<>();
        jobSeekerMap.put("location", resume.getJobSeeker().getLocation());
        resumeMap.put("jobSeeker", jobSeekerMap);
        
        return resumeMap;
    }
    
    private Map<String, Object> convertJobPostToMap(JobPost jobPost) {
        Map<String, Object> jobMap = new HashMap<>();
        jobMap.put("id", jobPost.getId());
        jobMap.put("title", jobPost.getTitle());
        jobMap.put("location", jobPost.getLocation());
        
        // Convert requirements
        List<Map<String, Object>> requirementsList = new ArrayList<>();
        for (Requirement req : jobPost.getRequirement()) {
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("type", req.getType().toString());
            reqMap.put("description", req.getDescription());
            requirementsList.add(reqMap);
        }
        jobMap.put("requirements", requirementsList);
        
        return jobMap;
    }
    
    
    public List<Map<String, Object>> findMatchingJobs(Resume resume, List<JobPost> jobPosts) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("resume",convertResumeToMap(resume));
            
            List<Map<String, Object>> jobPostsMaps = new ArrayList<>();
            for (JobPost jobPost : jobPosts) {
                jobPostsMaps.add(convertJobPostToMap(jobPost));
            }
            
            requestBody.put("job_posts", jobPostsMaps);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(matchingServiceUrl + "/match-jobs",request,Map.class);
            
            if (response.getBody() != null && response.getBody().get("status").equals("success")) {
                return (List<Map<String, Object>>) response.getBody().get("matches");
            }
            
            return new ArrayList<>();
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<Map<String, Object>> findMatchingCandidates(JobPost jobPost, List<Resume> resumes) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("job_post", convertJobPostToMap(jobPost));
            
            List<Map<String, Object>> resumeMaps = new ArrayList<>();
            for (Resume resume : resumes) {
                resumeMaps.add(convertResumeToMap(resume));
            }
            
            requestBody.put("resumes",resumeMaps);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(matchingServiceUrl + "/match-candidates",request,Map.class);
            
            if (response.getBody() != null && response.getBody().get("status").equals("success")) {
                return (List<Map<String, Object>>) response.getBody().get("matches");
            }
            
            return new ArrayList<>();
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}