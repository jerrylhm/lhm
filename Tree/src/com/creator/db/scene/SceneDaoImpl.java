package com.creator.db.scene;

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

import com.creator.db.template.TemplateDto;

public class SceneDaoImpl extends JdbcDaoSupport implements SceneDao{
	private static final String ROW_SELECT = "sc_id,sc_name,sc_treeid,sc_action,sc_nodeid";
	
	@Override
	public List<Scene> query() {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT sc_id,sc_name,sc_treeid,sc_action,sc_nodeid FROM tb_scene";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Scene>(Scene.class));
	}

	@Override
	public int add(Scene scene) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "INSERT INTO tb_scene(sc_name,sc_treeid,sc_action,sc_nodeid) VALUES(?,?,?,?)";
		return jdbcTemplate.update(sql, scene.getSc_name(), scene.getSc_treeid(), scene.getSc_action(), scene.getSc_nodeid());
	}

	@Override
	public int update(Scene scene) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "UPDATE tb_scene SET sc_name = ?,sc_treeid = ?,sc_action = ?,sc_nodeid = ? WHERE sc_id = ?";
		return jdbcTemplate.update(sql, scene.getSc_name(), scene.getSc_treeid(), scene.getSc_action(), scene.getSc_nodeid(), scene.getSc_id());
	}

	@Override
	public int delete(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_scene WHERE sc_id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public List<SceneDto> queryByPage(SceneDto sceneDto) {
		JdbcTemplate jdbcTemplateObject = this.getJdbcTemplate();
    	//搜索关键
    	String like = sceneDto.getLike();
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT sc_id, sc_name, sc_treeid, sc_action, sc_nodeid ");
        String fromAndWhere = "FROM tb_scene WHERE 1=1 AND FIND_IN_SET(sc_treeid,'" + sceneDto.getTreesStr() + "') ";
        sql.append(fromAndWhere);
        if(sceneDto.getLike()!=null){
        	sql.append("AND sc_name Like ? ");
        	String countSql = "select count(sc_id) " + fromAndWhere;
			countSql = countSql + " AND sc_name Like ? ";
			sceneDto.getPage().setTotalNumber(jdbcTemplateObject.queryForObject(countSql,new Object[]{"%"+like+"%"},int.class));
        }else{
        	String countSql = "select count(sc_id) "+fromAndWhere;
        	sceneDto.getPage().setTotalNumber(jdbcTemplateObject.queryForObject(countSql,new Object[]{},int.class));
        }    
        
        int currentPage = sceneDto.getPage().getCurrentPage();
		int pageNumber = sceneDto.getPage().getPageNumber();
		sql.append("ORDER BY sc_id DESC ");
		sql.append("limit "+(currentPage-1)*pageNumber+","+pageNumber);
        RowMapper<SceneDto> rm = new BeanPropertyRowMapper<>(SceneDto.class);
        if(sceneDto.getLike()!=null){
        return jdbcTemplateObject.query(sql.toString(),new Object[]{"%"+like+"%"},rm);	
        }else{
		return jdbcTemplateObject.query(sql.toString(),new Object[]{},rm);
        }
	}

	@Override
	public List<Scene> findById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT sc_id,sc_name,sc_treeid,sc_action,sc_nodeid FROM tb_scene WHERE sc_id = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Scene>(Scene.class), id);
	}

	@Override
	public List<Scene> queryByTreeId(int treeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "SELECT sc_id,sc_name,sc_treeid,sc_action,sc_nodeid FROM tb_scene WHERE sc_treeid = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Scene>(Scene.class), treeId);
	}
	
	/*
	 * 根据树根节点查询场景列表
	 */
	@Override
	public List<Map<String, Object>> querySceneByTreeId(int treeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_scene WHERE sc_treeid=?";
		List<Map<String,Object>> sceneList = jdbcTemplate.queryForList(SQL,treeId);
		return sceneList;
	}

	/*
	 * 根据场景节点查询场景
	 */
	@Override
	public List<Map<String, Object>> queryById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_scene WHERE sc_id=?";
		List<Map<String,Object>> sceneList = jdbcTemplate.queryForList(SQL,id);
		return sceneList;
	}

	
	/*
	 * 根据节点id查询场景
	 */
	@Override
	public List<Map<String, Object>> queryByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_scene WHERE FIND_IN_SET(?,sc_nodeid)";
		List<Map<String,Object>> sceneList =  jdbcTemplate.queryForList(SQL,nodeId);
		return sceneList;
	}

	/*
	 * 添加场景并返回场景id
	 */
	@Override
	public int addScene(final Scene scene) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		final String SQL = "INSERT INTO tb_scene(sc_name,sc_treeid,sc_action,sc_nodeid) VALUES(?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement statment = conn.prepareStatement(SQL, new String[]{"sc_name","sc_treeid","sc_action","sc_nodeid"});
				statment.setString(1, scene.getSc_name());
				statment.setInt(2, scene.getSc_treeid());
				statment.setString(3, scene.getSc_action());
				statment.setString(4, scene.getSc_nodeid());
				return statment;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();
	}

	/*
	 * 根据场景id修改场景
	 */
	@Override
	public void updateSceneById(Scene scene) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "UPDATE tb_scene SET sc_name=?,sc_treeid=?,sc_action=?,sc_nodeid=? WHERE sc_id=?";
		jdbcTemplate.update(SQL,scene.getSc_name(),scene.getSc_treeid(),scene.getSc_action(),scene.getSc_nodeid(),scene.getSc_id());
	}

	
	/*
	 * 根据场景名称判断场景是否存在
	 */
	@Override
	public boolean isExistByName(String name, int treeid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT COUNT(sc_id) FROM tb_scene WHERE sc_name=? AND sc_treeid=?";
		int count = jdbcTemplate.queryForObject(SQL, new Object[]{name,treeid}, Integer.class);
		if(count <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public int deleteByTreeId(int treeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "DELETE FROM tb_scene WHERE sc_treeid = ?";
		return jdbcTemplate.update(sql, treeId);
	}

	@Override
	public List<Scene> queryListById(int id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_scene WHERE sc_id=?";
		List<Scene> sceneList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<Scene>(Scene.class), id);
		return sceneList;
	}

}
