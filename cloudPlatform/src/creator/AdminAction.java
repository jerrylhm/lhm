package creator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.common.util.MD5Util;
import com.creator.common.util.StringUtil;
import com.creator.constant.Static;
import com.creator.db.application.Application;
import com.creator.db.application.ApplicationDao;
import com.creator.db.permission.Permission;
import com.creator.db.permission.PermissionDao;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.db.usergroup.UserGroup;
import com.creator.db.usergroup.UserGroupDao;
import com.creator.interceptor.AdminInterceptor;
import com.creator.rest.CodeEnum;
import com.creator.rest.CodeResult;
import com.creator.rest.ResultProcessor;

/**
 * 后台管理
 *
 */
@Controller
@RequestMapping(value="admin")
public class AdminAction extends BaseAction{
	
	private int index = 1;
	private int count;
	
	//log4j打印
	Logger logger = Logger.getLogger(AdminAction.class);
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserGroupDao userGroupDao;
	
	@Autowired
	private PermissionDao permissionDao;
	
	@Autowired
	private ApplicationDao applicationDao;
	
	/*
	 * 后台首页
	 */
	@RequestMapping(value={"","index"})
	public String index(HttpServletRequest request,Model model) {
		Integer userId = (Integer) request.getSession().getAttribute(AdminInterceptor.SESSION_USERID);
		if(userId != null) {
			User user = userDao.findById(userId).get(0);
			model.addAttribute("userId", userId);
			model.addAttribute("userName", user.getUr_nickname());
			model.addAttribute("userImage", user.getUr_image());
		}
		return "admin/index";
	}
	
	/**
	 * 登录页面
	 */
	@RequestMapping(value={"login"})
	public String login() {

		return "admin/login";
	}
	
	/**
	 * 注销页面
	 */
	@RequestMapping(value={"logout"})
	public String logout(HttpServletRequest request) {
		request.getSession().setAttribute(AdminInterceptor.SESSION_USERID, null);
		request.getSession().setAttribute(AdminInterceptor.SESSION_USERNAME, null);
		return "admin/login";
	}
	
