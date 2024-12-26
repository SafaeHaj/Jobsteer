package com.Jobsteer.Jobsteer.services;

import com.Jobsteer.Jobsteer.entities.Resume;
import com.Jobsteer.Jobsteer.entities.Experience;
import com.Jobsteer.Jobsteer.entities.ExperienceType;
import com.Jobsteer.Jobsteer.entities.JobSeeker;
import com.Jobsteer.Jobsteer.entities.Language;
import com.Jobsteer.Jobsteer.repositories.ResumeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Jobsteer.Jobsteer.repositories.JobSeekerRepository;
import com.Jobsteer.Jobsteer.exceptions.ResumeNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ResumeService {

	private static final Logger logger = LoggerFactory.getLogger(ResumeService.class);

	@Autowired
	private ResumeRepository resumeRepo;

	@Autowired
	private JobSeekerRepository jobSeekerRepo;

	@Autowired
	private ResumeParseService parseService;

	public ResponseEntity<List<Resume>> getAllResumes() {
        List<Resume> resumes = resumeRepo.findAll();
        return ResponseEntity.ok(resumes);
    }
	
	public ResponseEntity<?> getResumeById(Long id) {
		try {
			Optional<Resume> resume = resumeRepo.findById(id);
			System.out.println("resume: " + resume.get());
			if (resume.isPresent()) {
				return ResponseEntity.ok(resume.get());
			} else {
				throw new ResumeNotFoundException("Resume with id: " + id + " not found.");
			}
		} catch (ResumeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	public ResponseEntity<?> deleteResume(Long id) {
	    try {
	    	 Optional<JobSeeker> jobSeekerOpt = jobSeekerRepo.findAll().stream()
	                 .filter(js -> js.getResume() != null && js.getResume().getId() == id)
	                 .findFirst();
	        
	        resumeRepo.deleteById(id);
	        return ResponseEntity.ok("Resume successfully deleted");
	    } catch (ResumeNotFoundException e) {
	        return ResponseEntity.notFound().build();
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Failed to delete resume: " + e.getMessage());
	    }
	}

	public ResponseEntity<?> uploadResume(Long jobSeekerId, MultipartFile file) {
		logger.info("Starting resume upload for jobSeeker: {}", jobSeekerId);

		try {
			JobSeeker jobSeeker = jobSeekerRepo.findById(jobSeekerId).orElseThrow(
					() -> new IllegalArgumentException("JobSeeker with id: " + jobSeekerId + " not found"));

			Resume resume = Optional.ofNullable(jobSeeker.getResume()).orElseGet(() -> createNewResume(jobSeeker));

			resume.setSourceFile(file.getBytes());
			Map<String, Object> parsedData = parseService.parseResume(file);

			populateResumeData(resume, parsedData);
			Resume savedResume = resumeRepo.save(resume);

			if (resume.getId() == null) {
				jobSeeker.setResume(savedResume);
				jobSeekerRepo.save(jobSeeker);
			}

			return ResponseEntity.ok(Map.of("status", "success", "message", "Resume processed successfully", "resumeId",
					savedResume.getId(), "parsedData", parsedData));
		} catch (Exception e) {
			logger.error("Error processing resume: ", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("status", "error", "message", "Failed to process resume: " + e.getMessage()));
		}
	}

	public ResponseEntity<?> updateResume(Long id, MultipartFile file) {
		try {
			Resume resume = resumeRepo.findById(id)
					.orElseThrow(() -> new ResumeNotFoundException("Resume with id: " + id + " not found"));

			resume.setSourceFile(file.getBytes());
			Map<String, Object> parsedData = parseService.parseResume(file);

			populateResumeData(resume, parsedData);
			Resume updatedResume = resumeRepo.save(resume);

			return ResponseEntity.ok(updatedResume);
		} catch (ResumeNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("Error updating resume: ", e);
			return ResponseEntity.badRequest().body("Failed to update resume: " + e.getMessage());
		}
	}

	private Resume createNewResume(JobSeeker jobSeeker) {
		Resume newResume = new Resume();
		newResume.setJobSeeker(jobSeeker);
		return newResume;
	}

	private void populateResumeData(Resume resume, Map<String, Object> parsedData) {
		resume.getExperiences().clear();
		resume.getLanguages().clear();

		Map<String, ExperienceType> experienceMapping = Map.of("education", ExperienceType.EDUCATION, "work",
				ExperienceType.WORK, "project", ExperienceType.PROJECT, "extra", ExperienceType.EXTRA);

		experienceMapping.forEach((type, experienceType) -> {
			List<Map<String, String>> experiences = (List<Map<String, String>>) parsedData.get(type);
			if (experiences != null) {
				experiences.stream()
						.filter(exp -> exp.get("description") != null && !exp.get("description").trim().isEmpty())
						.forEach(exp -> {
							Experience experience = new Experience();
							experience.setType(experienceType);
							experience.setDescription(exp.get("description"));
							experience.setResume(resume);
							resume.addExperience(experience);
						});
			}
		});

		List<String> languages = (List<String>) parsedData.get("languages");
		if (languages != null) {
			languages.forEach(lang -> resume.addLanguage(new Language(lang, resume)));
		}
	}
}
