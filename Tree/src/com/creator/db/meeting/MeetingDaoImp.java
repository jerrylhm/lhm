package com.creator.db.meeting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class MeetingDaoImp extends JdbcDaoSupport implements MeetingDao{
	//要查询的字段
	private static final String ROW_SELECT = "me_id,me_userid,me_nodeid,me_title,me_number,me_starttime,me_endtime,me_description,me_status";

	/*
	 *  根据节点id查询会议
	 */
	@Override
	public List<Map<String, Object>> queryMeetingByNodeId(int nodeId) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + ",ur_username FROM tb_meeting,tb_user WHERE me_nodeid=? AND me_userid=ur_id AND me_status=1";
		List<Map<String, Object>> meetingList = jdbcTemplate.queryForList(SQL,nodeId);
		return meetingList;
	}

	/*
	 * 根据节点id和日期查询会议列表
	 */
	@Override
	public List<Map<String, Object>> queryMeetingByNodeIdAndDate(int nodeId,
			String date) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + " FROM tb_meeting WHERE me_nodeid=? AND SUBSTRING_INDEX(me_starttime,' ',1)=? AND me_status=1";
		List<Map<String, Object>> meetingList = jdbcTemplate.queryForList(SQL,nodeId,date);
		return meetingList;
	}

	
	/*
	 * 添加会议
	 */
	@Override
	public int addMeeting(final Meeting meeting) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		final String SQL = "INSERT INTO tb_meeting(me_userid,me_nodeid,me_title,me_number,me_starttime,me_endtime,me_description,me_status) VALUES(?,?,?,?,?,?,?,?)";
	//	jdbcTemplate.update(SQL,meeting.getMe_userid(),meeting.getMe_nodeid(),meeting.getMe_title(),meeting.getMe_starttime(),meeting.getMe_endtime(),meeting.getMe_description(),meeting.getMe_status());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement preparedStatement = conn.prepareStatement(SQL,new String[]{"me_userid","me_nodeid","me_title","me_number","me_starttime","me_endtime","me_description","me_status"});
				preparedStatement.setInt(1,meeting.getMe_userid());
				preparedStatement.setInt(2, meeting.getMe_nodeid());
				preparedStatement.setString(3, meeting.getMe_title());
				preparedStatement.setInt(4,meeting.getMe_number());
				preparedStatement.setString(5, meeting.getMe_starttime());
				preparedStatement.setString(6, meeting.getMe_endtime());
				preparedStatement.setString(7, meeting.getMe_description());
				preparedStatement.setInt(8, meeting.getMe_status());
				return preparedStatement;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();
		
	}
	
	/*
	 * 根据节点id和日期查询会议列表（修改）,包括用户名
	 */
	@Override
	public List<Map<String, Object>> queryByNodeIdAndDate(int nodeId,
			String date) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String SQL = "SELECT " + ROW_SELECT + ",ur_username FROM tb_meeting,tb_user WHERE me_nodeid=? AND TO_DAYS(me_starttime)=TO_DAYS(?) AND ur_id=me_userid AND me_status=1 ORDER BY DATE_FORMAT(me_starttime,'%T')";
		List<Map<String,Object>> meetingList = jdbcTemplate.queryForList(SQL,nodeId,date);
		return meetingList;
	}


	@Override
	public List<Map<String, Object>> queryMeetingByUserId(int userid,int index,int num) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		
		String sql = "select me_id,me_userid,me_nodeid,me_title,me_description,node_son_name"+
					 ",me_number,me_endtime,me_starttime,me_status,node_name,ur_username from (SELECT me_id,me_userid,me_nodeid,me_title,me_description"+
					 ",me_number,me_endtime,me_starttime,me_status,node_treeid,node_name as node_son_name,ur_username FROM tb_meeting,tb_tree,tb_user where node_userid=ur_id and node_userid=? and me_nodeid=node_id)"+
					 "as a LEFT JOIN tb_tree as b on a.node_treeid=b.node_id" 
					 +" limit "+(index-1)*num+","+num;
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,userid);
		return list;
	}

	@Override
	public int countMeetingByUserId(int userid) {
JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		
		String sql = "select count(me_id"+
					 ") from (SELECT me_id,me_userid,me_nodeid,me_title,me_description"+
					 ",me_number,me_endtime,me_starttime,me_status,node_treeid FROM tb_meeting,tb_tree where node_userid=? and me_nodeid=node_id)"+
					 "as a LEFT JOIN tb_tree as b on a.node_treeid=b.node_id";
		
		int count = jdbcTemplate.queryForObject(sql,new Object[]{userid}, Integer.class);
		return count;
	}

	@Override
	public List<Map<String, Object>> queryMeetingByMeId(int me_id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();

		String sql = "select * from tb_meeting where me_id=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,me_id);
		return list;
	}

	@Override
	public void updateMeeting(int me_id,Meeting me) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();

		String sql = "update tb_meeting set me_userid=?,me_nodeid=?,me_title=?,me_number=?,me_starttime=?,me_endtime=?,me_description=?,me_status=? where me_id=?";
		jdbcTemplate.update(sql,me.getMe_userid(),me.getMe_nodeid(),me.getMe_title(),me.getMe_number(),me.getMe_starttime(),me.getMe_endtime(),me.getMe_description(),me.getMe_status(),me_id);
		
	}

	@Override
	public void deleteMeeting(int me_id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		
		String sql = "delete from tb_meeting where me_id=?";
		jdbcTemplate.update(sql,me_id);
		
	}

	@Override
	public void deleteMeetingAlot(String me_id) {
JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		
		String sql = "delete from tb_meeting where me_id in (?)";
		jdbcTemplate.update(sql,me_id);
		
	}

	@Override
	public List<Map<String, Object>> compareMeetingDate(String start,String end,int userid,String node_id) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select * from tb_meeting where ((me_starttime>=? and me_starttime<=?) or (me_starttime<=? and me_endtime>=?) or (me_endtime>=? and me_endtime<=?)) and me_status=1 and me_userid=? and me_nodeid=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,start,end,start,end,start,end,userid,node_id);
		return list;
	}
	
}
