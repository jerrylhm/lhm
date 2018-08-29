package com.creator.db.template;

import java.util.List;
import java.util.Map;

public interface TemplateDao {

	public List<Template> query();
	public List<Template> findByNodeId(int nodeId);
	public List<Template> findById(int id);
	public List<Template> findByTreeId(int treeId);
	public List<TemplateDto> queryByPage(TemplateDto templateDto);
	public int add(Template template);
	public int update(Template template);
	public int updateTemplateName(Integer id, String name);
	public int delete(int id);
	public int deleteByTreeId(int treeId);
	
	/**
	 * 根据节点id查询模板
	 */
	public List<Map<String, Object>> queryTemplateByNodeId(int nodeId);
	
	/**
	 * 根据模板id查询模板
	 */
	public List<Map<String,Object>> queryTemplateByTpId(int tpid);
	
	/**
	 * 根据节点id查询模板
	 */
	public List<Map<String,Object>> queryByNodeId(int nodeId);
}
