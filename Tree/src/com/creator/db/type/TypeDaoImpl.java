package com.creator.db.type;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class TypeDaoImpl extends JdbcDaoSupport implements TypeDao {

	@Override
	public List<Type> query() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT type_id,type_name,type_name_cn,type_pid FROM tb_type";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Type>(Type.class));
	}

	@Override
	public Type findByName(String name) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT type_id,type_name,type_name_cn,type_pid FROM tb_type WHERE type_name = ?";
		List<Type> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Type>(Type.class), name);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
		
	}

	@Override
	public Type findById(Integer id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT type_id,type_name,type_name_cn,type_pid FROM tb_type WHERE type_id = ?";
		List<Type> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Type>(Type.class), id);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
	}
	
	
	/*
	 * 查询类型列表
	 */
	@Override
	public List<Map<String, Object>> queryTypeList() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT type_id,type_name,type_name_cn,type_pid FROM tb_type";
		List<Map<String,Object>> typeList =  jdbcTemplate.queryForList(SQL);
		return typeList;
	}

	/*
	 * 根据类型id查询类型
	 */
	@Override
	public List<Map<String, Object>> queryTypeById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT type_id,type_name,type_name_cn,type_pid FROM tb_type WHERE type_id=?";
		List<Map<String,Object>> typeList = jdbcTemplate.queryForList(SQL,id);
		return typeList;
	}

	/*
	 * 根据类型id查询子类型
	 */
	@Override
	public List<Map<String, Object>> queryTypeChildrenById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT type_id,type_name,type_name_cn,type_pid FROM tb_type WHERE type_pid=?";
		List<Map<String,Object>> typeList = jdbcTemplate.queryForList(SQL,id);
		return typeList;
	}

}
