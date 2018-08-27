package com.creator.db.ttnode;

import java.util.List;

import com.creator.common.db.CrudDao;

public interface TTNodeDao extends CrudDao<TTNode>{

	public int deleteByTtid(int ttId);
	public List<TTNode> queryByttId(int ttId);
}
