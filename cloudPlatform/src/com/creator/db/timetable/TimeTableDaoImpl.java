package com.creator.db.timetable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.creator.common.db.Page;


@Repository("timeTableDao")
public class TimeTableDaoImpl implements TimeTableDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<TimeTable> query() {
		String sql = "SELECT * FROM tb_timetable";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<TimeTable>(TimeTable.class));
	}

	@Override
	public TimeTable findById(int id) {
		String sql = "SELECT * FROM tb_timetable WHERE tt_id=?";
		List<TimeTable> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<TimeTable>(TimeTable.class),id);
		if(ls.size()>0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(TimeTable obj) {
		String sql = "UPDATE tb_timetable SET tt_name=?,tt_num=?,tt_termid=? WHERE tt_id=?";
		return jdbcTemplate.update(sql, obj.getTt_name(),obj.getTt_num(),obj.getTt_termid(),obj.getTt_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_timetable WHERE tt_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(TimeTable obj) {
		
		final TimeTable t = obj;
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		final String sql = "INSERT INTO tb_timetable(tt_name,tt_num,tt_termid) VALUES(?,?,?)";
		
		//通过接口回调和keyHolder获取新添加的课表的id
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setString(1, t.getTt_name());
				ps.setInt(2, t.getTt_num());
				ps.setInt(3, t.getTt_termid());
				return ps;
			}
		},keyHolder);
		//返回课表id
		return keyHolder.getKey().intValue();
	}

	@Override
	public List<TimeTableDto> queryByPage(Page page, String like) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tb_timetable ");
        sql.append("where 1=1 ");
        if(like!=null&&!like.equals("")){
        	sql.append("AND tt_name Like ? ");
        	String countSql = "select count(tt_id) from tb_timetable "
    		        + "where 1=1 "
    		        + "AND tt_name Like ? ";
            page.setTotalNumber(jdbcTemplate.queryForObject(countSql,new Object[]{"%"+like+"%"},int.class));
        }else{
        	String countSql = "select count(tt_id) from tb_timetable "
    		        + "where 1=1 ";
            page.setTotalNumber(jdbcTemplate.queryForObject(countSql,new Object[]{},int.class));
        }
        
        int currentPage = page.getCurrentPage();
		int pageNumber = page.getPageNumber();
		sql.append("ORDER BY tt_updatedate DESC ");
		sql.append("limit "+(currentPage-1)*pageNumber+","+pageNumber);
        RowMapper<TimeTableDto> rm = new BeanPropertyRowMapper<>(TimeTableDto.class);
        if(like!=null&&!like.equals("")){
        	return jdbcTemplate.query(sql.toString(),new Object[]{"%"+like+"%"},rm);	
        }else{
        	return jdbcTemplate.query(sql.toString(),rm);
        }
	}
	
	/*
	 * 获取课表列表
	 */
	@Override
	public List<TimeTable> listTimeTable() {
		String SQL = "SELECT * FROM tb_timetable";
		List<TimeTable> list = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<TimeTable>(TimeTable.class));
		return list;
	}

	@Override
	public int resetTermIdByTermId(int termId) {
		String sql = "UPDATE tb_timetable SET tt_termid=0 WHERE tt_termid=?";
		return jdbcTemplate.update(sql, termId);
	}
	
}
