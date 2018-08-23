package com.shiro.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.shiro.entity.User;

@Repository("userDao")
public class UserDaoImpl implements UserDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public User findByUsername(String username) {
		String sql = "SELECT * FROM users WHERE username = ?";
		List<User> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class), username);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

}
