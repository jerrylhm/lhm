package com.shiro.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.shiro.entity.Role;

@Repository("roleDao")
public class RoleDaoImpl implements RoleDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Role> findByUsername(String username) {
		System.out.println("数据库查询role");
		String sql = "SELECT * FROM user_roles WHERE username = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Role>(Role.class), username);
	}

}
