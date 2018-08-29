package creator;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.api.CodeEnum;
import com.creator.api.CodeHelper;
import com.creator.db.group.UserGroup;
import com.creator.db.group.UserGroupDao;
import com.creator.db.groupPermission.UserGroupPermission;
import com.creator.db.groupPermission.UserGroupPermissionDao;
import com.creator.db.scene.Scene;
import com.creator.db.scene.SceneDao;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.util.ClientUtil;

/**
 * 多媒体控制项目定制接口（T4000设备）
 * 返回json数据格式规定为：{"code":"" , "msg":"" , "data":[]}
 * 注：code:状态码,String    	msg:状态码的文字说明,String   	data:数据,Array
 * 
 */
//解决跨域问题
@CrossOrigin(origins="*",maxAge=3600)
@Controller
@RequestMapping(value="media")
public class MediaAction {
	@Autowired
	private UserGroupDao userGroupDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserGroupPermissionDao userGroupPermissionDao;
	@Autowired
	private SceneDao sceneDao;
	
	@RequestMapping(value="getUserGroundByTreeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getUserGroundByTreeId(String treeId,HttpServletRequest request) {
		
		CodeEnum code = CodeHelper.String2Number(treeId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		List<Map<String, Object>> ugs = userGroupDao.queryGroupByTreeId(Integer.valueOf(treeId));
		jsonObject.put("data", ugs);
		return jsonObject.toString();
	}
	
	@RequestMapping(value="getUserByGroundId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getUserByGroundId(String groundId,HttpServletRequest request) {
		
		CodeEnum code = CodeHelper.String2Number(groundId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		List<Map<String, Object>> users = userDao.queryUserByGroupId(Integer.valueOf(groundId));
		jsonObject.put("data", users);
		return jsonObject.toString();
	}
	
	@RequestMapping(value="getUserByTreeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getUserByTreeId(String treeId,HttpServletRequest request) {
		
		CodeEnum code = CodeHelper.String2Number(treeId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		List<Map<String, Object>> users = userDao.queryUserMapByTreeId(Integer.valueOf(treeId));
		jsonObject.put("data", users);
		return jsonObject.toString();
	}
	
	@RequestMapping(value="addUserGround",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String addUserGround(String name,String userId,String treeId,HttpServletRequest request) {
		
		CodeEnum code = CodeHelper.String2Number(userId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		UserGroup ug = new UserGroup();
		ug.setUrg_name(name);
		ug.setUrg_treeid(Integer.valueOf(treeId));
		ug.setUrg_userid(Integer.valueOf(userId));
		int upId = userGroupDao.add(ug);
		jsonObject.put("data", userGroupDao.queryGroupById(upId).get(0));
		return jsonObject.toString();
	}

	@RequestMapping(value="deleteUserGround",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String deleteUserGround(String upId,HttpServletRequest request) {
		
		CodeEnum code = CodeHelper.String2Number(upId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		userGroupDao.deleteUserGroupById(Integer.valueOf(upId));
		List<Map<String,Object>> users = userDao.queryUserByGroupId(Integer.valueOf(upId));
		for (Map<String, Object> map : users) {
			Number num = (Number) map.get("ur_id");
			int urId = num.intValue();
			User user = userDao.queryById(urId).get(0);
			user.setUr_group(0);
			userDao.updateUserById(urId, user);
		}
		return jsonObject.toString();
	}
	
	@RequestMapping(value="updateUserGround",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String updateUserGround(String upId,String ids,HttpServletRequest request) {
		
		CodeEnum code = CodeHelper.String2Number(upId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		String[] idArray = ids.split(",");
		List<User> ls = userDao.queryUserListByGroupId(Integer.valueOf(upId));
		for (User user : ls) {
			user.setUr_group(0);
			userDao.updateUserById(user.getUr_id(), user);
		}
		if(!ids.equals("")) {
			for (String userId : idArray) {
				User user = userDao.queryById(Integer.valueOf(userId)).get(0);
				user.setUr_group(Integer.valueOf(upId));
				userDao.updateUserById(user.getUr_id(), user);
			}
		}
		return jsonObject.toString();
	}
	
	@RequestMapping(value="getPermissionByGroundId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getPermissionByGroundId(String groundId,HttpServletRequest request) {
		
		CodeEnum code = CodeHelper.String2Number(groundId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		List<UserGroupPermission> permissions = userGroupPermissionDao.queryByGroupId(Integer.valueOf(groundId));
		if(permissions.size() > 0) {
			jsonObject.put("data", permissions.get(0));
		}else {
			jsonObject.put("data", null);
		}
		return jsonObject.toString();
	}
	
	@RequestMapping(value="updatePermission",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String updatePermission(String groundId,String ids,String treeId,HttpServletRequest request) {
		
		CodeEnum code = CodeHelper.String2Number(groundId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS || ids == null || ids.equals("")) {
			return jsonObject.toString();
		}
		List<UserGroupPermission> permissions = userGroupPermissionDao.queryByGroupId(Integer.valueOf(groundId));
		String result = ids;
		if(permissions.size() > 0) {
			String perIds = permissions.get(0).getGp_perid();
			String[] perArray = perIds.split(",");
			for (String perId : perArray) {
		        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		        boolean isNum = pattern.matcher(perId).matches();
				if(isNum && (Integer.valueOf(perId) < 4 || Integer.valueOf(perId) > 7)) {
					result = result + "," + perId;
				}
			}
			userGroupPermissionDao.updatePermissionByNodeAndGroup(Integer.valueOf(treeId), Integer.valueOf(groundId), result, Integer.valueOf(treeId));
			jsonObject.put("data", 1);
		}else {
			jsonObject.put("data", 0);
		}

		return jsonObject.toString();
	}
	
	@RequestMapping(value="getSceneByScId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getSceneByScId(String scId,HttpServletRequest request) {
		
		CodeEnum code = CodeHelper.String2Number(scId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		jsonObject.put("data", 0);
		return jsonObject.toString();
	}
	
	@RequestMapping(value="addScene",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String addScene(String nodeId,String treeId,String name,HttpServletRequest request) {
		CodeEnum code = CodeHelper.String2Number(nodeId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		Scene scene = new Scene();
		scene.setSc_name(name);
		scene.setSc_nodeid(nodeId);
		scene.setSc_treeid(Integer.valueOf(treeId));
		scene.setSc_action("");
		scene.setSc_id(sceneDao.addScene(scene));
		List<Map<String, Object>> result = sceneDao.queryById(scene.getSc_id());
		ClientUtil.putDetialToSceneList(result);
		jsonObject.put("data", result.get(0));
		return jsonObject.toString();
	}

	@RequestMapping(value="deleteScene",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String deleteScene(String scId,HttpServletRequest request) {
		CodeEnum code = CodeHelper.String2Number(scId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		jsonObject.put("data", sceneDao.delete(Integer.valueOf(scId)));
		return jsonObject.toString();
	}
	
	@RequestMapping(value="updateScene",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String updateScene(String scId,String action,HttpServletRequest request) {
		CodeEnum code = CodeHelper.String2Number(scId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		System.out.println(scId + ':' + action);
		List<Scene> scenes = sceneDao.queryListById(Integer.valueOf(scId));
		if(scenes.size() > 0) {
			Scene scene = scenes.get(0);
			scene.setSc_action(action);
			sceneDao.update(scene);
			List<Map<String, Object>> result = sceneDao.queryById(scene.getSc_id());
			ClientUtil.putDetialToSceneList(result);
			jsonObject.put("data", result);
		}
		return jsonObject.toString();
	}
	
	@RequestMapping(value="editSceneName",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String editSceneName(String scId,String name,HttpServletRequest request) {
		CodeEnum code = CodeHelper.String2Number(scId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		List<Scene> scenes = sceneDao.queryListById(Integer.valueOf(scId));
		if(scenes.size() > 0) {
			Scene scene = scenes.get(0);
			scene.setSc_name(name);
			sceneDao.update(scene);
		}
		return jsonObject.toString();
	}
	
	@RequestMapping(value="editGroundName",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String editGroundName(String ugId,String name,HttpServletRequest request) {
		CodeEnum code = CodeHelper.String2Number(ugId);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		List<UserGroup> ugs = userGroupDao.queryGroupListById(Integer.valueOf(ugId));
		if(ugs.size() > 0) {
			UserGroup userGroup = ugs.get(0);
			userGroupDao.updateUserGroup(userGroup.getUrg_id(), name, userGroup.getUrg_treeid());
		}
		return jsonObject.toString();
	}
}
