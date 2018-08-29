package creator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.api.CodeEnum;
import com.creator.api.CodeHelper;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.util.ClientUtil;
import com.creator.util.MD5Util;

/**
 * App客户端接口,主要负责视频多路输入输出app接口
 *
 */
//解决跨域问题
@CrossOrigin(origins="*",maxAge=3600)
@Controller
@RequestMapping(value="app")
public class AppAction {
	private static final String CLASS_NAME = AppAction.class.getName() + ": ";
	private String loginUrl;             //app登录后发送的视频多路输入输出网页链接地址
	private Map<String,String> urlMap; 
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private TreeDao treeDao;
	
	/*
	@RequestMapping(value="login",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String login(String username,String password,HttpServletRequest request) {
		System.out.println(CLASS_NAME + "login" + "  param:" + username + "  " + password);
		
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		if(username == null || password == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_LOGIN_MESSAGE);
			return jsonObject.toString();
		}
		//查询用户
		List<Map<String,Object>> userList = userDao.queryUserByCondition(username, MD5Util.md5(password), 0);
		if(userList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_LOGIN_MESSAGE);
			return jsonObject.toString();
		}
		Map<String,Object> map = new HashMap<String, Object>();
		//第一次进入，获取app.properties属性配置
		if(loginUrl == null) {
			String basePath = request.getSession().getServletContext().getRealPath("/");
			String filePath = basePath + "WEB-INF/classes/app.properties";
			//加载配置文件
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(filePath));
				loginUrl = (String) properties.get("url");
				System.out.println(CLASS_NAME + loginUrl);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			System.out.println(CLASS_NAME + basePath);
		}
		map.put("url", loginUrl);
		jsonObject.put("data", map);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	*/
	/**
	 * app登录，登录成功返回视频主页链接
	 * @param username 用户名
	 * @param password 密码
	 * @return json字符串
	 * 返回json说明:
	 * code: 标识码
	 * msg: 说明信息
	 * data: 数据，类型json
	 * 示例：{"code":"0000","msg":"成功","data":{"id":6,"username":"appuser","url":"http://113.108.182.132:6080/dataView/mobileIndex"}}
	 */
	@RequestMapping(value="login",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String login(String username,String password,HttpServletRequest request) {
		System.out.println(CLASS_NAME + "login" + "  param:" + username + "  " + password);
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		//第一次加载配置文件
		if(urlMap == null) {
			String basePath = request.getSession().getServletContext().getRealPath("/");
			String filePath = basePath + "WEB-INF/classes/app.properties";
			//加载配置文件
			Properties properties = new Properties();
			urlMap = new HashMap<String,String>();
			try {
				properties.load(new FileInputStream(filePath));
				Set<Object> keySet = properties.keySet();
				for(Object key : keySet) {
					urlMap.put(String.valueOf(key), String.valueOf(properties.get(key)));
				}
			}catch(Exception e) {
				e.printStackTrace();
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_UNKNOW);
				System.out.println(CLASS_NAME + jsonObject.toString());
				return jsonObject.toString();
			}
		}
		System.out.println(CLASS_NAME + urlMap.toString());
		
		
		if(username == null || password == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_LOGIN_MESSAGE);
			return jsonObject.toString();
		}
		
