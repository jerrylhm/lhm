package com.creator.db.organization;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository("organizationDao")
public class OrganizationDaoImpl implements OrganizationDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Organization> query() {
		String sql = "SELECT * FROM tb_organization";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Organization>(Organization.class));
	}

	@Override
	public Organization findById(int id) {
		String sql = "SELECT * FROM tb_organization WHERE org_id=?";
		List<Organization> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Organization>(Organization.class),id);
		if(ls.size()>0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(Organization obj) {
		String sql = "UPDATE tb_organization SET org_name=?,org_pid=?,org_type=?,org_flag=? WHERE org_id=?";
		return jdbcTemplate.update(sql, obj.getOrg_name(), obj.getOrg_pid(), obj.getOrg_type(), obj.getOrg_flag(), obj.getOrg_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_organization WHERE org_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(Organization obj) {
		String sql = "INSERT INTO tb_organization(org_name,org_pid,org_type,org_flag) VALUES(?,?,?,?)";
		return jdbcTemplate.update(sql, obj.getOrg_name(), obj.getOrg_pid(), obj.getOrg_type(), obj.getOrg_flag());
	}

	@Override
	public List<Organization> queryByOrgType(int type) {
		String sql = "SELECT * FROM tb_organization WHERE org_type=?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Organization>(Organization.class), type);
	}
	
	/*
	 * 根据父节点查询组织结构
	 */
	@Override
	public List<Organization> findByPid(int pid) {
		String SQL = "SELECT * FROM tb_organization WHERE org_pid=?";
		List<Organization> orgList = jdbcTemplate.query(SQL, new Object[]{pid}, new BeanPropertyRowMapper<Organization>(Organization.class));
		return orgList;
	}

	
	/*
	 * 根据父id和名称判断是否存在相同节点
	 */
	@Override
	public boolean isExistByPidAndName(Organization org) {
		String SQL = "SELECT COUNT(org_id) FROM tb_organization WHERE org_name=? AND org_pid=?";
		int count = jdbcTemplate.queryForObject(SQL, new Object[]{org.getOrg_name(),org.getOrg_pid()}, Integer.class);
		if(count > 0) {
			return true;
		}
		return false;
	}

	
	/*
	 * 添加组织结构并返回id
	 */
	@Override
	public int insertReturnWithId(final Organization org) {
		final String SQL = "INSERT INTO tb_organization(org_name,org_pid,org_type,org_flag) VALUES(?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement statement = conn.prepareStatement(SQL, new String[]{"org_name","org_pid","org_type","org_flag"});
				statement.setString(1, org.getOrg_name());
				statement.setInt(2, org.getOrg_pid());
				statement.setInt(3, org.getOrg_type());
				statement.setInt(4, org.getOrg_flag());
				return statement;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();
	}

	/*
	 * 根据类型查找组织
	 */
	@Override
	public List<Organization> findByType(int type) {
		String SQL = "SELECT * FROM tb_organization WHERE org_type=?";
		List<Organization> orgList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<Organization>(Organization.class),type);
		return orgList;
	}

	
	@Override
	public List<Organization> getAllWithConcat() {
		return jdbcTemplate.execute(
			new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					//调用的存储过程
	                String proc = "{call org_concat_result()}";
                    CallableStatement cs = con.prepareCall(proc);
                    return cs;
				}
			}, 
			new CallableStatementCallback<List<Organization>>() {
				@Override
				public List<Organization> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					List<Organization> org_list = new ArrayList<Organization>();
					if(cs.execute()) {
						ResultSet rs = cs.getResultSet();
						 while (rs.next()) {
							 Organization organization = new Organization();
							 organization.setOrg_id(rs.getInt(1));
							 organization.setOrg_name(rs.getString(2));
							 organization.setOrg_pid(rs.getInt(3));
							 org_list.add(organization);
						 }
					}
					return org_list;
				}
			});
	}
}
