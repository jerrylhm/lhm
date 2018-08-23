package com.shiro.entity;

public class Role {
	
	private String username;
	private String role_name;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	
	@Override
	public String toString() {
		return "Role [username=" + username + ", role_name=" + role_name + "]";
	}
	
}
