package com.creator.db.clientpermission;

public class ClientPermission {
	private Integer per_id;
	private String per_name;
	private Integer per_type;
	
	public ClientPermission() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ClientPermission(Integer per_id, String per_name, Integer per_type) {
		super();
		this.per_id = per_id;
		this.per_name = per_name;
		this.per_type = per_type;
	}
	public Integer getPer_id() {
		return per_id;
	}
	public void setPer_id(Integer per_id) {
		this.per_id = per_id;
	}
	public String getPer_name() {
		return per_name;
	}
	public void setPer_name(String per_name) {
		this.per_name = per_name;
	}
	public Integer getPer_type() {
		return per_type;
	}
	public void setPer_type(Integer per_type) {
		this.per_type = per_type;
	}
	
}
