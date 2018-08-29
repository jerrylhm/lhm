package com.creator.db.template;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class TemplateDaoImpl extends JdbcDaoSupport implements TemplateDao {
	private static final String ROW_SELECT = "tp_id,tp_name,tp_data,tp_treeid";
	
	@Override
	public List<Template> query() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT tp_id, tp_name, tp_data, tp_treeid from tb_template";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Template>(Template.class));
	}

	@Override
	public List<Template> findByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT tp_id, tp_name, tp_data, tp_treeid from tb_template where tp_nodeid = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Template>(Template.class), nodeId);
	}

	@Override
	public int add(Template template) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "INSERT INTO tb_template(tp_name,tp_data,tp_treeid) VALUES(?,?,?)";
		return jdbcTemplate.update(sql, template.getTp_name(), template.getTp_data(), template.getTp_treeid());
	}

	@Override
	public int update(Template template) {
		JdbcTemplate jdbcTemplate  = this.getJdbcTemplate();
		String sql = "UPDATE tb_template SET tp_name=?, tp_data = ?, tp_treeid = ? WHERE tp_id=?";
		return jdbcTemplate.update(sql, template.getTp_name(), template.getTp_data(), template.getTp_treeid(), template.getTp_id());
	}

	@Override
	public int delete(int id) {
		JdbcTemplate jdbcTemplate  = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_template WHERE tp_id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public List<Template> findById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT tp_id, tp_name, tp_data, tp_treeid from tb_template where tp_id = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Template>(Template.class), id);
	}

	@Override
	public int updateTemplateName(Integer id, String name) {
		JdbcTemplate jdbcTemplate  = this.getJdbcTemplate();
		String sql = "UPDATE tb_template SET tp_name=? WHERE tp_id=?";
		return jdbcTemplate.update(sql, name, id);
	}

	@Override
	public List<Template> findByTreeId(int treeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT tp_id, tp_name, tp_data, tp_treeid from tb_template where tp_treeid = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Template>(Template.class), treeId);
	}

	@Override
	public List<TemplateDto> queryByPage(TemplateDto templateDto) {
		JdbcTemplate jdbcTemplateObject = this.getJdbcTemplate();
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT tp_id, tp_name, tp_data, tp_treeid ");
        String fromAndWhere = "FROM tb_template WHERE 1=1 AND FIND_IN_SET(tp_treeid,'" + templateDto.getTreesStr() + "') ";

        sql.append(fromAndWhere);
        if(templateDto.getLike()!=null){
        	//搜索关键
        	String like = templateDto.getLike();
        	sql.append("AND tp_name Like ? ");
        	String countSql = "select count(tp_id) " + fromAndWhere;
        			countSql = countSql + " AND tp_name Like ? ";
        			templateDto.getPage().setTotalNumber(jdbcTemplateObject.queryForObject(countSql,new Object[]{"%"+templateDto.getLike()+"%"},int.class));
        }else{
        	String countSql = "select count(tp_id) "+fromAndWhere;
        	templateDto.getPage().setTotalNumber(jdbcTemplateObject.queryForObject(countSql,new Object[]{},int.class));
        }    
        
        int currentPage = templateDto.getPage().getCurrentPage();
		int pageNumber = templateDto.getPage().getPageNumber();
		sql.append("ORDER BY tp_id DESC ");
		sql.append("limit "+(currentPage-1)*pageNumber+","+pageNumber);
        RowMapper<TemplateDto> rm = new BeanPropertyRowMapper<>(TemplateDto.class);
        if(templateDto.getLike()!=null){
        return jdbcTemplateObject.query(sql.toString(),new Object[]{"%"+templateDto.getLike()+"%"},rm);	
        }else{
		return jdbcTemplateObject.query(sql.toString(),new Object[]{},rm);
        }
	}
	
	
	/*
	 * 根据节点id查询模板
	 */
	@Override
	public List<Map<String, Object>> queryTemplateByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_template WHERE tp_treeid=?";
		List<Map<String,Object>> tpList = jdbcTemplate.queryForList(SQL,nodeId); 
		return tpList;
	}

	
	/*
	 * 根据模板id查询模板
	 */
	@Override
	public List<Map<String, Object>> queryTemplateByTpId(int tpid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_template WHERE tp_id=?";
		List<Map<String,Object>> tpList = jdbcTemplate.queryForList(SQL,tpid);
		return tpList;
	}

	@Override
	public int deleteByTreeId(int treeId) {
		JdbcTemplate jdbcTemplate  = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_template WHERE tp_treeid = ?";
		return jdbcTemplate.update(sql, treeId);
	}
	
	/*
	 * 根据节点id查询模板
	 */
	@Override
	public List<Map<String, Object>> queryByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_template,tb_node_attr WHERE attr_type=1 AND attr_nodeid=? AND tp_id=attr_value";
		List<Map<String,Object>> tpList = jdbcTemplate.queryForList(SQL,nodeId);
		return tpList;
	}


}
