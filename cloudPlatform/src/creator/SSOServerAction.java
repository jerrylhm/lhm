package creator;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.common.util.LoginResult;
import com.creator.common.util.LoginUtil;
import com.creator.common.util.RedisUtil;
import com.creator.common.util.SSOServerUtil;
import com.creator.common.util.SystemUtils;
import com.creator.constant.Constant;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value="sso")
public class SSOServerAction {
	@Autowired
	private UserDao userDao;

	
	/**
	 * 接收认证中心或子系统的登录请求
	 * @param request
	 * @param url 子系统跳转url
	 * @param params 子系统跳转参数
	 * @param method 子系统跳转请求类型
	 * @param model
	 * @return
	 */
	@RequestMapping(value="login")
	public String login(HttpServletRequest request,String url,String params,String method,Model model) {
		try {
			model.addAttribute("url", url);
			model.addAttribute("params", params);
			model.addAttribute("method", method);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "common/login";
	}
	
	/**
	 * 验证登录用户名和密码
	 * @param request
	 * @param response
	 * @param username 用户名
	 * @param password 密码
	 * @param url 子系统跳转url
	 * @param params 子系统跳转参数
	 * @param method 子系统跳转请求类型
	 * @param model
	 * @return
	 */
	@RequestMapping(value="loginUser",
			produces="text/json;charset=UTF-8",
			method=RequestMethod.POST)
	@ResponseBody
	public String loginUser(HttpServletRequest request,HttpServletResponse response,String username,String password,
			String url,String params,String method,Model model) {
		try {
			model.addAttribute("url", url);
			model.addAttribute("params", params);
			model.addAttribute("method", method);
			
			//验证失败
			if(!LoginUtil.checkPassword(username, password)) {
				LoginResult lr = new LoginResult();
				lr.setResult(false);
				JSONObject result = JSONObject.fromObject(lr);
				return result.toString();
			
			}
			//设置session
			//根据用户名查询用户
			User user = userDao.findByUsername(username);
			request.getSession().setAttribute(Constant.USER_SESSION_KEY, user.getUr_id());
			request.getSession().setAttribute(Constant.USER_NAME_SESSION_KEY, user.getUr_username());
			//验证成功，属于认证中心请求
			if(url != null && !url.equals("")){
				
				String token = UUID.randomUUID().toString();
				//添加token缓存，默认有效时间为1小时
				RedisUtil.add(token, username);
				//设置认证中心登录成功Session
				request.getSession().setAttribute("isLogin", true);
				request.getSession().setAttribute("token", token);
				//对子系统跳转链接和参数进行解码
				String decodeUrl = SSOServerUtil.decrStr(url);
				String decodeParam = SSOServerUtil.decrStr(params);
				//把解码后的url参数转换为json格式字符串
				String paramJsonStr = JSONObject.fromObject(decodeParam).toString();
				//生成返回值
				JSONObject result = SSOServerUtil.createUrlJSON(decodeUrl, paramJsonStr, method, token);
				return result.toString();
				
			//验证成功，属于子系统请求
			}else {
				String token = UUID.randomUUID().toString();
				//添加token缓存，默认有效时间为1小时
				RedisUtil.add(token, username);
				//设置认证中心登录成功Session
				request.getSession().setAttribute("isLogin", true);
				request.getSession().setAttribute("token", token);
				//生成返回值
				LoginResult lr = new LoginResult();
				//验证结果为true
				lr.setResult(true);
				//登录令牌
				lr.setToken(token);
				//请求类型为认证中心请求
				lr.setType("normal");
				//生成返回值
				JSONObject result = JSONObject.fromObject(lr);
				return result.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 验证登录令牌
	 * @param request
	 * @param response
	 * @param url 子系统跳转url
	 * @param params 子系统跳转参数
	 * @param method 子系统跳转请求类型
	 * @param token 登录令牌
	 * @param model
	 * @return
	 */
	@RequestMapping("verify")
	public String verify(HttpServletRequest request,HttpServletResponse response,String url,String method,String params,String token,Model model) {
		try {		
			String requestUrl = SSOServerUtil.decrStr(url);
			String requestParam = SSOServerUtil.decrStr(params);
			model.addAttribute("requestUrl", requestUrl);
			model.addAttribute("requestParam", requestParam);
			model.addAttribute("requestMethod", method);
			model.addAttribute("token", token);
			String username = RedisUtil.get(token);
			if(request.getSession().getAttribute("isLogin") != null && 
					username != null && !username.equals("")) {
				RedisUtil.add(token, username);
				return "sso/verify";
			}else {
				if(username == null) {
					model.addAttribute("msg", "该用户登录已失效，请重新登录！");
				}
				model.addAttribute("url", url);
				model.addAttribute("params", params);
				model.addAttribute("method", method);
				return "common/login";
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 注销
	 * @param request
	 * @param response
	 * @param url 子系统注销成功跳转url
	 * @param token	登录令牌
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("logout")
	public String logout(HttpServletRequest request,HttpServletResponse response,String url,String token,Model model) throws Exception {
		if(url != null && !url.equals("")) {
			model.addAttribute("url", SSOServerUtil.decrStr(url));
		}
		if(token != null && !token.equals("")) {
			RedisUtil.delete(token);
		}
		request.getSession().removeAttribute("isLogin");
		SystemUtils.removeUserFromSession(request);
		return "sso/logout";
	}
	
	/**
	 * 注销成功页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("logoutSuccess")
	public String logoutSuccess(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
		return "sso/logoutSuccess";
	}

	@RequestMapping("verifyFail")
	public String verifyFail(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
		return "sso/verifyFail";
	}
}
