package com.creator.common.db;

import java.util.List;

public interface CrudDao<T> {
	
	public List<T> query();
	
	public T findById(int id);
	
	public int updateById(T obj);
	
	public int deleteById(int id);
	
	public int insert(T obj);
	
}
