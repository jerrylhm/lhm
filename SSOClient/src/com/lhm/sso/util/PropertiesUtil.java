package com.lhm.sso.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 对配置文件SSOClient的操作
 * @author LHMMM
 *
 */
public class PropertiesUtil {

	/**
	 * 获取配置文件的值
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static String getProperty(String key) throws IOException {
		Properties properties = new Properties();
	    InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("SSOClient.properties");
	    properties.load(in);
	    String url = properties.getProperty(key);
	    in.close();
	    return url;
	}
	
}
