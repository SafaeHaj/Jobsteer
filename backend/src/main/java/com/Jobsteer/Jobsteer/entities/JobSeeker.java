package com.Jobsteer.Jobsteer.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class JobSeeker extends User {
    @OneToOne(cascade = CascadeType.ALL)
    private Resume resume;
    
    @Column
    private String location;

    public Resume getResume() {
		return resume;
	}

	public void setResume(Resume newResume) {
        this.resume = newResume;
    }

    public void deleteResume() {
        this.resume = null;
    }

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
