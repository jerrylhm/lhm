package com.shiro.dao;

import java.util.List;

import com.shiro.entity.Role;

public interface RoleDao {

	public List<Role> findByUsername(String username);
}
