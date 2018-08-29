package creator;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lhm.sso.util.PropertiesUtil;
import com.lhm.sso.util.SSOClientUtil;


@Controller
@RequestMapping(value="")
public class SSOClientAction {
	
	@RequestMapping(value="test")
	public String testUpload(HttpServletRequest request,HttpServletResponse response,String id) {

		return "test/test";
	}
	
	@RequestMapping("addCookie")
	public void addCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue) {
		try {
			SSOClientUtil.setCrossHeader(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				throw new Exception("server地址配置错误！");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		Cookie cookie = new Cookie(cookieName,cookieValue);
		cookie.setDomain(request.getServerName());
		cookie.setMaxAge(3600);
		response.addCookie(cookie);
	}
	
	@RequestMapping("removeCookie")
	public String removeCookie(HttpServletRequest request,HttpServletResponse response) {
		try {
			SSOClientUtil.setCrossHeader(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				throw new Exception("server地址配置错误！");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		request.getSession().removeAttribute("isLogin");
		Cookie cookie = new Cookie("CREATORTOKEN", null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return "ok";
	}
	
	@RequestMapping("logout")
	public void logout(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().removeAttribute("isLogin");
		String token = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if(cookie.getName().equals("CREATORTOKEN")) {
				token = cookie.getValue();
			}
		}
		String url;
		try {
			url = SSOClientUtil.getServerOrigin() + "/" + PropertiesUtil.getProperty("serverContextPath") + 
					"/sso/logout?url=" + SSOClientUtil.encrStr(PropertiesUtil.getProperty("logoutRedirect"));
			if(token != null) {
				url = url + "&token=" + token;
			}
			request.getSession().removeAttribute("CREATORTOKEN");
			response.sendRedirect(url);
		}catch (Exception e) {
			try {
				throw new Exception("配置文件信息错误！");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
}
