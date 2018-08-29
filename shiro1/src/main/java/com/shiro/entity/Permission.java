package com.shiro.entity;

public class Permission {

	private String role_name;
	private String permission;
	
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	@Override
	public String toString() {
		return "Permission [role_name=" + role_name + ", permission=" + permission + "]";
	}	
	
}
