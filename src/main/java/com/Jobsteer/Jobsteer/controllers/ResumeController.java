package com.Jobsteer.Jobsteer.controllers;


import java.util.List;


import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Jobsteer.Jobsteer.entities.Resume;
import com.Jobsteer.Jobsteer.entities.JobSeeker;
import com.Jobsteer.Jobsteer.entities.Experience;
import com.Jobsteer.Jobsteer.entities.Language;
import com.Jobsteer.Jobsteer.entities.ExperienceType;
import com.Jobsteer.Jobsteer.exceptions.ResumeNotFoundException;
import com.Jobsteer.Jobsteer.repositories.ResumeRepository;
import com.Jobsteer.Jobsteer.repositories.JobSeekerRepository;
import com.Jobsteer.Jobsteer.repositories.LanguageRepository;
import com.Jobsteer.Jobsteer.services.ResumeUploadService;

@RequestMapping("/api") 
@RestController
public class ResumeController {
	
	private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);
	
    @Autowired
    private ResumeRepository resumeRepo;
    
    @Autowired
    private JobSeekerRepository jobSeekerRepo;
    
    @Autowired
    private LanguageRepository languageRepo;
    
    @Autowired
    private ResumeUploadService uploadService;

    @GetMapping("/resumes")
    public String getResumes(Model model) {
        List<Resume> resumes = resumeRepo.findAll(); 
        model.addAttribute("resumes", resumes); 
        return "resumes";
    }
    
    @GetMapping("/api/resumes")
    public ResponseEntity<List<Resume>> getAllResumes() {
        List<Resume> resumes = resumeRepo.findAll();
        return ResponseEntity.ok(resumes);
    }
    
    @GetMapping("/api/resume/{id}")
    public ResponseEntity<?> getResumeById(@PathVariable int id) {
        try {
            Optional<Resume> resume = resumeRepo.findById(id);
            System.out.println("resume: "+ resume.get());
            if (resume.isPresent()) {
                return ResponseEntity.ok(resume.get());
            } else {
                throw new ResumeNotFoundException("Resume with id: " + id + " not found.");
            }
        } catch (ResumeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //@PostMapping("/resume/upload/{jobSeekerId}")
    public ResponseEntity<?> uploadResume1(
            @PathVariable("jobSeekerId") Long jobSeekerId,
            @RequestParam("file") MultipartFile file) {
        
        logger.info("Starting resume upload for jobSeeker: {}", jobSeekerId);
        
        try {
            // Convert file to bytes
            logger.info("Converting file to bytes: {}", file.getOriginalFilename());
            byte[] fileBytes = file.getBytes();
            
            // Find JobSeeker
            logger.info("Looking for JobSeeker with ID: {}", jobSeekerId);
            Optional<JobSeeker> jobSeekerOpt = jobSeekerRepo.findById(jobSeekerId);
            if (!jobSeekerOpt.isPresent()) {
                logger.error("JobSeeker not found with ID: {}", jobSeekerId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "status", "error",
                        "message", "JobSeeker with id: " + jobSeekerId + " not found"
                    ));
            }
            JobSeeker jobSeeker = jobSeekerOpt.get();
            logger.info("Found JobSeeker: {}", jobSeeker.getEmail());

            // Parse resume using Python service
            logger.info("Sending file to Python service for parsing");
            Map<String, Object> parsedData = uploadService.uploadAndParseResume(file);
            logger.info("Received parsed data from Python service");

            // Create new Resume
            Resume resume = new Resume();
            resume.setSourceFile(fileBytes);
            resume.setJobSeeker(jobSeeker);

            // Process each experience type
            String[] experienceTypes = {"education", "work", "project", "extra"};
            ExperienceType[] mappedTypes = {
                ExperienceType.EDUCATION, 
                ExperienceType.WORK, 
                ExperienceType.PROJECT, 
                ExperienceType.EXTRA
            };

            for (int i = 0; i < experienceTypes.length; i++) {
                String type = experienceTypes[i];
                if (parsedData.containsKey(type)) {
                    List<Map<String, String>> experiences = (List<Map<String, String>>) parsedData.get(type);
                    logger.info("Processing {} experiences: {} found", type, experiences.size());
                    
                    for (Map<String, String> exp : experiences) {
                        if (exp.get("description") != null && !exp.get("description").trim().isEmpty()) {
                            Experience experience = new Experience();
                            experience.setType(mappedTypes[i]);
                            experience.setDescription(exp.get("description"));
                            resume.addExperience(experience);
                        }
                    }
                }
            }

            // Save resume
            logger.info("Saving resume to database");
            resume = resumeRepo.save(resume);
            logger.info("Resume saved with ID: {}", resume.getId());

            // Update JobSeeker
            logger.info("Updating JobSeeker with new resume");
            jobSeeker.uploadCv(resume);
            jobSeekerRepo.save(jobSeeker);
            logger.info("JobSeeker updated successfully");
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Resume uploaded successfully");
            response.put("resumeId", resume.getId());
            response.put("parsedData", parsedData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing resume: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "status", "error",
                    "message", "Failed to process resume: " + e.getMessage()
                ));
        }
    }

    
    
    @PostMapping("/resume/upload/{jobSeekerId}")
    public ResponseEntity<?> uploadResume(
            @PathVariable("jobSeekerId") Long jobSeekerId,
            @RequestParam("file") MultipartFile file) {
        
        logger.info("Starting resume upload for jobSeeker: {}", jobSeekerId);
        
        try {
            // Find JobSeeker
            Optional<JobSeeker> jobSeekerOpt = jobSeekerRepo.findById(jobSeekerId);
            if (!jobSeekerOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "status", "error",
                        "message", "JobSeeker with id: " + jobSeekerId + " not found"
                    ));
            }
            JobSeeker jobSeeker = jobSeekerOpt.get();
            
            Resume existingResume = jobSeeker.getResume();
            if (existingResume != null) {
                existingResume.getExperiences().clear();
                existingResume.getLanguages().clear();
                existingResume.setSourceFile(file.getBytes());
                Map<String, Object> parsedData = uploadService.uploadAndParseResume(file);
                
                
                String[] experienceTypes = {"education", "work", "project", "extra"};
                ExperienceType[] mappedTypes = {
                    ExperienceType.EDUCATION, 
                    ExperienceType.WORK, 
                    ExperienceType.PROJECT, 
                    ExperienceType.EXTRA
                };

                for (int i = 0; i < experienceTypes.length; i++) {
                    String type = experienceTypes[i];
                    if (parsedData.containsKey(type)) {
                        List<Map<String, String>> experiences = (List<Map<String, String>>) parsedData.get(type);
                        
                        for (Map<String, String> exp : experiences) {
                            if (exp.get("description") != null && !exp.get("description").trim().isEmpty()) {
                                Experience experience = new Experience();
                                experience.setType(mappedTypes[i]);
                                experience.setDescription(exp.get("description"));
                                existingResume.addExperience(experience);
                            }
                        }
                    }
                }
                
                // Save updated resume
                Resume updatedResume = resumeRepo.save(existingResume);
                
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Resume updated successfully",
                    "resumeId", updatedResume.getId(),
                    "parsedData", parsedData
                ));
                
            } else {
                // Create new Resume if none exists
                byte[] fileBytes = file.getBytes();
                Map<String, Object> parsedData = uploadService.uploadAndParseResume(file);
                
                Resume newResume = new Resume();
                newResume.setSourceFile(fileBytes);
                newResume.setJobSeeker(jobSeeker);
                
                // Process experiences
                String[] experienceTypes = {"education", "work", "project", "extra"};
                ExperienceType[] mappedTypes = {
                    ExperienceType.EDUCATION, 
                    ExperienceType.WORK, 
                    ExperienceType.PROJECT, 
                    ExperienceType.EXTRA
                };

                for (int i = 0; i < experienceTypes.length; i++) {
                    String type = experienceTypes[i];
                    if (parsedData.containsKey(type)) {
                        List<Map<String, String>> experiences = (List<Map<String, String>>) parsedData.get(type);
                        
                        for (Map<String, String> exp : experiences) {
                            if (exp.get("description") != null && !exp.get("description").trim().isEmpty()) {
                                Experience experience = new Experience();
                                experience.setType(mappedTypes[i]);
                                experience.setDescription(exp.get("description"));
                                newResume.addExperience(experience);
                            }
                        }
                    }
                }
                
                // Save new resume
                Resume savedResume = resumeRepo.save(newResume);
                
                // Update JobSeeker
                jobSeeker.uploadCv(savedResume);
                jobSeekerRepo.save(jobSeeker);
                
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Resume uploaded successfully",
                    "resumeId", savedResume.getId(),
                    "parsedData", parsedData
                ));
            }
            
        } catch (Exception e) {
            logger.error("Error processing resume: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "status", "error",
                    "message", "Failed to process resume: " + e.getMessage()
                ));
        }
    }
    
    
    
    

    @PostMapping("/api/resume/delete/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable int id) {
        try {
            if (!resumeRepo.existsById(id)) {
                throw new ResumeNotFoundException("Resume with id: " + id + " not found.");
            }
            
            Resume resume = resumeRepo.findById(id).get();
            Optional<JobSeeker> jobSeekerOpt = jobSeekerRepo.findAll().stream()
                .filter(js -> js.getResume() != null && js.getResume().getId() == id)
                .findFirst();
            
            if (jobSeekerOpt.isPresent()) {
                JobSeeker jobSeeker = jobSeekerOpt.get();
                jobSeeker.deleteCv();
                jobSeekerRepo.save(jobSeeker);
            }
            
            resumeRepo.deleteById(id);
            return ResponseEntity.ok().body("Resume successfully deleted");
            
        } catch (ResumeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Failed to delete resume: " + e.getMessage());
        }
    }
    
    @PostMapping("/api/resume/update/{id}")
    public ResponseEntity<?> updateResume(@PathVariable int id,@RequestParam("file") MultipartFile file) {
        try {

            if (!resumeRepo.existsById(id)) {
                throw new ResumeNotFoundException("Resume with id: " + id + " not found.");
            }
            
            Resume existingResume = resumeRepo.findById(id).get();
            
            Map<String, Object> parsedData = uploadService.uploadAndParseResume(file);
            
            existingResume.getExperiences().clear();
            List<Map<String, String>> experiences = (List<Map<String, String>>) parsedData.get("experience");
            if (experiences != null) {
                for (Map<String, String> exp : experiences) {
                    Experience experience = new Experience();
                    experience.setDescription(exp.get("description"));
                    experience.setResume(existingResume);
                    existingResume.getExperiences().add(experience);
                }
            }
            
            existingResume.getLanguages().clear();
            List<String> languages = (List<String>) parsedData.get("languages");
            if (languages != null) {
                for (String lang : languages) {
                    Language language = new Language(lang, existingResume);
                    existingResume.getLanguages().add(language);
                }
            }
            
            Resume updatedResume = resumeRepo.save(existingResume);
            return ResponseEntity.ok(updatedResume);
            
        } catch (ResumeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Failed to update resume: " + e.getMessage());
        }
    }
}