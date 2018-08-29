package com.creator.db.permission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("permissionDao")
public class PermissionDaoImpl implements PermissionDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Permission> query() {
		String sql = "SELECT * FROM tb_permission";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Permission>(Permission.class));
	}

	@Override
	public Permission findById(int id) {
		String sql = "SELECT * FROM tb_permission WHERE ps_id=?";
		List<Permission> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Permission>(Permission.class),id);
		if(ls.size()>0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(Permission obj) {
		String sql = "UPDATE tb_permission SET ps_name=?,ps_appid=? WHERE ps_id=?";
		return jdbcTemplate.update(sql, obj.getPs_name(), obj.getPs_appid(), obj.getPs_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_permission WHERE ps_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(Permission obj) {
		String sql = "INSERT INTO tb_permission(ps_name,ps_appid) VALUES(?,?)";
		return jdbcTemplate.update(sql, obj.getPs_name(), obj.getPs_appid());
	}
	
	

	/*
	 * 分页查询权限列表
	 */
	@Override
	public List<Permission> listByPage(int index, int count) {
		String SQL = "SELECT * FROM tb_permission LIMIT ?,?";
		List<Permission> perList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<Permission>(Permission.class),(index-1) * count,count);
		return perList;
	}

	
	/*
	 * 查询权限总记录数
	 */
	@Override
	public int countPermission() {
		String SQL = "SELECT COUNT(ps_id) FROM tb_permission";
		int count = jdbcTemplate.queryForObject(SQL, Integer.class);
		return count;
	}

	/*
	 * 查询全部权限
	 */
	@Override
	public List<Permission> listPermission() {
		String SQL = "SELECT * FROM tb_permission";
		List<Permission> permissionList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<Permission>(Permission.class));
		return permissionList;
	}


}
