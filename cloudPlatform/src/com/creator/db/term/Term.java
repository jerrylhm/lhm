package com.creator.db.term;

import java.util.Date;

public class Term {

	private Integer term_id;
	private String term_name;
	private Date term_start;
	private Date term_end;
	
	public Integer getTerm_id() {
		return term_id;
	}
	public void setTerm_id(Integer term_id) {
		this.term_id = term_id;
	}
	public String getTerm_name() {
		return term_name;
	}
	public void setTerm_name(String term_name) {
		this.term_name = term_name;
	}
	public Date getTerm_start() {
		return term_start;
	}
	public void setTerm_start(Date term_start) {
		this.term_start = term_start;
	}
	public Date getTerm_end() {
		return term_end;
	}
	public void setTerm_end(Date term_end) {
		this.term_end = term_end;
	}
	@Override
	public String toString() {
		return "Term [term_id=" + term_id + ", term_name=" + term_name + ", term_start=" + term_start + ", term_end="
				+ term_end + "]";
	}
	
}
