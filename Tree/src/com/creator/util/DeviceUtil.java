package com.creator.util;

import java.util.ArrayList;
import java.util.Map;

import net.sf.json.JSONObject;

import com.creator.api.CodeEnum;

/**
 * 设备接口相关工具类
 *
 */
public class DeviceUtil {
	/**
	 * 将状态码转成设备相关的json
	 * @param code
	 * @return json对象,格式：{"code":"" , "msg":"" , "data":[],"type":"1"},数据为空
	 */
	public static JSONObject getCodeJSON(CodeEnum code) {
		JSONObject jsonObject = new JSONObject();
		if(code != null) {
			jsonObject.put("code", code.getCode());
			jsonObject.put("msg", code.getMsg());
			jsonObject.put("data", new ArrayList<Map<String, Object>>());
			jsonObject.put("type", "1");
		}
		return jsonObject;
	}
}
