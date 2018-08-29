package com.creator.db.timetable;

import java.util.List;

import com.creator.common.db.CrudDao;
import com.creator.common.db.Page;

public interface TimeTableDao extends CrudDao<TimeTable>{

	public List<TimeTableDto> queryByPage(Page page,String like);
	
	/**
	 * 获取课表列表
	 */
	public List<TimeTable> listTimeTable();
	
	public int resetTermIdByTermId(int termId);
}
