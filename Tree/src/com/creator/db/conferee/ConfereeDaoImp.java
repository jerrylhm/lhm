package com.creator.db.conferee;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class ConfereeDaoImp extends JdbcDaoSupport implements ConfereeDao{

	/*
	 * 添加会议预约
	 */
	@Override
	public void addConferee(Conferee conferee) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "INSERT INTO tb_conferee(con_meid,con_userid) VALUES(?,?)";
		jdbcTemplate.update(SQL,conferee.getCon_meid(),conferee.getCon_userid());
		
	}
	
}
