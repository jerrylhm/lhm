package creator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.creator.common.util.MD5Util;
import com.creator.common.util.UploadUtil;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.db.usergroup.UserGroup;
import com.creator.db.usergroup.UserGroupDao;
import com.creator.rest.CodeResult;
import com.creator.rest.ResultProcessor;


/**
 * 登录注册
 *
 */
@Controller
public class LoginAction extends BaseAction{
	private Logger logger = Logger.getLogger(LoginAction.class);
	private static final int TYPE_TEACHER = 2;             //教师
	private static final int TYPE_STUDENT = 3;            //学生
	private static final int TYPE_PARENT = 4;            //家长
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserGroupDao userGroupDao;
	/**
	 * 登录页面
	 */
	@RequestMapping(value={"login"})
	public String login() {
		return "common/login";
	}
	
	/**
	 * 注册页面
	 */
	@RequestMapping(value={"register"})
	public String register() {
		return "common/register";
	}
	
	/**
	 * 判断用户名是否存在
	 */
	@RequestMapping(value="isExistUsername")
	@ResponseBody
	public CodeResult isExistUsername(final String username) {
		logger.debug("username:" + username);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				User user = userDao.findByUsername(username);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("result", 0);
				if(user == null) {
					map.put("result", 1);
				}
				return CodeResult.ok(map);
			}
		});
		return codeResult;
	}
	
	/**
	 * 获取所有的用户组列表
	 */
	@RequestMapping(value="register/listUserGroup")
	@ResponseBody
	public CodeResult listUserGroup() {
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				List<UserGroup> groupList = userGroupDao.query();
				return CodeResult.ok(groupList);
			}
		});
		
		return codeResult;
	}
	
	/**
	 * 注册
	 */
	@RequestMapping(value="register/doRegister",method=RequestMethod.POST)
	@ResponseBody
	public CodeResult doRegister(final String username,final String password,final String email,
			final String nickname,final String phone,@RequestParam(defaultValue="0")final Integer sex,
			final String usergroup,final String userclass,final HttpServletRequest request) {
		logger.debug("username:" + username + "  password:" + password + " email:" + email + " phone:" + phone);
		logger.debug("sex:" + sex + " usergroup:" + usergroup + "  userclass:" + userclass);
		
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//判断用户名是否存在
				User user = userDao.findByUsername(username);
				if(user != null) {
					return CodeResult.error();
				}
				
				//上传图片
				String image = "upload/head-image/default_image.gif";
				try {
					String filename = UploadUtil.uploadHeadImage(request, username);
					if(filename != null) {
						image = filename;
					}
					logger.debug("filename:" + filename);
				} catch (Exception e) {
					e.printStackTrace();
					return CodeResult.error();
				}
				
				user = new User();
				user.setUr_username(username);
				user.setUr_password(MD5Util.md5(password));
				user.setUr_nickname(nickname);
				user.setUr_phone(phone);
				user.setUr_email(email);
				user.setUr_type(usergroup);
				user.setUr_image(image);
				user.setUr_classid(userclass);
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				user.setUr_createdate(dateFormat.format(new Date()));
				user.setUr_sex(sex);
				
				//不是学生，审核状态为未审核
				int status = 1;
				if(!(isRoles(TYPE_STUDENT,usergroup)) ){
					status = 0;
				}
				logger.debug("审核状态：" + status);
				user.setUr_status(status);
				
				//新增用户
				int id = userDao.addUser(user);
				String[] groups = usergroup.split(",");
				for(int i=0;i<groups.length;i++) {
					userGroupDao.addUserGroupToUser(id, Integer.valueOf(groups[i]));
				}
				return CodeResult.ok();
			}
		});
		return codeResult;
	}
	
	/*
	 * 判断用户组字符串是否只有角色
	 */
	private boolean isRoles(int role,String usergroup) {
		String[] groups = usergroup.split(",");
		for(String group: groups) {
			if(role != Integer.valueOf(group)) {
				return false;
			}
		}
		return true;
	}
}
