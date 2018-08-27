package com.creator.db.term;

import java.util.List;

import com.creator.common.db.CrudDao;
import com.creator.common.db.Page;

public interface TermDao extends CrudDao<Term>{
	
	public List<TermDto> queryByPage(Page page,String like);
}
