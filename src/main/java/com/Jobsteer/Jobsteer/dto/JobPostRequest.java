package com.Jobsteer.Jobsteer.dto;

import java.util.List;

public class JobPostRequest {
    private String title;
    private String location;
    private String description;
    private String toApply;
    private List<RequirementRequest> requirements;

    // Inner class for Requirement DTO
    public static class RequirementRequest {
        private String type; // EDUCATION, LANGUAGE, SKILL, EXPERIENCE
        private String description;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}

        
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public String getToApply() {
		return toApply;
	}

	public void setToApply(String toApply) {
		this.toApply = toApply;
	}

	public List<RequirementRequest> getRequirements() {
		return requirements;
	}

	public void setRequirements(List<RequirementRequest> requirements) {
		this.requirements = requirements;
	}

    
}

