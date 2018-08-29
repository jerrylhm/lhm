package com.shiro.dao;

import com.shiro.entity.User;

public interface UserDao {

	public User findByUsername(String username); 
}
