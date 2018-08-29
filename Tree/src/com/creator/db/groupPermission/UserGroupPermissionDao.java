package com.creator.db.groupPermission;

import java.util.List;
import java.util.Map;

public interface UserGroupPermissionDao {

	public List<Map<String, Object>> queryNodePermission(int id);

	public String updatePermissionByNodeAndGroup(Integer nodeid, Integer groupid,
			String idAll,Integer treeid);
    public List<UserGroupPermission> queryByGroupId(int id);
    public int add(UserGroupPermission ugp);
    public int deleteByNodeId(int nodeId);
    
    /**
	 * 根据树根节点id、节点id和用户组id查询权限
	 */
	public List<UserGroupPermission> queryByTreeIdAndGroupId(int treeId,int nodeId,int groupId);
	
	/**
	 * 根据节点id和用户组id查询权限
	 */
	public List<UserGroupPermission> queryByNodeIdAndGroupId(int nodeId,int groupId);
	
	/**
	 * 根据节点id和用户组id、权限类型查询权限
	 */
	public List<UserGroupPermission> queryByNodeIdAndPermissId(int nodeId,int permissId,int groupId);
	
	/**
	 * 根据节点id、用户组id和权限类型查询是否拥有该权限
	 */
	public boolean hasPermissionByGroupIdAndNodeId(int groupId,int nodeId,int permissionId);
	
	/**
	 * 根据用户组id删除权限
	 */
	public void deletePermissionByGroupId(int groupId);
}
