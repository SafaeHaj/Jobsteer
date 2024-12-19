package com.Jobsteer.Jobsteer.models;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Jobsteer.Jobsteer.entities.Resume;
import com.Jobsteer.Jobsteer.exceptions.ResumeNotFoundException;
import com.Jobsteer.Jobsteer.repositories.ResumeRepository;

public class ResumeModel {
	@Autowired
	private ResumeRepository resumeRepo;

	public List<Resume> getAll() {
		List<Resume> resumes = resumeRepo.findAll();
		return resumes;
	}

	public Resume findById(int id) {
		Optional<Resume> resume = resumeRepo.findById(id);

		if (resume.isEmpty()) {
			throw new ResumeNotFoundException("Resume with id: " + id + " not found.");
		}

		return resume.get();
	}

	public void deleteById(int id) {
	    if (!resumeRepo.existsById(id)) {
	        throw new ResumeNotFoundException("Resume with id: " + id + " not found.");
	    }
	    resumeRepo.deleteById(id);
	    System.out.println("Successfully deleted resume with id: " + id);
	}

	public void deleteAll() {
		resumeRepo.deleteAll();
		System.out.println("Deleted all Resumes.");
	}
}
