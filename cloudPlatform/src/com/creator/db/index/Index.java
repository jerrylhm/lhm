package com.creator.db.index;

public class Index {

	private Integer index_id;
	private Integer index_index;
	private Integer index_day;
	private Integer index_stid;
	
	public Integer getIndex_id() {
		return index_id;
	}
	public void setIndex_id(Integer index_id) {
		this.index_id = index_id;
	}
	public Integer getIndex_index() {
		return index_index;
	}
	public void setIndex_index(Integer index_index) {
		this.index_index = index_index;
	}
	public Integer getIndex_day() {
		return index_day;
	}
	public void setIndex_day(Integer index_day) {
		this.index_day = index_day;
	}
	public Integer getIndex_stid() {
		return index_stid;
	}
	public void setIndex_stid(Integer index_stid) {
		this.index_stid = index_stid;
	}
	
	@Override
	public String toString() {
		return "Index [index_id=" + index_id + ", index_index=" + index_index + ", index_day=" + index_day
				+ ", index_stid=" + index_stid + "]";
	}
	
}
