package com.creator.db.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setUr_id(rs.getInt("ur_id"));
		user.setUr_username(rs.getString("ur_username"));
		user.setUr_password(rs.getString("ur_password"));
		user.setUr_type(rs.getInt("ur_type"));
		user.setUr_datetime(rs.getString("ur_datetime"));
		user.setUr_group(rs.getInt("ur_group"));
		user.setUr_treeid(rs.getInt("ur_treeid"));
		return user;
	}

}
