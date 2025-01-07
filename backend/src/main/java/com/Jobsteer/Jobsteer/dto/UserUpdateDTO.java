package com.Jobsteer.Jobsteer.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String location;    // for JobSeeker
    private String companyName; // for Recruiter
}