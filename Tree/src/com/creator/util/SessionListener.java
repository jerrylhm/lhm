package com.creator.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.util.StringUtils;

import creator.UserAction;

public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		Map<String,Map<String,String>> loginMap = (Map<String, Map<String,String>>) session.getServletContext().getAttribute(UserAction.LOGIN_MAP_KEY);
		if(loginMap == null) {
			loginMap = new ConcurrentHashMap<String,Map<String,String>>();
			session.getServletContext().setAttribute(UserAction.LOGIN_MAP_KEY, loginMap);
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		String username = (String) session.getAttribute("USERNAME");
		Map<String,String> loginMap = (Map<String, String>) session.getServletContext().getAttribute(UserAction.LOGIN_MAP_KEY);
		if(username != null && loginMap != null) {
			loginMap.remove(username);
		}
	}

	public static String getIp2(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        System.out.println(request.getRemoteAddr());
        return request.getRemoteAddr();
    }
}
