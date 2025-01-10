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
	
	public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setCompany(String company) {
        this.company = company;
    }
	
}
