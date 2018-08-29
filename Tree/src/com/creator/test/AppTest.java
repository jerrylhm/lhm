package com.creator.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.creator.util.HttpUtil;

public class AppTest {
	private final static String APP_ADDRESS = "http://localhost:8080/Tree/app/";
	
	@Test
	public void testAppLogin() throws Exception {
		String host = APP_ADDRESS + "login";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "178");
		map.put("username", "admin");
		map.put("password", "123456");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testRegister() throws Exception {
		String host = APP_ADDRESS + "register";
		Map<String,String> map = new HashMap<String, String>();
		map.put("username", "admin1234");
		map.put("password", "123456");
		map.put("treeid", "149");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	
	@Test
	public void testGetTreeList() throws Exception {
		String host = APP_ADDRESS + "getTreeList";
		Map<String,String> map = new HashMap<String, String>();
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
}
