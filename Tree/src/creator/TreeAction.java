package creator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.db.group.UserGroup;
import com.creator.db.group.UserGroupDao;
import com.creator.db.group.UserGroupDto;
import com.creator.db.groupPermission.UserGroupPermission;
import com.creator.db.groupPermission.UserGroupPermissionDao;
import com.creator.db.host.Host;
import com.creator.db.host.HostDao;
import com.creator.db.nodeattr.NodeAttr;
import com.creator.db.nodeattr.NodeAttrDao;
import com.creator.db.permission.Permission;
import com.creator.db.permission.PermissionDao;
import com.creator.db.permission.PermissionDto;
import com.creator.db.scene.Scene;
import com.creator.db.scene.SceneDao;
import com.creator.db.template.Template;
import com.creator.db.template.TemplateDao;
import com.creator.db.tpcontent.TPContent;
import com.creator.db.tpcontent.TPContentDao;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.tree.TreeDto;
import com.creator.db.type.Type;
import com.creator.db.type.TypeDao;
import com.creator.db.type.TypeEnum;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.db.value.Value;
import com.creator.db.value.ValueDao;
import com.creator.util.Protocol;
import com.creator.util.ZTree;

@Controller
@RequestMapping("/tree")
public class TreeAction {

	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private TreeDao treeDao;
	@Autowired 
	private UserDao userDao;
	@Autowired
	private ValueDao valueDao;
	@Autowired
	private TypeDao typeDao;
	@Autowired
	private TemplateDao templateDao;
	@Autowired
	private NodeAttrDao nodeAttrDao;
	@Autowired
	private UserGroupDao userGroupDao;
	@Autowired
	private UserGroupPermissionDao uRGPDao;
	@Autowired
	private UserGroupPermissionDao userGroupPermissionDao;
	@Autowired
	private SceneDao sceneDao;
	@Autowired
	private TPContentDao tpcDao;
	@Autowired
	private HostDao hostDao;
	/**
	 * 进入树管理首页
	 * @param request
	 * @param permissionDto(属性page用于分页)
	 * @param model
	 * @return
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request,TreeDto treeDto,Model model) {
		//登录用户的
		Integer id = (Integer) request.getSession().getAttribute("USERID");
		if(id == null) {
			return "login";
		}
		model.addAttribute("like", treeDto.getLike());
		treeDto.setNode_userid(id);
		List<TreeDto> trees = treeDao.queryTreeNodeByPage(treeDto);
		for (TreeDto tree : trees) {
			tree.setIsCreator(1);
			tree.setUr_name(userDao.queryUserById(tree.getNode_userid()).get(0).getUr_username());
		}
		List<User> users = userDao.queryUserById(id);
		int urgId = users.get(0).getUr_group();
		List<Map<String,Object>> urgsMap =  userGroupDao.queryGroupById(urgId);
		if(urgsMap.size() > 0) {
			int treeId = (int) urgsMap.get(0).get("urg_treeid");
			Tree tree = treeDao.queryById(treeId).get(0);
			TreeDto treeDto2 = new TreeDto();
			BeanUtils.copyProperties(tree, treeDto2);
			treeDto2.setIsCreator(1);
			treeDto2.setUr_name(userDao.queryUserById(id).get(0).getUr_username());

			model.addAttribute("tree", treeDto2);
		}	
		model.addAttribute("trees", trees);
		//返回用于分页的信息
		model.addAttribute("searchParam", treeDto);
		//返回like搜索关键字
		model.addAttribute("like", treeDto.getLike());
		return "tree/index";
	}

	/**
	 * 新增节点
	 * @param request
	 * @param isParent
	 * @param pid
	 * @param name
	 * @param model
	 * @return
	 */
	@Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
	@RequestMapping("addTree")
	@ResponseBody
	public TreeDto addTree(HttpServletRequest request,boolean isParent,Integer pid,String name,String title,
			String url,Integer type,String sn,String nodeClass,String tpId,String scene,Integer tsType,Model model) {
		//用户id
		int userId = (int) request.getSession().getAttribute("USERID");
		//新增的节点的ID
		int treeId = 0;
		//要新增的树的对象
		Tree tree = new Tree();
		//要返回的树的对象（TreeDto是Tree的扩展类,多了用户名等的属性）
		TreeDto treeDto = new TreeDto();
		tree.setNode_name(name);
		tree.setNode_title(title);
		tree.setNode_userid(userId);
		tree.setNode_type(type);
		if(tsType == null) {
			tsType = 0;
		}
		tree.setNode_tstype(tsType);
		tree.setNode_class(nodeClass);
		if(null != type &&type == 2 && null != sn && !sn.equals("")) {
			tree.setNode_sn(sn);
		}
		if(null == url || url.equals("")) {
			tree.setNode_url("");
		}else {
			tree.setNode_url(url);
		}
		//初始化新增节点的分组id
		tree.setNode_treeid(0);
		//设置新增节点的父节点id(值为0)
		tree.setNode_pid(pid);
		//如果新增的是根点
		if(isParent) {	
			tree.setNode_type(typeDao.findByName(TypeEnum.NORMAL.getName()).getType_id());
			//新增节点
			treeId = treeDao.addTree(tree);
			tree.setNode_id(treeId);
			//设置新增节点的分组id
			tree.setNode_treeid(treeId);
			//为了设置treeid，所以要更新一次新增的节点
			treeDao.updateTree(tree);
			//因为新增的是父节点，所以需要新增新的权限
			Permission permission = new Permission();
			permission.setUrnode_iscreator(1);
			permission.setUrnode_nodeid(String.valueOf(treeId));
			permission.setUrnode_userid(userId);
			permission.setUrnode_treeid(treeId);
			permissionDao.addPermission(permission);

			//新增用户组
			UserGroup userGroup = new UserGroup();
			userGroup.setUrg_name(tree.getNode_name());
			userGroup.setUrg_treeid(treeId);
			userGroup.setUrg_userid(userId);
			userGroupDao.addUserGroup(userGroup);
			//如果新增的是子节点
		} else {
			//找到pid对应的节点
			Tree parentTree = treeDao.queryById(pid).get(0);
			//设置新增的节点的分组id
			tree.setNode_treeid(parentTree.getNode_treeid());
			//新增节点
			treeId = treeDao.addTree(tree);
			if(userId != parentTree.getNode_userid()) {
				UserGroupPermission ugp = new UserGroupPermission();
				int ugId = userDao.queryById(userId).get(0).getUr_group();
				ugp.setGp_usergroupid(ugId);
				ugp.setGp_perid("admin_1");
				ugp.setGp_treeid(parentTree.getNode_id());
				ugp.setGp_nodeid(treeId);
				userGroupPermissionDao.add(ugp);
			}
			tree.setNode_id(treeId);
			//把新增节点的id放入含有pid的权限组的属性nodeid中
			List<Permission> permissions = permissionDao.queryAllPermission();
			for (Permission p : permissions) {
				String[] ids = p.getUrnode_nodeid().split(",");
				for (String id : ids) {
					if (!id.equals("") && Integer.valueOf(id).intValue() == pid) {
						if(userId == p.getUrnode_userid() || p.getUrnode_iscreator() == 1) {
							p.setUrnode_nodeid(p.getUrnode_nodeid() + "," + treeId);
							permissionDao.updatePermission(p);
							break;
						}						
					}
				}
			}	
		}
		//类型为设备
		if(null != type &&type == 2) {
			//如果模板id不为-1，则在tb_node_attr表添加他们的关系
			if(!tpId.equals("-1")) {
				NodeAttr attr = new NodeAttr(null, treeId, 1, tpId);
				nodeAttrDao.add(attr);
			}
		}
		//把节点信息放进它的扩展类，就可以把用户名也放进去
		BeanUtils.copyProperties(tree, treeDto);
		String userName = userDao.queryById(tree.getNode_userid()).get(0).getUr_username();
		treeDto.setUr_name(userName);
		if(pid > 0) {
			Integer psTreeId = treeDao.queryById(pid).get(0).getNode_treeid();
			List<Permission> ps = permissionDao.queryByUserIdAndtreeId(userId, psTreeId);
			if(ps.size()>0) {
				treeDto.setIsCreator(ps.get(0).getUrnode_iscreator());
			}else {
				treeDto.setIsCreator(0);
			}	
		}		
		//添加场景
		addNodeIdToScene(treeId, scene);
		return treeDto;
	}

