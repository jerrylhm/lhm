package com.creator.db.ttnode;

public class TTNode {
	private Integer node_id;
	private Integer node_index;
	private String node_start;
	private String node_end;
	private Integer node_ttid;
	
	public Integer getNode_id() {
		return node_id;
	}
	public void setNode_id(Integer node_id) {
		this.node_id = node_id;
	}
	public Integer getNode_index() {
		return node_index;
	}
	public void setNode_index(Integer node_index) {
		this.node_index = node_index;
	}
	public String getNode_start() {
		return node_start;
	}
	public void setNode_start(String node_start) {
		this.node_start = node_start;
	}
	public String getNode_end() {
		return node_end;
	}
	public void setNode_end(String node_end) {
		this.node_end = node_end;
	}

	
	public Integer getNode_ttid() {
		return node_ttid;
	}
	public void setNode_ttid(Integer node_ttid) {
		this.node_ttid = node_ttid;
	}
	@Override
	public String toString() {
		return "TTNode [node_id=" + node_id + ", node_index=" + node_index + ", node_start=" + node_start
				+ ", node_end=" + node_end + ", ttid=" + node_ttid + "]";
	}	
	
}
