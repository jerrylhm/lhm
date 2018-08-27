package com.creator.db.timetable;

import java.util.Date;

public class TimeTable {
	private Integer tt_id;
	private String tt_name;
	private Integer tt_num;
	private Integer tt_termid;
	private Date tt_updatedate;
	
	public Integer getTt_id() {
		return tt_id;
	}
	public void setTt_id(Integer tt_id) {
		this.tt_id = tt_id;
	}
	public String getTt_name() {
		return tt_name;
	}
	public void setTt_name(String tt_name) {
		this.tt_name = tt_name;
	}
	public Integer getTt_num() {
		return tt_num;
	}
	public void setTt_num(Integer tt_num) {
		this.tt_num = tt_num;
	}
	public Integer getTt_termid() {
		return tt_termid;
	}
	public void setTt_termid(Integer tt_termid) {
		this.tt_termid = tt_termid;
	}
	public Date getTt_updatedate() {
		return tt_updatedate;
	}
	public void setTt_updatedate(Date tt_updatedate) {
		this.tt_updatedate = tt_updatedate;
	}
	@Override
	public String toString() {
		return "TimeTable [tt_id=" + tt_id + ", tt_name=" + tt_name + ", tt_num=" + tt_num + ", tt_termid=" + tt_termid
				+ ", tt_updatedate=" + tt_updatedate + "]";
	}
	
}
