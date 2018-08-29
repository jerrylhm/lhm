package com.creator.db.permission;

import java.util.List;

import com.creator.common.db.CrudDao;

public interface PermissionDao extends CrudDao<Permission>{
	/**
	 * 分页查询权限列表
	 */
	public List<Permission> listByPage(int index,int count);
	
	/**
	 * 查询权限总记录数
	 */
	public int countPermission();
	
	/**
	 * 查询全部权限
	 */
	public List<Permission> listPermission();
}
