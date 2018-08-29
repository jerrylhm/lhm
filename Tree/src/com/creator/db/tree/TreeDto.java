package com.creator.db.tree;

import com.creator.util.Page;

public class TreeDto extends Tree{

	//用户名称
	private String ur_name;
    //是否是创建者
	private Integer isCreator;
    private Page page;
    private String like;
	
    public TreeDto() {
		this.page = new Page();
	}
    
	public Integer getIsCreator() {
		return isCreator;
	}

	public void setIsCreator(Integer isCreator) {
		this.isCreator = isCreator;
	}

	public String getUr_name() {
		return ur_name;
	}

	public void setUr_name(String ur_name) {
		this.ur_name = ur_name;
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
	
}
