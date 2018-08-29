package com.creator.db.studenttable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.creator.db.timetable.TimeTable;

@Repository("studentTableDao")
public class StudentTableDaoImpl implements StudentTableDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<StudentTable> query() {
		String sql = "SELECT * FROM tb_studenttable";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<StudentTable>(StudentTable.class));
	}

	@Override
	public StudentTable findById(int id) {
		String sql = "SELECT * FROM tb_studenttable WHERE st_id=?";
		List<StudentTable> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<StudentTable>(StudentTable.class),id);
		if(ls.size()>0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(StudentTable obj) {
		String sql = "UPDATE tb_studenttable SET st_weeks=?,st_name=?,st_orgid=?,st_teacherid=?,st_num=?,st_addid=?,st_ttid WHERE st_id=?";
		return jdbcTemplate.update(sql, obj.getSt_weeks(), obj.getSt_name(), obj.getSt_orgid(), obj.getSt_teacherid(), obj.getSt_num(), obj.getSt_addid(), obj.getSt_ttid(), obj.getSt_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_studenttable WHERE st_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(StudentTable obj) {
		
		final StudentTable t = obj;
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		final String sql = "INSERT INTO tb_studenttable(st_weeks,st_name,st_orgid,st_teacherid,st_num,st_addid,st_ttid) VALUES(?,?,?,?,?,?,?)";
		
		//通过接口回调和keyHolder获取新添加的课表的id
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setString(1, t.getSt_weeks());
				ps.setString(2, t.getSt_name());
				ps.setString(3, t.getSt_orgid());
				ps.setInt(4, t.getSt_teacherid());
				ps.setInt(5, t.getSt_num());
				ps.setInt(6, t.getSt_addid());
				ps.setInt(7, t.getSt_ttid());
				return ps;
			}
		},keyHolder);
		//返回课表id
		return keyHolder.getKey().intValue();
	}

	@Override
	public List<StudentTableDto> queryByParam(Integer week, Integer ttid, Integer addid, Integer orgId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tb_studenttable WHERE st_ttid = ?");
		sql.append(" AND FIND_IN_SET('" + week + "', st_weeks)");
		if(addid != null && addid != 0) {
			sql.append(" AND st_addid = " + addid);
		}
		if(orgId != null && orgId != 0) {
			sql.append(" AND FIND_IN_SET('" + orgId + "', st_orgid)");
		}
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<StudentTableDto>(StudentTableDto.class), ttid);
	}
	
}
