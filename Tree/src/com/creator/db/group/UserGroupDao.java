package com.creator.db.group;

import java.util.List;
import java.util.Map;


public interface UserGroupDao {


	public List<Map<String, Object>> queryUserGroupByUserID(Object userid,Integer userType, int index,
			int num);

	public int countUserGroupByUserID(Object userid,Integer userType);

	public List<Map<String, Object>> queryTreeByUserIdAndType(Object userid,
			Integer userType);

	public String addUserGroup(UserGroup userGroup);

	public String deleteUserGroup(Object id);
	
	public Integer deleteByTreeId(int treeId);

	public String updateUserGroup(Integer id, String name, Integer treeid);
	
	/**
	 * 根据用户组id查询用户组
	 */
	public List<Map<String,Object>> queryGroupById(int groupId);
	
	/**
	 * 根据树id查询用户组
	 */
	public List<Map<String,Object>> queryGroupByTreeId(int treeId);	
	
	/**
	 * 根据用户组id查询用户组是否存在
	 */
	public boolean isExistGroupById(int groupId);
	
	public List<UserGroupDto> findByUserIdAndPage(UserGroupDto userGroupDto);
	
	public UserGroup findByTreeIdAndUserId(int treeId,int userId);
	
	
	/**
	 * 添加用户组，并返回添加后的用户组id
	 */
	public int add(UserGroup userGroup);
	
	/**
	 * 根据树根节点和用户组名称查询是否存在
	 */
	public boolean isExistByName(String name,int treeid);
	
	/**
	 * 根据用户组id删除用户组
	 */
	public void deleteUserGroupById(int id);
	/**
	 * 根据用户组id查询用户组
	 */
	public List<UserGroup> queryGroupListById(int groupId);
}
