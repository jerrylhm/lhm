package com.creator.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.creator.util.HttpUtil;

public class CloudTest {
	private final static String CLOUD_ADDRESS = "http://localhost:8080/Tree/cloud/";
	
	@Test
	public void testGetValueDataByDate() throws Exception {
		String host = CLOUD_ADDRESS + "getValueDataByDate";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "178");
		map.put("key", "value");
		map.put("date", "2018-03-28");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetStatisticsDataApp() throws Exception {
		String host = CLOUD_ADDRESS + "getStatisticsData";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "178");
		map.put("key", "value");
		map.put("opcode", "0002");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
}
