package com.creator.db.nodeattr;

public class NodeAttr {
	private Integer attr_id;
	private Integer attr_nodeid;
	private Integer attr_type;
	private String attr_value;
	
	
	
	public NodeAttr() {
		super();
		// TODO Auto-generated constructor stub
	}



	public NodeAttr(Integer attr_id, Integer attr_nodeid, Integer attr_type,
			String attr_value) {
		super();
		this.attr_id = attr_id;
		this.attr_nodeid = attr_nodeid;
		this.attr_type = attr_type;
		this.attr_value = attr_value;
	}



	public Integer getAttr_id() {
		return attr_id;
	}



	public void setAttr_id(Integer attr_id) {
		this.attr_id = attr_id;
	}



	public Integer getAttr_nodeid() {
		return attr_nodeid;
	}



	public void setAttr_nodeid(Integer attr_nodeid) {
		this.attr_nodeid = attr_nodeid;
	}



	public Integer getAttr_type() {
		return attr_type;
	}



	public void setAttr_type(Integer attr_type) {
		this.attr_type = attr_type;
	}



	public String getAttr_value() {
		return attr_value;
	}



	public void setAttr_value(String attr_value) {
		this.attr_value = attr_value;
	}



	@Override
	public String toString() {
		return "NodeAttr [attr_id=" + attr_id + ", attr_nodeid=" + attr_nodeid
				+ ", attr_type=" + attr_type + ", attr_value=" + attr_value
				+ "]";
	}

	
	
}
