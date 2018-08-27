package com.creator.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.creator.common.util.RedisUtil;
import com.creator.constant.Constant;
import com.creator.rest.CodeEnum;
import com.creator.rest.CodeResult;

import net.sf.json.JSONObject;

public class ApiInterceptor implements HandlerInterceptor{
	
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
		String token = request.getHeader("token");
		System.out.println(token);
		if(token !=null && RedisUtil.get(token) != null) {
			return true;
		}else {

			response.setHeader("Access-Control-Allow-Origin","*");  
			response.setCharacterEncoding("UTF-8");  
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = null ;
			try{
				
			    JSONObject res = JSONObject.fromObject(new CodeResult(CodeEnum.ERROR.getCode(), "数据获取失败！token失效或为空!"));
			    out = response.getWriter();
			    out.append(res.toString());
			
			}
			catch (Exception e){
			    e.printStackTrace();
			    response.sendError(500);
			}
			return false;	
		}
	}

}
