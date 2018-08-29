package com.creator.common.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarUtil {

	//获取现在是第几周
	public static int getWeekForNow(Date firstDate) {
			Date date = new Date();
			Calendar cd = Calendar.getInstance();
			cd.setTime(firstDate);
			if(cd.get(Calendar.DAY_OF_WEEK) == 1) {
				cd.add(Calendar.DATE, -1);
			}
			int firstWeek = cd.get(Calendar.WEEK_OF_YEAR);
			cd.setTime(date);
			if(cd.get(Calendar.DAY_OF_WEEK) == 1) {
				cd.add(Calendar.DATE, -1);
			}
			int newWeek = cd.get(Calendar.WEEK_OF_YEAR);
			return newWeek - firstWeek + 1;
	}
	
	//获取指定周数中每一天的年月日
	public static List<Map<String,Object>> getDatesOfWeek(Date firstDate,int week){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Calendar ca = Calendar.getInstance();
		ca.setTime(firstDate);
		ca.add(Calendar.DATE, 7*(week-1));
		Date newDate = ca.getTime();
		int dofw = ca.get(Calendar.DAY_OF_WEEK) - 1;
		if(dofw == 0) {
			dofw = 7;
		}	
		for(int i=1;i<dofw;i++) {
			Map<String,Object> map = new HashMap<>();
			Calendar ca1 = Calendar.getInstance();
			ca1.setTime(newDate);
			ca1.add(Calendar.DATE, i-7);
			int year = ca1.get(Calendar.YEAR);
			int month = ca1.get(Calendar.MONTH) + 1;
			int day = ca1.get(Calendar.DATE);
			map.put("year", year);
			map.put("month", month);
			map.put("day", day);
			result.add(map);
		}
		for(int j=dofw;j<=7;j++) {
			Map<String,Object> map = new HashMap<>();
			Calendar ca1 = Calendar.getInstance();
			ca1.setTime(newDate);
			ca1.add(Calendar.DATE, 7-j);
			int year = ca1.get(Calendar.YEAR);
			int month = ca1.get(Calendar.MONTH) + 1;
			int day = ca1.get(Calendar.DATE);
			map.put("year", year);
			map.put("month", month);
			map.put("day", day);
			result.add(map);
		}
		return result;
	}
}
