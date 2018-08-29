package com.creator.db.permission;

public class Permission {
	private Integer ps_id;
	private String ps_name;
	private Integer ps_appid;
	
	public Integer getPs_id() {
		return ps_id;
	}
	public void setPs_id(Integer ps_id) {
		this.ps_id = ps_id;
	}
	public String getPs_name() {
		return ps_name;
	}
	public void setPs_name(String ps_name) {
		this.ps_name = ps_name;
	}
	public Integer getPs_appid() {
		return ps_appid;
	}
	public void setPs_appid(Integer ps_appid) {
		this.ps_appid = ps_appid;
	}
	@Override
	public String toString() {
		return "Permission [ps_id=" + ps_id + ", ps_name=" + ps_name + ", ps_appid=" + ps_appid + "]";
	}

	
}
