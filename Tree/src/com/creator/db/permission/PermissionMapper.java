package com.creator.db.permission;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PermissionMapper implements RowMapper<Permission>{

	@Override
	public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
		Permission permission = new Permission();
		permission.setUrnode_id(rs.getInt("urnode_id"));
		permission.setUrnode_userid(rs.getInt("urnode_userid"));
		permission.setUrnode_nodeid(rs.getString("urnode_nodeid"));
		permission.setUrnode_iscreator(rs.getInt("urnode_iscreator"));
		return permission;
	}

}
