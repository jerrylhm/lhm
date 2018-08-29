package com.creator.db.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class UserDaoImp extends JdbcDaoSupport implements UserDao{
	private static final String ROW_SELECT = "ur_id,ur_username,ur_password,ur_type,ur_datetime,ur_group,ur_treeid";
	/*
	 * 查询所有用户
	 */
	@Override
	public List<User> queryAllUser() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_user";
		List<User> userList = jdbcTemplate.query(SQL, new UserMapper());
		return userList;
	}
	/*
	 * 根据类型验证账号密码是否存在
	 */
	@Override
	public boolean isExist(String username, String password) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(ur_id) FROM tb_user WHERE ur_username=? AND ur_password=? ";
		int count = jdbcTemplate.queryForObject(SQL, new Object[]{username,password}, Integer.class);
		if(count == 0) {
			return false;
		}
		return true;
	}
	/*
	 * 根据用户账号、密码、类型查询用户
	 */
	@Override
	public List<User> queryUserByUsernameAndPsw(String username,
			String password) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_user WHERE ur_username=? AND ur_password=?";
		List<User> userList = jdbcTemplate.query(SQL, new Object[]{username,password}, new UserMapper());
		return userList;
	}

	@Override
	public List<User> queryById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_user WHERE ur_id=?";
		List<User> userList = jdbcTemplate.query(SQL, new UserMapper(), id);
		return userList;
	}
	
	/*
	 * 根据页数、用户类型、搜索值分页查询用户
	 */
	@Override
	public List<Map<String, Object>> queryAllUsersByCondition(int index,
			int num, String searchValue) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_user ";
		if(searchValue != null && !("").equals(searchValue)) {
			SQL += " WHERE ur_username LIKE ?";
		}
		SQL += " ORDER BY ur_type DESC LIMIT ?,?";
		List<Map<String,Object>> userList = null;
		if(searchValue != null && !("").equals(searchValue)) {
			userList = jdbcTemplate.queryForList(SQL, "%"+searchValue+"%",(index-1)*num,num);
		}else {
			userList = jdbcTemplate.queryForList(SQL,(index-1)*num,num);
		}
		return userList;
	}
	
	/*
	 * 根据页数、用户类型、搜索值查询用户总数
	 */
	@Override
	public int countUserByCondition(String searchValue) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(ur_id) FROM tb_user ";
		if(searchValue != null && !("").equals(searchValue)) {
			SQL += " WHERE ur_username LIKE ?";
		}
		
		int count = 0;
		if(searchValue != null && !("").equals(searchValue)) {
			count = jdbcTemplate.queryForObject(SQL, new Object[]{"%"+searchValue+"%"}, Integer.class);
		}else {
			count = jdbcTemplate.queryForObject(SQL, Integer.class);
		}
		return count;
	}
	
	/*
	 * 查询是否存在用户名
	 */
	@Override
	public boolean isExistUsername(String username) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(ur_id) FROM tb_user WHERE ur_username=?";
		int count = jdbcTemplate.queryForObject(SQL, new Object[]{username},Integer.class);
		if(count <= 0) {
			return false;
		}
		return true;
	}
	
	/*
	 * 添加用户
	 */
	@Override
	public void addUser(User user) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "INSERT INTO tb_user(ur_username,ur_password,ur_type,ur_datetime) VALUES(?,?,?,?)";
		jdbcTemplate.update(SQL,user.getUr_username(),user.getUr_password(),user.getUr_type(),user.getUr_datetime());
		
		
	}
	
	/*
	 * 根据用户id查询用户
	 */
	@Override
	public List<User> queryUserById(int ur_id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT * FROM tb_user WHERE ur_id=?";
		List<User> userList = jdbcTemplate.query(SQL, new Object[]{ur_id}, new UserMapper());
		return userList;
	}
	
	/*
	 * 修改用户信息
	 */
	@Override
	public void updateUserById(int ur_id,User user) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "UPDATE tb_user SET ur_username=?,ur_password=?,ur_type=?,ur_group=? WHERE ur_id=?";
		jdbcTemplate.update(SQL, user.getUr_username(),user.getUr_password(),user.getUr_type(),user.getUr_group(),ur_id);
		
	}
	
	/*
	 * 根据用户id删除对应的用户
	 */
	@Override
	public void deleteUserById(int ur_id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "DELETE FROM tb_user WHERE ur_id=?";
		jdbcTemplate.update(SQL, ur_id);
		
	}
	
	
	/*
	 * 根据用户id判断是否存在用户
	 */
	@Override
	public boolean isExistByUserId(int userId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(ur_id) FROM tb_user WHERE ur_id=?";
		int count = jdbcTemplate.queryForObject(SQL, new Object[]{userId}, Integer.class);
		if(count <= 0) {
			return false;
		}
		return true;
	}
	
	
	
	/********************************************/
	
	/*
	 * 根据用户名、密码和用户类型查询用户
	 */
	@Override
	public List<Map<String, Object>> queryUserByCondition(String username,
			String password, int type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_user WHERE ur_username=? AND ur_password=? AND ur_type=?";
		List<Map<String,Object>> userList = jdbcTemplate.queryForList(SQL,username,password,type);
		return userList;
	}
	
	/*
	 * 根据用户id和用户类型查询用户
	 */
	@Override
	public List<Map<String, Object>> queryUserByUserIdAndType(int userId,int type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_user WHERE ur_id=? AND ur_type=?";
		List<Map<String,Object>> userList = jdbcTemplate.queryForList(SQL,userId,type);
		return userList;
	}
	
	/*
	 * 添加用户(修改),并返回添加的用户id
	 */
	@Override
	public int addUserNew(final User user) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		final String SQL = "INSERT INTO tb_user(ur_username,ur_password,ur_type,ur_datetime,ur_group,ur_treeid) VALUES(?,?,?,?,?,?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement preparedStatement = conn.prepareStatement(SQL,new String[]{"ur_username","ur_password","ur_type","ur_datetime","ur_group","ur_treeid"});
				preparedStatement.setString(1, user.getUr_username());
				preparedStatement.setString(2, user.getUr_password());
				preparedStatement.setInt(3, user.getUr_type());
				preparedStatement.setString(4, user.getUr_datetime());
				preparedStatement.setInt(5, user.getUr_group());
				preparedStatement.setInt(6, user.getUr_treeid());
				return preparedStatement;
			}
		}, keyHolder);
