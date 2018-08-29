package com.creator.db.group;

public class UserGroup {
	private Integer urg_id;
	private String urg_name;
	private Integer urg_userid;
	private Integer urg_treeid;
	
	public UserGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserGroup(Integer urg_id, String urg_name, Integer urg_userid,
			Integer urg_treeid) {
		super();
		this.urg_id = urg_id;
		this.urg_name = urg_name;
		this.urg_userid = urg_userid;
		this.urg_treeid = urg_treeid;
	}

	public Integer getUrg_id() {
		return urg_id;
	}

	public void setUrg_id(Integer urg_id) {
		this.urg_id = urg_id;
	}

	public String getUrg_name() {
		return urg_name;
	}

	public void setUrg_name(String urg_name) {
		this.urg_name = urg_name;
	}

	public Integer getUrg_userid() {
		return urg_userid;
	}

	public void setUrg_userid(Integer urg_userid) {
		this.urg_userid = urg_userid;
	}

	public Integer getUrg_treeid() {
		return urg_treeid;
	}

	public void setUrg_treeid(Integer urg_treeid) {
		this.urg_treeid = urg_treeid;
	}

	@Override
	public String toString() {
		return "UserGroup [urg_id=" + urg_id + ", urg_name=" + urg_name
				+ ", urg_userid=" + urg_userid + ", urg_treeid=" + urg_treeid
				+ "]";
	}
	
	
}
