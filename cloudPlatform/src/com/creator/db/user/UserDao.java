package com.creator.db.user;

import java.util.List;
import java.util.Map;

public interface UserDao {
	
	/**
	 * 获取全部用户
	 */
	public List<User> listUsers();
	
	/**
	 * 根据id获取用户
	 */
	public List<User> findById(int id);
	
	/**
	 * 根据id删除用户
	 */
	public boolean deleteById(int id);
	
	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	public User findByUsername(String username);

	public List<Map<String, Object>> queryAllListUser(int index,int num,String query,String date,String role,Integer status);

	public int countCheckUser(String query,String date,String role,Integer status);

	public String changeUserStatus(int id,int status);

	/**
	 * 按需分页查询所有用户
	 * @param index		分页参数，第几页
	 * @param num		分页参数，每页多少个数据
	 * @param query		搜索内容
	 * @param date		筛选日期，该值为"0"时不筛选
	 * @param role		筛选角色，该值为"0"时不筛选
	 * @param status	筛选状态，该值为-1时不筛选
	 * @param ug_id		筛选用户组，该值为-1时不筛选
	 * @param orderBy	排序，该值为空时为默认排序
	 * @param isDESC	是否倒序
	 * @return
	 */
	public List<Map<String, Object>> queryAllUserByNeed(int index, int num, String query, String date, String role, Integer status, Integer ug_id, String orderBy, boolean isDESC);
	
	/**
	 * 按需查询所有用户数量
	 * @param query		搜索内容
	 * @param date		筛选日期，该值为"0"时不筛选
	 * @param role		筛选角色，该值为"0"时不筛选
	 * @param status	筛选状态，该值为-1时不筛选
	 * @param ug_id		筛选用户组，该值为-1时不筛选
	 * @return
	 */
	public int countAllUserByNeed(String query, String date, String role, Integer status, Integer ug_id);
	
	
	
	/**
	 * 更新用户信息
	 * @param user	用户对象
	 * @return		成功返回1，失败返回0
	 */
	public int updateUser(User user);

	/**
	 * 新增用户
	 * @param user	用户对象
	 * @return		成功返回自增长id，失败返回-1
	 */
	public int addUser(User user);	
	
	/**
	 * 检查是否存在相同用户名的用户
	 * @param username	用户名
	 * @return	存在返回1，不存在返回0
	 */
	public int isExist(String username);

	public List<Map<String, Object>> queryClassNameByUserId(int id);
	
	/**
	 * 根据班级id查询用户名
	 */
	public List<User> findByClassId(int classId);
	
	/**
	 * 根据用户id修改用户班级
	 */
	public int updateClassId(User user);

	public List<User> queryTeachers();
}
