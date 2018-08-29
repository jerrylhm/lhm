package com.creator.db.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.creator.db.permission.PermissionDto;
import com.creator.db.scene.SceneDto;

public class UserGroupDaoImp extends JdbcDaoSupport implements UserGroupDao {
	private static final String ROW_SELECT = "urg_id,urg_name,urg_userid,urg_treeid";
	
	/*
	 * 根据当前用户ID，用户身份类型，查询他的用户组,并分页
	 */
	@Override
	public List<Map<String, Object>> queryUserGroupByUserID(Object userid,Integer userType,int index,int num){
		
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL;
		if(userType == 1){
			SQL = "SELECT * FROM tb_usergroup,tb_tree,tb_user WHERE urg_treeid=node_id AND ur_id=urg_userid ORDER BY urg_id DESC LIMIT "+(index-1)*num+","+num;
		}else{
			SQL = "SELECT * FROM tb_usergroup,tb_tree WHERE urg_treeid=node_id AND urg_userid="+userid+" ORDER BY urg_id DESC LIMIT "+(index-1)*num+","+num;
		}
		
		List<Map<String, Object>> UserGroupList = jdbcTemplate.queryForList(SQL);
		return UserGroupList;
		
	}
	
	/*
	 * 根据当前用户ID，查询他的用户组,统计总条数
	 */
	@Override
	public int countUserGroupByUserID(Object userid,Integer userType){
		
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL;
		if(userType == 1){
			SQL = "SELECT COUNT(*) FROM tb_usergroup,tb_tree,tb_user WHERE urg_treeid=node_id AND ur_id=urg_userid";
		}else{
			SQL = "SELECT COUNT(*) FROM tb_usergroup,tb_tree WHERE urg_treeid=node_id AND urg_userid="+userid;
		}
		int CountNum = jdbcTemplate.queryForObject(SQL, int.class);
		return CountNum;
		
	}
	
	/*
	 * 根据用户id和类型查询树
	 */
	@Override
	public List<Map<String, Object>> queryTreeByUserIdAndType(Object userid,Integer userType){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL;
		if(userType == 1){
			SQL = "SELECT * FROM tb_tree WHERE node_pid=0";
		}else{
			SQL = "SELECT * FROM tb_tree WHERE node_pid=0 AND node_userid="+userid;
		}
		List<Map<String, Object>> TreeList = jdbcTemplate.queryForList(SQL);
		return TreeList;
	}
	
	/*
	 * 增加用户组
	 */
	@Override
	public String addUserGroup(UserGroup userGroup){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "INSERT INTO tb_usergroup(urg_name,urg_userid,urg_treeid) VALUES (?,?,?)";
		jdbcTemplate.update(SQL, userGroup.getUrg_name(),userGroup.getUrg_userid(),userGroup.getUrg_treeid());
		return "true";
	}
	
	/*
	 * 删除用户组
	 */
	@Override
	public String deleteUserGroup(Object id){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "DELETE FROM tb_usergroup WHERE urg_id="+id;
		jdbcTemplate.update(SQL);
		return "true";
	}
	
	/*
	 * 修改用户组
	 */
	@Override
	public String updateUserGroup(Integer id,String name,Integer treeid){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "UPDATE tb_usergroup SET urg_name=\""+name+"\",urg_treeid="+treeid+" WHERE urg_id="+id;
		jdbcTemplate.update(SQL);
		return "true";
	}
	
	/*
	 * 根据用户组id查询用户组
	 */
	@Override
	public List<Map<String, Object>> queryGroupById(int groupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_usergroup WHERE urg_id=?";
		List<Map<String,Object>> groupList = jdbcTemplate.queryForList(SQL,groupId);
		return groupList;
	}

	/*
	 * 根据树id查询用户组
	 */
	@Override
	public List<Map<String, Object>> queryGroupByTreeId(int treeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_usergroup WHERE urg_treeid=?";
		List<Map<String,Object>> groupList = jdbcTemplate.queryForList(SQL,treeId);
		return groupList;
	}

