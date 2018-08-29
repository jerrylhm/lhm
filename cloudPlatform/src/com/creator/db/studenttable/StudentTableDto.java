package com.creator.db.studenttable;

import java.util.List;

import com.creator.db.index.Index;

public class StudentTableDto extends StudentTable{

	private List<Index> indexs;

	public List<Index> getIndexs() {
		return indexs;
	}

	public void setIndexs(List<Index> indexs) {
		this.indexs = indexs;
	}

	@Override
	public String toString() {
		return super.toString() + " indexs=" + indexs;
	}
	
	
}
