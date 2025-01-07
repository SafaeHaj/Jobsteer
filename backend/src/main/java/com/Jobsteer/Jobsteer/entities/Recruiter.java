package com.Jobsteer.Jobsteer.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class Recruiter extends User {
    @NotNull
    private String companyName;

    public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "recruiter")
	@JsonManagedReference
	private List<JobPost> jobPostings = new ArrayList<>();

    public void postJob(JobPost jobPosting) {
        jobPosting.setRecruiter(this);
        this.jobPostings.add(jobPosting);
    }

    public void updateJob(JobPost jobPosting, String updatedTitle) {
        if (jobPostings.contains(jobPosting)) {
        jobPosting.setTitle(updatedTitle);
        }
    }

    public void deleteJob(JobPost jobPosting) {
        jobPostings.remove(jobPosting);
    }
}