package creator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.creator.common.util.SystemUtils;
import com.creator.constant.Constant;
import com.creator.db.user.User;
import com.creator.db.user.UserDaoImpl;
import com.creator.interceptor.AdminInterceptor;

@Controller
@RequestMapping(value="")
public class IndexAction {
	@Autowired
	private UserDaoImpl userDao;
	
	/**
	 * 认证中心主页
	 * @return
	 */
	@RequestMapping(value="index")
	public String index(HttpServletRequest request, Model model) {
		Integer userId = (Integer) SystemUtils.getUserIdFromSession(request);
		if(userId != null) {
			List<User> ls = userDao.findById(userId);
			if(ls.size() > 0) {
				User user = ls.get(0);
				model.addAttribute("user", user);
			}
		}
		return "index/index";
	}
	
	/**
	 * 关于我们
	 * @return
	 */
	@RequestMapping(value="introduce")
	public String introduce() {
		return "index/introduce";
	}
}
