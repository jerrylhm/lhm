package com.creator.common.util;

import net.sf.json.JSONObject;

public class SSOServerUtil {

	public static String encrStr(String str) throws Exception {
		String encodestr = RESUtil.parseByte2HexStr(RESUtil.enCrypt(str, RESUtil.RES_KEY));
		return encodestr;
	}
	
	public static String decrStr(String str) throws Exception {
		String decodestr = RESUtil.deCrypt(RESUtil.parseHexStr2Byte(str), RESUtil.RES_KEY);
		return decodestr;
	}

	public static String coverJSONToParam(JSONObject jo) {
		String paramStr = "";
		for(Object key:jo.keySet()) {
			String value = (String) jo.get(key);
			paramStr = paramStr + key + "=" + value + "&";
		}
		if(paramStr.endsWith("&")) {
			paramStr = paramStr.substring(0, paramStr.length() - 1);
		}
		return paramStr;
	}
	
	public static JSONObject createUrlJSON(String decodeUrl,String paramJsonStr,String method,String token) {
		LoginResult lr = new LoginResult();
		lr.setType("forware");
		lr.setResult(true);
		lr.setUrl(decodeUrl);
		lr.setMethod(method);
		lr.setParamJsonStr(paramJsonStr);
		lr.setToken(token);
		JSONObject result = JSONObject.fromObject(lr);
		return result;
	}
}
