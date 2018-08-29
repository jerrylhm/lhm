package creator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.creator.db.clientpermission.ClientPermissionDao;
import com.creator.db.groupPermission.UserGroupPermissionDao;
import com.creator.db.permission.Permission;
import com.creator.db.permission.PermissionDao;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.user.UserDao;
import com.creator.db.value.ValueDao;
import com.creator.util.ZTree;

@Controller
@RequestMapping(value = "permission")
public class PermissionAction {
	@Autowired
	private TreeDao treeDao;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired 
	private UserDao userDao;
	@Autowired
	private ValueDao valueDao;
	@Autowired
	private ClientPermissionDao clientPermissionDao;
	@Autowired
	private UserGroupPermissionDao userGroupPermissionDao;
	
	private Integer Treeid;
	
	@RequestMapping(value = "")
	public String Index(HttpServletRequest request,Model model,Integer id,Integer groupid){
		JSONArray jsonArray = new JSONArray();
		if(id != null){
			Tree tree = treeDao.queryById(id).get(0);
	        model.addAttribute("tree", tree);            
	        int userid = (int) request.getSession().getAttribute("USERID");
	        //获取在页面初始化ztree的json对象
	        List<ZTree> zTrees =getTree(request,userid,id);
			jsonArray.addAll(zTrees);
		}
		model.addAttribute("ztree", jsonArray);
		model.addAttribute("TreeID", id);
		model.addAttribute("GroupID", groupid);
		return "permission/index";
	}
	
	public List<ZTree> getTree(HttpServletRequest request,Integer userid,Integer treeId) {
		//项目路径
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ path + "/";
		 //返回到前台的zTree的json数据
        List<ZTree> zTrees = new ArrayList<ZTree>();
        
        //获取该用户对该树的权限
        List<Permission> ls = permissionDao.queryByUserIdAndtreeId(userid, treeId);
        if(ls.size() <= 0) {
        	return null;
        }
        //用户对该树的权限
        Permission permission = ls.get(0);
        //如果这棵树是该用户创建的
        if(permission.getUrnode_iscreator() == 1) {
        	//该树的所有节点信息
        	List<Tree> trees = treeDao.queryByTreeId(treeId);
        	for (Tree tree2 : trees) {
        		//把节点信息放入ztree对象里面
        		ZTree ztree = new ZTree();
				ztree.setId(tree2.getNode_id());
				ztree.setpId(tree2.getNode_pid());
				ztree.setName(tree2.getNode_name());
				//点击节点跳转的地址（暂时无法使用）
				ztree.setUrl(tree2.getNode_url());
				//点击节点跳转的地址（可以使用）
				ztree.setMyUrl(ztree.getUrl());
				//跳转窗口
				ztree.setTarget("treeValue");
				//是否拥有该节点权限
				ztree.setHasP(true);
				//是否是该树的创建者
				ztree.setIsCreator(true);
				zTrees.add(ztree);
			}
        	//如果该树不是该用户创建的
        } else {	
        	//该树的所有节点信息
        	List<Tree> trees = treeDao.queryByTreeId(treeId);
        	String[] nodeIds = permission.getUrnode_nodeid().split(","); 
        	for (Tree tree : trees) {  
        		//把节点信息放入ztree对象里面
				ZTree ztree = new ZTree();
				ztree.setId(tree.getNode_id());
				ztree.setpId(tree.getNode_pid());
				ztree.setName(tree.getNode_name());
				ztree.setUrl(tree.getNode_url());
				ztree.setMyUrl(ztree.getUrl());
				ztree.setTarget("treeValue");
				ztree.setHasP(false);
				//如果用户拥有该节点权限，则hasP为true
        		for (String nodeId : nodeIds) {	
					if(tree.getNode_id().equals(Integer.valueOf(nodeId))) {
						ztree.setHasP(true);
						ztree.setIsCreator(false);	
						break;
					}   					  
        		}
        		zTrees.add(ztree);
			}     	       				
        }
        //返回ztree的简单json数据
        return zTrees;
	}
	
	//某一节点详细弹窗
	@RequestMapping(value = "detailPermission")
	public String DetailPermission(HttpServletRequest request,Model model,Integer id,String name, Integer treeid,Integer groupid){
		List<Map<String, Object>> ClientList = null;
		if(id == 0){	
			name = treeDao.queryById(treeid).get(0).getNode_name();
			id = treeid;
			Treeid = treeid;
		}
		ClientList = clientPermissionDao.queryClientPermission();
		List<Map<String, Object>> PermissionList = userGroupPermissionDao.queryNodePermission(id);
		model.addAttribute("nodeid", id);
		model.addAttribute("name", name);
		model.addAttribute("ClientList", ClientList);
		if(PermissionList.size() > 0){
			model.addAttribute("NodePermission", PermissionList.get(0).get("gp_perid"));
		}
		model.addAttribute("groupid", groupid);
		model.addAttribute("Treeid", Treeid);
		return "permission/detailPermission";
	}
	
	/*
	 * 修改某一节点权限
	 */
	@RequestMapping(value = "changePermission",method = RequestMethod.POST)
	public String ChangePermission(HttpServletRequest request,Model model,String idAll,Integer nodeid,Integer groupid){
		String result = userGroupPermissionDao.updatePermissionByNodeAndGroup(nodeid, groupid, idAll, Treeid);
		if(result == "true"){
			return "true";
		}else{
			return "false";
		}
		
	}
	
}
