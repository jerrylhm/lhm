package com.creator.db.permission;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;


public class PermissionDaoImp extends JdbcDaoSupport implements PermissionDao{

	@Override
	public List<Permission> queryByUserId(int userId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT urnode_id,urnode_userid,urnode_nodeid,urnode_iscreator,urnode_treeid "
		           + "FROM tb_permission WHERE urnode_userid = ?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Permission.class), userId);
	}

	@Override
	public List<Permission> queryCreatorsPermission(int userId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT urnode_id,urnode_userid,urnode_nodeid,urnode_iscreator,urnode_treeid "
		           + "FROM tb_permission WHERE urnode_userid = ? AND urnode_iscreator = 1";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Permission.class), userId);
	}

	@Override
	public Integer addPermission(Permission permission) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "INSERT INTO tb_permission(urnode_userid,urnode_nodeid,urnode_iscreator,urnode_treeid) "
		           + "VALUES(?,?,?,?)";  
		return jdbcTemplate.update(sql, permission.getUrnode_userid(), permission.getUrnode_nodeid(), permission.getUrnode_iscreator(), permission.getUrnode_treeid());
	}

	@Override
	public List<Permission> queryAllPermission() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT urnode_id,urnode_userid,urnode_nodeid,urnode_iscreator,urnode_treeid "
		           + "FROM tb_permission";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Permission.class));
	}

	@Override
	public int updatePermission(Permission permission) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE tb_permission SET urnode_userid=?,urnode_nodeid=?,urnode_iscreator=?,urnode_treeid=? WHERE urnode_id=?";
		return jdbcTemplate.update(sql, permission.getUrnode_userid(),permission.getUrnode_nodeid(),permission.getUrnode_iscreator(), permission.getUrnode_treeid(), permission.getUrnode_id());
	}

	@Override
	public List<Permission> queryById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT urnode_id,urnode_userid,urnode_nodeid,urnode_iscreator,urnode_treeid "
		           + "FROM tb_permission WHERE urnode_id = ?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Permission.class), id);
	}

	@Override
	public int deletePermission(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_permission WHERE urnode_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public List<PermissionDto> queryByPage(PermissionDto permissionDto) {
		JdbcTemplate jdbcTemplateObject = this.getJdbcTemplate();
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT urnode_id,urnode_userid,urnode_nodeid,urnode_iscreator,urnode_treeid,node_name FROM tb_permission,tb_tree ");
        sql.append("WHERE urnode_userid=? AND urnode_treeid=node_id ");
        if(permissionDto.getLike()!=null&&!permissionDto.getLike().equals("")){
        	//搜索关键
        	String like = permissionDto.getLike();
        	sql.append("AND CONCAT(node_name) Like ? ");
        	 String countSql = "select count(urnode_id) from tb_permission,tb_tree "
 		            + "where urnode_userid=? AND urnode_treeid=node_id "
    		        + "AND CONCAT(node_name) Like ? ";
        	 permissionDto.getPage().setTotalNumber(jdbcTemplateObject.queryForObject(countSql,new Object[]{permissionDto.getUrnode_userid(),"%"+permissionDto.getLike()+"%"},int.class));
        }else{
        String countSql = "select count(urnode_id) from tb_permission,tb_tree "
    		            + "where urnode_userid=? AND urnode_treeid=node_id";
        permissionDto.getPage().setTotalNumber(jdbcTemplateObject.queryForObject(countSql,new Object[]{permissionDto.getUrnode_userid()},int.class));
        }
        int currentPage = permissionDto.getPage().getCurrentPage();
		int pageNumber = permissionDto.getPage().getPageNumber();
		sql.append("ORDER BY urnode_id DESC ");
		sql.append("limit "+(currentPage-1)*pageNumber+","+pageNumber);
        RowMapper<PermissionDto> rm = new BeanPropertyRowMapper<>(PermissionDto.class);
        if(permissionDto.getLike()!=null&&!permissionDto.getLike().equals("")){
            return jdbcTemplateObject.query(sql.toString(),new Object[]{permissionDto.getUrnode_userid(), "%"+permissionDto.getLike()+"%"},rm);	
            }else{
		    return jdbcTemplateObject.query(sql.toString(), rm, permissionDto.getUrnode_userid());
            }
	}

	@Override
	public List<Permission> queryByTreeId(int treeid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT urnode_id,urnode_userid,urnode_nodeid,urnode_iscreator,urnode_treeid "
		           + "FROM tb_permission WHERE urnode_treeid = ?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Permission.class), treeid);
	}

	@Override
	public List<Permission> queryByUserIdAndtreeId(int userId, int treeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT urnode_id,urnode_userid,urnode_nodeid,urnode_iscreator,urnode_treeid "
		           + "FROM tb_permission WHERE urnode_treeid = ? AND urnode_userid=?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Permission.class), treeId,userId);
	}
	
	/*
	 * 
     * 根据用户id和树节点删除对应的权限
	 */
	@Override
	public void deletePermissionByUserid(int userid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "DELETE FROM tb_permission WHERE urnode_userid=? OR urnode_treeid IN (SELECT node_treeid FROM tb_tree WHERE node_userid=? GROUP BY node_treeid)";
		jdbcTemplate.update(SQL, userid,userid);
	}
}
