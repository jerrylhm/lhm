package creator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.db.group.UserGroup;
import com.creator.db.group.UserGroupDao;
import com.creator.db.permission.PermissionDao;
import com.creator.db.tree.TreeDao;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.db.value.ValueDao;
import com.creator.util.MD5Util;

@Controller
@RequestMapping(value="admin")
public class AdminAction {
	@Autowired
	private UserDao userDao;
	@Autowired
	private TreeDao treeDao;
	@Autowired
	private ValueDao valueDao;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private UserGroupDao userGroupDao;
	
	private int index = 1;
	private int count;
	
	/*
	 * 后台主页
	 */
	@RequestMapping(value="index")
	public String index(HttpServletRequest request,Model model) {
		return "admin/index";
	}
	
	/*
	 * 后台管理页面
	 */
	@RequestMapping(value="userManage")
	public String userManage(HttpServletRequest request,Model model) {
		return "admin/userManage";
	}
	
	/*
	 * 分页显示用户
	 */
	@RequestMapping(value="showUser")
	public String showUser(HttpServletRequest request,Model model,String page,String searchValue) {
		int eachPageCount = 10;       //每页记录数
		int index = 1;      //当前页数
		if(page != null) {
			index = Integer.valueOf(page);
		}
		if(searchValue == null) {
			searchValue = "";
		}
		List<Map<String, Object>> userList = userDao.queryAllUsersByCondition(index, eachPageCount, searchValue);
		//System.out.println("记录数：" + userList.size());
		model.addAttribute("UserList", userList);
		
		int pageCount = 0;      //总页数
		//总记录数
		int count = userDao.countUserByCondition(searchValue);
		pageCount = (count + (eachPageCount -1)) / eachPageCount;
		
		model.addAttribute("Index", index);
		model.addAttribute("PageCount", pageCount);
		model.addAttribute("Count", count);
		model.addAttribute("SearchValue", searchValue);
		
		return "admin/showUser";
	}
	
	/*
	 * 添加用户
	 */
	@RequestMapping(value="addUser",method=RequestMethod.POST,produces="text/json;charset=UTF-8")
	@ResponseBody
	public String addUser(HttpServletRequest request,Model model,String username,String password,String confirm,int type) {
		/*
		System.out.println("用户名：" + username);
		System.out.println("密码：" + password);
		System.out.println("确认：" + confirm);
		System.out.println("类型：" + type);
		*/
		JSONObject result = new JSONObject();
		result.put("result", "1");
		//检查用户名
		if(!username.matches("^([0-9A-z]){5,16}$")) {
			result.put("result", 0);
			result.put("username", "用户名必须为5~16位的英文数字组合");
		}else if(userDao.isExistUsername(username)) {
			result.put("result", 0);
			result.put("username", "用户名已存在");
		}
		if(!password.matches("^([0-9A-z]){5,16}$")) {
			result.put("result", 0);
			result.put("password", "密码必须为5~16位的英文数字组合");
		}
		if(!password.equals(confirm)) {
			result.put("result", 0);
			result.put("confirm", "确认密码与密码不一致");
		}
		
		String strResult = result.getString("result");
		//添加用户
		if(strResult.equals("1")) {
			User user = new User();
			user.setUr_username(username);
			user.setUr_password(MD5Util.md5(password));
			user.setUr_type(type);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			user.setUr_datetime(format.format(new Date()));
			
			userDao.addUser(user);
		}
		
		return result.toString();
	}
	
	/*
	 * 修改用户
	 */
	@RequestMapping(value="updateUser",method = RequestMethod.POST)
	@ResponseBody
	public String updateUser(HttpServletRequest request,Model model,int id,int type,int reset) {
		/*
		System.out.println("id:" + id);
		System.out.println("type:" + type);
		System.out.println("reset:" + reset);
		*/
		
		List<User> userList = userDao.queryUserById(id);
		if(userList.size() == 0) {
			return "0";
		}
		
		User user = userList.get(0);
		user.setUr_type(type);
		if(reset == 1) {
			user.setUr_password(MD5Util.md5("123456"));
		}
		userDao.updateUserById(id, user);
		return "1";
	}
	
	/*
	 * 删除用户
	 */
	@RequestMapping(value="deleteUser",method=RequestMethod.POST)
	@ResponseBody
	public String deleteUser(HttpServletRequest request,Model model,String ids) {
		//System.out.println("ids:" + ids);
		if(!ids.contains(",")) {
			return "0";
		}
		String[] strIds = ids.split(",");
		for(int i=0;i<strIds.length-1;i++) {
			int id = Integer.valueOf(strIds[i]);
			//防止删除id=1的admin
			if(id == 1) {
				continue;
			}
			//第一步、删除对应的值
			valueDao.deleteValueByUserId(id);
			//第二步、删除对应的权限
			permissionDao.deletePermissionByUserid(id);
			//第三步、删除对应的树节点
			treeDao.deleteNodeByUserid(id);
			//第四步、删除用户
			userDao.deleteUserById(id);
		}
		return "1";
	}
	
