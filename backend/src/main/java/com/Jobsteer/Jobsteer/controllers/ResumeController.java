package com.Jobsteer.Jobsteer.controllers;

import com.Jobsteer.Jobsteer.services.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
@RestController
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @GetMapping("/resumes")
    public ResponseEntity<?> getAllResumes() {
        return resumeService.getAllResumes();
    }

    @GetMapping("/resume/{id}")
    public ResponseEntity<?> getResumeById(@PathVariable("id") Integer id) {
        return resumeService.getResumeById(id);
    }

    @PostMapping("/resume/upload/{jobSeekerId}")
    public ResponseEntity<?> uploadResume(
            @PathVariable("jobSeekerId") Long jobSeekerId,
            @RequestParam("file") MultipartFile file) {
        return resumeService.uploadResume(jobSeekerId, file);
    }

    @PostMapping("/resume/delete/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable("id") Integer id) {
        return resumeService.deleteResume(id);
    }

    @PutMapping("/resume/update/{id}")
    public ResponseEntity<?> updateResume(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file) {
        try {
			return resumeService.updateResume(id, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
}
