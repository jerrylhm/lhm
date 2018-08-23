package com.shiro.dao;

import java.util.List;

import com.shiro.entity.Permission;

public interface PermissionDao {

	public List<Permission> findByRole(String role);
}
