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

	public MatchingService() {
		this.restTemplate = new RestTemplate();
	}

	private Map<String, Object> convertResumeToMap(Resume resume) {
		Map<String, Object> resumeMap = new HashMap<>();
		resumeMap.put("id", resume.getId());

		List<Map<String, Object>> experiencesList = new ArrayList<>();
		for (Experience exp : resume.getExperiences()) {
			Map<String, Object> expMap = new HashMap<>();
			expMap.put("type", exp.getType().toString());
			expMap.put("description", exp.getDescription());
			experiencesList.add(expMap);
		}
		resumeMap.put("experiences", experiencesList);

		List<Map<String, Object>> languagesList = new ArrayList<>();
		for (Language lang : resume.getLanguages()) {
			Map<String, Object> langMap = new HashMap<>();
			langMap.put("language", lang.getLanguage());
			languagesList.add(langMap);
		}
		resumeMap.put("languages", languagesList);

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

	private Map<String, Object> convertObjectToMap(Matchable object) {
		if (object instanceof Resume) {
			return convertResumeToMap((Resume) object);
		} else if (object instanceof JobPost) {
			return convertJobPostToMap((JobPost) object);
		}
		throw new IllegalArgumentException("Unsupported object type");
	}

	public List<Map<String, Object>> findMatchingObjects(Matchable mainObject, List<? extends Matchable> objectList,
			String mainObjectKey, String objectListKey, String endpoint) {
		try {
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put(mainObjectKey, convertObjectToMap(mainObject));

			List<Map<String, Object>> objectMaps = new ArrayList<>();
			for (Matchable object : objectList) {
				objectMaps.add(convertObjectToMap(object));
			}

			requestBody.put(objectListKey, objectMaps);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

			ResponseEntity<Map> response = restTemplate.postForEntity(matchingServiceUrl + endpoint, request,
					Map.class);

			if (response.getBody() != null && "success".equals(response.getBody().get("status"))) {
				return (List<Map<String, Object>>) response.getBody().get("matches");
			}

			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<Map<String, Object>> findMatchingJobs(Resume resume, List<JobPost> jobPosts) {
		return findMatchingObjects(resume, jobPosts, "resume", "job_posts", "/match-jobs");
	}

	public List<Map<String, Object>> findMatchingCandidates(JobPost jobPost, List<Resume> resumes) {
		return findMatchingObjects(jobPost, resumes, "job_post", "resumes", "/match-candidates");
	}

}