package com.Jobsteer.Jobsteer.services;

import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Service
public class ResumeUploadService {
    
    private static final Logger logger = LoggerFactory.getLogger(ResumeUploadService.class);
    
    @Value("${python.parsing.service.url}")
    private String pythonServiceUrl;
    
    private final RestTemplate restTemplate;
    
    public ResumeUploadService() {
        this.restTemplate = new RestTemplate();
    }
    
    public Map<String, Object> uploadAndParseResume(MultipartFile file) throws Exception {
        logger.info("Starting resume upload and parse process");
        try {
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("file", fileResource);
            
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            logger.info("Sending request to Python service at: {}", pythonServiceUrl);
            
           
            ResponseEntity<String> response = restTemplate.postForEntity(
                pythonServiceUrl + "/parse-resume",
                requestEntity,
                String.class
            );
            
            logger.info("Received response from Python service. Status: {}", response.getStatusCode());
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Python service returned error: " + response.getBody());
            }
            
            
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> result = mapper.readValue(response.getBody(), Map.class);
            logger.info("Successfully parsed response");
            return result;
            
        } catch (Exception e) {
            logger.error("Error in uploadAndParseResume: ", e);
            throw new RuntimeException("Failed to process resume: " + e.getMessage(), e);
        }
    }
}