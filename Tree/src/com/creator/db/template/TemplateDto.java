package com.creator.db.template;

import com.creator.util.Page;

public class TemplateDto extends Template{

	private String treeName;
    private Page page;
	private String like;
    private int userId;
	private String treesStr;
	public TemplateDto() {
		this.page = new Page();
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



	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTreesStr() {
		return treesStr;
	}

	public void setTreesStr(String treesStr) {
		this.treesStr = treesStr;
	}

	@Override
	public String toString() {
		return "TemplateDto [treeName=" + treeName + ", page=" + page
				+ ", like=" + like + "]";
	}



	
	
	
}
