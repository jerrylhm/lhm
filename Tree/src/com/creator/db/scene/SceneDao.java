package com.creator.db.scene;

import java.util.List;
import java.util.Map;

public interface SceneDao {

	public List<Scene> query();
	public List<SceneDto> queryByPage(SceneDto sceneDto);
	public List<Scene> queryByTreeId(int treeId);
	public List<Scene> findById(int id);
 	public int add(Scene scene);
	public int update(Scene scene);
	public int delete(int id);
	public int deleteByTreeId(int treeId);
	
	/**
	 * 根据树根节点查询场景列表
	 */
	public List<Map<String, Object>> querySceneByTreeId(int treeId);
	
	/**
	 * 根据场景节点查询场景
	 */
	public List<Map<String,Object>> queryById(int id);
	
	/**
	 * 根据节点id查询场景
	 */
	public List<Map<String,Object>> queryByNodeId(int nodeId);
	
	/**
	 * 添加场景并返回场景id
	 */
	public int addScene(Scene scene);
	
	/**
	 * 根据场景id修改场景
	 */
	public void updateSceneById(Scene scene);
	
	/**
	 * 根据场景名称判断场景是否存在
	 */
	public boolean isExistByName(String name,int treeid);
	
	/**
	 * 根据场景节点查询场景
	 */
	public List<Scene> queryListById(int id);
}
