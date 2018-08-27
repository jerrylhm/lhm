package com.creator.db.equipment;

public class Equipment {

	private Integer eqm_id;
	private String eqm_name;
	private Integer eqm_type;
	private String eqm_sn;
	private String eqm_class;
	
	public Integer getEqm_id() {
		return eqm_id;
	}
	public void setEqm_id(Integer eqm_id) {
		this.eqm_id = eqm_id;
	}
	public String getEqm_name() {
		return eqm_name;
	}
	public void setEqm_name(String eqm_name) {
		this.eqm_name = eqm_name;
	}
	public Integer getEqm_type() {
		return eqm_type;
	}
	public void setEqm_type(Integer eqm_type) {
		this.eqm_type = eqm_type;
	}
	public String getEqm_sn() {
		return eqm_sn;
	}
	public void setEqm_sn(String eqm_sn) {
		this.eqm_sn = eqm_sn;
	}
	public String getEqm_class() {
		return eqm_class;
	}
	public void setEqm_class(String eqm_class) {
		this.eqm_class = eqm_class;
	}
	@Override
	public String toString() {
		return "Equipment [eqm_id=" + eqm_id + ", eqm_name=" + eqm_name + ", eqm_type=" + eqm_type + ", eqm_sn="
				+ eqm_sn + ", eqm_class=" + eqm_class + "]";
	}
	
	
}
