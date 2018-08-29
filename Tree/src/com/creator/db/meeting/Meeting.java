package com.creator.db.meeting;

public class Meeting {
	private Integer me_id;
	private Integer me_userid;
	private Integer me_nodeid;
	private String me_title;
	private Integer me_number;
	private String me_starttime;
	private String me_endtime;
	private String me_description;
	private Integer me_status;
	
	public Meeting() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Meeting(Integer me_id, Integer me_userid, Integer me_nodeid,
			String me_title, Integer me_number, String me_starttime,
			String me_endtime, String me_description, Integer me_status) {
		super();
		this.me_id = me_id;
		this.me_userid = me_userid;
		this.me_nodeid = me_nodeid;
		this.me_title = me_title;
		this.me_number = me_number;
		this.me_starttime = me_starttime;
		this.me_endtime = me_endtime;
		this.me_description = me_description;
		this.me_status = me_status;
	}

	public Integer getMe_id() {
		return me_id;
	}

	public void setMe_id(Integer me_id) {
		this.me_id = me_id;
	}

	public Integer getMe_userid() {
		return me_userid;
	}

	public void setMe_userid(Integer me_userid) {
		this.me_userid = me_userid;
	}

	public Integer getMe_nodeid() {
		return me_nodeid;
	}

	public void setMe_nodeid(Integer me_nodeid) {
		this.me_nodeid = me_nodeid;
	}

	public String getMe_title() {
		return me_title;
	}

	public void setMe_title(String me_title) {
		this.me_title = me_title;
	}

	public Integer getMe_number() {
		return me_number;
	}

	public void setMe_number(Integer me_number) {
		this.me_number = me_number;
	}

	public String getMe_starttime() {
		return me_starttime;
	}

	public void setMe_starttime(String me_starttime) {
		this.me_starttime = me_starttime;
	}

	public String getMe_endtime() {
		return me_endtime;
	}

	public void setMe_endtime(String me_endtime) {
		this.me_endtime = me_endtime;
	}

	public String getMe_description() {
		return me_description;
	}

	public void setMe_description(String me_description) {
		this.me_description = me_description;
	}

	public Integer getMe_status() {
		return me_status;
	}

	public void setMe_status(Integer me_status) {
		this.me_status = me_status;
	}

	@Override
	public String toString() {
		return "Meeting [me_id=" + me_id + ", me_userid=" + me_userid
				+ ", me_nodeid=" + me_nodeid + ", me_title=" + me_title
				+ ", me_number=" + me_number + ", me_starttime=" + me_starttime
				+ ", me_endtime=" + me_endtime + ", me_description="
				+ me_description + ", me_status=" + me_status + "]";
	}
}
