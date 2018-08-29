package com.creator.db.tpcontent;

public class TPContent {

	private Integer tpc_id;
	private Integer tpc_tpid;
	private Integer tpc_nodeid;
	private Integer tpc_userid;
	private String tpc_content;
	private String tpc_date;
	
	
	public Integer getTpc_id() {
		return tpc_id;
	}
	public void setTpc_id(Integer tpc_id) {
		this.tpc_id = tpc_id;
	}
	public Integer getTpc_tpid() {
		return tpc_tpid;
	}
	public void setTpc_tpid(Integer tpc_tpid) {
		this.tpc_tpid = tpc_tpid;
	}
	public Integer getTpc_nodeid() {
		return tpc_nodeid;
	}
	public void setTpc_nodeid(Integer tpc_nodeid) {
		this.tpc_nodeid = tpc_nodeid;
	}
	public Integer getTpc_userid() {
		return tpc_userid;
	}
	public void setTpc_userid(Integer tpc_userid) {
		this.tpc_userid = tpc_userid;
	}
	public String getTpc_content() {
		return tpc_content;
	}
	public void setTpc_content(String tpc_content) {
		this.tpc_content = tpc_content;
	}
	public String getTpc_date() {
		return tpc_date;
	}
	public void setTpc_date(String tpc_date) {
		this.tpc_date = tpc_date;
	}
	@Override
	public String toString() {
		return "TPContent [tpc_id=" + tpc_id + ", tpc_tpid=" + tpc_tpid
				+ ", tpc_nodeid=" + tpc_nodeid + ", tpc_userid=" + tpc_userid
				+ ", tpc_content=" + tpc_content + ", tpc_date=" + tpc_date
				+ "]";
	}
	
	
}