	/**
	 * 添加节点id到场景的nodeid字段
	 * @param nodeId
	 * @param sceneStr
	 */
	public void addNodeIdToScene(int nodeId, String sceneStr) {
		if(nodeId == 0 || sceneStr == null || sceneStr.equals("")) {
			return;
		}
		String[] sceneIds = sceneStr.split(",");
		for (String sceneId : sceneIds) {
			Scene scene = sceneDao.findById(Integer.valueOf(sceneId)).get(0);
			String nodes = scene.getSc_nodeid();
			if(nodes == null) {
				nodes = "";
			}

			if(nodes.equals("")) {
				nodes = nodes + nodeId;
			}else {
				nodes = nodes + "," + nodeId;
			}
			scene.setSc_nodeid(nodes);
			sceneDao.update(scene);
		}
	}

	/**
	 * 更新节点名称
	 * @param request
	 * @param name
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("updateTreeName")
	@ResponseBody
	public Integer updateTreeName(HttpServletRequest request,String name,Integer id,Model model) {
		Tree tree = treeDao.queryById(id).get(0);
		//设置节点名称
		tree.setNode_name(name);
		if(treeDao.updateTree(tree) > 0) {
			return 1;
		}
		return 0;
	}

	/**
	 * 删除节点
	 * @param request
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("removeTree")
	@ResponseBody
	public Integer removeTree(HttpServletRequest request,Integer id,Model model) {
		Tree tree = treeDao.queryById(id).get(0);
		//删除该节点和它的子孙节点并删除与这些节点相关的所有权限和数据
		deleteSonNode(tree);	    
		return 1;
	}

	/**
	 * 删除该节点和它的子节点并移除相关节点权限
	 * @param tree
	 */
	public void deleteSonNode(Tree tree) {
		List<Tree> sons = treeDao.querySonNode(tree.getNode_id());
		for (Tree tree2 : sons) {
			deleteSonNode(tree2);
		}

		//获取所有节点权限
		List<Permission> permissions = permissionDao.queryByTreeId(tree.getNode_treeid());
		//遍历节点权限
		for (Permission p : permissions) {
			String[] ids = p.getUrnode_nodeid().split(",");
			List<String> idLs = new ArrayList<String>();
			//permission更新后的urnode_nodeid
			String newIds = "";
			for(int i = 0;i < ids.length;i++) {
				//如果nodeid含有即将删除的节点的id，则删除这个id
				if(!ids[i].equals("") && Integer.valueOf(ids[i]).intValue() != tree.getNode_id().intValue()) {
					idLs.add(ids[i]);
				}								
			}
			for (int i = 0;i < idLs.size();i++) {
				if(i == 0) {
					newIds = newIds + idLs.get(i);
				} else {
					newIds = newIds + "," + idLs.get(i);
				}
			}
			//如果节点权限的node_id为空，则删除这条权限,否则，更新这条权限
			if(newIds.equals("")) {
				permissionDao.deletePermission(p.getUrnode_id());
			} else {
				p.setUrnode_nodeid(newIds);
				permissionDao.updatePermission(p);
			}
		}
		//删除该节点的value
		List<Value> values = valueDao.queryByNodeId(tree.getNode_id());
		if(values.size() > 0) {
			for (Value value : values) {
				valueDao.deleteValue(value.getValue_id());
			}
		}
		//删除用户组权限
		uRGPDao.deleteByNodeId(tree.getNode_id());
		//删除根节点时删除用户组、场景、模板
		if(tree.getNode_pid() == 0) {
			userGroupDao.deleteByTreeId(tree.getNode_treeid());
			sceneDao.deleteByTreeId(tree.getNode_treeid());
			templateDao.deleteByTreeId(tree.getNode_treeid());
		}
		//删除中间表tb_node_attr的记录
		nodeAttrDao.deleteByNodeId(tree.getNode_id());
		//从场景中删除相关节点
		List<Scene> scenes = sceneDao.queryByTreeId(tree.getNode_treeid());
		for (Scene scene : scenes) {
			removeNodeIdFromScene(scene.getSc_id(), tree.getNode_id());
			removeActionIdFromScene(scene.getSc_id(), tree.getNode_id());
		}
		//删除节点
		treeDao.deleteTree(tree.getNode_id());
	}



