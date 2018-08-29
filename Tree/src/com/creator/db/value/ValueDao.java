package com.creator.db.value;

import java.util.List;
import java.util.Map;

public interface ValueDao {

	List<Value> queryByNodeId(int nodeId);
	List<Value> queryByKeyAndNodeId(String key,Integer nodeId);
 	Integer updateValue(Value value);
	Integer addValue(Value value);
	Integer deleteValue(int id);
	/**
	 * 删除用户创建的所有树节点的值
	 */
	public void deleteValueByUserId(int userid);
	
	/**
	 * 根据节点id查询对应的值
	 */
	public List<Map<String,Object>> queryValueByNodeId(int nodeId);
	
	/**
	 * 根据节点id查询最新的指定条数的值
	 */
	public List<Map<String,Object>> queryLimitValueByNodeId(int nodeId,int num);
	
	
	/**
	 * 查询开始时间和结束时间之间的值
	 */
	public List<Map<String,Object>> queryValueBetweenDate(String begin,String end);
	
	/**
	 * 根据节点id查询在开始时间和结束时间之间的值
	 */
	public List<Value> queryBetweenTimeByNodeId(int nodeId,String begin,String end);
	
	/**
	 * 根据节点id查询在开始日期和结束日期之间的值
	 */
	public List<Value> queryBetweenDateByNodeId(int nodeId,String begin,String end);
	
	/**
	 * 根据id查询值
	 */
	public List<Value> queryValueById(int id);
}
