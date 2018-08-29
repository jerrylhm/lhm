package creator;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.db.group.UserGroupDao;
import com.creator.db.groupPermission.UserGroupPermission;
import com.creator.db.groupPermission.UserGroupPermissionDao;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.util.MD5Util;
import com.creator.util.SessionListener;

@Controller
@RequestMapping(value="login")
public class UserAction {
	public final static String LOGIN_MAP_KEY = "loginUserMap"; 
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserGroupDao userGroupDao;
	@Autowired
	private UserGroupPermissionDao userGroupPermissionDao;
	/*
	 * 跳转到登录页面
	 */
	@RequestMapping(value="")
	public String login(HttpServletRequest request,Model model) {
		
		return "user/login";
	}
	
	/*
	 * 验证登录
	 */
	@RequestMapping(value="verifyLogin",method=RequestMethod.POST)
	@ResponseBody
	public String verifyLogin(HttpServletRequest request,Model model,String username,String password) {
		//验证账号密码是否存在
		String md5Psw = MD5Util.md5(password);
		if(!userDao.isExist(username,md5Psw )) {
			return "0";
		}
		//查询用户
		List<User> userList = userDao.queryUserByUsernameAndPsw(username, md5Psw);
		if(userList.size() == 0) {
			return "0";
		}
		String ip = SessionListener.getIp2(request);
		HttpSession session = request.getSession();
		Map<String,Map<String,String>> loginMap = (Map<String, Map<String,String>>) session.getServletContext().getAttribute(LOGIN_MAP_KEY);
		if(loginMap != null) {
			Map<String,String> loginMsg = loginMap.get(userList.get(0).getUr_username());
			if(loginMsg != null) {
				if(loginMsg.get("sessionId") == session.getId()) {
					return "2";
				}else if(!ip.equals(loginMsg.get("ip"))){
					return "3";
				}
			} else {
				loginMsg = new HashMap<String,String>();
				loginMsg.put("sessionId", session.getId());
				loginMsg.put("ip", ip);
				loginMap.put(userList.get(0).getUr_username(), loginMsg);
			}	
		}else {
			loginMap = new ConcurrentHashMap<String,Map<String,String>>();
			Map<String,String> loginMsg = new HashMap<String,String>();
			loginMsg.put("sessionId", session.getId());
			loginMsg.put("ip", ip);
			loginMap.put(userList.get(0).getUr_username(), loginMsg);
			session.getServletContext().setAttribute(LOGIN_MAP_KEY, loginMap);
		}
		session.setAttribute("USERID", userList.get(0).getUr_id());
		session.setAttribute("USERNAME", userList.get(0).getUr_username());
		session.setAttribute("USERTYPE",userList.get(0).getUr_type());
		return "1";
	}
	
	/*
	 * 验证后台权限
	 */
	@RequestMapping(value="validUserPermission",method=RequestMethod.POST)
	@ResponseBody
	public String validUserPermission(HttpServletRequest request,Model model,String username) {
		List<User> users = userDao.queryUserListByUsername(username);
		if(users.size() > 0) {
			User user = users.get(0);
			//管理员无需验证
			if(user.getUr_type() == 1) {
				return "1";
			}
			//非管理员验证后台管理权限
			List<UserGroupPermission> ups =userGroupPermissionDao.queryByGroupId(user.getUr_group());
			if(ups.size() > 0) {
				UserGroupPermission up = ups.get(0);
				String[] upArray = up.getGp_perid().split(",");
				for (String perid : upArray) {
					if(perid.equals("admin_1")) {
						return "1";
					}
				}
			}
		}
		return "0";
	}
	
	/*
	 * 注销
	 */
	@RequestMapping(value="logout")
	public String logout(HttpServletRequest request,Model model) {
		//获取所有session，销毁
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("USERNAME");
		Map<String,String> loginMap = (Map<String, String>) session.getServletContext().getAttribute(LOGIN_MAP_KEY);
		if(loginMap != null) {
			loginMap.remove(username);
		}
		Enumeration<String> sessionNames =  session.getAttributeNames();
		while(sessionNames.hasMoreElements()) {
			String name = sessionNames.nextElement();
//			System.out.println("session:" + name);
			session.removeAttribute(name);
		}
		
		return "redirect:/login";
	}
	
}
