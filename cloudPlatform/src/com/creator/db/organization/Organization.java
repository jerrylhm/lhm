package com.creator.db.organization;

public class Organization {
	private Integer org_id;
	private String org_name;
	private Integer org_pid;
	private Integer org_type;
	private Integer org_flag;
	
	public Integer getOrg_id() {
		return org_id;
	}
	public void setOrg_id(Integer org_id) {
		this.org_id = org_id;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public Integer getOrg_pid() {
		return org_pid;
	}
	public void setOrg_pid(Integer org_pid) {
		this.org_pid = org_pid;
	}
	public Integer getOrg_type() {
		return org_type;
	}
	public void setOrg_type(Integer org_type) {
		this.org_type = org_type;
	}
	public Integer getOrg_flag() {
		return org_flag;
	}
	public void setOrg_flag(Integer org_flag) {
		this.org_flag = org_flag;
	}
	
	@Override
	public String toString() {
		return "Organization [org_id=" + org_id + ", org_name=" + org_name + ", org_pid=" + org_pid + ", org_type="
				+ org_type + ", org_flag=" + org_flag + "]";
	}
	
}
