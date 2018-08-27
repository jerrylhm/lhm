package com.creator.db.index;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository("indexDao")
public class IndexDaoImpl implements IndexDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Index> query() {
		String sql = "SELECT * FROM tb_index";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Index>(Index.class));
	}

	@Override
	public Index findById(int id) {
		String sql = "SELECT * FROM tb_index WHERE index_id=?";
		List<Index> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Index>(Index.class), id);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(Index obj) {
		String sql = "UPDATE tb_index SET index_day=?,index_index=?,index_stid=? WHERE index_id=?";
		return jdbcTemplate.update(sql, obj.getIndex_day(), obj.getIndex_index(), obj.getIndex_stid(), obj.getIndex_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_index WHERE index_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(Index obj) {
		String sql = "INSERT INTO tb_index(index_day,index_index,index_stid) values(?,?,?)";
		return jdbcTemplate.update(sql, obj.getIndex_day(), obj.getIndex_index(), obj.getIndex_stid());
	}

	@Override
	public List<Index> queryByStId(int stid) {
		String sql = "SELECT * FROM tb_index WHERE index_stid = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Index>(Index.class), stid);
	}

	@Override
	public List<Index> queryExist(Integer ttId, Integer week, Integer addId, Integer index, Integer day) {
		String sql = "SELECT ind.* FROM tb_index ind, tb_studenttable st WHERE st.st_id = ind.index_stid" 
	                 + " AND FIND_IN_SET(?, st.st_weeks) AND st.st_addid = ? AND ind.index_index = ?"
				     + " AND ind.index_day = ? AND st.st_ttid = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Index>(Index.class), week, addId, index, day, ttId);
	}

	@Override
	public int deleteByStId(int stId) {
		String sql = "DELETE FROM tb_index WHERE index_stid = ?";
		return jdbcTemplate.update(sql, stId);
	}
	
}
