package com.creator.db.ttnode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("ttNodeDao")
public class TTNodeDaoImpl implements TTNodeDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<TTNode> query() {
		String sql = "SELECT * FROM tb_node";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<TTNode>(TTNode.class));
	}

	@Override
	public TTNode findById(int id) {
		String sql = "SELECT * FROM tb_node WHERE node_id=?";
		List<TTNode> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<TTNode>(TTNode.class), id);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(TTNode obj) {
		String sql = "UPDATE tb_node SET node_index=?,node_start=?,node_end=?,node_ttid=? WHERE node_id=?";
		return jdbcTemplate.update(sql, obj.getNode_index(), obj.getNode_start(), obj.getNode_end(), obj.getNode_ttid(), obj.getNode_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_node WHERE node_id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(TTNode obj) {
		String sql = "INSERT INTO tb_node(node_index,node_start,node_end,node_ttid) VALUES(?,?,?,?)";
		return jdbcTemplate.update(sql, obj.getNode_index(), obj.getNode_start(), obj.getNode_end(), obj.getNode_ttid());
	}

	@Override
	public int deleteByTtid(int ttId) {
		String sql = "DELETE FROM tb_node WHERE node_ttid = ?";
		return jdbcTemplate.update(sql, ttId);
	}

	@Override
	public List<TTNode> queryByttId(int ttId) {
		String sql = "SELECT * FROM tb_node WHERE node_ttid=?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<TTNode>(TTNode.class), ttId);
	}

}
