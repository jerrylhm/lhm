package com.creator.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.creator.constant.Constant;

import creator.AdminAction;

public class SystemUtils {

	static Logger logger = Logger.getLogger(AdminAction.class);
	/**
	 * 获取前台登录用户id
	 */
	public static Object getUserIdFromSession(HttpServletRequest request) {
		return request.getSession().getAttribute(Constant.USER_SESSION_KEY);
	}
	
	/**
	 * 获取前台登录用户用户名
	 */
	public static Object getUserNameFromSession(HttpServletRequest request) {
		return request.getSession().getAttribute(Constant.USER_NAME_SESSION_KEY);
	}
	
	/**
	 * 获取后台登录管理员id
	 */
	public static Object getAdminIdFromSession(HttpServletRequest request) {
		return request.getSession().getAttribute(Constant.ADMIN_SESSION_KEY);
	}
	
	/**
	 * 获取后台登录管理员用户名
	 */
	public static Object getAdminNameFromSession(HttpServletRequest request) {
		return request.getSession().getAttribute(Constant.ADMIN_NAME_SESSION_KEY);
	}
	
	/**
	 * 获取后台登录管理员用户名
	 */
	public static boolean removeUserFromSession(HttpServletRequest request) {
		try {
			request.getSession().setAttribute(Constant.USER_SESSION_KEY, null);
			request.getSession().setAttribute(Constant.USER_NAME_SESSION_KEY, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("清除前台登录用户session失败");
			return false;
		}
		
	}
}
