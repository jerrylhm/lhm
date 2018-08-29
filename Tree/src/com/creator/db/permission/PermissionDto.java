package com.creator.db.permission;

import com.creator.util.Page;

public class PermissionDto extends Permission{

	private Page page;
	private String like;
	private String node_name;
	public PermissionDto() {
		this.page = new Page();
	}

	
	public String getNode_name() {
		return node_name;
	}


	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}


	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

	@Override
	public String toString() {
		return "PermissionDto [page=" + page + ", like=" + like + "]";
	}
	
	
}
