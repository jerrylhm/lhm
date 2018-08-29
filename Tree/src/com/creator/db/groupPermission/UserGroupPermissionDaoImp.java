package com.creator.db.groupPermission;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class UserGroupPermissionDaoImp extends JdbcDaoSupport implements UserGroupPermissionDao {

	/*
	 * 查询当前节点的权限
	 */
	@Override
	public List<Map<String, Object>> queryNodePermission(int id){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_group_permission WHERE gp_nodeid="+id;
		List<Map<String, Object>> PermissionList = jdbcTemplate.queryForList(SQL);
		return PermissionList;
	}
	
	/*
	 * 更新某一节点权限
	 */
	@Override
	public String updatePermissionByNodeAndGroup(Integer nodeid,Integer groupid,String idAll,Integer treeid){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_group_permission WHERE gp_usergroupid="+groupid+" AND gp_nodeid="+nodeid;
		List<Map<String, Object>> PermissionList = jdbcTemplate.queryForList(SQL);
		if(PermissionList.size() > 0){
			SQL = "UPDATE tb_group_permission SET gp_perid=\""+idAll+"\" WHERE gp_usergroupid="+groupid+" AND gp_nodeid="+nodeid;
		}else{
			SQL = "INSERT INTO tb_group_permission(gp_usergroupid,gp_treeid,gp_nodeid,gp_perid) VALUES ("+groupid+","+treeid+","+nodeid+",\""+idAll+"\")";
		}
		jdbcTemplate.update(SQL);
		return "true";
	}

	@Override
	public List<UserGroupPermission> queryByGroupId(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT * FROM tb_group_permission WHERE gp_usergroupid = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserGroupPermission>(UserGroupPermission.class), id);
	}

	@Override
	public int add(UserGroupPermission ugp) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "INSERT INTO tb_group_permission(gp_usergroupid,gp_treeid,gp_nodeid,gp_perid) VALUES(?,?,?,?)";
		return jdbcTemplate.update(sql, ugp.getGp_usergroupid(), ugp.getGp_treeid(), ugp.getGp_nodeid(), ugp.getGp_perid());
	}

	@Override
	public int deleteByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_group_permission WHERE gp_nodeid = ?";
		return jdbcTemplate.update(sql, nodeId);
	}
	

	/*
	 * 根据树根节点id、节点id和用户组id查询权限
	 */
	@Override
	public List<UserGroupPermission> queryByTreeIdAndGroupId(int treeId,int nodeId,
			int groupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_group_permission WHERE gp_usergroupid=? AND gp_treeid=? AND gp_nodeid=?";
		List<UserGroupPermission> groupPerList = jdbcTemplate.query(SQL, new Object[]{groupId,treeId,nodeId}, new UserGroupPermissionMapper());
		return groupPerList;
	}

	
	/*
	 * 根据节点id和用户组id查询权限
	 */
	@Override
	public List<UserGroupPermission> queryByNodeIdAndGroupId(int nodeId,
			int groupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_group_permission WHERE gp_usergroupid=?  AND gp_nodeid=?";
		List<UserGroupPermission> groupPerList = jdbcTemplate.query(SQL, new Object[]{groupId,nodeId}, new UserGroupPermissionMapper());
		return groupPerList;
	}

	
	/*
	 * 根据节点id和用户组id、权限类型查询权限
	 */
	@Override
	public List<UserGroupPermission> queryByNodeIdAndPermissId(int nodeId,int permissId,
			int groupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_group_permission WHERE gp_usergroupid=? AND gp_nodeid=? AND FIND_IN_SET(?,gp_perid)";
		List<UserGroupPermission> groupPerList = jdbcTemplate.query(SQL, new Object[]{groupId,nodeId,permissId}, new UserGroupPermissionMapper());
		return groupPerList;
	}

	
	/*
	 * 根据节点id、用户组id和权限类型查询是否拥有该权限
	 */
	@Override
	public boolean hasPermissionByGroupIdAndNodeId(int groupId, int nodeId,int permissionId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(gp_id) FROM tb_group_permission WHERE gp_usergroupid=? AND gp_nodeid=? AND FIND_IN_SET(?,gp_perid)";
		int count = jdbcTemplate.queryForObject(SQL, Integer.class, groupId,nodeId,permissionId);
		if(count <= 0) {
			return false;
		}
		return true;
	}

	
	/*
	 * 根据用户组id删除权限
	 */
	@Override
	public void deletePermissionByGroupId(int groupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "DELETE FROM tb_group_permission WHERE gp_usergroupid=?";
		jdbcTemplate.update(SQL,groupId);
	}
}
