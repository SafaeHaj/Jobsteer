package com.Jobsteer.Jobsteer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class ResumeParseService {

    private static final Logger logger = LoggerFactory.getLogger(ResumeParseService.class);

    @Value("${python.parsing.service.url}")
    private String pythonServiceUrl;

    private final RestTemplate restTemplate;

    public ResumeParseService() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> parseResume(MultipartFile file) throws Exception {
        logger.info("Uploading file to parsing service");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", createFileResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(pythonServiceUrl + "/parse-resume", requestEntity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Python service returned error: " + response.getBody());
        }

        return new ObjectMapper().readValue(response.getBody(), Map.class);
    }

    private ByteArrayResource createFileResource(MultipartFile file) throws Exception {
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
    }
}
