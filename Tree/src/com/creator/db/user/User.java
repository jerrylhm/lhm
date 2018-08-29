package com.creator.db.user;

public class User {
	private Integer ur_id;
	private String ur_username;
	private String ur_password;
	private Integer ur_type;
	private String ur_datetime;
	private Integer ur_group;
	private Integer ur_treeid;
	public User() {
		super();
	}
	
	
	
	public User(Integer ur_id, String ur_username, String ur_password,
			Integer ur_type, String ur_datetime, Integer ur_group,
			Integer ur_treeid) {
		super();
		this.ur_id = ur_id;
		this.ur_username = ur_username;
		this.ur_password = ur_password;
		this.ur_type = ur_type;
		this.ur_datetime = ur_datetime;
		this.ur_group = ur_group;
		this.ur_treeid = ur_treeid;
	}



	public Integer getUr_id() {
		return ur_id;
	}

	public void setUr_id(Integer ur_id) {
		this.ur_id = ur_id;
	}
	public String getUr_username() {
		return ur_username;
	}
	public void setUr_username(String ur_username) {
		this.ur_username = ur_username;
	}
	public String getUr_password() {
		return ur_password;
	}
	public void setUr_password(String ur_password) {
		this.ur_password = ur_password;
	}
	public Integer getUr_type() {
		return ur_type;
	}
	public void setUr_type(Integer ur_type) {
		this.ur_type = ur_type;
	}

	public String getUr_datetime() {
		return ur_datetime;
	}

	public void setUr_datetime(String ur_datetime) {
		this.ur_datetime = ur_datetime;
	}

	public Integer getUr_group() {
		return ur_group;
	}

	public void setUr_group(Integer ur_group) {
		this.ur_group = ur_group;
	}



	public Integer getUr_treeid() {
		return ur_treeid;
	}



	public void setUr_treeid(Integer ur_treeid) {
		this.ur_treeid = ur_treeid;
	}



	@Override
	public String toString() {
		return "User [ur_id=" + ur_id + ", ur_username=" + ur_username
				+ ", ur_password=" + ur_password + ", ur_type=" + ur_type
				+ ", ur_datetime=" + ur_datetime + ", ur_group=" + ur_group
				+ ", ur_treeid=" + ur_treeid + "]";
	}
	
	
	
}