	//进入树节点管理页面
	@RequestMapping("editNode")
	public String editNode(HttpServletRequest request, boolean creator, Integer treeId,Model model) {
		if(treeId == null || treeId == 0) {
			return "tree/editNode";
		}        
		//需要编辑的根节点
		Tree tree = treeDao.queryById(treeId).get(0);
		model.addAttribute("tree", tree);                   
		int userid = (int) request.getSession().getAttribute("USERID");     

		//获取在页面初始化ztree的json对象
		List<ZTree> zTrees =getTree(request,userid,creator,treeId);
		JSONArray jsonArray = new JSONArray();
		jsonArray.addAll(zTrees);
		model.addAttribute("ztree", jsonArray);
		//获取用户列表
		List<User> userList = userDao.queryAllUser();
		List<User> returnUsers = new ArrayList<User>();
		//当前用户不加入用户列表
		for (User user : userList) {
			if(user.getUr_id() != userid) {
				returnUsers.add(user);
			}
		}
		model.addAttribute("scenes", sceneDao.queryByTreeId(treeId));
		model.addAttribute("users", returnUsers);
		model.addAttribute("types", typeDao.query());
		model.addAttribute("templates", templateDao.findByTreeId(treeId));
		return "tree/editNode";		
	}

	public String getNodeScene(List<Scene> scenes, int nodeId) {
		String sceneStr = "";
		for (Scene sc : scenes) {
			String nodeIds = sc.getSc_nodeid();
			if(nodeIds != null) {
				String[] nodeArray = nodeIds.split(",");
				for (String id : nodeArray) {
					if(id.equals(String.valueOf(nodeId))) {
						sceneStr = sceneStr + sc.getSc_id() + ",";
					}
				}
			}
		}
		if(sceneStr.length() > 0) {
			sceneStr = sceneStr.substring(0, sceneStr.length() - 1);
		}
		return sceneStr;
	}

