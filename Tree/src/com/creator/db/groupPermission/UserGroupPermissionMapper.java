package com.creator.db.groupPermission;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserGroupPermissionMapper implements RowMapper<UserGroupPermission>{

	@Override
	public UserGroupPermission mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		UserGroupPermission userGroupPermission = new UserGroupPermission();
		userGroupPermission.setGp_id(rs.getInt("gp_id"));
		userGroupPermission.setGp_nodeid(rs.getInt("gp_nodeid"));
		userGroupPermission.setGp_perid(rs.getString("gp_perid"));
		userGroupPermission.setGp_treeid(rs.getInt("gp_treeid"));
		userGroupPermission.setGp_usergroupid(rs.getInt("gp_usergroupid"));
		return userGroupPermission;
	}

}
