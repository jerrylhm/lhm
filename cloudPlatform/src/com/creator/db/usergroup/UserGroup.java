package com.creator.db.usergroup;

public class UserGroup {
	private Integer ug_id;
	private String ug_name;
	private String ug_permissionid;
	
	public Integer getUg_id() {
		return ug_id;
	}
	public void setUg_id(Integer ug_id) {
		this.ug_id = ug_id;
	}
	public String getUg_name() {
		return ug_name;
	}
	public void setUg_name(String ug_name) {
		this.ug_name = ug_name;
	}
	public String getUg_permissionid() {
		return ug_permissionid;
	}
	public void setUg_permissionid(String ug_permissionid) {
		this.ug_permissionid = ug_permissionid;
	}
	
	@Override
	public String toString() {
		return "UserGroup [ug_id=" + ug_id + ", ug_name=" + ug_name + ", ug_permissionid=" + ug_permissionid + "]";
	}
	
}
