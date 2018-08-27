package com.creator.db.usergroup;

import java.util.List;
import java.util.Map;

import com.creator.common.db.CrudDao;

public interface UserGroupDao extends CrudDao<UserGroup>{
	/**
	 * 分页查询用户组
	 */
	public List<UserGroup> listByPage(int index,int count);
	
	/**
	 * 查询用户组的总记录数
	 */
	public int countUserGroup();
	
	/**
	 * 根据用户组名和id查询除id外的用户组是否存在该名称(当id=0时，查询全部)
	 */
	public boolean isExistByNameAndId(String name,int id);

	public List<Map<String, Object>> queryUserGroupByUserId(int id);

	public Map<String, Object> queryPermissionByGroupId(Object id);
	
	/**
	 * 删除指定用户的所有用户组(删除中间表)
	 * @param ur_id		用户id
	 * @return
	 */
	public int deleteByUrid(int ur_id);
	
	/**
	 * 给用户添加指定用户组(在中间表中)
	 * @param ur_id		用户id
	 * @param ug_id		用户组id
	 * @return
	 */
	public int addUserGroupToUser(int ur_id, int ug_id);
	
	/**
	 * 根据用户组删除用户组-用户（中间表）
	 */
	public int deleteByUgid(int ugid);
}