	/**
	 * 获取ztree简单数据
	 * @param request
	 * @param userid 用户id
	 * @param creator 是否树创建者
	 * @param treeId 树id
	 * @return
	 */
	public List<ZTree> getTree(HttpServletRequest request,Integer userid, boolean creator, Integer treeId) {
		//项目路径
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ path + "/";
		//返回到前台的zTree的json数据
		List<ZTree> zTrees = new ArrayList<ZTree>();
		List<Scene> scenes = sceneDao.queryByTreeId(treeId);
		if(creator) {
			//该树的所有节点信息
			List<Tree> trees = treeDao.queryByTreeId(treeId);
			for (Tree tree2 : trees) {
				//把节点信息放入ztree对象里面
				ZTree ztree = new ZTree();
				ztree.setId(tree2.getNode_id());
				ztree.setpId(tree2.getNode_pid());
				ztree.setName(tree2.getNode_name());
				//点击节点跳转的地址（无法使用）
				//ztree.setUrl(tree2.getNode_url());
				//点击节点跳转的地址（可以使用）
				ztree.setMyUrl(ztree.getUrl());
				//跳转窗口
				ztree.setTarget("treeValue");
				//是否拥有该节点权限
				ztree.setHasP(true);
				//是否是该树的创建者
				ztree.setIsCreator(true);
				Type type = typeDao.findById(tree2.getNode_type());
				//节点类型
				ztree.setType(type);
				//节点title
				ztree.setTitle(tree2.getNode_name() + "(" + type.getType_name_cn() + ") id:" + tree2.getNode_id());
				//节点sn
				ztree.setSn(tree2.getNode_sn());
				//设备所属类
				ztree.setNodeClass(tree2.getNode_class());
				String sceneStr = getNodeScene(scenes, tree2.getNode_id());
				//节点场景
				ztree.setScene(sceneStr);
				//传输类型
				ztree.setTsType(tree2.getNode_tstype());
				List<NodeAttr> attrs = nodeAttrDao.findByNodeIdAndType(tree2.getNode_id(), 1);
				if(attrs.size() > 0) {
					NodeAttr attr = attrs.get(0);
					//设备模板id
					ztree.setTpId(attr.getAttr_value());
				}
				zTrees.add(ztree);
			}
		}else {
			int urgId = userDao.queryById(userid).get(0).getUr_group();
			List<UserGroupPermission> ugps = uRGPDao.queryByGroupId(urgId);
			//该树的所有节点信息
			List<Tree> trees = treeDao.queryByTreeId(treeId);
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
				Type type = typeDao.findById(tree.getNode_type());
				//节点类型
				ztree.setType(type);
				//节点title
				ztree.setTitle(tree.getNode_name() + "(" + type.getType_name_cn() + ")");
				//节点sn
				ztree.setSn(tree.getNode_sn());
				//设备所属类
				ztree.setNodeClass(tree.getNode_class());
				String sceneStr = getNodeScene(scenes, tree.getNode_id());
				//节点场景
				ztree.setScene(sceneStr);
				//传输类型
				ztree.setTsType(tree.getNode_tstype());
				//如果用户拥有该节点权限，则hasP为true
				for(UserGroupPermission ugp : ugps) {
					String[] perIds = StringUtils.split(ugp.getGp_perid(),",");
					if(ugp.getGp_nodeid().equals(tree.getNode_id()) && ArrayUtils.contains(perIds, "admin_1")) {		
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

	/**
	 * 删除节点的某个场景
	 * @param request
	 * @param nodeId 节点id
	 * @param scene 场景id
	 * @param model
	 * @return
	 */
	@RequestMapping("removeScene")
	@ResponseBody
	public String removeScene(HttpServletRequest request,Integer nodeId,Integer scene,Model model) {
		try {
			Tree tree = treeDao.queryById(nodeId).get(0);
			int treeId = tree.getNode_treeid();
			List<Scene> scenes = sceneDao.queryByTreeId(treeId);
			for (Scene sc : scenes) {
				String nodeIds = sc.getSc_nodeid();
				if(nodeIds != null) {
					String[] nodeArray = nodeIds.split(",");
					for (int i = 0;i<nodeArray.length;i++) {
						if(nodeArray[i].equals(String.valueOf(nodeId)) && sc.getSc_id().equals(scene)) {
							nodeArray = (String[]) ArrayUtils.remove(nodeArray, i);
						}
					}
					nodeIds = StringUtils.join(nodeArray, ",");
				}
				sc.setSc_nodeid(nodeIds);
				sceneDao.update(sc);
			}
			return "1";
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * 节点更新场景
	 * @param request
	 * @param nodeId
	 * @param oldScene
	 * @param newScene
	 * @param model
	 * @return
	 */
	@RequestMapping("updateScene")
	@ResponseBody
	public String updateScene(HttpServletRequest request,Integer nodeId,Integer oldScene,Integer newScene,Model model) {
		removeNodeIdFromScene(oldScene, nodeId);
		addNodeIdToScene(nodeId, String.valueOf(newScene));
		return "1";
	}

	/**
	 * 删除指定节点的指定场景
	 * @param scId 场景id
	 * @param nodeId 节点id
	 */
	public void removeNodeIdFromScene(int scId, int nodeId) {
		Scene scene = sceneDao.findById(scId).get(0);
		String nodeIds = scene.getSc_nodeid();
		if(nodeIds == null) {
			return;
		}
		String[] nodeArray = nodeIds.split(",");
		boolean run = false;
		for (int i=0;i<nodeArray.length;i++ ){
			if(nodeArray[i].equals(String.valueOf(nodeId))) {
				nodeArray = (String[]) ArrayUtils.remove(nodeArray, i);
				run = true;
			}
		}
		nodeIds = StringUtils.join(nodeArray, ",");
		scene.setSc_nodeid(nodeIds);
		if(run) {
			sceneDao.update(scene);
		}
	}

	/**
	 * 从场景中删除指定动作
	 * @param scId
	 * @param nodeId
	 */
	public void removeActionIdFromScene(int scId, int nodeId) {
		Scene scene = sceneDao.findById(scId).get(0);
		String ActionIds = scene.getSc_action();
		if(ActionIds == null) {
			return;
		}
		String[] nodeArray = ActionIds.split(",");
		boolean run = false;
		for (int i=0;i<nodeArray.length;i++ ){
			if(nodeArray[i].equals(String.valueOf(nodeId))) {
				nodeArray = (String[]) ArrayUtils.remove(nodeArray, i);
				run = true;
			}
		}
		ActionIds = StringUtils.join(nodeArray, ",");
		scene.setSc_action(ActionIds);
		if(run) {
			sceneDao.update(scene);
		}
	}

	/**
	 * 节点新增场景
	 * @param request
	 * @param scene
	 * @param nodeId
	 * @param model
	 * @return
	 */
	@RequestMapping("addScene")
	@ResponseBody
	public String addScene(HttpServletRequest request,Integer scene,Integer nodeId,Model model) {
		addNodeIdToScene(nodeId, String.valueOf(scene));
		return "1";
	}

	/**
	 * 进入数据管理页面
	 * @param request
	 * @param nodeId（节点id）
	 * @param nodeName（节点名称）
	 * @param like（搜索关键字）
	 * @param model
	 * @return
	 */
	@RequestMapping("treeValue")
	public String treeValue(HttpServletRequest request,Integer nodeId,String nodeName,String like,Model model) {
		System.out.println(nodeName);
		//该节点的所有数据
		List<Value> values = valueDao.queryByNodeId(nodeId);		
		List<Tree> trees = treeDao.queryById(nodeId);
		Tree tree = null;
		String protocolStr;
		if(trees.size() > 0) {
			tree = trees.get(0);
			if(tree.getNode_tstype() != null && tree.getNode_tstype().equals(0)) {
				protocolStr = tree.getNode_protocol();
				if(null != protocolStr && !protocolStr.equals("[]")) {
					JSONArray jsonArray = JSONArray.fromObject(protocolStr);
					Protocol[] protocols = (Protocol[]) JSONArray.toArray(jsonArray, Protocol.class);
					model.addAttribute("protocols", protocols);
					model.addAttribute("ArrayLength", protocols.length);
				}
			}
			model.addAttribute("tsType", tree.getNode_tstype());
			List<Host> hosts = hostDao.queryByTreeId(tree.getNode_treeid());
			if(hosts.size() > 0) {
				model.addAttribute("host", hosts.get(0));
			}
		}
		model.addAttribute("values", values);
		//该节点的id
		model.addAttribute("nodeId", nodeId);
		//该节点的名称
		model.addAttribute("nodeName", nodeName);	
		return "tree/treeValue";
	}

	/**
	 * 新增节点数据
	 * @param request
	 * @param id（节点id）
	 * @param key（数据键值）
	 * @param data（数据内容）
	 * @param model
	 * @return
	 */
	@RequestMapping("addData")
	@ResponseBody
	public Integer addData(HttpServletRequest request,Integer id,String key,String data,Model model) {
		Value value = new Value();
		value.setValue_data(data);
		value.setValue_nodeid(id);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		value.setValue_datetime(sf.format(date));
		return valueDao.addValue(value);
	}

	/**
	 * 删除数据
	 * @param request
	 * @param ids（要删除的数据id的字符串列表，按逗号隔开）
	 * @param model
	 * @return
	 */
	@RequestMapping("deleteValue")
	@ResponseBody
	public Integer deleteValue(HttpServletRequest request,String ids,Model model) {
		try {
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				if(null != id && !id.equals("")) {
					valueDao.deleteValue(Integer.valueOf(id));
				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * 更新节点数据
	 * @param request
	 * @param key（新的键值）
	 * @param id（数据id）
	 * @param data（新的内容）
	 * @param nodeId（节点id）
	 * @param model
	 * @return
	 */
	@RequestMapping("updateValue")
	@ResponseBody
	public Integer updateValue(HttpServletRequest request,String key,Integer id,String data,Integer nodeId,Model model) {
		Value value = new Value();
		value.setValue_id(id);
		value.setValue_nodeid(nodeId);
		value.setValue_data(data);
		return valueDao.updateValue(value);
	}

	/**
	 * 验证数据的键值是否存在，存在返回0，不存在返回1
	 * @param request
	 * @param key（新的键值）
	 * @param isUpdate（是否是更新操作）
	 * @param oldKey（旧的键值）
	 * @param model
	 * @return
	 */
	@RequestMapping("validKey")
	@ResponseBody
	public Integer validKey(HttpServletRequest request,String key,Integer nodeId,Boolean isUpdate,String oldKey,Model model) {
		if(key.equals("")) {
			String name = treeDao.queryById(nodeId).get(0).getNode_name();
			key = name;
		}
		List<Value> ls = valueDao.queryByKeyAndNodeId(key,nodeId);
		if(ls.size()>0) {			
			if(isUpdate == null || !isUpdate) {			
				return 0;
			} else {
				for (Value value : ls) {
					if(value.getValue_key().equals(oldKey)) {
						return 1;
					}
				}
				return 0;
			}
		}else {
			return 1;
		}

	}

	/**
	 * 获取用户对某棵树的有权限的节点
	 * @param request
	 * @param userId（用户id）
	 * @param treeId（树id）
	 * @param model
	 * @return
	 */
	@RequestMapping("getUserPermission")
	@ResponseBody
	public Permission getUserPermission(HttpServletRequest request,Integer userId,Integer treeId,Model model) {
		//用户对该棵树有权限的节点列表
		List<Permission> permissions = permissionDao.queryByUserIdAndtreeId(userId, treeId);
		if(permissions.size() > 0) {
			return permissions.get(0);
		} else {
			return null;
		}

	}

	/**
	 * 获取用户对某棵树的有权限的节点
	 * @param request
	 * @param treeId
	 * @param model
	 * @return
	 */
	@RequestMapping("getTree")
	@ResponseBody
	public List<ZTree> getTree(HttpServletRequest request,Integer treeId,boolean creator ,Model model) {
		int userid = (int) request.getSession().getAttribute("USERID");
		return getTree(request, userid, creator, treeId);		       
	}

	/**
	 * 保存权限设置
	 * @param request
	 * @param ids（赋予权限的节点id列表）
	 * @param treeId（树id）
	 * @param userid（用户id）
	 * @param alreadEdit（用户对该树是否已存在某些节点的权限）
	 * @param model
	 * @return
	 */
	@RequestMapping("savePermission")
	@ResponseBody
	public Integer savePermission(HttpServletRequest request,String ids,Integer treeId,Integer userid,boolean alreadEdit,Model model) {
		//如果用户对该树某些节点已经有权限，则更新或删除该条权限信息
		if(alreadEdit) {
			List<Permission> ps = permissionDao.queryByUserIdAndtreeId(userid, treeId);	
			if(ps.size() > 0) {
				Permission p = ps.get(0);
				if(ids.equals("")) {
					permissionDao.deletePermission(p.getUrnode_id());						
				} else {
					p.setUrnode_nodeid(ids);
					permissionDao.updatePermission(p);
				}
				return 1;
			} else {
				return 0;
			}
			//如果用户不存在对该树节点的权限，则新增信息
		} else {
			Permission permission = new Permission(null,userid,ids,0,treeId);
			if(!ids.equals("")) {
				return permissionDao.addPermission(permission);
			} else {
				return 1;
			}				
		}

	}		

	/**
	 * 编辑节点属性
	 * @param request
	 * @param nodeid
	 * @param url
	 * @param model
	 * @return
	 */
	@RequestMapping("editUrl")
	@ResponseBody
	public Integer editUrl(HttpServletRequest request,Integer nodeid,String url,String sn,String nodeClass,Integer oldType,Integer type,String tpId,Integer tsType,Model model) {
		List<Tree> trees = treeDao.queryById(nodeid);
		if(trees.size() <= 0) {	
			return 0;
		}
		Tree tree = trees.get(0);
		tree.setNode_url(url);	
		tree.setNode_tstype(tsType);
		tree.setNode_class(nodeClass);
		if(oldType.equals(type)) {
			if(type.equals(TypeEnum.EQUIPMENT.getId())) {
				updateTemplateId(tpId, nodeid);
			}				
			tree.setNode_sn(sn);
		}else if(!oldType.equals(type) && type.equals(TypeEnum.EQUIPMENT.getId())) {
			removeActionForUpdate(tree.getNode_treeid(), nodeid);
			tree.setNode_sn(sn);
			tree.setNode_type(type);
			updateTemplateId(tpId, nodeid);
		}else if(!oldType.equals(type) && oldType.equals(TypeEnum.EQUIPMENT.getId())) {
			removeActionForUpdate(tree.getNode_treeid(), nodeid);
			tree.setNode_sn(null);
			tree.setNode_class(null);
			tree.setNode_type(type);
			updateTemplateId("-1", nodeid);
		}else {
			removeActionForUpdate(tree.getNode_treeid(), nodeid);
			tree.setNode_type(type);
		}
		return treeDao.updateTree(tree);
	}		

	public void removeActionForUpdate(int treeId, int nodeId) {
		List<Scene> scenes = sceneDao.queryByTreeId(treeId);
		for (Scene scene : scenes) {
			removeActionIdFromScene(scene.getSc_id(), nodeId);
		}			
	}

	/**
	 * 更新设备模板id
	 */
	public void updateTemplateId(String tpId, Integer nodeid) {
		if(tpId != null && !tpId.equals("")){
			if(tpId.equals("-1")) {
				nodeAttrDao.deleteByNodeIdAndType(nodeid, 1);
			}else {
				List<NodeAttr> attrs = nodeAttrDao.findByNodeIdAndType(nodeid, 1);
				if(attrs.size() > 0) {
					NodeAttr attr = attrs.get(0);
					attr.setAttr_value(tpId);
					nodeAttrDao.update(attr);
				}else {
					NodeAttr attr = new NodeAttr();
					attr.setAttr_nodeid(nodeid);
					attr.setAttr_type(1);
					attr.setAttr_value(tpId);
					nodeAttrDao.add(attr);
				}
			}
		}
	}

	/**
	 * 验证sn是否存在
	 * @param request
	 * @param sn     设备sn
	 * @param action 操作类型（add或update）
	 * @param model  
	 * @return
	 */
	@RequestMapping("validSN")
	@ResponseBody
	public Integer validSN(HttpServletRequest request,String sn,String oldSn,String action,Model model) {
		if(action.equals("add")) {
			List<Tree> trees = treeDao.queryBySn(sn);
			if(trees.size() > 0) {
				return 0;
			}else {
				return 1;
			}
		}else if(action.equals("update")) {
			List<Tree> trees = treeDao.queryBySn(sn);
			if(trees.size() > 0 && !sn.equals(oldSn)) {
				return 0;
			}else {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 协议管理
	 * @param request
	 * @param nodeId
	 * @param model
	 * @return
	 */
	@RequestMapping("treeProtocol")
	public String treeProtocol(HttpServletRequest request, Integer nodeId, Model model) {
		List<Tree> trees = treeDao.queryById(nodeId);
		if(trees.size() > 0) {
			Tree tree = trees.get(0);
			String protocolStr = tree.getNode_protocol();
			if(protocolStr != null && !protocolStr.equals("")) {
				JSONArray j = JSONArray.fromObject(tree.getNode_protocol());
				model.addAttribute("protocol", JSONArray.toArray(j, Protocol.class));
			}
			model.addAttribute("nodeName", tree.getNode_name());
		}
		model.addAttribute("nodeId", nodeId);
		
		return "tree/treeProtocol";

	}


	/**
	 * 添加数据点
	 * @param request
	 * @param protocol
	 * @param model
	 * @return
	 */
	@Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
	@RequestMapping("addProtocol")
	@ResponseBody
	public Integer addProtocol(HttpServletRequest request, Protocol protocol, Model model) {
		List<Tree> trees = treeDao.queryById(protocol.getNodeId());
		if(trees.size() > 0) {
			Tree tree = trees.get(0);
			String protocolStr = tree.getNode_protocol();
			JSONArray jsonArray;
			if(protocolStr != null && !protocolStr.equals("")) {
				jsonArray = JSONArray.fromObject(protocolStr);
			}else {
				jsonArray = new JSONArray();
			}
			jsonArray.add(protocol);
			tree.setNode_protocol(jsonArray.toString());
			treeDao.updateTree(tree);
			/*协议新增之后更新该节点的value*/
			List<Value> values = valueDao.queryByNodeId(protocol.getNodeId());
			for (Value value : values) {
				String valueJSON = value.getValue_data();
				JSONObject jo = JSONObject.fromObject(valueJSON);
				jo.put(protocol.getIdentification(), "");
				value.setValue_data(jo.toString());
				valueDao.updateValue(value);
			}
			return 1;
		}else {
			return 0;
		}			    

	}

	/**
	 * 验证标识名是否存在
	 * @param request
	 * @param protocol
	 * @param model
	 * @return
	 */
	@RequestMapping("validProtocol")
	@ResponseBody
	public Integer validProtocol(HttpServletRequest request, Protocol protocol, String action, String oldId, Model model) {
		List<Tree> trees = treeDao.queryById(protocol.getNodeId());
		if(trees.size() > 0) {
			Tree tree = trees.get(0);
			String protocolStr = tree.getNode_protocol();
			JSONArray jsonArray;
			if(protocolStr != null && !protocolStr.equals("")) {
				jsonArray = JSONArray.fromObject(protocolStr);
			}else {
				jsonArray = new JSONArray();
			}
			Protocol[] protocols = (Protocol[]) JSONArray.toArray(jsonArray, Protocol.class);
			for (Protocol protocol2 : protocols) {
				if(protocol2.getIdentification().equals(protocol.getIdentification()) && action.equals("add")) {
					return 0;
				}
				if(protocol2.getIdentification().equals(protocol.getIdentification()) && !oldId.equals(protocol.getIdentification()) && action.equals("update")) {
					return 0;
				}
			}
			return 1;
		}else {
			return 0;
		}

	}

	/**
	 * 更新数据点
	 * @param request
	 * @param protocol
	 * @param oldId
	 * @param model
	 * @return
	 */
	@Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
	@RequestMapping("updateProtocol")
	@ResponseBody
	public Integer updateProtocol(HttpServletRequest request, Protocol protocol, String oldId, Model model) {
		List<Tree> trees = treeDao.queryById(protocol.getNodeId());
		Tree tree = trees.get(0);
		String protocolStr = tree.getNode_protocol();
		JSONArray jsonArray = JSONArray.fromObject(protocolStr);
		Protocol[] ps = (Protocol[]) JSONArray.toArray(jsonArray, Protocol.class);
		for (Protocol protocol2 : ps) {
			if(protocol2.getIdentification().equals(oldId)) {
				protocol2.setIdentification(protocol.getIdentification());
				protocol2.setName(protocol.getName());
				protocol2.setRemark(protocol.getRemark());
			}
		}
		jsonArray = JSONArray.fromObject(ps);
		tree.setNode_protocol(jsonArray.toString());
		if(treeDao.updateTree(tree) != 1) {
			return 0;
		}

		/*协议更新之后更新该节点的value*/
		List<Value> values = valueDao.queryByNodeId(protocol.getNodeId());
		for (Value value : values) {
			String valueJSON = value.getValue_data();
			if(valueJSON != null && !protocol.getIdentification().equals(oldId)) {
				JSONObject jo = JSONObject.fromObject(valueJSON);
				String oldValue = jo.getString(oldId);
				jo.remove(oldId);
				jo.put(protocol.getIdentification(), oldValue);
				value.setValue_data(jo.toString());
				valueDao.updateValue(value);
			}	
		}
		return 1;   
	}

	/**
	 * 删除数据点
	 * @param request
	 * @param id
	 * @param nodeId
	 * @param model
	 * @return
	 */
	@Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
	@RequestMapping("deleteProtocol")
	@ResponseBody
	public Integer deleteProtocol(HttpServletRequest request, String id, Integer nodeId, Model model) {
		List<Tree> trees = treeDao.queryById(nodeId);
		Tree tree = trees.get(0);
		String protocolStr = tree.getNode_protocol();
		JSONArray jsonArray = JSONArray.fromObject(protocolStr);
		Protocol[] ps = (Protocol[]) JSONArray.toArray(jsonArray, Protocol.class);
		for (int i=0;i < ps.length; i++) {
			if(ps[i].getIdentification().equals(id)) {
				jsonArray.remove(i);
			}
		}
		tree.setNode_protocol(jsonArray.toString());
		if(treeDao.updateTree(tree) != 1) {
			return 0;
		}       
		/*协议删除之后更新该节点的value*/
		List<Value> values = valueDao.queryByNodeId(nodeId);
		for (Value value : values) {
			String valueJSON = value.getValue_data();
			if(valueJSON != null) {
				JSONObject jo = JSONObject.fromObject(valueJSON);
				jo.remove(id);
				value.setValue_data(jo.toString());
				valueDao.updateValue(value);
			}	
		}
		return 1;
	}

	/**
	 * 复制节点
	 * @param request
	 * @param nodes 需要复制的节点，用逗号分隔
	 * @param model
	 * @return
	 */
	@Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
	@RequestMapping(value="copyNode",produces={"text/html;charset=UTF-8;","application/json;"})
	@ResponseBody
	public String copyNode(HttpServletRequest request, String nodes, Model model) {
		JSONArray ja = JSONArray.fromObject(nodes);
		Map<Integer, Integer> idBox = new HashMap<Integer, Integer>();
		for(int i=0;i<ja.size();i++) {
			JSONObject jo = ja.getJSONObject(i);
			int nodeId = jo.getInt("id");
			int nodePid = jo.getInt("pId");
			Tree tree = treeDao.queryById(nodeId).get(0);
			if(idBox.get(nodePid) != null) {
				tree.setNode_pid(idBox.get(nodePid));
			}else {
				tree.setNode_pid(nodePid);
			}
			int id = treeDao.addTree(tree);
			jo.remove("id");
			jo.remove("pId");
			jo.remove("children");
			jo.put("id", id);
			jo.put("pId", tree.getNode_pid());
			idBox.put(nodeId, id);
			//为复制的节点添加相应value
			List<Value> values = valueDao.queryByNodeId(nodeId);
			for (Value value : values) {
				value.setValue_nodeid(id);
				valueDao.addValue(value);
			}
			//为复制的节点添加相应模板内容
            List<TPContent> tpcs = tpcDao.findByNodeId(nodeId);
            for (TPContent tpc : tpcs) {
            	tpc.setTpc_nodeid(id);
            	tpcDao.add(tpc);
			}
          //为复制的节点添加相应场景
            List<Map<String, Object>> scenes = sceneDao.queryByNodeId(nodeId);
            for (Map<String, Object> map : scenes) {
				String nodeIdStr = (String) map.get("sc_nodeid");
				if(nodeIdStr == null || nodeIdStr.equals("")) {
					nodeIdStr = String.valueOf(id);
				}else {
					nodeIdStr = nodeIdStr + ',' + String.valueOf(id);
				}
				Scene scene = new Scene();
				Number num = (Number) map.get("sc_id");
				scene.setSc_id(num.intValue());
				scene.setSc_name((String) map.get("sc_name"));
				scene.setSc_nodeid(nodeIdStr);
				scene.setSc_treeid((Integer) map.get("sc_treeid"));
				scene.setSc_action((String) map.get("sc_action"));
				sceneDao.update(scene);
			}
		}
		return ja.toString();
	}
}
