package com.creator.db.nodeattr;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class NodeAttrDaoImp extends JdbcDaoSupport implements NodeAttrDao{
	private final static String ROW_SELECT = "attr_id,attr_nodeid,attr_type,attr_value";
	
	@Override
	public List<NodeAttr> query() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT attr_id,attr_nodeid,attr_type,attr_value FROM tb_node_attr";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<NodeAttr>(NodeAttr.class));
	}

	@Override
	public int add(NodeAttr attr) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "INSERT INTO tb_node_attr(attr_nodeid,attr_type,attr_value) VALUES(?,?,?)";
		return jdbcTemplate.update(sql, attr.getAttr_nodeid(), attr.getAttr_type(), attr.getAttr_value());
	}

	@Override
	public int update(NodeAttr attr) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE tb_node_attr SET attr_nodeid = ?,attr_type = ?,attr_value = ? WHERE attr_id = ?";
		return jdbcTemplate.update(sql, attr.getAttr_nodeid(), attr.getAttr_type(), attr.getAttr_value(), attr.getAttr_id());
	}

	@Override
	public int delete(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_node_attr WHERE attr_id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int deleteByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_node_attr WHERE attr_nodeid = ?";
		return jdbcTemplate.update(sql, nodeId);
	}

	@Override
	public List<NodeAttr> findByNodeIdAndType(int nodeId, int type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT attr_id,attr_nodeid,attr_type,attr_value FROM tb_node_attr WHERE attr_nodeid = ? AND attr_type = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<NodeAttr>(NodeAttr.class), nodeId, type);
	}

	@Override
	public int deleteByNodeIdAndType(int nodeId, int type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_node_attr WHERE attr_nodeid = ? AND attr_type = ?";
		return jdbcTemplate.update(sql, nodeId, type);
	}
	
	/*
	 * 根据节点id和类型查询对应的属性
	 */
	@Override
	public List<Map<String, Object>> queryAttrByNodeIdAndType(int nodeId,
			int type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_node_attr WHERE attr_nodeid=? AND attr_type=?";
		List<Map<String,Object>> attrList = jdbcTemplate.queryForList(SQL,nodeId,type);
		return attrList;
	}
	
	/*
	 * 添加节点属性
	 */
	@Override
	public void addNodeAttr(NodeAttr nodeAttr) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "INSERT INTO tb_node_attr(attr_nodeid,attr_type,attr_value) VALUES(?,?,?)";
		jdbcTemplate.update(SQL, nodeAttr.getAttr_nodeid(),nodeAttr.getAttr_type(),nodeAttr.getAttr_value());
	}

	

	/*
	 * 根据节点id和类型修改属性
	 */
	@Override
	public void updateByNodeIdAndType(int nodeId, int type,NodeAttr nodeAttr) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "UPDATE tb_node_attr SET attr_nodeid=?,attr_type=?,attr_value=? WHERE attr_nodeid=? AND attr_type=?";
		jdbcTemplate.update(SQL,nodeAttr.getAttr_nodeid(),nodeAttr.getAttr_type(),nodeAttr.getAttr_value(),nodeId,type);
		
	}

}
