package com.creator.db.template;

public class Template {

	private Integer tp_id;
	private String tp_name;
	private String tp_data;
	private Integer tp_treeid;
	
	public Integer getTp_id() {
		return tp_id;
	}
	public void setTp_id(Integer tp_id) {
		this.tp_id = tp_id;
	}
	public String getTp_data() {
		return tp_data;
	}
	public void setTp_data(String tp_data) {
		this.tp_data = tp_data;
	}
	public String getTp_name() {
		return tp_name;
	}
	public void setTp_name(String tp_name) {
		this.tp_name = tp_name;
	}	
	public Integer getTp_treeid() {
		return tp_treeid;
	}
	public void setTp_treeid(Integer tp_treeid) {
		this.tp_treeid = tp_treeid;
	}
	@Override
	public String toString() {
		return "Template [tp_id=" + tp_id + ", tp_name=" + tp_name
				+ ", tp_data=" + tp_data + ", tp_treeid=" + tp_treeid + "]";
	}
	
	
}
