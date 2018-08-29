package com.creator.db.tpcontent;

import java.util.List;
import java.util.Map;

public interface TPContentDao {

	public List<TPContent> findById(int id);
	public List<TPContent> findByNodeId(int nodeId);
	public List<TPContent> findByTpId(int tpId);
	public int update(TPContent tpc);
	public int delete(int id);
	public int add(TPContent tpc);
	
	/**
	 * 添加模板内容，并且返回添加后的数据id
	 */
	public int addContent(TPContent content);
	
	/**
	 * 根据id查询模板内容
	 */
	public List<Map<String, Object>> queryContentById(int id);
	
	/**
	 * 根据模板id查询模板内容
	 */
	public List<Map<String,Object>> queryContentByTpId(int tpid);
	
	/**
	 * 根据模板id更新模板内容
	 */
	public void updateByTpId(int tpid,TPContent tpcontent);
	
	/**
	 * 根据模板id和节点id查询模板内容
	 */
	public List<Map<String,Object>> queryContentByNodeIdAndTpId(int nodeid,int tpid);
	
	/**
	 * 根据id更新模板内容
	 */
	public void updateById(int id,TPContent content);
	
	/**
	 * 根据节点id查询模板内容
	 */
	public List<Map<String,Object>> queryContentByNodeId(int nodeId);
}
