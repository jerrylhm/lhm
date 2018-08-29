package com.creator.db.meeting;

import java.util.List;
import java.util.Map;

public interface MeetingDao {
	
	/**
	 * 根据节点id查询会议
	 */
	public List<Map<String,Object>> queryMeetingByNodeId(int nodeId);
	
	
	/**
	 * 根据节点id和日期查询会议列表
	 */
	public List<Map<String,Object>> queryMeetingByNodeIdAndDate(int nodeId,String date);
	
	/**
	 * 添加会议
	 */
	public int addMeeting(Meeting meeting);
	
	/**
	 * 根据节点id和日期查询会议列表（修改）
	 */
	public List<Map<String,Object>> queryByNodeIdAndDate(int nodeId,String date);
	

	
	/**
	 * 根据用户id查询所有会议
	 */
	public List<Map<String,Object>> queryMeetingByUserId(int userid,int index,int num);
	
	/**
	 * 根据用户id查询所有会议数量
	 */
	public int countMeetingByUserId(int userid);
	
	/**
	 * 根据会议Id查询
	 * @param me_id
	 * @return
	 */
	public List<Map<String,Object>> queryMeetingByMeId(int me_id);
	
	/**
	 * 
	 * 根据会议Id更新会议数据
	 * 
	 * @param me_id
	 */
	public void updateMeeting(int me_id,Meeting me);
	
	/**
	 * 
	 * 根据会议Id删除记录
	 * 
	 * @param me_id
	 */
	public void deleteMeeting(int me_id);

	/**
	 * 
	 * 批量删除会议记录
	 * 
	 * @param me_id
	 */
	public void deleteMeetingAlot(String me_id);
	
	/**
	 * 
	 * 对比会议时间
	 * 
	 * @param me
	 * @return
	 */
	public List<Map<String, Object>> compareMeetingDate(String start,String end,int userid,String node_id);

}
