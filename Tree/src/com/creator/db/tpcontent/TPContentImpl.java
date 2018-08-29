package com.creator.db.tpcontent;

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

public class TPContentImpl extends JdbcDaoSupport implements TPContentDao {
	private static final String ROW_SELECT = "tpc_id,tpc_tpid,tpc_nodeid,tpc_content,tpc_userid,tpc_date";
	
	@Override
	public List<TPContent> findById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select tpc_id, tpc_tpid, tpc_nodeid, tpc_content, tpc_userid, tpc_date FROM tb_tpcontent WHERE tpc_id = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<TPContent>(TPContent.class), id);
	}

	@Override
	public int update(TPContent tpc) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE tb_tpcontent SET tpc_tpid = ?, tpc_nodeid = ?, tpc_content = ?, tpc_userid = ?, tpc_date = ? WHERE tpc_id = ?";
		return jdbcTemplate.update(sql, tpc.getTpc_tpid(), tpc.getTpc_nodeid(), tpc.getTpc_content(), tpc.getTpc_userid(), tpc.getTpc_date(), tpc.getTpc_id());
	}

	@Override
	public int delete(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql =  "DELETE fROM tb_tpcontent WHERE tpc_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int add(TPContent tpc) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "INSERT INTO tb_tpcontent(tpc_tpid,tpc_nodeid,tpc_content,tpc_userid,tpc_date) values(?,?,?,?,?) ";
		return jdbcTemplate.update(sql, tpc.getTpc_tpid(), tpc.getTpc_nodeid(), tpc.getTpc_content(), tpc.getTpc_userid(), tpc.getTpc_date());
	}

	@Override
	public List<TPContent> findByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select tpc_id, tpc_tpid, tpc_nodeid, tpc_content, tpc_userid, tpc_date FROM tb_tpcontent WHERE tpc_nodeid = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<TPContent>(TPContent.class), nodeId);
	}

	@Override
	public List<TPContent> findByTpId(int tpId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select tpc_id, tpc_tpid, tpc_nodeid, tpc_content, tpc_userid, tpc_date FROM tb_tpcontent WHERE tpc_tpid = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<TPContent>(TPContent.class), tpId);
	}
	
	/*
	 * 添加模板内容，并且返回添加后的数据id
	 */
	@Override
	public int addContent(final TPContent content) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		final String SQL = "INSERT INTO tb_tpcontent(tpc_tpid,tpc_nodeid,tpc_content,tpc_userid,tpc_date) VALUES(?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement statement = conn.prepareStatement(SQL, new String[]{"tpc_tpid","tpc_nodeid","tpc_content","tpc_userid","tpc_date"});
				statement.setInt(1, content.getTpc_tpid());
				statement.setInt(2,content.getTpc_nodeid());
				statement.setString(3, content.getTpc_content());
				statement.setInt(4, content.getTpc_userid());
				statement.setString(5, content.getTpc_date());
				return statement;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();
	}

	/*
	 * 根据模板内容id查询模板内容
	 */
	@Override
	public List<Map<String, Object>> queryContentById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tpcontent WHERE tpc_id=?";
		List<Map<String,Object>> contentList = jdbcTemplate.queryForList(SQL,id);
		return contentList;
	}

	
	/*
	 * 根据模板id查询模板内容
	 */
	@Override
	public List<Map<String, Object>> queryContentByTpId(int tpid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tpcontent WHERE tpc_tpid=?";
		List<Map<String,Object>> contentList = jdbcTemplate.queryForList(SQL,tpid);
		return contentList;
	}

	
	/*
	 *  根据模板id更新模板内容
	 */
	@Override
	public void updateByTpId(int tpid,TPContent tpcontent) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "UPDATE tb_tpcontent SET tpc_content=?,tpc_userid=?,tpc_date=? WHERE tpc_tpid=?";
		jdbcTemplate.update(SQL, tpcontent.getTpc_content(),tpcontent.getTpc_userid(),tpcontent.getTpc_date(),tpid);
	}

	
	/*
	 *  根据模板id和节点id查询模板内容
	 */
	@Override
	public List<Map<String, Object>> queryContentByNodeIdAndTpId(int nodeid,
			int tpid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tpcontent WHERE tpc_tpid=? AND tpc_nodeid=?";
		List<Map<String,Object>> contentList = jdbcTemplate.queryForList(SQL,tpid,nodeid);
		return contentList;
	}

	
	/*
	 * 根据id更新模板内容
	 */
	@Override
	public void updateById(int id,TPContent content) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "UPDATE tb_tpcontent SET tpc_tpid=?,tpc_nodeid=?,tpc_content=?,tpc_userid=?,tpc_date=? WHERE tpc_id=?";
		jdbcTemplate.update(SQL,content.getTpc_tpid(),content.getTpc_nodeid(),content.getTpc_content(),content.getTpc_userid(),content.getTpc_date(),id);
		
	}
	
	/*
	 *  根据节点id查询模板内容
	 */
	@Override
	public List<Map<String, Object>> queryContentByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tpcontent WHERE tpc_nodeid=? ";
		List<Map<String,Object>> contentList = jdbcTemplate.queryForList(SQL, nodeId);
		return contentList;
	}

}
