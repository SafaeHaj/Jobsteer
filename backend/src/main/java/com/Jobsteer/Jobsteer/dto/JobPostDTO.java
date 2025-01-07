package com.Jobsteer.Jobsteer.dto;

import java.util.List;

public class JobPostDTO {
    private String title;
    private String location;
    private String description;
    private String toApply;
    private List<RequirementDTO> requirements; 

    public JobPostDTO() {}

    public JobPostDTO(String title, String location, String description, String toApply, List<RequirementDTO> requirements) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.toApply = toApply;
        this.requirements = requirements;
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

    public List<RequirementDTO> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<RequirementDTO> requirements) {
        this.requirements = requirements;
    }
}
