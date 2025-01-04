package com.Jobsteer.Jobsteer.entities;

import java.io.InputStream;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "resume")
public class Resume implements Matchable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Experience> experiences;

	@OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Language> languages;
    
    @Lob
    @Column(name = "sourceFile", columnDefinition = "LONGBLOB")
    private byte[] sourceFile;
    
	@OneToOne(cascade = CascadeType.ALL)
    private JobSeeker jobSeeker;
    
    
    public JobSeeker getJobSeeker() {
		return jobSeeker;
	}

	public void setJobSeeker(JobSeeker jobSeeker) {
		this.jobSeeker = jobSeeker;
	}
	
    public Resume() {
		this.languages = new ArrayList<Language>();
		this.experiences = new ArrayList<Experience>();
    }

	public Resume(byte[] file) {
		this.sourceFile = file;
		this.languages = new ArrayList<Language>();
		this.experiences = new ArrayList<Experience>();
	}
	
	public Resume(long id2, InputStream inputStream) {
        this.languages = new ArrayList<>();
        this.experiences = new ArrayList<>();
	}
	public byte[] getSourceFile() {
		return sourceFile;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Experience> getExperiences() {
		return experiences;
	}

	public void setExperiences(List<Experience> experiences) {
		this.experiences = experiences;
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public void addLanguage(Language language) {
		this.languages.add(language);
		language.setResume(this);
	}

	public void setSourceFile(byte[] fileBytes) {
		this.sourceFile = fileBytes ;
		
	}
	public void addExperience(Experience experience) {
	    experiences.add(experience);
	    experience.setResume(this); 
	}
    public void removeExperience(Experience experience) {
        experiences.remove(experience);
        experience.setResume(null); 
    }

	
}
