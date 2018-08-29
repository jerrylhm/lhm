package com.creator.db.index;

import java.util.List;

import com.creator.common.db.CrudDao;

public interface IndexDao extends CrudDao<Index>{

	public List<Index> queryByStId(int stid);
	
	public List<Index> queryExist(Integer ttId, Integer week, Integer addId, Integer index, Integer day);
	
	public int deleteByStId(int stId);
}
