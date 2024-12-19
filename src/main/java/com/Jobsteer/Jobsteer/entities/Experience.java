package com.Jobsteer.Jobsteer.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "experience")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ExperienceType type;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @Lob
    private String description;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    public Experience() {}

    public Experience(ExperienceType type, String description, Resume resume) {
        this.type = type;
        this.description = description;
        this.resume = resume;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExperienceType getType() {
        return type;
    }

    public void setType(ExperienceType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    @Override
    public String toString() {
        return "Experience [id=" + id + ", type=" + type + ", description=" + description + " resumeId=" + (resume != null ? resume.getId() : "null") + "]";
    }
}
