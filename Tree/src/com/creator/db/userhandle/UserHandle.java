package com.creator.db.userhandle;

public class UserHandle {

	private Integer uh_id;
	private String uh_content;
	private Integer uh_nodeid;
	private String uh_update;
	
	public UserHandle() {
		this.uh_update = "false";
	}
	
	public UserHandle(Integer uh_id, String uh_content, Integer uh_nodeid, String uh_update) {
		this.uh_id = uh_id;
		this.uh_content = uh_content;
		this.uh_nodeid = uh_nodeid;
		if(uh_update != null) {
			this.uh_update = uh_update;
		}else {
			this.uh_update = "false";
		}
		
	}
	
	public Integer getUh_id() {
		return uh_id;
	}

	public void setUh_id(Integer uh_id) {
		this.uh_id = uh_id;
	}

	public String getUh_content() {
		return uh_content;
	}

	public void setUh_content(String uh_content) {
		this.uh_content = uh_content;
	}

	public Integer getUh_nodeid() {
		return uh_nodeid;
	}

	public void setUh_nodeid(Integer uh_nodeid) {
		this.uh_nodeid = uh_nodeid;
	}

	public String getUh_update() {
		return uh_update;
	}

	public void setUh_update(String uh_update) {
		this.uh_update = uh_update;
	}

	@Override
	public String toString() {
		return "UserHandle [uh_id=" + uh_id + ", uh_content=" + uh_content
				+ ", uh_nodeid=" + uh_nodeid + ", uh_update=" + uh_update + "]";
	}
	
	
}
