package creator;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.creator.common.util.MD5Util;
import com.creator.common.util.SystemUtils;
import com.creator.common.util.UploadUtil;
import com.creator.db.organization.Organization;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.rest.CodeEnum;
import com.creator.rest.CodeResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="personalSpace")
public class PersonalSpaceAction {
	//log4j打印
	Logger logger = Logger.getLogger(StudentTableAction.class);
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value= {"","index"})
	public String index(HttpServletRequest request, Model model) {
		Integer userId = (Integer) SystemUtils.getUserIdFromSession(request);
		if(userId != null) {
			User user = userDao.findById(userId).get(0);
			model.addAttribute("user", user);
			return "personalspace/index";
		}else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value="userEdit")
	public String userEdit(HttpServletRequest request, Model model) {
		Integer userId = (Integer) SystemUtils.getUserIdFromSession(request);
		if(userId != null) {
			User user = userDao.findById(userId).get(0);
			model.addAttribute("user", user);
		}
		return "personalspace/userEdit";
	}
	
	@RequestMapping(value="userImage")
	public String userImage(HttpServletRequest request, Model model) {
		Integer userId = (Integer) SystemUtils.getUserIdFromSession(request);
		if(userId != null) {
			User user = userDao.findById(userId).get(0);
			model.addAttribute("user", user);
		}
		return "personalspace/userImage";
	}
	
	@RequestMapping(value="verfifyUsernameExist")
	@ResponseBody
	public CodeResult verfifyUsernameExist(HttpServletRequest request, String username,Integer urid, Model model) {
		User user = userDao.findById(urid).get(0);
		if(username.equals(user.getUr_username())) {
			return CodeResult.ok();
		}
		
		if(userDao.findByUsername(username) != null) {
			return new CodeResult(CodeEnum.ERROR.getCode(), "该用户已存在");
		}else {
			return CodeResult.ok();
		}
	}
	
	@RequestMapping(value="updateBase")
	@ResponseBody
	public CodeResult updateBase(HttpServletRequest request, User user, Model model) {

		try {
			User user1 = userDao.findById(user.getUr_id()).get(0);
			user1.setUr_classid(user.getUr_classid());
			user1.setUr_email(user.getUr_email());
			user1.setUr_nickname(user.getUr_nickname());
			user1.setUr_phone(user.getUr_phone());
			user1.setUr_sex(user.getUr_sex());
			userDao.updateUser(user1);
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	@RequestMapping(value="uploadImage")
	@ResponseBody
	public CodeResult uploadImage(HttpServletRequest request,@RequestParam("image") CommonsMultipartFile image, Model model) {
		try {
			String ur_image = UploadUtil.uploadHeadImage(request, (String) SystemUtils.getUserNameFromSession(request));
			User user = userDao.findById((Integer) SystemUtils.getUserIdFromSession(request)).get(0);
			UploadUtil.deleteHeadImage(request, user.getUr_image());
			user.setUr_image(ur_image);
			userDao.updateUser(user);
	        return CodeResult.ok(ur_image);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	@RequestMapping(value="verfifyPsw")
	@ResponseBody
	public CodeResult verfifyPsw(HttpServletRequest request,Integer urid, String password_old, Model model) {
		try {

			User user = userDao.findById(urid).get(0);
			if(user.getUr_password().equals(MD5Util.md5(password_old))) {
				return CodeResult.ok();
			}else {
				return new CodeResult(CodeEnum.ERROR.getCode(), "旧密码不正确");
			}	   
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	@RequestMapping(value="updatePassword")
	@ResponseBody
	public CodeResult updatePassword(HttpServletRequest request,Integer urid, String password_new, Model model) {
		try {

			User user = userDao.findById(urid).get(0);
			user.setUr_password(MD5Util.md5(password_new));
			userDao.updateUser(user);
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
}
