package com.creator.db.nodeattr;

import java.util.List;
import java.util.Map;

public interface NodeAttrDao {

	public List<NodeAttr> query();
	public List<NodeAttr> findByNodeIdAndType(int nodeId,int type);
	public int add(NodeAttr attr);
	public int update(NodeAttr attr);
	public int delete(int id);
	public int deleteByNodeId(int nodeId);
	public int deleteByNodeIdAndType(int nodeId,int type);
	
	/**
	 * 根据节点id和类型查询对应的属性
	 */
	public List<Map<String,Object>> queryAttrByNodeIdAndType(int nodeId,int type);
	
	/**
	 * 添加节点属性
	 */
	public void addNodeAttr(NodeAttr nodeAttr);
	
	
	/**
	 * 根据节点id和类型修改属性
	 */
	public void updateByNodeIdAndType(int nodeId,int type,NodeAttr nodeAttr);
}
