package com.creator.db.subject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.creator.common.db.Page;

@Repository("subjectDao")
public class SubjectDaoImpl implements SubjectDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Subject> query() {
		String sql = "SELECT * FROM tb_subject";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Subject>(Subject.class));
	}

	@Override
	public Subject findById(int id) {
		String sql = "SELECT * FROM tb_subject WHERE sub_id=?";
		List<Subject> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Subject>(Subject.class),id);
		if(ls.size()>0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(Subject obj) {
		String sql = "UPDATE tb_subject SET sub_name=?,sub_orgid=?,sub_type=? WHERE sub_id=?";
		return jdbcTemplate.update(sql, obj.getSub_name(), obj.getSub_orgid(), obj.getSub_type(), obj.getSub_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_subject WHERE sub_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(Subject obj) {
		String sql = "INSERT INTO tb_subject(sub_name,sub_orgid,sub_type) VALUES(?,?,?)";
		return jdbcTemplate.update(sql, obj.getSub_name(), obj.getSub_orgid(), obj.getSub_type());
	}

	@Override
	public List<SubjectDto> queryByPage(Page page, String like) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tb_subject ");
        sql.append("where 1=1 ");
        if(like!=null&&!like.equals("")){
        	sql.append("AND sub_name Like ? ");
        	String countSql = "select count(sub_id) from tb_subject "
    		        + "where 1=1 "
    		        + "AND sub_name Like ? ";
            page.setTotalNumber(jdbcTemplate.queryForObject(countSql,new Object[]{"%"+like+"%"},int.class));
        }else{
        	String countSql = "select count(sub_id) from tb_subject "
    		        + "where 1=1 ";
            page.setTotalNumber(jdbcTemplate.queryForObject(countSql,new Object[]{},int.class));
        }
        
        int currentPage = page.getCurrentPage();
		int pageNumber = page.getPageNumber();
		sql.append("ORDER BY sub_id DESC ");
		sql.append("limit "+(currentPage-1)*pageNumber+","+pageNumber);
        RowMapper<SubjectDto> rm = new BeanPropertyRowMapper<>(SubjectDto.class);
        if(like!=null&&!like.equals("")){
        	return jdbcTemplate.query(sql.toString(),new Object[]{"%"+like+"%"},rm);	
        }else{
        	return jdbcTemplate.query(sql.toString(),rm);
        }
	}

	@Override
	public List<SubjectDto> queryByPage(Page page, String like, Integer orgId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tb_subject ");
        sql.append("where 1=1 AND FIND_IN_SET( '" + orgId + "' , sub_orgid)");
        if(like!=null&&!like.equals("")){
        	sql.append("AND sub_name Like ? ");
        	String countSql = "select count(sub_id) from tb_subject "
    		        + "where 1=1 AND FIND_IN_SET( '" + orgId + "' , sub_orgid)"
    		        + "AND sub_name Like ? ";
            page.setTotalNumber(jdbcTemplate.queryForObject(countSql,new Object[]{"%"+like+"%"},int.class));
        }else{
        	String countSql = "select count(sub_id) from tb_subject "
    		        + "where 1=1 AND FIND_IN_SET( '" + orgId + "' , sub_orgid)";
            page.setTotalNumber(jdbcTemplate.queryForObject(countSql,new Object[]{},int.class));
        }
        
        int currentPage = page.getCurrentPage();
		int pageNumber = page.getPageNumber();
		sql.append("ORDER BY sub_id DESC ");
		sql.append("limit "+(currentPage-1)*pageNumber+","+pageNumber);
        RowMapper<SubjectDto> rm = new BeanPropertyRowMapper<>(SubjectDto.class);
        if(like!=null&&!like.equals("")){
        	return jdbcTemplate.query(sql.toString(),new Object[]{"%"+like+"%"},rm);	
        }else{
        	return jdbcTemplate.query(sql.toString(),rm);
        }
	}

}
