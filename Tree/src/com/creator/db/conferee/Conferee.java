package com.creator.db.conferee;

public class Conferee {
	private Integer con_id;
	private Integer con_meid;
	private Integer con_userid;
	
	public Conferee() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Conferee(Integer con_id, Integer con_meid, Integer con_userid) {
		super();
		this.con_id = con_id;
		this.con_meid = con_meid;
		this.con_userid = con_userid;
	}

	public Integer getCon_id() {
		return con_id;
	}

	public void setCon_id(Integer con_id) {
		this.con_id = con_id;
	}

	public Integer getCon_meid() {
		return con_meid;
	}

	public void setCon_meid(Integer con_meid) {
		this.con_meid = con_meid;
	}

	public Integer getCon_userid() {
		return con_userid;
	}

	public void setCon_userid(Integer con_userid) {
		this.con_userid = con_userid;
	}
	
	@Override
	public String toString() {
		return "Conferee [con_id=" + con_id + ", con_meid=" + con_meid
				+ ", con_userid=" + con_userid + "]";
	}
}
