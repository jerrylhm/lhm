package com.creator.db.subject;

import java.util.List;

import com.creator.common.db.CrudDao;
import com.creator.common.db.Page;

public interface SubjectDao extends CrudDao<Subject>{

	public List<SubjectDto> queryByPage(Page page,String like);
	
	public List<SubjectDto> queryByPage(Page page,String like,Integer orgId);
}