	/*
	 * 修改密码
	 * 返回值：1、修改成功	0、用户未登录	2、原密码输入错误  	3、新密码不符合格式  		4、确认密码与新密码不一致
	 */
	@RequestMapping(value="updatePsw",method=RequestMethod.POST)
	@ResponseBody
	public String updatePsw(HttpServletRequest request,Model model,String oldPsw,String newPsw,String confirmPsw) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("USERID");
		if(userId == null) {
			return "0";
		}
		
		//查询原密码是否正确
		List<User> userList = userDao.queryUserById(userId);
		if(userList.size() <= 0) {
			return "0";
		}
		User user = userList.get(0);
		if(!user.getUr_password().equals(MD5Util.md5(oldPsw))) {
			return "2";
		}
		if(!newPsw.matches("^([0-9A-z]){5,16}$")) {
			return "3";
		}
		
		if(!newPsw.equals(confirmPsw)) {
			return "4";
		}
		
		user.setUr_password(MD5Util.md5(newPsw));
		userDao.updateUserById(userId, user);
		return "1";
	}
	
	/*
	 * 用户组管理
	 */
	@RequestMapping(value = "userGroupManage")
	public String UserGroupManage(HttpServletRequest request,Model model,String page){
		
		if(page == null){
			index = 1;
		}else{
			index = Integer.parseInt(page);
		}
		Integer userid = (Integer) request.getSession().getAttribute("USERID");
		Integer userType = (Integer) request.getSession().getAttribute("USERTYPE");
		List<Map<String, Object>> UserGroupList = userGroupDao.queryUserGroupByUserID(userid, userType, index, 10);
		int CountNum = userGroupDao.countUserGroupByUserID(userid, userType);
		count = (CountNum+9)/10;
		model.addAttribute("userType", userType);
		model.addAttribute("UserGroupList", UserGroupList);
		model.addAttribute("index", index);
		model.addAttribute("countPage", count);
		model.addAttribute("CountNum", CountNum);
		//查询树
		List<Map<String, Object>> TreeList = userGroupDao.queryTreeByUserIdAndType(userid, userType);
		model.addAttribute("TreeList", TreeList);
		//调出整棵树
		
		
		
		
		return "userGroup/index";
	}
	
	/*
	 * 增加用户组
	 */
	@RequestMapping(value = "addUserGroup",method = RequestMethod.POST)
	@ResponseBody
	public String AddUserGroup(HttpServletRequest request,Model model,String name,Integer treeid){
		Integer userid = (Integer) request.getSession().getAttribute("USERID");
		UserGroup userGroup = new UserGroup();
		userGroup.setUrg_name(name);
		userGroup.setUrg_treeid(treeid);
		userGroup.setUrg_userid(userid);
		String result = userGroupDao.addUserGroup(userGroup);
		if(result == "true"){
			return "1";
		}else{
			return "0";
		}
	}
	
	/*
	 * 删除用户组
	 */
	@RequestMapping(value = "deleteUserGroup",method = RequestMethod.POST)
	@ResponseBody
	public String DeleteUserGroup(HttpServletRequest request,Model model,String ids){
		String[] arr = ids.split(",");
		String result = null;
		for(int i=0;i<arr.length;i++){
			result = userGroupDao.deleteUserGroup(arr[i]);
		}
		if(result == "true"){
			return "true";
		}else{
			return "false";
		}
	}
	
	/*
	 * 修改用户组
	 */
	@RequestMapping(value = "updateUserGroup",method = RequestMethod.POST)
	@ResponseBody
	public String UpdateUserGroup(HttpServletRequest request,Model model,Integer id,String name,Integer treeid){
		String result = userGroupDao.updateUserGroup(id, name, treeid);
		if(result == "true"){
			return "true";
		}else{
			return "false";
		}
	}
	
	
	/*
	 * 用户审核
	 */
	@RequestMapping(value="auditUserManage")
	public String auditManage(HttpServletRequest request,Model model,String page) {
		model.addAttribute("title", "用户审核");
		model.addAttribute("href", "admin/userAudit");
		return "audit/auditManage";
	}
	
	/*
	 * 用户审核
	 */
	@RequestMapping(value="userAudit")
	public String userAudit(HttpServletRequest request,Model model,String page) {
		System.out.println(page);
		int eachPageCount = 10;       //每页记录数
		int index = 1;      //当前页数
		if(page != null) {
			index = Integer.valueOf(page);
		}
		
		int userid = (int) request.getSession().getAttribute("USERID");

		List<Map<String, Object>> userList = userDao.queryUserByGroupAndStateAndLoginId(userid,index, eachPageCount);
		//System.out.println("记录数：" + userList.size());
		model.addAttribute("UserList", userList);
		
		int pageCount = 0;      //总页数
		//总记录数
		int count = userDao.countUserByGroupAndStateAndLoginId(userid);
		pageCount = (count + (eachPageCount -1)) / eachPageCount;
		
		model.addAttribute("Index", index);
		model.addAttribute("PageCount", pageCount);
		model.addAttribute("Count", count);
		
		return "audit/userAudit";
	}
	
	

}
