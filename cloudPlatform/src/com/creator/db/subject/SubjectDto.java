package com.creator.db.subject;

import java.util.List;

import com.creator.db.organization.Organization;

public class SubjectDto extends Subject{

	private List<Organization> majors;

	public List<Organization> getMajors() {
		return majors;
	}

	public void setMajors(List<Organization> majors) {
		this.majors = majors;
	}

	@Override
	public String toString() {
		return super.toString() + " majors=" + majors + "]";
	}
	
}
