package com.creator.db.backgroundmenumanamgment;

public class BackgroundMenuManagment {

	private int bck_mm_id;
	private String bck_mm_name;
	private short bck_mm_treeid;
	private short bck_mm_del_flag;
	private short bck_mm_state;
	
	public BackgroundMenuManagment(int bck_mm_id, String bck_mm_name,
			short bck_mm_treeid, short bck_mm_del_flag, short bck_mm_state) {
		super();
		this.bck_mm_id = bck_mm_id;
		this.bck_mm_name = bck_mm_name;
		this.bck_mm_treeid = bck_mm_treeid;
		this.bck_mm_del_flag = bck_mm_del_flag;
		this.bck_mm_state = bck_mm_state;
	}

	public BackgroundMenuManagment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getBck_mm_id() {
		return bck_mm_id;
	}

	public void setBck_mm_id(int bck_mm_id) {
		this.bck_mm_id = bck_mm_id;
	}

	public String getBck_mm_name() {
		return bck_mm_name;
	}

	public void setBck_mm_name(String bck_mm_name) {
		this.bck_mm_name = bck_mm_name;
	}

	public short getBck_mm_treeid() {
		return bck_mm_treeid;
	}

	public void setBck_mm_treeid(short bck_mm_treeid) {
		this.bck_mm_treeid = bck_mm_treeid;
	}

	public short getBck_mm_del_flag() {
		return bck_mm_del_flag;
	}

	public void setBck_mm_del_flag(short bck_mm_del_flag) {
		this.bck_mm_del_flag = bck_mm_del_flag;
	}

	public short getBck_mm_state() {
		return bck_mm_state;
	}

	public void setBck_mm_state(short bck_mm_state) {
		this.bck_mm_state = bck_mm_state;
	}
	
	
	
}
