package com.creator.db.type;

import java.util.List;
import java.util.Map;

public interface TypeDao {

	public List<Type> query();
	public Type findByName(String name);
	public Type findById(Integer id);
	
	/**
	 * 查询类型列表
	 */
	public List<Map<String,Object>> queryTypeList();
	
	/**
	 * 根据类型id查询类型
	 */
	public List<Map<String,Object>> queryTypeById(int id);
	
	/**
	 * 根据类型id查询子类型
	 */
	public List<Map<String,Object>> queryTypeChildrenById(int id);
}
