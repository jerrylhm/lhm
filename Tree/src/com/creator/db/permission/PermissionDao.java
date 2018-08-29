package com.creator.db.permission;

import java.util.List;

public interface PermissionDao {
	List<Permission> queryAllPermission();
	List<Permission> queryById(int id);
    List<Permission> queryByUserId(int userId);
    List<PermissionDto> queryByPage(PermissionDto permissionDto);
    List<Permission> queryByTreeId(int treeid);
    List<Permission> queryCreatorsPermission(int userId);
    List<Permission> queryByUserIdAndtreeId(int userId,int treeId);
    Integer addPermission(Permission permission);
    int updatePermission(Permission permission);
    int deletePermission(int id);
    
    /**
     * 根据用户id和树节点删除对应的权限
     */
    public void deletePermissionByUserid(int userid);
}
