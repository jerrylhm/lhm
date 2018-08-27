package com.creator.db.subject;

public class Subject {
	private Integer sub_id;
	private String sub_name;
	private String sub_orgid;
	private Integer sub_type;
	
	public Integer getSub_id() {
		return sub_id;
	}
	public void setSub_id(Integer sub_id) {
		this.sub_id = sub_id;
	}
	public String getSub_name() {
		return sub_name;
	}
	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
	}
	public String getSub_orgid() {
		return sub_orgid;
	}
	public void setSub_orgid(String sub_orgid) {
		this.sub_orgid = sub_orgid;
	}
	public Integer getSub_type() {
		return sub_type;
	}
	public void setSub_type(Integer sub_type) {
		this.sub_type = sub_type;
	}
	@Override
	public String toString() {
		return "Subject [sub_id=" + sub_id + ", sub_name=" + sub_name + ", sub_orgid=" + sub_orgid + ", sub_type="
				+ sub_type + "]";
	}
	
	
}
