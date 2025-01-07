package com.Jobsteer.Jobsteer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.Jobsteer.Jobsteer.dto.JobPostDTO;
import com.Jobsteer.Jobsteer.dto.RequirementDTO;
import com.Jobsteer.Jobsteer.entities.JobPost;
import com.Jobsteer.Jobsteer.entities.Requirement;
import com.Jobsteer.Jobsteer.entities.RequirementType;

public class JobPostMapper {

    public static JobPostDTO toDTO(JobPost jobPost) {
        // Map JobPost to JobPostDTO
        List<RequirementDTO> requirementDTOs = jobPost.getRequirement().stream()
                .map(req -> new RequirementDTO(req.getType().name(), req.getDescription()))
                .collect(Collectors.toList());

        return new JobPostDTO(
            jobPost.getTitle(),
            jobPost.getLocation(),
            jobPost.getDescription(),
            jobPost.getToApply(),
            requirementDTOs
        );
    }

    public static JobPost toEntity(JobPostDTO jobPostDTO) {
        // Map JobPostDTO to JobPost entity
        JobPost jobPost = new JobPost(
            jobPostDTO.getTitle(),
            jobPostDTO.getDescription(),
            jobPostDTO.getLocation(),
            jobPostDTO.getToApply(),
            "source"  // Handle the source field as needed
        );

        // Map RequirementDTO list to Requirement entities
        ArrayList<Requirement> requirements = jobPostDTO.getRequirements().stream()
                .map(reqDTO -> new Requirement(
                    RequirementType.valueOf(reqDTO.getType()), // Convert string to enum
                    reqDTO.getDescription(),
                    jobPost // Set the jobPost reference
                ))
                .collect(Collectors.toCollection(ArrayList::new)); // Collect as ArrayList

        jobPost.setRequirements(requirements);

        return jobPost;
    }
}
