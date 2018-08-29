package com.creator.db.application;

public class Application {
	private Integer app_id;
	private String app_name;
	private String app_detail;
	private String app_link;
	public Integer getApp_id() {
		return app_id;
	}
	public void setApp_id(Integer app_id) {
		this.app_id = app_id;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApp_detail() {
		return app_detail;
	}
	public void setApp_detail(String app_detail) {
		this.app_detail = app_detail;
	}
	public String getApp_link() {
		return app_link;
	}
	public void setApp_link(String app_link) {
		this.app_link = app_link;
	}
	@Override
	public String toString() {
		return "Application [app_id=" + app_id + ", app_name=" + app_name
				+ ", app_detail=" + app_detail + ", app_link=" + app_link + "]";
	}
	
	
}
