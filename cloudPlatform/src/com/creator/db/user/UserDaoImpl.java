package com.creator.db.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.StringUtils;

@Repository("userDao")
public class UserDaoImpl implements UserDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*
	 * 获取所有用户
	 */
	@Override
	public List<User> listUsers() {
		String SQL = "SELECT * FROM tb_user";
		List<User> userList = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<>(User.class));
		return userList;
	}
	
	/*
	 * 获取未审核用户信息,分页
	 */
	@Override
	public List<Map<String, Object>> queryAllListUser(int index,int num,String query,String date,String role,Integer status) {
		
		String SQL = "SELECT * FROM tb_user WHERE ur_status =  "+status;
		if(query != "#null"){
			SQL += " AND CONCAT (ur_username,ur_nickname) LIKE \"%"+query+"%\"";
		}else if(date != "0"){
			SQL += " AND ur_createdate = \""+date+"\"";
		}else if(!role.equals("0") && !role.equals("100")){
			SQL += " AND ur_type LIKE \"%"+role+"%\"";
		}else if(role.equals("100")){
			SQL += " AND ur_type LIKE \"%1%\" OR ur_type LIKE \"%4%\" OR ur_type LIKE \"%5%\"";
		}
		
		SQL += " ORDER BY ur_id DESC LIMIT "+(index-1)*num+","+num;
		List<Map<String, Object>> userList = jdbcTemplate.queryForList(SQL);
		for(int i=0;i<userList.size();i++){
			if(userList.get(i).get("ur_classid").equals("0")){
			   userList.get(i).put("className", "无");
			}else{
				String []arr = ((String) userList.get(i).get("ur_classid")).split(",");
				String ClassName = "";
				for(int k=0;k<arr.length;k++){
					String ClassSQL = "SELECT * FROM tb_organization WHERE org_id = "+arr[k];
					List<Map<String, Object>> cName = jdbcTemplate.queryForList(ClassSQL);
					String GradeSQL = "SELECT org_name FROM tb_organization WHERE org_id = "+cName.get(0).get("org_pid");
					List<Map<String, Object>> gName = jdbcTemplate.queryForList(GradeSQL);
					if(ClassName == ""){
						ClassName = gName.get(0).get("org_name")+""+cName.get(0).get("org_name");
					}else{
						ClassName += " <span style='color:red;'>/</span> "+gName.get(0).get("org_name")+""+cName.get(0).get("org_name");
					}
					
				}
				userList.get(i).put("className", ClassName);
			}
		}
		return userList;
	}
	
	/*
	 * 统计未审核总数
	 */
	@Override
	public int countCheckUser(String query,String date,String role,Integer status){
		
		String SQL = "SELECT COUNT(*) FROM tb_user WHERE ur_status = "+status;
		if(query !="#null"){
			SQL += " AND CONCAT (ur_username,ur_nickname) LIKE \"%"+query+"%\"";
		}else if(date != "0"){
			SQL += " AND ur_createdate = \""+date+"\"";
		}else if(!role.equals("0") && !role.equals("100")){
			SQL += " AND ur_type LIKE \"%"+role+"%\"";
		}else if(role.equals("100")){
			SQL += " AND ur_type LIKE \"%1%\" OR ur_type LIKE \"%4%\" OR ur_type LIKE \"%5%\"";
		}
		int count = jdbcTemplate.queryForObject(SQL, int.class);
		return count;
	}
	
	/*
	 * 根据id获取用户
	 */
	@Override
	public List<User> findById(int id) {
		String SQL = "SELECT * FROM tb_user WHERE ur_id=?";
		List<User> userList = jdbcTemplate.query(SQL,new Object[]{id},new BeanPropertyRowMapper<>(User.class));
		return userList;
	}

	/*
	 * 根据id删除用户
	 */
	@Override
	public boolean deleteById(int id) {
		String SQL = "DELETE FROM tb_user WHERE ur_id=?";
		jdbcTemplate.update(SQL, id);
		return true;
	}

	@Override
	public User findByUsername(String username) {
		String SQL = "SELECT * FROM tb_user WHERE ur_username=?";
		List<User> userList = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<User>(User.class),username);
		if(userList.size() > 0) {
			return userList.get(0);
		}else {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc) 修改用户状态
	 * @see com.creator.db.user.UserDao#changeUserStatus(int)
	 */
	@Override
	public String changeUserStatus(int id,int status){
		String SQL = "UPDATE tb_user SET ur_status = "+status+" WHERE ur_id = ?";
		jdbcTemplate.update(SQL, id);
		return "true";
	}
	
	@Override
	public List<Map<String, Object>> queryAllUserByNeed(int index, int num,
			String query, String date, String role, Integer status, Integer ug_id, String orderBy, boolean isDESC) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT uwg.*, GROUP_CONCAT(o.org_name ORDER BY o.org_id) AS ur_classname FROM ");
		sql.append("(SELECT ur_id,ur_username,ur_image,ur_email,ur_nickname,ur_classid,ur_createdate,ur_sex,ur_phone,ur_type,ur_status,GROUP_CONCAT(uug.uug_ugid ORDER BY uug.ug_id) AS ur_group,GROUP_CONCAT(uug.ug_name ORDER BY uug.ug_id) AS ur_groupname ");
		sql.append("FROM tb_user u ");
		sql.append("LEFT JOIN (SELECT * FROM tb_user_usergroup,tb_usergroup WHERE ug_id=uug_ugid) AS uug ");
		sql.append("ON u.ur_id=uug.uug_urid WHERE 1 ");
	
		//状态筛选
		if(status != null && status != -1) {
			sql.append("AND u.ur_status=" + status + " ");
		}
		
		//角色筛选
		if(!"0".equals(role)) {
			sql.append("AND FIND_IN_SET(" + role + ",u.ur_type) ");
		}	
		
		//查询筛选
		sql.append("AND CONCAT(u.ur_username,u.ur_nickname) LIKE ? ");
		
		//日期筛选
		if(!StringUtils.isEmpty(date)) {
			sql.append("AND u.ur_createdate='" + date + "' ");
		}
		
		sql.append("GROUP BY u.ur_id) AS uwg ");
		
		sql.append("LEFT JOIN (SELECT o2.org_id,CONCAT(o1.org_name,o2.org_name) AS org_name FROM tb_organization o1,tb_organization o2 WHERE o2.org_pid=o1.org_id AND o2.org_type=4) AS o ");
		sql.append("ON FIND_IN_SET(o.org_id,uwg.ur_classid) ");

		//筛选用户组
		if(ug_id != null && ug_id != -1) {
			sql.append("WHERE FIND_IN_SET(" + ug_id + ",uwg.ur_group) ");
		}
		
		sql.append("GROUP BY uwg.ur_id ");
		
		//排序
		if(!StringUtils.isEmpty(orderBy)) {
			sql.append("ORDER BY uwg." + orderBy + " ");
			if(isDESC) {
				sql.append("DESC,uwg.ur_id DESC ");
			} else {
				sql.append(",uwg.ur_id ");
			}
		}
		
		//分页
		sql.append("LIMIT ?,?");
		
		return jdbcTemplate.queryForList(sql.toString(), "%" + query + "%", (index-1)*num, num);
	}

	@Override
	public int countAllUserByNeed(String query,
			String date, String role, Integer status, Integer ug_id) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT COUNT(uwg.ur_id) FROM ");
		sql.append("(SELECT u.*,GROUP_CONCAT(uug.uug_ugid) AS ur_group FROM tb_user u ");
		sql.append("LEFT JOIN tb_user_usergroup uug ON u.ur_id=uug.uug_urid WHERE 1 ");
	
		//状态筛选
		if(status != -1 && status != null) {
			sql.append("AND u.ur_status=" + status + " ");
		}
		
		//角色筛选
		if(!"0".equals(role)) {
			sql.append("AND FIND_IN_SET(" + role + ",u.ur_type) ");
		} 	
		
		//查询筛选
		sql.append("AND CONCAT(u.ur_username,u.ur_nickname) LIKE ? ");
		
		//日期筛选
		if(!StringUtils.isEmpty(date)) {
			sql.append("AND u.ur_createdate='" + date + "' ");
		}
		
		sql.append("GROUP BY u.ur_id) AS uwg ");
		
		//筛选用户组
		if(ug_id != null && ug_id != -1) {
			sql.append("WHERE FIND_IN_SET(" + ug_id + ",uwg.ur_group) ");
		}
		
		return jdbcTemplate.queryForObject(sql.toString(), new Object[] {"%" + query + "%"}, int.class);
	}

	@Override
	public int updateUser(User user) {
		String sql = "UPDATE tb_user SET ur_username=?, ur_password=?, ur_image=?, ur_email=?, ur_nickname=?, ur_classid=?, ur_createdate=?, ur_sex=?, ur_phone=?, ur_type=?, ur_status=? WHERE ur_id=?";
		return jdbcTemplate.update(sql, user.getUr_username(), user.getUr_password(), user.getUr_image(), user.getUr_email(), user.getUr_nickname(), user.getUr_classid(), user.getUr_createdate(), user.getUr_sex(), user.getUr_phone(), user.getUr_type(), user.getUr_status(), user.getUr_id());
	}

	@Override
	public int addUser(final User user) {
		final String sql = "INSERT INTO tb_user(ur_username,ur_password,ur_image,ur_email,ur_nickname,ur_classid,ur_createdate,ur_sex,ur_phone,ur_type,ur_status) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"ur_id"});
                psst.setString(1, user.getUr_username());
                psst.setString(2, user.getUr_password());
                psst.setString(3, user.getUr_image());
                psst.setString(4, user.getUr_email());
                psst.setString(5, user.getUr_nickname());
                psst.setString(6, user.getUr_classid());
                psst.setString(7, user.getUr_createdate());
                psst.setInt(8, user.getUr_sex());
                psst.setString(9, user.getUr_phone());
                psst.setString(10, user.getUr_type());
                psst.setInt(11, user.getUr_status());
                return psst;
            }
        }, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public int isExist(String username) {
		String sql = "SELECT COUNT(ur_id) FROM tb_user WHERE ur_username=?";
		return jdbcTemplate.queryForObject(sql, new Object[] {username}, int.class);
	}
	
	/*
	 * 根据用户id查询班级
	 */
	@Override
	public List<Map<String, Object>> queryClassNameByUserId(int id){
		String SQL = "SELECT ur_classid FROM tb_user WHERE ur_id = "+id;
		List<Map<String, Object>> ClassNameList = jdbcTemplate.queryForList(SQL);
		for(int i=0;i<ClassNameList.size();i++){
			if(ClassNameList.get(i).get("ur_classid").equals("0")){
				ClassNameList.get(i).put("ClassName", "无");
			}else if(("").equals(ClassNameList.get(i).get("ur_classid")) || ClassNameList.get(i).get("ur_classid") == null ){
				ClassNameList.get(i).put("ClassName", "无");
			}else{
				String []arr = ((String) ClassNameList.get(i).get("ur_classid")).split(",");
				String ClassName = "";
				for(int k=0;k<arr.length;k++){
					String ClassSQL = "SELECT org_name,org_pid FROM tb_organization WHERE org_id = "+arr[k];
					List<Map<String, Object>> className = jdbcTemplate.queryForList(ClassSQL);
					String GradeSQL = "SELECT org_name FROM tb_organization WHERE org_id = "+className.get(0).get("org_pid");
					List<Map<String, Object>> gradeName = jdbcTemplate.queryForList(GradeSQL);
					if(ClassName == ""){
						ClassName = gradeName.get(0).get("org_name")+""+className.get(0).get("org_name");
					}else{
						ClassName += "/"+gradeName.get(0).get("org_name")+""+className.get(0).get("org_name");
					}
				}
				ClassNameList.get(i).put("ClassName", ClassName);
			}
		}
		
		return ClassNameList;
	}

	
	/*
	 *  根据班级id查询用户名
	 */
	@Override
	public List<User> findByClassId(int classId) {
		String SQL = "SELECT * FROM tb_user WHERE FIND_IN_SET(?,ur_classid)";
		List<User> userList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<User>(User.class),classId);
		return userList;
	}

	
	/*
	 * 根据用户id修改用户班级
	 */
	@Override
	public int updateClassId(User user) {
		String SQL = "UPDATE tb_user SET ur_classid=? WHERE ur_id=?";
		return jdbcTemplate.update(SQL, user.getUr_classid(),user.getUr_id());
	}

	@Override
	public List<User> queryTeachers() {
		String sql = "SELECT u.* FROM tb_user u,tb_user_usergroup uug WHERE u.ur_id = uug.uug_urid AND uug.uug_ugid=2 AND u.ur_status=1";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
	}
	
	
	
	
	
	
}
