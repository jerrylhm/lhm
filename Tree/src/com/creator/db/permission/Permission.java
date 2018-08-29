package com.creator.db.permission;

public class Permission {
	private Integer urnode_id;
	private Integer urnode_userid;
	private String urnode_nodeid;
	private Integer urnode_iscreator;
	private Integer urnode_treeid;
	public Permission() {
		super();
	}
	public Permission(Integer id, Integer userid,
			String nodeid, Integer iscreator, Integer treeid) {
		super();
		this.urnode_id = id;
		this.urnode_userid = userid;
		this.urnode_nodeid = nodeid;
		this.urnode_iscreator = iscreator;
		this.urnode_treeid = treeid;
	}
	
	public Integer getUrnode_treeid() {
		return urnode_treeid;
	}
	public void setUrnode_treeid(Integer urnode_treeid) {
		this.urnode_treeid = urnode_treeid;
	}
	public Integer getUrnode_id() {
		return urnode_id;
	}
	public void setUrnode_id(Integer urnode_id) {
		this.urnode_id = urnode_id;
	}
	public Integer getUrnode_userid() {
		return urnode_userid;
	}
	public void setUrnode_userid(Integer urnode_userid) {
		this.urnode_userid = urnode_userid;
	}
	public String getUrnode_nodeid() {
		return urnode_nodeid;
	}
	public void setUrnode_nodeid(String urnode_nodeid) {
		this.urnode_nodeid = urnode_nodeid;
	}
	public Integer getUrnode_iscreator() {
		return urnode_iscreator;
	}
	public void setUrnode_iscreator(Integer urnode_iscreator) {
		this.urnode_iscreator = urnode_iscreator;
	}
	
	
	

}
