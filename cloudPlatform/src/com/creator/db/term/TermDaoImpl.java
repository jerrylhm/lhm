package com.creator.db.term;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.creator.common.db.Page;

@Repository("termDao")
public class TermDaoImpl implements TermDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Term> query() {
		String sql = "SELECT * FROM tb_term";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Term>(Term.class));
	}

	@Override
	public Term findById(int id) {
		String sql = "SELECT * FROM tb_term WHERE term_id=?";
		List<Term> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Term>(Term.class), id);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(Term obj) {
		String sql = "UPDATE tb_term set term_name=?,term_start=?,term_end=? WHERE term_id=?";
		return jdbcTemplate.update(sql, obj.getTerm_name(), obj.getTerm_start(), obj.getTerm_end(), obj.getTerm_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_term WHERE term_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(Term obj) {
		String sql = "INSERT INTO tb_term(term_name, term_start, term_end) values(?, ?, ?)";
		return jdbcTemplate.update(sql, obj.getTerm_name(), obj.getTerm_start(), obj.getTerm_end());
	}

	@Override
	public List<TermDto> queryByPage(Page page, String like) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tb_term ");
        sql.append("where 1=1 ");
        if(like!=null&&!like.equals("")){
        	sql.append("AND term_name Like ? ");
        	String countSql = "select count(term_id) from tb_term "
    		        + "where 1=1 "
    		        + "AND term_name Like ? ";
            page.setTotalNumber(jdbcTemplate.queryForObject(countSql,new Object[]{"%"+like+"%"},int.class));
        }else{
        	String countSql = "select count(term_id) from tb_term "
    		        + "where 1=1 ";
            page.setTotalNumber(jdbcTemplate.queryForObject(countSql,new Object[]{},int.class));
        }
        
        int currentPage = page.getCurrentPage();
		int pageNumber = page.getPageNumber();
		sql.append("ORDER BY term_id DESC ");
		sql.append("limit "+(currentPage-1)*pageNumber+","+pageNumber);
        RowMapper<TermDto> rm = new BeanPropertyRowMapper<>(TermDto.class);
        if(like!=null&&!like.equals("")){
        	return jdbcTemplate.query(sql.toString(),new Object[]{"%"+like+"%"},rm);	
        }else{
        	return jdbcTemplate.query(sql.toString(),rm);
        }
	}
	
}
