package com.creator.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	public static String getClientUrl() throws IOException {
		Properties properties = new Properties();
	    // 使用ClassLoader加载properties配置文件生成对应的输入流
	    InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("SSOServer.properties");
	    // 使用properties对象加载输入流
	    properties.load(in);
	    //获取key对应的value值
	    String url = properties.getProperty("clientURl");
	    in.close();
	    return url;
	}
	
}
