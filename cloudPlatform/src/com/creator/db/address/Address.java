package com.creator.db.address;

public class Address {

	private Integer add_id; 
	private String add_name;
	private String add_camera;
	private Integer add_type;
	private Integer add_pid;
	
	public Integer getAdd_id() {
		return add_id;
	}
	public void setAdd_id(Integer add_id) {
		this.add_id = add_id;
	}
	public String getAdd_name() {
		return add_name;
	}
	public void setAdd_name(String add_name) {
		this.add_name = add_name;
	}
	public String getAdd_camera() {
		return add_camera;
	}
	public void setAdd_camera(String add_camera) {
		this.add_camera = add_camera;
	}
	public Integer getAdd_type() {
		return add_type;
	}
	public void setAdd_type(Integer add_type) {
		this.add_type = add_type;
	}
	public Integer getAdd_pid() {
		return add_pid;
	}
	public void setAdd_pid(Integer add_pid) {
		this.add_pid = add_pid;
	}
	@Override
	public String toString() {
		return "Address [add_id=" + add_id + ", add_name=" + add_name + ", add_camera="
				+ add_camera + ", add_type=" + add_type + ", add_pid=" + add_pid + "]";
	}
	
	
}
