package com.couponProject.entities;

import org.springframework.stereotype.Component;

@Component
public class Administrator {
	
	private String name;
	private final String email = "admin@admin.com";
	private final String password = "admin";

	public Administrator() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
