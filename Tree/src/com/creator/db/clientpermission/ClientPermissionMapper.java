package com.creator.db.clientpermission;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ClientPermissionMapper implements RowMapper<ClientPermission>{

	@Override
	public ClientPermission mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		ClientPermission clientPermission = new ClientPermission();
		clientPermission.setPer_id(rs.getInt("per_id"));
		clientPermission.setPer_name(rs.getString("per_name"));
		clientPermission.setPer_type(rs.getInt("per_type"));
		return clientPermission;
	}

}
