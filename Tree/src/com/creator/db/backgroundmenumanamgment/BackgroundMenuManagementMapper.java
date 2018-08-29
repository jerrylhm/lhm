package com.creator.db.backgroundmenumanamgment;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BackgroundMenuManagementMapper implements RowMapper<BackgroundMenuManagment> {

	@Override
	public BackgroundMenuManagment mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		BackgroundMenuManagment backMenuManagment = new BackgroundMenuManagment();
		backMenuManagment.setBck_mm_id(rs.getInt("bck_mm_id"));
		backMenuManagment.setBck_mm_name(rs.getString("bck_mm_name"));
		backMenuManagment.setBck_mm_treeid(rs.getShort("bck_mm_treeid"));
		backMenuManagment.setBck_mm_del_flag(rs.getShort("bck_mm_del_flag"));
		backMenuManagment.setBck_mm_state(rs.getShort("bck_mm_state"));
		return backMenuManagment;
	}

}
