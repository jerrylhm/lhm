package com.creator.db.studenttable;

import java.util.List;

import com.creator.common.db.CrudDao;

public interface StudentTableDao extends CrudDao<StudentTable>{
	
	public List<StudentTableDto> queryByParam(Integer week, Integer ttid, Integer addid, Integer orgId);
}
