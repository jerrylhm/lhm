package com.creator.db.tree;

public class Tree {
	private Integer node_id;
	private String node_name;
	private Integer node_pid;
	private Integer node_userid;
	private Integer node_treeid;
	private String node_url;
	private Integer node_type;
	private String 	node_protocol;
	private String node_class;
	private String node_sn;
	private Integer node_tstype;
	private String node_title;
	public Tree() {
		super();
	}
	
	
	
	public Tree(Integer node_id, String node_name, Integer node_pid,
			Integer node_userid, Integer node_treeid, String node_url,
			Integer node_type, String node_protocol, String node_class,
			String node_sn, Integer node_tstype, String node_title) {
		super();
		this.node_id = node_id;
		this.node_name = node_name;
		this.node_pid = node_pid;
		this.node_userid = node_userid;
		this.node_treeid = node_treeid;
		this.node_url = node_url;
		this.node_type = node_type;
		this.node_protocol = node_protocol;
		this.node_class = node_class;
		this.node_sn = node_sn;
		this.node_tstype = node_tstype;
		this.node_title = node_title;
	}



	public Integer getNode_id() {
		return node_id;
	}

	public void setNode_id(Integer node_id) {
		this.node_id = node_id;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public Integer getNode_pid() {
		return node_pid;
	}

	public void setNode_pid(Integer node_pid) {
		this.node_pid = node_pid;
	}

	public Integer getNode_userid() {
		return node_userid;
	}

	public void setNode_userid(Integer node_userid) {
		this.node_userid = node_userid;
	}

	public Integer getNode_treeid() {
		return node_treeid;
	}

	public void setNode_treeid(Integer node_treeid) {
		this.node_treeid = node_treeid;
	}

	public String getNode_url() {
		return node_url;
	}

	public void setNode_url(String node_url) {
		this.node_url = node_url;
	}

	public Integer getNode_type() {
		return node_type;
	}

	public void setNode_type(Integer node_type) {
		this.node_type = node_type;
	}

	public String getNode_protocol() {
		return node_protocol;
	}

	public void setNode_protocol(String node_protocol) {
		this.node_protocol = node_protocol;
	}

	public String getNode_class() {
		return node_class;
	}

	public void setNode_class(String node_class) {
		this.node_class = node_class;
	}

	public String getNode_sn() {
		return node_sn;
	}

	public void setNode_sn(String node_sn) {
		this.node_sn = node_sn;
	}

	public Integer getNode_tstype() {
		return node_tstype;
	}

	public void setNode_tstype(Integer node_tstype) {
		this.node_tstype = node_tstype;
	}

	
	public String getNode_title() {
		return node_title;
	}

	public void setNode_title(String node_title) {
		this.node_title = node_title;
	}

	@Override
	public String toString() {
		return "Tree [node_id=" + node_id + ", node_name=" + node_name
				+ ", node_pid=" + node_pid + ", node_userid=" + node_userid
				+ ", node_treeid=" + node_treeid + ", node_url=" + node_url
				+ ", node_type=" + node_type + ", node_protocol="
				+ node_protocol + ", node_class=" + node_class + ", node_sn="
				+ node_sn + ", node_tstype=" + node_tstype + "]";
	}




	
	
	
}
