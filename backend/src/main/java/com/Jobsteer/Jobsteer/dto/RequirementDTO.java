package com.Jobsteer.Jobsteer.dto;

public class RequirementDTO {
    private String type; // We can map this to RequirementType enum
    private String description;

    // Constructors
    public RequirementDTO() {}

    public RequirementDTO(String type, String description) {
        this.type = type;
        this.description = description;
    }

    // Getters and Setters
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
