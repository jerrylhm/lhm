package com.creator.db.type;

public class Type {

	private Integer type_id;
	private String type_name;
	private String type_name_cn;
	private Integer type_pid;
	
	public Type() {
		super();
	}
	
	public Type(Integer type_id, String type_name, String type_name_cn, Integer type_pid) {
		super();
		this.type_id = type_id;
		this.type_name = type_name;
		this.type_name_cn = type_name_cn;
		this.type_pid = type_pid;
	}
	
	public Integer getType_id() {
		return type_id;
	}
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getType_name_cn() {
		return type_name_cn;
	}
	public void setType_name_cn(String type_name_cn) {
		this.type_name_cn = type_name_cn;
	}
	public Integer getType_pid() {
		return type_pid;
	}
	public void setType_pid(Integer type_pid) {
		this.type_pid = type_pid;
	}
	
	
}
