package com.creator.util;

public class Protocol {

	//显示名称
	private String name;
	//标识名
	private String identification;
	//备注
	private String remark;
	//节点id
	private Integer nodeId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	@Override
	public String toString() {
		return "Protocol [name=" + name + ", identification=" + identification
				+ ", remark=" + remark + ", nodeId=" + nodeId + "]";
	}
	
	
	
}
