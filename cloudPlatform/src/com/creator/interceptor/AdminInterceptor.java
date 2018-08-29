package com.creator.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.creator.constant.Constant;

public class AdminInterceptor implements HandlerInterceptor{

	public static final String SESSION_USERID = Constant.ADMIN_SESSION_KEY;
	public static final String SESSION_USERNAME = Constant.ADMIN_NAME_SESSION_KEY;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handlerMethod) throws Exception {
		Integer userId = (Integer) request.getSession().getAttribute(SESSION_USERID);
		if(userId != null) {
			return true;
		}else {
			response.sendRedirect("/cloud/admin/login");
			return false;
		}
	}

}
