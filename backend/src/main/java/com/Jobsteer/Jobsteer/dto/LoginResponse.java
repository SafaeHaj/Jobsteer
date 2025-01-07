package com.Jobsteer.Jobsteer.dto;

public class LoginResponse {
    private String token;
    private String email;
	private String role;

	
    public LoginResponse(String email, String role, String token) {
		this.email = email;
		this.role = role;
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}
	
    public String getToken() {
        return token;
    }
}
