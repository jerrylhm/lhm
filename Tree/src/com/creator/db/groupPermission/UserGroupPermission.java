package com.creator.db.groupPermission;

public class UserGroupPermission {
	private Integer gp_id;
	private Integer gp_usergroupid;
	private Integer gp_treeid;
	private Integer gp_nodeid;
	private String gp_perid;
	
	public UserGroupPermission() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserGroupPermission(Integer gp_id, Integer gp_usergroupid,
			Integer gp_treeid, Integer gp_nodeid, String gp_perid) {
		super();
		this.gp_id = gp_id;
		this.gp_usergroupid = gp_usergroupid;
		this.gp_treeid = gp_treeid;
		this.gp_nodeid = gp_nodeid;
		this.gp_perid = gp_perid;
	}

	public Integer getGp_id() {
		return gp_id;
	}

	public void setGp_id(Integer gp_id) {
		this.gp_id = gp_id;
	}

	public Integer getGp_usergroupid() {
		return gp_usergroupid;
	}

	public void setGp_usergroupid(Integer gp_usergroupid) {
		this.gp_usergroupid = gp_usergroupid;
	}

	public Integer getGp_treeid() {
		return gp_treeid;
	}

	public void setGp_treeid(Integer gp_treeid) {
		this.gp_treeid = gp_treeid;
	}

	public Integer getGp_nodeid() {
		return gp_nodeid;
	}

	public void setGp_nodeid(Integer gp_nodeid) {
		this.gp_nodeid = gp_nodeid;
	}

	public String getGp_perid() {
		return gp_perid;
	}

	public void setGp_perid(String gp_perid) {
		this.gp_perid = gp_perid;
	}
	
}
