package com.creator.test;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.creator.util.HttpUtil;

/**
 * 测试设备接口
 * @author 魏彦
 *
 */
public class DeviceTest {
	private final static String HOST_ADDRESS = "http://localhost:8080/Tree/device/";
	
	@Test
	public void testSendValue() throws Exception {
		String host = HOST_ADDRESS + "sendValue";
		Map<String,String> map = new HashMap<String, String>();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("time", "88");
		jsonObject.put("state", "1");
		map.put("sn", "123");
		map.put("value","[178]");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		System.out.println("结果：" + result);
	}
}
