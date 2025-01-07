package com.Jobsteer.Jobsteer.entities;
import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity
@Table(name = "jobpost")
public class JobPost implements Matchable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String title;
	
	@Column
	private String location;
    
	@OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Requirement> requirements= new ArrayList<>();
    
	@ManyToOne
    @JsonBackReference
    private Recruiter recruiter;
	
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @Lob
	private String description;
	
	@Column 
	private String source;

    @Column
    private String toApply; 
    
	public List<Requirement> getRequirement() {
		return requirements;
	}

	public Recruiter getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(Recruiter recruiter) {
		this.recruiter = recruiter;
	}

	public JobPost(String title,String description, String location, String toApply, String source) {
		this.title = title;
		this.location = location;
		this.requirements = new ArrayList<Requirement>();
		this.toApply = toApply;
		this.source= source;
		this.description=description;
	}
	
	public JobPost() {
	}

	public String getToApply() {
		return toApply;
	}

	public void setToApply(String toApply) {
		this.toApply = toApply;
	}

	
    public void addRequirement(Requirement requirement) {
        requirement.setJobPost(this);
        this.requirements.add(requirement);
    }
    
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public void setRequirements(ArrayList<Requirement> requirements) {
		this.requirements = requirements;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "JobPost [id=" + id + ", title=" + title + ", location=" + location + ", requirements=" + requirements
				+ ", recruiter=" + recruiter + ", description=" + description + ", source=" + source + ", toApply="
				+ toApply + "]";
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description2) {
		this.description = description2;
		
	}

	public void setSource(String object) {
		this.source = object;
		
	}

	public void setRecruiterID(Long recruiterId) {
		this.recruiter.setId(recruiterId);
		
	}

	
	
}