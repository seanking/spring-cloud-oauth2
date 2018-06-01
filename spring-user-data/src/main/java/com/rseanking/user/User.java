package com.rseanking.user;

import org.springframework.data.annotation.Id;

public class User {
	@Id
	private String id;
	private String username;
	private String pasword;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasword() {
		return pasword;
	}

	public void setPasword(String pasword) {
		this.pasword = pasword;
	}
}
