package com.Jobsteer.Jobsteer.dto;

public class RegistrationRequest {
	private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String userType;  
    private String location;  
    private String company;  
    
    public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getUserType() {
		return userType;
	}
	public String getLocation() {
		return location;
	}
	public String getCompany() {
		return company;
	}
}