	/*
	 * 根据用户组id查询用户组是否存在
	 */
	@Override
	public boolean isExistGroupById(int groupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(urg_id) FROM tb_usergroup WHERE urg_id=?";
		int count = jdbcTemplate.queryForObject(SQL, Integer.class, groupId);
		if(count <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public List<UserGroupDto> findByUserIdAndPage(UserGroupDto userGroupDto) {
		JdbcTemplate jdbcTemplateObject = this.getJdbcTemplate();
    	//搜索关键
    	String like = userGroupDto.getLike();
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT urg_id,urg_name,urg_treeid,urg_userid,node_id,node_name ");
        String fromAndWhere = "FROM tb_usergroup,tb_tree WHERE urg_treeid=node_id AND urg_userid = ? ";
        sql.append(fromAndWhere);
        if(userGroupDto.getLike()!=null){
        	sql.append("AND node_name Like ? ");
        	String countSql = "select count(urg_id) " + fromAndWhere;
			countSql = countSql + " AND node_name Like ? ";
			userGroupDto.getPage().setTotalNumber(jdbcTemplateObject.queryForObject(countSql,new Object[]{userGroupDto.getUrg_userid(),"%"+like+"%"},int.class));
        }else{
        	String countSql = "select count(urg_id) "+fromAndWhere;
        	userGroupDto.getPage().setTotalNumber(jdbcTemplateObject.queryForObject(countSql,new Object[]{userGroupDto.getUrg_userid()},int.class));
        }    
        
        int currentPage = userGroupDto.getPage().getCurrentPage();
		int pageNumber = userGroupDto.getPage().getPageNumber();
		sql.append("ORDER BY urg_id DESC ");
		sql.append("limit "+(currentPage-1)*pageNumber+","+pageNumber);
        RowMapper<UserGroupDto> rm = new BeanPropertyRowMapper<>(UserGroupDto.class);
        if(userGroupDto.getLike()!=null){
        return jdbcTemplateObject.query(sql.toString(),new Object[]{userGroupDto.getUrg_userid(),"%"+like+"%"},rm);	
        }else{
		return jdbcTemplateObject.query(sql.toString(),new Object[]{userGroupDto.getUrg_userid()},rm);
        }
	}

	@Override
	public UserGroup findByTreeIdAndUserId(int treeId, int userId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT * FROM tb_usergroup WHERE urg_treeid = ? AND urg_userid = ?";
		List<UserGroup> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserGroup>(UserGroup.class), treeId, userId);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public Integer deleteByTreeId(int treeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "DELETE FROM tb_usergroup WHERE urg_treeid = ?";
		return jdbcTemplate.update(SQL, treeId);
	}
	
	/*
	 * 添加用户组，并返回添加后的用户组id
	 */
	@Override
	public int add(final UserGroup userGroup) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		final String SQL = "INSERT INTO tb_usergroup(urg_name,urg_userid,urg_treeid) VALUES(?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement statement = conn.prepareStatement(SQL,new String[]{"urg_name","urg_userid","urg_treeid"});
				statement.setString(1, userGroup.getUrg_name());
				statement.setInt(2, userGroup.getUrg_userid());
				statement.setInt(3, userGroup.getUrg_treeid());
				return statement;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();
	}

	
	/*
	 * 根据树根节点和用户组名称查询是否存在
	 */
	@Override
	public boolean isExistByName(String name,int treeid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(urg_id) FROM tb_usergroup WHERE urg_name=? AND urg_treeid=?";
		int count = jdbcTemplate.queryForObject(SQL, new Object[]{name,treeid}, Integer.class);
		if(count <= 0) {
			return false;
		}
		return true;
	}

	
	/*
	 * 根据用户组id删除用户组
	 */
	@Override
	public void deleteUserGroupById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "DELETE FROM tb_usergroup WHERE urg_id=?";
		jdbcTemplate.update(SQL, id);
	}

	@Override
	public List<UserGroup> queryGroupListById(int groupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_usergroup WHERE urg_id=?";
		List<UserGroup> groupList = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<UserGroup>(UserGroup.class) ,groupId);
		return groupList;
	}
	
}
