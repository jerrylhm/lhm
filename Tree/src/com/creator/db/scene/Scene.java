package com.creator.db.scene;

public class Scene {

	private Integer sc_id;
	private String sc_name;
	private Integer sc_treeid;
	private String sc_action;
	private String sc_nodeid;
	
	
	public Integer getSc_id() {
		return sc_id;
	}
	public void setSc_id(Integer sc_id) {
		this.sc_id = sc_id;
	}
	public String getSc_name() {
		return sc_name;
	}
	public void setSc_name(String sc_name) {
		this.sc_name = sc_name;
	}
	public Integer getSc_treeid() {
		return sc_treeid;
	}
	public void setSc_treeid(Integer sc_treeid) {
		this.sc_treeid = sc_treeid;
	}
	public String getSc_action() {
		return sc_action;
	}
	public void setSc_action(String sc_action) {
		this.sc_action = sc_action;
	}
	public String getSc_nodeid() {
		return sc_nodeid;
	}
	public void setSc_nodeid(String sc_nodeid) {
		this.sc_nodeid = sc_nodeid;
	}
	@Override
	public String toString() {
		return "SceneAction [sc_id=" + sc_id + ", sc_name=" + sc_name
				+ ", sc_treeid=" + sc_treeid + ", sc_action=" + sc_action
				+ ", sc_nodeid=" + sc_nodeid + "]";
	}
}
