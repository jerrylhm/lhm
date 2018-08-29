package com.creator.common.util;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

public class MD5Util {
	/*
	 * 将字符串进行MD5加密
	 */
	public static String md5(String str) {
		String strMD5 = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			BASE64Encoder base64 = new BASE64Encoder();
			//加密后的字符串
			strMD5 = base64.encode(md.digest(str.getBytes("utf-8")));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
		return strMD5;
	}
	
}
