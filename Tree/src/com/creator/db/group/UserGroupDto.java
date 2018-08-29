package com.creator.db.group;

import com.creator.util.Page;

public class UserGroupDto extends UserGroup{

	private Page page;
	private String like;
	private String node_id;
	private String node_name;
	private String userName;
	private Integer isCreator;
	
	public UserGroupDto() {
		this.page = new Page();
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

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getIsCreator() {
		return isCreator;
	}

	public void setIsCreator(Integer isCreator) {
		this.isCreator = isCreator;
	}

	@Override
	public String toString() {
		return super.toString() + "[page=" + page + ", like=" + like + ", node_id="
				+ node_id + ", node_name=" + node_name + ", userName="
				+ userName + ", isCreator=" + isCreator + "]";
	}

	
	
	
}
