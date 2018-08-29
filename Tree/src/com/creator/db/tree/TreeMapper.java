package com.creator.db.tree;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TreeMapper implements RowMapper<Tree>{

	@Override
	public Tree mapRow(ResultSet rs, int rowNum) throws SQLException {
		Tree tree = new Tree();
		tree.setNode_id(rs.getInt("node_id"));
		tree.setNode_name(rs.getString("node_name"));
		tree.setNode_pid(rs.getInt("node_pid"));
		tree.setNode_userid(rs.getInt("node_userid"));
		tree.setNode_treeid(rs.getInt("node_treeid"));
		tree.setNode_url(rs.getString("node_url"));
		tree.setNode_type(rs.getInt("node_type"));
		tree.setNode_title(rs.getString("node_title"));
		return tree;
	}

}
