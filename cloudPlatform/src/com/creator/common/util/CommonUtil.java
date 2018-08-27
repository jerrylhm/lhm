package com.creator.common.util;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {
	
	public static String getProjectUrl(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ path + "/";
		return basePath;
	}
}
