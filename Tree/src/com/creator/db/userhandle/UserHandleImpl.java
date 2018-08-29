package com.creator.db.userhandle;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class UserHandleImpl extends JdbcDaoSupport implements UserHandleDao {
	
	@Override
	public Integer insert(UserHandle uh) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "INSERT INTO tb_userhandle(uh_content,uh_nodeid,uh_update) VALUES(?,?,?)";
		return jdbcTemplate.update(sql, uh.getUh_content(), uh.getUh_nodeid(), uh.getUh_update());
	}

	@Override
	public Integer update(UserHandle uh) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
	String sql = "UPDATE tb_userhandle SET uh_content = ?, uh_nodeid = ?, uh_update = ? WHERE uh_id = ?";
		return jdbcTemplate.update(sql, uh.getUh_content(), uh.getUh_nodeid(), uh.getUh_update(), uh.getUh_id());
	}

	@Override
	public Integer delete(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_userhandle WHERE uh_id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public List<UserHandle> query() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT uh_id,uh_content,uh_nodeid,uh_update FROM tb_userhandle";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserHandle>(UserHandle.class));
	}

	@Override
	public UserHandle findByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT uh_id,uh_content,uh_nodeid,uh_update FROM tb_userhandle WHERE uh_nodeid = ?";
		List<UserHandle> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserHandle>(UserHandle.class), nodeId);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

}
