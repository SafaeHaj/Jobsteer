package com.Jobsteer.Jobsteer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "requirement")
public class Requirement{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(columnDefinition = "ENUM('EDUCATION', 'LANGUAGE', 'SKILL', 'EXPERIENCE')")
	@Enumerated(EnumType.STRING)
	private RequirementType type;

	@Column
	private String description;
	

	@ManyToOne
	@JoinColumn(name = "jobpost")
	private JobPost jobPost;

	public Requirement(RequirementType type, String description) {
		this.type = type;
		this.description = description;
	}
	
	public Requirement() {
	}

	public Requirement(RequirementType valueOf, String description2, JobPost jobPost2) {
		this.type = valueOf;
		this.description = description2;
		this.jobPost = jobPost2;
	}

	@Override
	public String toString() {
		return "Requirement [id=" + id + ", type=" + type + ", description=" + description + ", jobPost=" + jobPost
				+ "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RequirementType getType() {
		return type;
	}

	public void setType(RequirementType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JobPost getJobPost() {
		return jobPost;
	}

	public void setJobPost(JobPost jobPost) {
		this.jobPost = jobPost;
	}




}
