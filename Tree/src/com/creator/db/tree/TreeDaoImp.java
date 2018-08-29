package com.creator.db.tree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.creator.db.scene.SceneDto;
import com.creator.db.type.TypeEnum;


public class TreeDaoImp extends JdbcDaoSupport implements TreeDao{
	//要查询的字段
	private static final String ROW_SELECT = " node_id,node_userid,node_name,node_pid,node_treeid,node_url,node_type,node_protocol,node_class,node_sn,node_tstype,node_title ";
	
	@Override
	public List<Tree> queryById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT "+ ROW_SELECT +" "
		           + "FROM tb_tree WHERE node_id = ?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Tree>(Tree.class), id);
	}

	@Override
	public int addTree(Tree tree) {
        JdbcTemplate jdbcTemplateObject = this.getJdbcTemplate();
		
		final Tree t = tree;
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		final String sql = "insert into tb_tree(node_name,node_userid,node_pid,node_treeid,node_url,node_type,node_protocol,node_class,node_sn,node_tstype,node_title) values(?,?,?,?,?,?,?,?,?,?,?)";
		
		//通过接口回调和keyHolder获取新添加的节点的id
		jdbcTemplateObject.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setString(1, t.getNode_name());
				ps.setInt(2, t.getNode_userid());
				ps.setInt(3, t.getNode_pid());
				ps.setInt(4, t.getNode_treeid());
				ps.setString(5, t.getNode_url());
				ps.setInt(6, t.getNode_type());
				ps.setString(7, t.getNode_protocol());
				ps.setString(8, t.getNode_class());
				ps.setString(9, t.getNode_sn());
				ps.setInt(10, t.getNode_tstype());
				ps.setString(11, t.getNode_title());
				return ps;
			}
		},keyHolder);
		//返回课表id
		return keyHolder.getKey().intValue();
	}

	@Override
	public int updateTree(Tree tree) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE tb_tree SET node_userid=?,node_name=?,node_pid=?,node_treeid=?,node_url=?,node_type=?,node_protocol=?,node_class=?,node_sn=?,node_tstype=?,node_title=? "
		           + "WHERE node_id=?";  
		return jdbcTemplate.update(sql, tree.getNode_userid(), tree.getNode_name(), tree.getNode_pid(), tree.getNode_treeid(), tree.getNode_url(), tree.getNode_type(), tree.getNode_protocol(), tree.getNode_class(), tree.getNode_sn(), tree.getNode_tstype(), tree.getNode_title(), tree.getNode_id());
	}

	@Override
	public List<Tree> querySonNode(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT " + ROW_SELECT + " "
		           + "FROM tb_tree WHERE node_pid = ?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tree.class), id);
	}

	@Override
	public int deleteTree(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_tree WHERE node_id=?";  
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public List<Tree> queryByTreeId(int treeid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT " + ROW_SELECT + " "
		           + "FROM tb_tree WHERE node_treeid = ?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tree.class), treeid);
	}
	
	/*
	 * 根据用户id删除树节点
	 */
	@Override
	public void deleteNodeByUserid(int userid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "DELETE FROM tb_tree WHERE node_userid=?";
		jdbcTemplate.update(SQL, userid);
	}

	@Override
	public List<Tree> queryByNameAndPid(String name,int pid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT " + ROW_SELECT + " "
		           + "FROM tb_tree WHERE node_name = ? AND node_pid = ?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tree.class), name, pid);
	}

	@Override
	public List<Tree> queryBySn(String sn) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT " + ROW_SELECT + " "
		           + "FROM tb_tree WHERE node_sn = ?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tree.class), sn);
	}
	
	/*
	 * 根据用户id查询其创建的树根节点
	 */
	@Override
	public List<Map<String, Object>> queryCreateRootByUserId(int userId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree WHERE node_userid=?";
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL,userId);
		return nodeList;
	}

	/*
	 * 根据用户id查询有权限的树根节点
	 */
	@Override
	public List<Map<String, Object>> queryRootByUserId(int userId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree,tb_permission WHERE urnode_treeid=node_id AND node_pid=0 AND urnode_userid=?";
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL,userId);
		return nodeList;
	}

	/*
	 * 判断节点是否存在
	 */
	@Override
	public boolean isExistNode(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(node_id) FROM tb_tree WHERE node_id=?";
		int count = jdbcTemplate.queryForObject(SQL, Integer.class, nodeId);
		if(count <= 0) {
			return false;
		}
		return true;
	}

	/*
	 * 根据节点id查询节点
	 */
	@Override
	public List<Map<String, Object>> queryNodeByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT" + ROW_SELECT + " FROM tb_tree WHERE node_id=?";
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL, nodeId);
		return nodeList;
	}

	
	/*
	 * 根据节点id查询子节点
	 */
	@Override
	public List<Map<String, Object>> queryChilrenByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree WHERE node_pid=?";
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL, nodeId);
		return nodeList;
	}

	/*
	 * 根据节点id和用户id查询有权限的子节点
	 */
	@Override
	public List<Map<String, Object>> queryChilrenByNodeIdAndUserId(int nodeId,
			int userId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree,tb_permission WHERE node_pid=? AND urnode_userid=? AND FIND_IN_SET(node_id,urnode_nodeid)";
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL,nodeId,userId);
		return nodeList;
	}

	/*
	 * 根据节点id查询会议和普通子节点
	 */
	@Override
	public List<Map<String, Object>> queryMeetingChildrenById(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree WHERE node_pid=? AND (node_type=? OR node_type=?)";
		//获得普通和会议对应的id
		int normal = TypeEnum.NORMAL.getId();
		int meeting = TypeEnum.MEETING.getId();
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL, nodeId,normal,meeting);
		return nodeList;
	}

	@Override
	public List<Tree> queryTreeNode() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT " + ROW_SELECT + " "
		           + "FROM tb_tree WHERE node_pid = 0";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tree.class));
	}

	@Override
	public List<Tree> queryTreeNodeByUserId(int userId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT " + ROW_SELECT + " "
		           + "FROM tb_tree WHERE node_pid = 0 AND node_userid = ?";  
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tree.class), userId);
	}
	
	/*
	 * 根据树id和类名查询设备
	 */
	@Override
	public List<Map<String, Object>> queryDeviceByTreeIdAndClassName(
			int treeId, String className) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree WHERE node_treeid=? AND node_class=?";
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL,treeId,className);
		return nodeList;
	}
	
	/*
	 * 根据节点id和类型名称查询子节点
	 */
	@Override
	public List<Map<String, Object>> queryChildrenByClassName(int nodeId,
			String className) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree WHERE node_pid=? AND (node_class=? OR node_type=?)";
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL,nodeId,className,TypeEnum.NORMAL.getId());
		return nodeList;
	}

	/*
	 * 根据节点id和节点类型查询子节点(包括普通类型节点)
	 */
	@Override
	public List<Map<String, Object>> queryChildrenByNodeTypeOrNormal(int nodeId,
			int type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree WHERE node_pid=? AND (node_type=? OR node_type=? )";
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL,nodeId,type,TypeEnum.NORMAL.getId());
		return nodeList;
	}

	/*
	 * 根据节点id和节点类型查询子节点(不包括普通类型节点)
	 */
	@Override
	public List<Map<String, Object>> queryChildrenByNodeType(int nodeId,
			int type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree WHERE node_pid=? AND node_type=?";
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL,nodeId,type);
		return nodeList;
	}

	/*
	 * 根据节点id判断树根节点是否存在
	 */
	@Override
	public boolean isExistRootById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(node_id) FROM tb_tree WHERE node_id=? AND node_pid=0";
		int count = jdbcTemplate.queryForObject(SQL, Integer.class, id);
		if(count <= 0) {
			return false;
		}
		return true;
	}

	/*
	 * 对该用户所创建的树进行分类
	 */
	@Override
	public List<TreeDto> queryTreeNodeByPage(TreeDto treeDto) {
		JdbcTemplate jdbcTemplateObject = this.getJdbcTemplate();
    	//搜索关键
    	String like = treeDto.getLike();
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT " + ROW_SELECT + " ");
        String fromAndWhere = "FROM tb_tree WHERE 1=1 AND node_pid = 0 AND node_userid = " + treeDto.getNode_userid() + " ";
        sql.append(fromAndWhere);
        if(treeDto.getLike()!=null){
        	sql.append("AND node_name Like ? ");
        	String countSql = "select count(node_id) " + fromAndWhere;
			countSql = countSql + " AND node_name Like ? ";
			treeDto.getPage().setTotalNumber(jdbcTemplateObject.queryForObject(countSql,new Object[]{"%"+like+"%"},int.class));
        }else{
        	String countSql = "select count(node_id) "+fromAndWhere;
        	treeDto.getPage().setTotalNumber(jdbcTemplateObject.queryForObject(countSql,new Object[]{},int.class));
        }    
        
        int currentPage = treeDto.getPage().getCurrentPage();
		int pageNumber = treeDto.getPage().getPageNumber();
		sql.append("ORDER BY node_id DESC ");
		sql.append("limit "+(currentPage-1)*pageNumber+","+pageNumber);
        RowMapper<TreeDto> rm = new BeanPropertyRowMapper<>(TreeDto.class);
        if(treeDto.getLike()!=null){
        return jdbcTemplateObject.query(sql.toString(),new Object[]{"%"+like+"%"},rm);	
        }else{
		return jdbcTemplateObject.query(sql.toString(),new Object[]{},rm);
        }
	}

	
	/*
	 * 根据节点id和类名组合查询子节点(包括普通节点)
	 */
	@Override
	public List<Map<String, Object>> queryChildrenByClassNameGroup(int nodeId,
			String classNameGroup) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		//分割类名组合
		String[] classNames = classNameGroup.split(",");
//		System.out.println(classNames.length);
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree WHERE node_pid=? AND (node_type=? ";
		String condition = "";
		for(int i=0;i<classNames.length;i++) {
			condition += " OR node_class=\"" + classNames[i] + "\"";
		}
		condition += " )";
		SQL += condition;
//		System.out.println("SQL:" + SQL);
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL,nodeId,TypeEnum.NORMAL.getId());
		return nodeList;
	}

	/*
	 * 根据节点id和类名查询子节点（不包括普通节点）
	 */
	@Override
	public List<Map<String, Object>> queryChildrenByClassNameWithoutNormal(
			int nodeId, String className) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_tree WHERE node_pid=? AND node_class=?";
		List<Map<String,Object>> nodeList = jdbcTemplate.queryForList(SQL,nodeId,className);
		return nodeList;
	}
}