		//查询用户
		List<Map<String,Object>> userList = userDao.queryByUsernameAndPassword(username, MD5Util.md5(password));
		if(userList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_LOGIN_MESSAGE);
			return jsonObject.toString();
		}
		//查询用户所在的项目id(treeid)
		int treeid = (int) userList.get(0).get("ur_treeid");
		System.out.println(CLASS_NAME + "treeid: " + treeid);
		String url = urlMap.get(String.valueOf(treeid));
		//结果集合，包含用户id、用户名、跳转url
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("id", userList.get(0).get("ur_id"));
		resultMap.put("username", userList.get(0).get("ur_username"));
		if(url == null) {
			url = "";
		}
		resultMap.put("url",url);
		jsonObject.put("data", resultMap);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * app注册用户
	 * @param username 用户名，格式要求5~16位英文数字组合
	 * @param password 密码，格式要求5~16位英文数字组合
	 * @param treeid 项目id，要求不能为空且为数字格式
	 * 
	 * @return 有错误：返回错误json；注册成功：返回注册成功的用户信息（id和用户名）
	 * data参数说明：类型json
	 * 错误：
	 * username: 用户名出现的错误
	 * password: 密码出现的错误
	 * treeid: 未选择项目提示
	 * 示例:{"code":"0014","msg":"注册信息有误","data":[],"dara":{"username":"用户名已存在","password":"密码必须为5~16位的英文数字组合","treeid":"请选择项目"}}
	 * 
	 * 注册成功：
	 * id : 用户id
	 * username: 用户名
	 * 示例：{"code":"0000","msg":"成功","data":{"id":7,"username":"appuser1"}}
	 * @throws Exception 
	 * 
	 */
	@RequestMapping(value="register",produces="text/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	//public String register(String username,String password,String treeid) {
	public String register(String username,String password,String treeid) throws Exception {
		
		System.out.println(CLASS_NAME + "register()" + " param:" + username + " " + password);
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_REG_MESSAGE);     //默认注册信息错误
		//检查用户名、密码是否符合要求
		Map<String,Object> error = new HashMap<String, Object>();       //错误集合
		
		if(username == null) {
			error.put("username", "用户名不能为空");
		}else if(!username.matches("^([0-9A-z]){5,16}$")) {
			error.put("username", "用户名必须为5~16位的英文数字组合");
		}else if(userDao.isExistUsername(username)) {
			error.put("username", "用户名已存在");
		}
		
		//检查密码
		if(password == null) {
			error.put("password", "密码不能为空");
		}else if(!password.matches("^([0-9A-z]){5,16}$")) {
			error.put("password", "密码必须为5~16位的英文数字组合");
		}
		
		//检查树id
		if(!NumberUtils.isNumber(treeid)) {
			error.put("treeid", "项目id不符合格式");
		}else {
			//检查树是否存在
			/*
			List<Tree> rootList = treeDao.querySonNode(0);
			boolean isExistRoot = false;
			for(Tree root : rootList) {
				if(root.getNode_id().intValue() == Integer.valueOf(treeid)) {
					isExistRoot = true;
				}
			}
			*/
			if(!treeDao.isExistRootById(Integer.valueOf(treeid))) {
				error.put("treeid", "项目不存在");
			}
			
		}
		if(!error.isEmpty()) {
			jsonObject.put("data", error);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		//注册成功
		jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		User user = new User();
		user.setUr_username(username);
		user.setUr_password(MD5Util.md5(password));
		user.setUr_datetime(dateFormat.format(new Date()));
		user.setUr_type(0);
		//暂时默认用户组id为0
		user.setUr_group(0);
		user.setUr_treeid(Integer.valueOf(treeid));
		int id = userDao.addUserNew(user);
		//查询用户
		List<User> userList = userDao.queryById(id);
		if(userList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_UNKNOW);
			return jsonObject.toString();
		}
		User userAdd = userList.get(0);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("id", userAdd.getUr_id());
		resultMap.put("username", userAdd.getUr_username());
		jsonObject.put("data", resultMap);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 获取所有项目列表
	 * @return 项目列表json字符串
	 * 参数说明：
	 * {code: String,msg: String,data: Array}
	 * data说明，类型Array
	 * id: 项目id(即树id)
	 * title: 项目标题
	 * 示例：{"code":"0000","msg":"成功","data":[{"id":149,"title":"项目1"},{"id":168,"title":"项目2"},{"id":189,"title":"项目3"}]}
	 */
	@RequestMapping(value="getTreeList",produces="text/json;charset=UTF-8") 
	@ResponseBody
	public String getTreeList() {
		System.out.println(CLASS_NAME + "getTreeList()");
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		List<Tree> treeList =  treeDao.querySonNode(0);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Tree tree : treeList) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", tree.getNode_id());
			map.put("title", tree.getNode_title());
			resultList.add(map);
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
}
