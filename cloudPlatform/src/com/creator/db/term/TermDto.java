package com.creator.db.term;

import java.util.List;

import com.creator.common.db.Page;
import com.creator.db.ttnode.TTNode;

public class TermDto extends Term{
	private Page page;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
}