	/**
	 * 验证用户登录信息
	 */
	@RequestMapping(value={"verifyUser"})
	@ResponseBody
	public CodeResult verifyUser(HttpServletRequest request,String username,String password) {
		try {
			User user = userDao.findByUsername(username);
			if(user == null) {
				return new CodeResult(CodeEnum.ERROR.getCode(), "用户不存在!");
			}
			if(!MD5Util.md5(password).equals(user.getUr_password())) {
				return new CodeResult(CodeEnum.ERROR.getCode(), "用户密码错误!");
			}
			if(user.getUr_status() == 0) {
				return new CodeResult(CodeEnum.ERROR.getCode(), "用户审核未通过!");
			}
			request.getSession().setAttribute(AdminInterceptor.SESSION_USERID, user.getUr_id());
			request.getSession().setAttribute(AdminInterceptor.SESSION_USERNAME, user.getUr_username());
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/*
	 * 后台主页内容
	 */
	@RequestMapping(value="indexContent")
	public String indexContent() {
		return "admin/index_content";
	}
	
	
	/** 系统用户开始 **/
	
	/*
	 * 系统用户页面入口
	 */
	@RequestMapping(value = "userManage")
	public String userManage(Model model){
		return "user/userManage";
	}
	
	/** 系统用户结束 **/
	
	
	/*
	 * 用户审核
	 */
	@RequestMapping(value = "userCheck")
	public String userCheck(HttpServletRequest request,Model model){
		
		return "user/userCheck";
	}
	
	/*
	 * 用户审核列表
	 */
	@RequestMapping(value = "UserTables")
	public String userTables(HttpServletRequest request,Model model,String page,String queryValue,String date,String role,Integer status){
		
		if(page == null){
			index = 1;
		}else{
			index = Integer.parseInt(page);
		}
		
		if(queryValue == null){
			queryValue = "#null";
		}
		
		if(date == null){
			date = "0";
		}
		
		if(role == null){
			role = "0";
		}
		
		if(status == null){
			status = 0;
		}
		
		List<Map<String, Object>> AllUserList = userDao.queryAllListUser(index,9,queryValue,date,role,status);
		int countNum = userDao.countCheckUser(queryValue,date,role,status);
		count = (countNum+8)/9;
		
		model.addAttribute("index", index);
		model.addAttribute("count", count);
		model.addAttribute("AllUserList", AllUserList);
		model.addAttribute("status", status);
		return "user/userTables";
	}
	
	/*
	 * 单个审核通过
	 */
	@RequestMapping(value = "ChangeSingleUserStatus",method = RequestMethod.POST)
	@ResponseBody
	public String changeSingleUserStatus(HttpServletRequest request,Model model,int id){
		String result = userDao.changeUserStatus(id,1);
		if(result == "true"){
			return "1";
		}else{
			return "0";
		}
		
	}
	
	/*
	 * 批量审核通过
	 */
	@RequestMapping(value = "ChangeAllUserStatus",method = RequestMethod.POST)
	@ResponseBody
	public String changeAllUserStatus(HttpServletRequest request,Model model,String idAll){
		String []arr = idAll.split(",");
		String result = null;
		for(int i=0;i<arr.length;i++){
			result = userDao.changeUserStatus(Integer.parseInt(arr[i]),1);
		}
		
		if(result == "true"){
			return "1";
		}else{
			return "0";
		}
		
	}
	
	/*
	 * 删除单个注册用户
	 */
	@RequestMapping(value = "DeleteSingleUser",method = RequestMethod.POST)
	@ResponseBody
	public String deleteSingleUser(HttpServletRequest request,Model model,int id){
		Boolean result = userDao.deleteById(id);
		if(result == true){
			return "1";
		}else{
			return "0";
		}
	}
	
	/*
	 * 批量删除注册用户
	 */
	@RequestMapping(value = "DeleteAllUser",method = RequestMethod.POST)
	@ResponseBody
	public String deleteAllUser(HttpServletRequest request,Model model,String idAll){
		String []arr = idAll.split(",");
		Boolean result = null;
		for(int i=0;i<arr.length;i++){
			result = userDao.deleteById(Integer.parseInt(arr[i]));
		}
		if(result == true){
			return "1";
		}else{
			return "0";
		}
		
	}
	
	/*
	 * 单个取消通过审核
	 */
	@RequestMapping(value = "CancelChangeSingleUserStatus",method = RequestMethod.POST)
	@ResponseBody
	public String cancelChangeSingleUserStatus(HttpServletRequest request,Model model,int id){
		String result = userDao.changeUserStatus(id, 0);
		if(result == "true"){
			return "1";
		}else{
			return "0";
		}
	}
	
	/*
	 * 批量取消审核通过
	 */
	@RequestMapping(value = "CancelChangeAllUserStatus",method = RequestMethod.POST)
	@ResponseBody
	public String cancelChangeAllUserStatus(HttpServletRequest request,Model model,String idAll){
		String []arr = idAll.split(",");
		String result = null;
		for(int i=0;i<arr.length;i++){
			result = userDao.changeUserStatus(Integer.parseInt(arr[i]), 0);
		}
		if(result == "true"){
			return "1";
		}else{
			return "0";
		}
	}
	
	
	/*
	 * 获取访问人数
	 */
	@RequestMapping(value="getVisitor")
	@ResponseBody
	public CodeResult getVisitor() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("num", ++Static.adminVisitor);
		return CodeResult.ok(map);
	}
	
/***************************************用户组管理******************************************************/
	
	/**
	 * 进入用户组管理页面
	 */
	@RequestMapping(value="userGroupManage")
	public String userGroupManage() {
		return "user/userGroupManage";
	}
	
	/**
	 * 分页查询用户组列表
	 */
	@RequestMapping(value="getUserGroupPage")
	@ResponseBody
	public CodeResult getUserGroupPage(@RequestParam(defaultValue="1")final Integer index,
			@RequestParam(defaultValue="10")final Integer count) {
		logger.debug("index:" + index + " count:" + count);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				if(index < 1) {
					return CodeResult.error();
				}
				Map<String,Object> resultMap = new HashMap<String,Object>();
				List<UserGroup> groupList = userGroupDao.listByPage(index, count);
				resultMap.put("usergroup", groupList);
				
				int groupCount = userGroupDao.countUserGroup();            //总记录数
				int pageCount = (groupCount + count - 1) / count;           //总页数
				resultMap.put("index", index);
				resultMap.put("pageCount", pageCount);
				resultMap.put("groupCount", groupCount);
				resultMap.put("count", count);
				return CodeResult.ok(resultMap);
			}
		});
		
		return codeResult;
	}
	
	/**
	 * 分页查询权限列表
	 */
	@RequestMapping(value="listPermissionPage")
	@ResponseBody
	public CodeResult listPermissionPage(@RequestParam(defaultValue="1")final Integer index,
			@RequestParam(defaultValue="10")final Integer count) {
		logger.debug("index:" + index + " count:" + count);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				if(index < 1) {
					return CodeResult.error();
				}
				Map<String,Object> resultMap = new HashMap<String,Object>();
				List<Permission> permissionList = permissionDao.listByPage(index, count);
				resultMap.put("permission", permissionList);
				
				int recordCount = permissionDao.countPermission();
				int pageCount = (recordCount + count - 1) / count;           //总页数
				resultMap.put("index", index);
				resultMap.put("pageCount", pageCount);
				resultMap.put("recordCount", recordCount);
				resultMap.put("count", count);
				return CodeResult.ok(resultMap);
			}
		});
		return codeResult;
	}
	
	/**
	 * 查询所有的权限列表
	 */
	@RequestMapping(value="listPermission")
	@ResponseBody
	public CodeResult listPermission() {
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				List<Permission> permissionList = permissionDao.listPermission();
				return CodeResult.ok(permissionList);
			}
		});
		return codeResult;
	}
	
	/**
	 * 查询所有应用列表
	 */
	@RequestMapping(value="listApplication")
	@ResponseBody
	public CodeResult listApplication() {
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				List<Application> appList = applicationDao.query();
				return CodeResult.ok(appList);
			}
		});
		return codeResult;
	}
	
	/**
	 * 新增用户组
	 */
	@RequestMapping(value="addUsergroup")
	@ResponseBody
	public CodeResult addUsergroup(final String name) {
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//验证用户组名是否存在
				boolean isExist = userGroupDao.isExistByNameAndId(name, 0);
				if(isExist) {
					return new CodeResult("1001","名称已存在");
				}
				//新增用户组
				UserGroup userGroup = new UserGroup();
				userGroup.setUg_name(name);
				userGroup.setUg_permissionid("");
				userGroupDao.insert(userGroup);
				return CodeResult.ok();
			}
		});
		return codeResult;
	}
	
	/**
	 * 修改用户组
	 */
	@RequestMapping(value="updateUsergroup")
	@ResponseBody
	public CodeResult updateUsergroup(final Integer id,final String name) {
		logger.debug("id:" + id + "  name:" + name);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//验证用户组名是否存在
				boolean isExist = userGroupDao.isExistByNameAndId(name, id);
				if(isExist) {
					return new CodeResult("1001","名称已存在");
				}
				//修改用户组
				UserGroup userGroup = userGroupDao.findById(id);
				if(userGroup == null) {
					return CodeResult.error();
				}
				userGroup.setUg_name(name);
				userGroupDao.updateById(userGroup);
				return CodeResult.ok(userGroup);
			}
		});
		return codeResult;
	}
	
	/**
	 * 删除用户组
	 */
	@RequestMapping(value="deleteUserGroup")
	@ResponseBody
	public CodeResult deleteUserGroup(final Integer id) {
		logger.debug("id:" + id);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//根据id删除用户组
				userGroupDao.deleteById(id);
				
				//删除用户组-用户中间表数据
				userGroupDao.deleteByUgid(id);
				return CodeResult.ok();
			}
		});
		return codeResult;
	}
	
	
	/**
	 * 修改用户组权限
	 * @param id 用户组id
	 * @param permissions 要修改的权限数组
	 * @param isAuth 添加或移除权限,true：添加   false:移除
	 * @return
	 */
	@RequestMapping(value="updateGroupPermission")
	@ResponseBody
	public CodeResult updateGroupPermission(final Integer id,
			@RequestParam(value="permissions[]")final String[] permissions,final Boolean isAuth) {
		logger.debug("id:" + id + "   permissions:" + permissions + "   isAuth:" + isAuth);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				//获取用户组信息
				UserGroup userGroup = userGroupDao.findById(id);
				if(userGroup == null) {
					return CodeResult.error();
				}
				//用户组原来的权限字符串
				String ug_permissionid = userGroup.getUg_permissionid();
				//原来权限数组
				String[] oldPermssions = null;
				if(ug_permissionid != null && !("").equals(ug_permissionid)) {
					oldPermssions = ug_permissionid.split(",");
				}
				//请求参数转字符串
				String permissionParam = StringUtil.arrayToString(permissions);
				
				//修改后的权限字符串
				String newPermissionid = "";
				if(isAuth) {
					//添加权限
					if(oldPermssions == null) {
						newPermissionid = permissionParam;
					}else {
						Set<String> idSet = StringUtil.arrayToSet(oldPermssions);
						for(int i=0;i<permissions.length;i++) {
							idSet.add(permissions[i]);
						}
						newPermissionid = StringUtil.collectionToString(idSet);
					}
					
				}else {
					//移除权限
					if(oldPermssions != null) {
						Set<String> idSet = StringUtil.arrayToSet(oldPermssions);
						for(String id: permissions) {
							if(idSet.contains(id)) {
								//如果有该id，则从set中移除
								idSet.remove(id);
							}
						}
						newPermissionid = StringUtil.collectionToString(idSet);
					}
				}
				logger.debug("修改后的权限：" + newPermissionid);
				userGroup.setUg_permissionid(newPermissionid);
				userGroupDao.updateById(userGroup);
				return CodeResult.ok(userGroup);
			}
		});
		return codeResult;
	}
}

	
