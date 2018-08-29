package com.creator.db.clientpermission;

import java.util.List;
import java.util.Map;

public interface ClientPermissionDao {

	public List<Map<String, Object>> queryClientPermission();
	/**
	 * 根据权限id和权限类型获取权限
	 */
	public List<Map<String,Object>> queryByIdAndType(int id,int type);
	
	/**
	 * 根据权限类型获取权限列表
	 */
	public List<Map<String,Object>> queryByType(int type);
	
	/**
	 * 获取全部权限列表
	 */
	public List<Map<String,Object>> queryAllPermission();
}
