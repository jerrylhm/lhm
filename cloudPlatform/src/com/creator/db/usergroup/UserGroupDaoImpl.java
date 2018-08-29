package com.creator.db.usergroup;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("userGroupDao")
public class UserGroupDaoImpl implements UserGroupDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<UserGroup> query() {
		String sql = "SELECT * FROM tb_usergroup";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserGroup>(UserGroup.class));
	}

	@Override
	public UserGroup findById(int id) {
		String sql = "SELECT * FROM tb_usergroup WHERE ug_id=?";
		List<UserGroup> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserGroup>(UserGroup.class), id);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(UserGroup obj) {
		String sql = "UPDATE tb_usergroup SET ug_name=?,ug_permissionid=? WHERE ug_id=?";
		return jdbcTemplate.update(sql, obj.getUg_name(), obj.getUg_permissionid(), obj.getUg_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_usergroup WHERE ug_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(UserGroup obj) {
		String sql = "INSERT INTO tb_usergroup(ug_name, ug_permissionid) VALUES(?,?)";
		return jdbcTemplate.update(sql, obj.getUg_name(), obj.getUg_permissionid());
	}
	
	/*
	 * 分页查询用户组
	 */
	@Override
	public List<UserGroup> listByPage(int index, int count) {
		String SQL = "SELECT * FROM tb_usergroup LIMIT ?,?";
		List<UserGroup> groupList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<UserGroup>(UserGroup.class),count*(index-1),count);
		return groupList;
	}

	/*
	 * 查询用户组的总记录数
	 */
	@Override
	public int countUserGroup() {
		String SQL = "SELECT COUNT(ug_id) FROM tb_usergroup";
		int count = jdbcTemplate.queryForObject(SQL, Integer.class);
		return count;
	}

	
	/*
	 * 根据用户组名和id查询除id外的用户组是否存在该名称(当id=0时，查询全部)
	 */
	@Override
	public boolean isExistByNameAndId(String name, int id) {
		String SQL = "SELECT COUNT(ug_id)FROM tb_usergroup WHERE ug_name=? AND ug_id!=?";
		int count = jdbcTemplate.queryForObject(SQL, new Object[]{name,id},Integer.class);
		if(count <= 0) {
			return false;
		}
		return true;
	}
	
	/*
	 * 根据用户id查询该用户所属的所有用户组
	 */
	@Override
	public List<Map<String, Object>> queryUserGroupByUserId(int id){
		String SQL = "SELECT * FROM tb_user_usergroup WHERE uug_urid = "+id;
		List<Map<String, Object>> GroupList = jdbcTemplate.queryForList(SQL);
		return GroupList;
		
	}
	
	/*
	 * 根据用户组id查询当前用户组所具有的权限
	 */
	@Override
	public Map<String, Object> queryPermissionByGroupId(Object id){
		String SQL = "SELECT * FROM tb_usergroup WHERE ug_id = "+id;
		Map<String, Object> MapPermissionList = jdbcTemplate.queryForMap(SQL);
		return MapPermissionList;
	}
	
	@Override
	public int deleteByUrid(int ur_id) {
		String SQL = "DELETE FROM tb_user_usergroup WHERE uug_urid=?";
		return jdbcTemplate.update(SQL, ur_id);
	}

	@Override
	public int addUserGroupToUser(int ur_id, int ug_id) {
		String SQL = "INSERT INTO tb_user_usergroup(uug_urid, uug_ugid) values(?,?)";
		return jdbcTemplate.update(SQL, ur_id, ug_id);
	}

	
	/*
	 * 根据用户组删除用户组-用户（中间表）
	 */
	@Override
	public int deleteByUgid(int ugid) {
		String SQL = "DELETE FROM tb_user_usergroup WHERE uug_ugid=?";
		return jdbcTemplate.update(SQL, ugid);
	}

}
