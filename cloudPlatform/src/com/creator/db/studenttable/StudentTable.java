package com.creator.db.studenttable;

public class StudentTable {
	private Integer st_id;
	private String st_weeks;
	private String st_name;
	private String st_orgid;
	private Integer st_teacherid;
	private Integer st_num;
	private Integer st_addid;
	private Integer st_ttid;
	
	public Integer getSt_id() {
		return st_id;
	}
	public void setSt_id(Integer st_id) {
		this.st_id = st_id;
	}
	public String getSt_weeks() {
		return st_weeks;
	}
	public void setSt_weeks(String st_weeks) {
		this.st_weeks = st_weeks;
	}
	public String getSt_name() {
		return st_name;
	}
	public void setSt_name(String st_name) {
		this.st_name = st_name;
	}
	public String getSt_orgid() {
		return st_orgid;
	}
	public void setSt_orgid(String st_orgid) {
		this.st_orgid = st_orgid;
	}
	public Integer getSt_teacherid() {
		return st_teacherid;
	}
	public void setSt_teacherid(Integer st_teacherid) {
		this.st_teacherid = st_teacherid;
	}
	public Integer getSt_num() {
		return st_num;
	}
	public void setSt_num(Integer st_num) {
		this.st_num = st_num;
	}	
	public Integer getSt_addid() {
		return st_addid;
	}
	public void setSt_addid(Integer st_addid) {
		this.st_addid = st_addid;
	}
	public Integer getSt_ttid() {
		return st_ttid;
	}
	public void setSt_ttid(Integer st_ttid) {
		this.st_ttid = st_ttid;
	}
	
	@Override
	public String toString() {
		return "StudentTable [st_id=" + st_id + ", st_weeks=" + st_weeks + ", st_name=" + st_name + ", st_orgid="
				+ st_orgid + ", st_teacherid=" + st_teacherid + ", st_num=" + st_num + ", st_addid=" + st_addid
				+ ", st_ttid=" + st_ttid + "]";
	}
	
}
