package com.lhm.sso.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lhm.sso.util.PropertiesUtil;
import com.lhm.sso.util.SSOClientUtil;

import net.sf.json.JSONObject;

public class LoginInterceptor implements HandlerInterceptor{

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
		//请求类型
		String requestMethod = request.getMethod();
		//登录令牌
		String token = (String) request.getParameter("CREATORTOKEN");
		//跳转url
		String url = request.getRequestURL().toString();
		//请求参数
		String param = SSOClientUtil.coverParamToJSON(request);
		
		//加密后的url
		String encodeUrl = SSOClientUtil.encrStr(url);
		//加密后的参数
		String encodeParam = SSOClientUtil.encrStr(param);
		//cookie保存的token
		String cookieToken = SSOClientUtil.getTokenFromCookie(request);

		if(request.getParameter("CREATORISOK") != null && request.getParameter("CREATORISOK").equals(cookieToken)) {
			request.getSession().setAttribute("isLogin",true);
			if(requestMethod.equalsIgnoreCase("get")) {
				String paramStr = "";
				JSONObject paramJSON = JSONObject.fromObject(param);
				for(Object key:paramJSON.keySet()) {
					if(!key.equals("CREATORISOK") && !key.equals("CREATORTOKEN")) {
						paramStr = paramStr + key + "=" + paramJSON.get(key) + "&";
					}
				}
				if(paramStr.endsWith("&")) {
					paramStr = paramStr.substring(0, paramStr.length() - 1);
				}
				String newRequestUrl = "";
				if(paramStr.equals("")) {
					newRequestUrl = url + paramStr;
				}else {
					newRequestUrl = url + "?" + paramStr;
				}
				response.sendRedirect(newRequestUrl);
			}
			return false;
		}
		if(request.getSession().getAttribute("isLogin") != null) {
			return true;
		}
		if(token == null) {
			token = cookieToken;
		}
		String serverUrl = SSOClientUtil.getServerOrigin() + "/" + PropertiesUtil.getProperty("serverContextPath");
		if(token != null) {
			response.sendRedirect(serverUrl + "/sso/verify?url=" + encodeUrl + "&params=" + encodeParam + "&method=" + requestMethod + "&token=" + token);
		}else {
			response.sendRedirect(serverUrl + "/sso/login?url=" + encodeUrl + "&params=" + encodeParam + "&method=" + requestMethod);
		}
		return false;
	}

}
