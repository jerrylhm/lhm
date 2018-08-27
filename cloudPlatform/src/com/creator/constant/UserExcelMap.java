package com.creator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户Excel数据映射定义
 * @author ilongli
 *
 */
public class UserExcelMap {
	public static final Map<String, Map<String, String>> attrsMap = new HashMap<String, Map<String, String>>();
	
	static {
		HashMap<String, String> ur_sex_map = new HashMap<String, String>();
		ur_sex_map.put("男", "0");
		ur_sex_map.put("女", "1");
		HashMap<String, String> ur_type_map = new HashMap<String, String>();
		ur_type_map.put("管理员", "1");
		ur_type_map.put("教师", "2");
		ur_type_map.put("学生", "3");
		ur_type_map.put("家长", "4");
		attrsMap.put("ur_sex", ur_sex_map);
		attrsMap.put("ur_type", ur_type_map);
	}
}
