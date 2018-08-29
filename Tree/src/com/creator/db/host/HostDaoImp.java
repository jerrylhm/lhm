package com.creator.db.host;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class HostDaoImp extends JdbcDaoSupport implements HostDao{

	/*
	 * 添加NodeJs主机信息
	 */
	@Override
	public void addHost(Host host) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "INSERT INTO tb_host(host_treeid,host_ip,host_port,host_updatetime) VALUES(?,?,?,?)";
		jdbcTemplate.update(SQL,host.getHost_treeid(),host.getHost_ip(),host.getHost_port(),host.getHost_updatetime());
	}

	/*
	 * 根据树id查询主机信息是否存在
	 */
	@Override
	public boolean isExistHostByTreeId(int treeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(host_id) FROM tb_host WHERE host_treeid=?";
		int count = jdbcTemplate.queryForObject(SQL, Integer.class, treeId);
		if(count <= 0) {
			return false;
		}
		return true;
	}

	
	/*
	 * 根据树id更新主机信息
	 */
	@Override
	public void updateHostByTreeId(Host host) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "UPDATE tb_host SET host_ip=?,host_port=?,host_updatetime=? WHERE host_treeid=?";
		jdbcTemplate.update(SQL,host.getHost_ip(),host.getHost_port(),host.getHost_updatetime(),host.getHost_treeid());
		
	}

	@Override
	public List<Host> queryByTreeId(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT host_id,host_treeid,host_ip,host_port,host_updatetime FROM tb_host WHERE host_treeid = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Host>(Host.class), id);
	}
	
	
}
