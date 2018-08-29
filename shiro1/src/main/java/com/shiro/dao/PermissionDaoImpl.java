package com.shiro.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.shiro.entity.Permission;

@Repository("permissionDao")
public class PermissionDaoImpl implements PermissionDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Permission> findByRole(String role) {
		System.out.println("数据库查询permission");
		String sql = "SELECT * FROM roles_permissions WHERE role_name = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Permission>(Permission.class), role);
	}
	
}
