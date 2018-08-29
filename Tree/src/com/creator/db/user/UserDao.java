package com.creator.db.user;

import java.util.List;
import java.util.Map;

public interface UserDao {
	/**
	 * 查询所有用户
	 */
	public List<User> queryAllUser();
	public List<User> queryById(int id);
	/**
	 * 验证账号密码是否存在
	 */
	public boolean isExist(String username,String password) ;
	
	/**
	 * 根据用户账号、密码查询用户
	 */
	public List<User> queryUserByUsernameAndPsw(String username,String password);
	
	/**
	 * 根据页数、用户类型、搜索值分页查询用户
	 */
	public List<Map<String,Object>> queryAllUsersByCondition(int index,int num,String searchValue);
	
	/**
	 * 根据页数、用户类型、搜索值查询用户总数
	 */
	public int countUserByCondition(String searchValue);
	
	/**
	 * 查询是否存在用户名
	 */
	public boolean isExistUsername(String username);
	
	/**
	 * 添加用户
	 */
	public void addUser(User user);
	
	/**
	 * 根据用户id查询用户
	 */
	public List<User> queryUserById(int ur_id);
	
	/**
	 * 修改用户信息
	 */
	public void updateUserById(int ur_id,User user);
	
	/**
	 * 根据用户id删除对应的用户
	 */
	public void deleteUserById(int ur_id);
	
	
	/**
	 * 根据用户id判断是否存在用户
	 */
	public boolean isExistByUserId(int userId);
	
	/**
	 * 根据用户名、密码和用户类型查询用户
	 */
	public List<Map<String,Object>> queryUserByCondition(String username,String password,int type);
	
	/**
	 * 根据用户id和用户类型查询用户
	 */
	public List<Map<String,Object>> queryUserByUserIdAndType(int userId,int  type);
	
	/**
	 * 添加用户(修改),并返回添加的用户id
	 */
	public int addUserNew(User user);
	
	/**
	 * 根据用户id和密码查询用户信息
	 */
	public List<Map<String,Object>> queryUserByIdAndPsw(int userId,String password,int type);
	
	/**
	 * 查询所有用户信息(包括管理员)
	 */
	public List<Map<String,Object>> queryAllUsers();

	/**
	 * 根据用户组id查询用户
	 */
	public List<Map<String,Object>> queryUserByGroupId(int groupId);
	
	/**
	 * 根据用户组id修改用户用户组id
	 */
	public void updateUserGroup(int oldGroupId,int newGroupId);
	
	/**
	 * 根据用户id修改用户组id
	 */
	public void updateGroupById(int id,int groupId);
	
	/**
	 * 根据条件分页或者不分页查询无用户组用户
	 */
	public List<Map<String,Object>> queryUserPageBySearch(String search,String index,String count);
	
	/**
	 * 根据条件查询用户总人数
	 */
	public int countNoGroupUserByCondition(String search);
	
	
	/**
	 * 
	 * 根据登录用户id查出该用户管理的所有组的所有成员
	 * @param userid
	 */
	public List<Map<String,Object>> queryUserByGroupAndStateAndLoginId(int userid,int index,int num);
	
	/**
	 * 
	 * 根据登录用户id查出该用户管理的所有组的所有成员的数量
	 * @param userid
	 */
	public Integer countUserByGroupAndStateAndLoginId(int userid);
	
	/**
	 * 根据用户名和密码查询用户列表
	 */
	public List<Map<String,Object>> queryByUsernameAndPassword(String username,String password);
	
	/**
	 * 根据用户id查询用户信息
	 */
	public List<Map<String,Object>> queryUserMapById(int id);
	
	/**
	 * 根据树id查询用户信息
	 */
	public List<Map<String,Object>> queryUserMapByTreeId(int treeId);
	
	/**
	 * 根据用户组id查询用户列表
	 */
	public List<User> queryUserListByGroupId(int groupId);
	
	/**
	 * 根据用户名查询用户列表
	 */
	public List<User> queryUserListByUsername(String username);
}
