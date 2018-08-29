package com.creator.db.group;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class UserGroupMapper implements RowMapper<UserGroup>{

	@Override
	public UserGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		UserGroup userGroup = new UserGroup();
		userGroup.setUrg_id(rs.getInt("urg_id"));
		userGroup.setUrg_name(rs.getString("urg_name"));
		userGroup.setUrg_userid(rs.getInt("urg_userid"));
		userGroup.setUrg_treeid(rs.getInt("urg_treeid"));
		return userGroup;
	}

}
