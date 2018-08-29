package com.lhm.sso.util;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class SSOClientUtil {

	/**
	 * 设置跨域请求头
	 * @param response
	 * @throws IOException
	 */
	public static void setCrossHeader(HttpServletResponse response) throws IOException {
		String origin = getServerOrigin();
		response.setHeader("Access-Control-Allow-Origin",origin);  
		response.setHeader("Access-Control-Allow-Methods","POST");  
		response.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type");
		response.setHeader("Access-Control-ALLOW-Credentials","true"); 
	}
	
	/**
	 * 字符串加密
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encrStr(String str) throws Exception {
		String encodestr = RESUtil.parseByte2HexStr(RESUtil.enCrypt(str, RESUtil.RES_KEY));
		return encodestr;
	}
	
	/**
	 * 获取token
	 * @param request
	 * @return
	 */
	public static String getTokenFromCookie(HttpServletRequest request) {
		String token = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("CREATORTOKEN")) {
					token = cookie.getValue();
				}
			}
		}
		return token;
	}
	
	/**
	 * 将请求参数转换成json格式
	 * @param request
	 * @return
	 */
	public static String coverParamToJSON(HttpServletRequest request) {
		String param = "";
		Map<String, String[]> paramMap = request.getParameterMap();
		JSONObject jo = new JSONObject();
		for(String key:paramMap.keySet()) {
			String value = paramMap.get(key)[0];
			jo.put(key, value);
		}
		param = jo.toString();
		return param;
	}
	
	/**
	 * 获取请求的源
	 * @return
	 * @throws IOException
	 */
	public static String getServerOrigin() throws IOException {
		String serverScheme = PropertiesUtil.getProperty("serverScheme");
		String serverDomain = PropertiesUtil.getProperty("serverDomain");
		String serverPort = PropertiesUtil.getProperty("serverPort");
		String origin = "";
		if(serverPort != null && !serverPort.equals("")) {
			origin = serverScheme + "://" + serverDomain + ":" + serverPort;
		}else {
			origin = serverScheme + "://" + serverDomain;
		}
		return origin;
	}
}
