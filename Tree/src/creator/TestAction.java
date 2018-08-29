package creator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.creator.db.permission.Permission;
import com.creator.db.permission.PermissionDao;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.util.ZTree;

@Controller
@RequestMapping(value="")
public class TestAction {

	@Autowired
	private TreeDao treeDao;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value="test")
	public String test(HttpServletRequest request,Model model) {
		
		return "test/test";
	}
	@RequestMapping(value="test1")
	public String test1(HttpServletRequest request,Model model) {
		
		return "test/test1";
	}
	@RequestMapping(value="test2")
	public String test2(HttpServletRequest request,Model model) {

		return "test/test2";
	}		

	@RequestMapping(value="test4")
	public String test4(HttpServletRequest request,Model model) {
		List<Permission> ls = permissionDao.queryCreatorsPermission(1);
		List<ZTree> zTrees = new ArrayList<ZTree>();
		List<User> users = userDao.queryAllUser();
		for (Permission permission : ls) {
			String[] nodeIds = permission.getUrnode_nodeid().split(",");
			for (String nodeId : nodeIds) {				
				Tree tree = treeDao.queryById(Integer.valueOf(nodeId)).get(0);
				ZTree ztree = new ZTree();
				ztree.setId(tree.getNode_id());
				ztree.setpId(tree.getNode_pid());
				ztree.setName(tree.getNode_name());
				ztree.setUrl(tree.getNode_url());
				zTrees.add(ztree);
			}
		}
		JSONArray jsonArray = new JSONArray();
		jsonArray.addAll(zTrees);
		model.addAttribute("ztree", jsonArray);
        for (User user : users) {
			if(user.getUr_id() == 1) {
				users.remove(user);
				break;
			}
		}
		model.addAttribute("users", users);
		return "test/test4";
	}
	
	@RequestMapping(value="test5")
	public String test5(HttpServletRequest request,Model model) {
		return "test/test5";
	}
	
	@RequestMapping(value="testUpload")
	public String testUpload() {
		return "test/testUpload";
	}
}
