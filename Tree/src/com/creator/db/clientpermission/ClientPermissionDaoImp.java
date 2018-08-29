package com.creator.db.clientpermission;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class ClientPermissionDaoImp extends JdbcDaoSupport implements ClientPermissionDao {
	private final static String ROW_SELECT = "per_id as id,per_name as name,per_type as type";
	
	/*
	 * 查询前端所有权限
	 */
	@Override
	public List<Map<String, Object>> queryClientPermission(){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_clientpermission";
		List<Map<String, Object>> ClientList = jdbcTemplate.queryForList(SQL);
		return ClientList;
	}

	/*
	 * 根据权限id和权限类型获取权限
	 */
	@Override
	public List<Map<String, Object>> queryByIdAndType(int id, int type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_clientpermission WHERE per_id=? AND per_type=?";
		List<Map<String,Object>> clientList = jdbcTemplate.queryForList(SQL,id,type);
		return clientList;
	}

	/*
	 * 根据权限类型获取权限列表
	 */
	@Override
	public List<Map<String, Object>> queryByType(int type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_clientpermission WHERE per_type=?";
		List<Map<String,Object>> permissionList = jdbcTemplate.queryForList(SQL,type);
		return permissionList;
	}

	
	/*
	 * 获取全部权限列表
	 */
	@Override
	public List<Map<String, Object>> queryAllPermission() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM  tb_clientpermission";
		List<Map<String,Object>> permissionList = jdbcTemplate.queryForList(SQL);
		return permissionList;
	}
}