//		jdbcTemplate.update(SQL,user.getUr_username(),user.getUr_password(),user.getUr_type(),user.getUr_datetime(),user.getUr_group());
		return keyHolder.getKey().intValue();
		
	}
	
	/*
	 * 根据用户id和密码查询用户信息
	 */
	@Override
	public List<Map<String, Object>> queryUserByIdAndPsw(int userId,
			String password,int type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_user WHERE ur_id=? AND ur_password=? AND ur_type=?";
		List<Map<String,Object>> userList = jdbcTemplate.queryForList(SQL,userId,password,type);
		return userList;
	}
	
	/*
	 * 查询所有用户信息(包括管理员)
	 */
	@Override
	public List<Map<String, Object>> queryAllUsers() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT ur_id,ur_username FROM tb_user";
		List<Map<String,Object>> userList = jdbcTemplate.queryForList(SQL);
		return userList;
	}
	
	/*
	 * 根据用户组id查询用户
	 */
	@Override
	public List<Map<String, Object>> queryUserByGroupId(int groupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_user WHERE ur_group=?";
		List<Map<String, Object>> userList = jdbcTemplate.queryForList(SQL,groupId);
		return userList;
	}
	
	/*
	 * 根据用户组id修改用户用户组id
	 */
	@Override
	public void updateUserGroup(int oldGroupId, int newGroupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "UPDATE tb_user SET ur_group=? WHERE ur_group=?";
		jdbcTemplate.update(SQL,newGroupId,oldGroupId);
	}
	
	/*
	 * 根据用户id修改用户组id
	 */
	@Override
	public void updateGroupById(int id, int groupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "UPDATE tb_user SET ur_group=? WHERE ur_id=?";
		jdbcTemplate.update(SQL,groupId,id);
	}
	
	/*
	 * 根据条件分页或者不分页查询无用户组用户
	 */
	@Override
	public List<Map<String, Object>> queryUserPageBySearch(String search,
			String index, String count) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_user WHERE ur_group=0 ";
		if(search != null) {
			SQL += " AND ur_username LIKE ? ";
		}
		if(index != null && count != null) {
			int nIndex = Integer.valueOf(index);
			int nCount = Integer.valueOf(count);
			SQL += " LIMIT " + (nIndex-1)*nCount + "," + nCount;
		}
		List<Map<String,Object>> userList = null;
		if(search != null) {
			userList = jdbcTemplate.queryForList(SQL,"%" + search + "%");
		}else {
			userList = jdbcTemplate.queryForList(SQL);
		}
		return userList;
	}
	
	/*
	 * 根据条件查询用户总人数
	 */
	@Override
	public int countNoGroupUserByCondition(String search) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(ur_id) FROM tb_user WHERE ur_group=0" ;
		if(search != null) {
			SQL += " AND ur_username LIKE ? ";
		}
		int count = 0;
		
		if(search != null) {
			count = jdbcTemplate.queryForObject(SQL, new Object[]{"%" + search + "%"}, Integer.class);
		}else {
			count = jdbcTemplate.queryForObject(SQL, Integer.class);
		}
		return count;
	}
	
	/**
	 * 
	 * 根据登录用户id查出该用户管理的所有组的所有成员
	 * @param userid
	 */
	@Override
	public List<Map<String, Object>> queryUserByGroupAndStateAndLoginId(
			int userid,int index,int num) {
		
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();

		String sql = "select * from tb_user,tb_usergroup,tb_tree where node_userid=? AND urg_treeid=node_id AND ur_group=urg_id limit "+(index-1)*num+","+num;
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,userid);
		return list;
	}
	@Override
	public Integer countUserByGroupAndStateAndLoginId(int userid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();

		String sql = "select count(ur_id) from tb_user,tb_usergroup,tb_tree where node_userid=? AND urg_treeid=node_id AND ur_group=urg_id";
		Integer count = jdbcTemplate.queryForObject(sql,new Object[]{userid},Integer.class);
		return count;
	}
	
	
	/*
	 *  根据用户名和密码查询用户列表
	 */
	@Override
	public List<Map<String, Object>> queryByUsernameAndPassword(
			String username, String password) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_user WHERE ur_username=? AND ur_password=?";
		List<Map<String,Object>> userList = jdbcTemplate.queryForList(SQL,username,password);
		return userList;
	}
	
	/*
	 * 根据用户id查询用户信息
	 */
	@Override
	public List<Map<String, Object>> queryUserMapById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_user WHERE ur_id=?";
		List<Map<String,Object>> userList = jdbcTemplate.queryForList(SQL,id);
		return userList;
	}
	
	/*
	 * 根据树id查询用户信息
	 */
	@Override
	public List<Map<String, Object>> queryUserMapByTreeId(int treeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_user WHERE ur_treeid=?";
		List<Map<String,Object>> userList = jdbcTemplate.queryForList(SQL,treeId);
		return userList;
	}
	
	@Override
	public List<User> queryUserListByGroupId(int groupId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_user WHERE ur_group=?";
		List<User> userList = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<User>(User.class), groupId);
		return userList;
	}
	@Override
	public List<User> queryUserListByUsername(String username) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_user WHERE ur_username=?";
		List<User> userList = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<User>(User.class), username);
		return userList;
	}
	
	
}
