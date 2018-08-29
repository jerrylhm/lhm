package creator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.rest.CodeResult;
//解决跨域问题
@CrossOrigin(origins="*",maxAge=3600)
@Controller
@RequestMapping(value="test")
public class TestAction {
	@RequestMapping(value="")
	public String test() {
		return "index/test";
	}
	
	@RequestMapping(value="setToken")
	@ResponseBody
	public CodeResult setToken(String token,HttpServletRequest request,HttpServletResponse response) {
		System.out.println("token:" + token);
		HttpSession session = request.getSession();
		session.setAttribute("Token", token);
		//添加cookie
		Cookie cookie = new Cookie("Token", token);
		cookie.setPath("/");
		response.addCookie(cookie);
		System.out.println("setToken sessionId:" + session.getId());
		return CodeResult.ok();
	}
	
	@RequestMapping(value="getToken")
	@ResponseBody
	public CodeResult getToken(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		map.put("token", session.getAttribute("Token"));
		System.out.println("获取到的token:" + session.getAttribute("Token"));
		System.out.println(request.getContextPath());
		System.out.println("getToken sessionId:" + session.getId());
		return CodeResult.ok(map);
	}
}
