package com.Jobsteer.Jobsteer.models;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.exceptions.JobPostNotFoundException;
import com.Jobsteer.Jobsteer.repositories.JobPostRepository;

public class JobPostModel {
	@Autowired
	private JobPostRepository jobpostRepo;

	public List<JobPost> getAll() {
		List<JobPost> jobposts = jobpostRepo.findAll();
		return jobposts;
	}

	public JobPost findById(int id) {
		Optional<JobPost> jobpost = jobpostRepo.findById(id);

		if (jobpost.isEmpty()) {
			throw new JobPostNotFoundException("Jobpost with id: " + id + " not found.");
		}

		return jobpost.get();
	}

	public void deleteById(int id) {
		if (!jobpostRepo.existsById(id)) {
			throw new JobPostNotFoundException("JobPost with id: " + id + " not found.");
		}
		jobpostRepo.deleteById(id);
	    System.out.println("Successfully deleted jobpost with id: " + id);
	}

	public void deleteAll() {
		jobpostRepo.deleteAll();
		System.out.println("Deleted all Jobposts.");
	}

}
