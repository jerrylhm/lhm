package com.creator.db.timetable;

import java.util.List;

import com.creator.common.db.Page;
import com.creator.db.term.Term;
import com.creator.db.ttnode.TTNode;

public class TimeTableDto extends TimeTable{
	private List<TTNode> nodes;
	private Page page;
	private Term term;
	
	public List<TTNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<TTNode> nodes) {
		this.nodes = nodes;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}


	
	
}
