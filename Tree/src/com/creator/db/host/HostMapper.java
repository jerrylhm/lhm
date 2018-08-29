package com.creator.db.host;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class HostMapper implements RowMapper<Host>{

	@Override
	public Host mapRow(ResultSet rs, int rowNum) throws SQLException {
		Host host = new Host();
		host.setHost_id(rs.getInt("host_id"));
		host.setHost_ip(rs.getString("host_ip"));
		host.setHost_port(rs.getInt("host_port"));
		host.setHost_updatetime(rs.getString("host_updatetime"));
		return host;
	}

}
