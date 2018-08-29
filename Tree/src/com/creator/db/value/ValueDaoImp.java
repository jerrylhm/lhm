package com.creator.db.value;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.creator.db.tree.Tree;


public class ValueDaoImp extends JdbcDaoSupport implements ValueDao{
	//要查询的字段
	private final static String ROW_SELECT  = "value_id,value_nodeid,value_data,value_datetime";
	@Override
	public List<Value> queryByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT value_id,value_nodeid,value_data,value_key,value_datetime "
		           + "FROM tb_value WHERE value_nodeid=?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Value.class), nodeId);
	}

	@Override
	public Integer addValue(Value value) {
        JdbcTemplate jdbcTemplateObject = this.getJdbcTemplate();
		
		final Value t = value;
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		final String sql = "INSERT INTO tb_value(value_nodeid,value_data,value_key,value_datetime) VALUES(?,?,?,?)";
		
		//通过接口回调和keyHolder获取新添加的id
		jdbcTemplateObject.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, t.getValue_nodeid());
				ps.setString(2, t.getValue_data());
				ps.setString(3, t.getValue_key());
				ps.setString(4, t.getValue_datetime());
				return ps;
			}
		},keyHolder);
		//返回课表id
		return keyHolder.getKey().intValue();
	}

	@Override
	public Integer deleteValue(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_value WHERE value_id=?"; 
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public List<Value> queryByKeyAndNodeId(String key,Integer nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT value_id,value_nodeid,value_data,value_key,value_datetime "
		           + "FROM tb_value WHERE value_key=? AND value_nodeid = ?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Value.class), key, nodeId);
	}

	@Override
	public Integer updateValue(Value value) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE tb_value SET value_nodeid=?,value_data=?,value_key=?,value_datetime=? "
		           + "WHERE value_id=?";  
		return jdbcTemplate.update(sql, value.getValue_nodeid(), value.getValue_data(), value.getValue_key(), value.getValue_datetime(), value.getValue_id());
	}
	
	/*
	 * 删除用户创建的所有树节点的值
	 */
	@Override
	public void deleteValueByUserId(int userid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "DELETE FROM tb_value WHERE value_nodeid IN ( SELECT node_id FROM tb_tree WHERE node_userid=? )";
		jdbcTemplate.update(SQL, userid);
	}
	
	/*
	 * 根据节点id查询对应的值
	 */
	@Override
	public List<Map<String, Object>> queryValueByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_value WHERE value_nodeid=?";
		List<Map<String,Object>> valueList = jdbcTemplate.queryForList(SQL,nodeId);
		return valueList;
	}

	
	/*
	 * 根据节点id查询最新的指定条数的值 
	 */
	@Override
	public List<Map<String, Object>> queryLimitValueByNodeId(int nodeId,int num) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String  SQL = "SELECT " + ROW_SELECT + " FROM tb_value WHERE value_nodeid=? ORDER BY value_id DESC LIMIT ?";
		List<Map<String,Object>> valueList = jdbcTemplate.queryForList(SQL, nodeId,num);
		return valueList;
	}
	
	/*
	 * 查询开始时间和结束时间之间的值
	 */
	@Override
	public List<Map<String, Object>> queryValueBetweenDate(String begin,
			String end) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_value WHERE UNIX_TIMESTAMP(value_datetime) BETWEEN UNIX_TIMESTAMP(?) AND UNIX_TIMESTAMP(?)";
		List<Map<String,Object>> valueList = jdbcTemplate.queryForList(SQL,begin,end);
		return valueList;
	}

	
	/*
	 * 根据节点id查询在开始时间和结束时间之间的值
	 */
	@Override
	public List<Value> queryBetweenTimeByNodeId(int nodeId, String begin,
			String end) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_value WHERE value_nodeid=? AND UNIX_TIMESTAMP(value_datetime) BETWEEN UNIX_TIMESTAMP(?) AND UNIX_TIMESTAMP(?)";
		List<Value> valueList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Value.class),nodeId,begin,end);
		return valueList;
	}

	
	/*
	 * 根据节点id查询在开始日期和结束日期之间的值
	 */
	@Override
	public List<Value> queryBetweenDateByNodeId(int nodeId, String begin,
			String end) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_value WHERE value_nodeid=? AND TO_DAYS(value_datetime) BETWEEN TO_DAYS(?) AND TO_DAYS(?)";
		List<Value> valueList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Value.class),nodeId,begin,end);
		return valueList;
	}

	
	/*
	 * 根据id查询值
	 */
	@Override
	public List<Value> queryValueById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_value WHERE value_id=?";
		List<Value> valueList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Value.class),id);
		return valueList;
	}

}
