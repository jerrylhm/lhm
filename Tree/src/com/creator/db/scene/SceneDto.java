package com.creator.db.scene;

import com.creator.util.Page;

public class SceneDto extends Scene{

	private String actionName;
	private String treeName;
	private Page page;
	private String like;
	private String treesStr;
	
	public SceneDto() {
		this.page = new Page();
	}
	
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getTreeName() {
		return treeName;
	}
	public void setTreeName(String treeName) {
		this.treeName = treeName;
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

	public String getTreesStr() {
		return treesStr;
	}

	public void setTreesStr(String treesStr) {
		this.treesStr = treesStr;
	}

	@Override
	public String toString() {
		return "SceneDto [actionName=" + actionName + ", treeName=" + treeName
				+ "]";
	}
	
	
}
