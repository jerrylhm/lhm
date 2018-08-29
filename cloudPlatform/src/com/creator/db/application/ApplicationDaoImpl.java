package com.creator.db.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("applicationDao")
public class ApplicationDaoImpl implements ApplicationDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Application> query() {
		String SQL = "SELECT * FROM tb_application";
		return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<Application>(Application.class));
	}

	@Override
	public Application findById(int id) {
		String SQL = "SELECT * FROM tb_application WHERE app_id=?";
		return jdbcTemplate.queryForObject(SQL, new BeanPropertyRowMapper<Application>(Application.class), id);
	}

	@Override
	public int updateById(Application app) {
		String SQL = "UPDATE tb_application SET app_name=?,app_detail=?,app_link=? WHERE app_id=?";
		return jdbcTemplate.update(SQL, app.getApp_name(),app.getApp_detail(),app.getApp_link(),app.getApp_id());
	}

	@Override
	public int deleteById(int id) {
		String SQL = "DELETE FROM tb_application WHERE app_id=?";
		return jdbcTemplate.update(SQL, id);
	}

	@Override
	public int insert(Application app) {
		String SQL = "INSERT INTO tb_application(app_name,app_detail,app_link) VALUES(?,?,?)";
		return jdbcTemplate.update(SQL, app.getApp_name(),app.getApp_detail(),app.getApp_link());
	}

}
