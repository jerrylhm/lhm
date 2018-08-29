package creator;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.creator.api.CodeEnum;
import com.creator.api.CodeHelper;
import com.creator.api.OperationEnum;
import com.creator.db.clientpermission.ClientPermissionDao;
import com.creator.db.conferee.Conferee;
import com.creator.db.conferee.ConfereeDao;
import com.creator.db.group.UserGroup;
import com.creator.db.group.UserGroupDao;
import com.creator.db.groupPermission.UserGroupPermission;
import com.creator.db.groupPermission.UserGroupPermissionDao;
import com.creator.db.host.Host;
import com.creator.db.host.HostDao;
import com.creator.db.meeting.Meeting;
import com.creator.db.meeting.MeetingDao;
import com.creator.db.nodeattr.NodeAttr;
import com.creator.db.nodeattr.NodeAttrDao;
import com.creator.db.scene.Scene;
import com.creator.db.scene.SceneDao;
import com.creator.db.template.TemplateDao;
import com.creator.db.tpcontent.TPContent;
import com.creator.db.tpcontent.TPContentDao;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.type.Type;
import com.creator.db.type.TypeDao;
import com.creator.db.type.TypeEnum;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.db.value.Value;
import com.creator.db.value.ValueDao;
import com.creator.util.ClientUtil;
import com.creator.util.MD5Util;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.mysql.fabric.xmlrpc.Client;
import com.mysql.fabric.xmlrpc.base.Array;
import com.sun.org.apache.bcel.internal.classfile.Code;


/**
 * 客户端接口，负责与客户端通信，处理客户端发送的数据和返回数据给客户端
 * 返回json数据格式规定为：{"code":"" , "msg":"" , "data":[] [, "type":"0"]}
 * 注：code:状态码,String    	msg:状态码的文字说明,String   	data:数据,Array    type(可选):发送给客户端或设备的标识(用于nodejs传输)，0:客户端   1:设备
 *
 */
//解决跨域问题
@CrossOrigin(origins="*",maxAge=3600)
@Controller
@RequestMapping(value="client")
public class ClientAction {
	private final static String CLASS_NAME = ClientAction.class.getName() + "：";     //类名，便于打印时区分信息
	public final static int TRANS_PASSTHROUGH = 1;           //透传
	public final static int TRANS_PROTOCOL = 0;        //协议传输
	
	@Autowired
	private TypeDao typeDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private TreeDao treeDao;
	
	@Autowired
	private UserGroupDao userGroupDao;
	
	@Autowired
	private MeetingDao meetingDao;
	
	@Autowired
	private ValueDao valueDao;
	
	@Autowired
	private ConfereeDao confereeDao;
	
	@Autowired
	private TemplateDao templateDao;
	
	@Autowired
	private TPContentDao tPContentDao;
	
	@Autowired
	private NodeAttrDao nodeAttrDao;
	
	@Autowired
	private SceneDao sceneDao;
	
	@Autowired
	private UserGroupPermissionDao userGroupPermissionDao;
	
	@Autowired
	private ClientPermissionDao clientPermissionDao;
	
	@Autowired
	private HostDao hostDao;
	
	/***************************************节点类型操作接口****************************************************/
	/**
	 * 获取所有节点类型(如普通节点、设备、文本、图片等)
	 * @return json字符串 ，格式为{"code":"" , "msg":"" , "data":[]}
	 * data参数说明: 类型：Array
	 * type_id: 节点类型id
	 * type_name: 节点类型名称
	 * type_name_cn: 节点类型对应的中文名称
	 * type_pid: 节点类型的父类型id,0说明没有父类型
	 */
	@RequestMapping(value="getNodeTypeList",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getNodeTypeList() {
		System.out.println(CLASS_NAME + "getNodeTypeList()");
		List<Map<String,Object>> typeList = typeDao.queryTypeList();
		
		CodeEnum success = CodeEnum.SUCCESS;   //成功
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", success.getCode());
		jsonObject.put("msg", success.getMsg());
		jsonObject.put("data", typeList);
		
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	

	/**
	 * 根据类型id获取类型信息
	 * @param id 类型id
	 * @return
	 */
	@RequestMapping(value="getNodeTypeById",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getNodeTypeById(String id) {
		System.out.println(CLASS_NAME + "getNodeTypeById()");
		System.out.println(CLASS_NAME + id);
		//判断id的有效性
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		//参数错误
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		int nId = Integer.valueOf(id);
		//根据id查询节点类型
		List<Map<String,Object>> typeList = typeDao.queryTypeById(nId);
		if(typeList.size() == 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODETYPE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//存入数据
		jsonObject.put("data", typeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据类型id查询下一级子类型列表
	 * @param id
	 * @return
	 */
	@RequestMapping(value="getTypeChildById",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getTypeChildById(String id) {
		System.out.println(CLASS_NAME + "getTypeChildById" + "  param:" + id);
		//判断参数有效性
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		int nId = Integer.valueOf(id);
		//根据id查询子节点
		List<Map<String,Object>> childList = typeDao.queryTypeChildrenById(nId);
		if(childList.size() == 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CHILD_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", childList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/***************************************节点树操作接口*******************************************************/
	
	/**
	 * 根据用户id获取其创建的树根节点列表
	 * @param id   用户id
	 * @return json字符串 ，格式为{"code":"" , "msg":"" , "data":[]}
	 * data参数说明： 类型Array
	 * node_id: 节点id
	 * node_userid: 创建节点的用户id
	 * node_name: 节点的名称
	 * node_pid: 节点的父id,根节点pid为0
	 * node_treeid: 节点所对应的树id,即根节点id
	 * node_url: 节点对应的跳转链接地址
	 * node_type: 节点类型
	 * node_protocol: 节点协议，需要是设备的下一级节点，其他为null
	 * node_class: 节点属性名称，需要设备节点，其他为null
	 * node_sn: 节点设备号，需要设备节点，其他为null
	 */
	@RequestMapping(value="getRootByCreateUserId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getRootByCreateUserId(String id) {
		System.out.println(CLASS_NAME + "getRootByCreateUserId" + "  param: " + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断用户是否存在
		int nId = Integer.valueOf(id);
		if(!userDao.isExistByUserId(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询该用户创建的树根节点列表
		List<Map<String,Object>> nodeList = treeDao.queryCreateRootByUserId(nId);
		jsonObject.put("data", nodeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据用户id获取该用户有权限的根节点列表(包括创建)
	 * @param id 用户id
	 * @return 节点json字符串
	 * 注：已废弃
	 */
	/*
	@RequestMapping(value="getRootByUserId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getRootByUserId(String id) {
		System.out.println(CLASS_NAME + "getRootByUserId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断用户是否存在
		int nId = Integer.valueOf(id);
		if(!userDao.isExistByUserId(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询有权限的树根节点
		List<Map<String,Object>> nodeList = treeDao.queryRootByUserId(nId);
		jsonObject.put("data", nodeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	*/
	
	/**
	 * 根据节点id获取对应的节点信息
	 * @param id  节点id
	 * @return 节点json字符串
	 */
	@RequestMapping(value="getNodeByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getNodeByNodeId(String id) {
		System.out.println(CLASS_NAME + "getNodeByNodeId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//查询对应的节点
		List<Map<String,Object>> nodeList = treeDao.queryNodeByNodeId(nId);
		jsonObject.put("data", nodeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id查询下一级子节点列表
	 * @param id 节点id
	 * @param containsValue 可选参数，是否包含节点对应的数据标志，如果为空，则不查询节点对应的数据
	 * @return 子节点json字符串
	 */
	@RequestMapping(value="getChildrenByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getChildrenByNodeId(String id,String containsValue) {
		System.out.println(CLASS_NAME + "getChildrenByNodeId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询子节点
		List<Map<String,Object>> nodeList = treeDao.queryChilrenByNodeId(nId);
		if(nodeList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CHILD_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//查询对应的value
		if(containsValue != null) {
			for(Map<String,Object> node : nodeList) {
				Map<String,Object> map = new HashMap<String, Object>();
				List<Map<String,Object>> valueList = valueDao.queryLimitValueByNodeId(((Long)node.get("node_id")).intValue(), 1);
				if(valueList.size() > 0) {
					map = valueList.get(0);
				}
				node.put("value", map);
			}
		}
		jsonObject.put("data", nodeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据节点id查询后代节点列表
	 * @param id 节点id
	 * @param flag 是否包含该节点数据标志，等于空时不包含传入的节点数据，等于1时包含该节点数据
	 * @return 节点json字符串
	 */
	@RequestMapping(value="getAllChildrenByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getAllChildrenByNodeId(String id,String flag) {
		System.out.println(CLASS_NAME + "getAllChildrenByNodeId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		List<Map<String, Object>> nodeList = ClientUtil.getDescendantById(nId);
		//不存在子节点
		if(nodeList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CHILD_NOT_EXIST);
			return jsonObject.toString();
		}
		if(flag != null && "1".equals(flag)) {
			List<Map<String,Object>> parentList = treeDao.queryNodeByNodeId(nId);
			nodeList.addAll(0, parentList);
		}
		jsonObject.put("data", nodeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id和用户id查询该用户有权限的下一级节点
	 * @param nodeid 节点id
	 * @param userid 用户id
	 * @return 子节点json字符串
	 * 注：已废弃
	 */
	/*
	@RequestMapping(value="getPermissChildrenByIds",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getPermissChildrenByIds(String nodeid,String userid) {
		System.out.println(CLASS_NAME + "getPermissChildrenByIds" + " params:" + nodeid + "  " + userid);
		CodeEnum code = CodeHelper.Strings2Numbers(nodeid,userid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nNodeid = Integer.valueOf(nodeid);
		if(!treeDao.isExistNode(nNodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询有权限的子节点
		List<Map<String,Object>> nodeList = treeDao.queryChilrenByNodeIdAndUserId(nNodeid, nUserid);
		if(nodeList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CHILD_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", nodeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	*/
	
	
	
	
	
	/**
	 * 根据节点获取距离自己最近的父级(或自身)的设备信息
	 * @param id 节点id
	 * @return 设备节点json字符串
	 */
	@RequestMapping(value="getParentDeviceByNodeid",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getParentDeviceByNodeid(String id) {
		System.out.println(CLASS_NAME + "getParentDeviceByNodeid" + " param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//获取节点,判断节点类型是否设备或设备子类
		List<Map<String,Object>> nodeList = treeDao.queryNodeByNodeId(nId);
		int type = (Integer) nodeList.get(0).get("node_type");
		if(type == TypeEnum.NORMAL.getId()) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_NODE_TYPE);
			return jsonObject.toString();
		}
		List<Map<String,Object>> deviceList = ClientUtil.getDeviceParent(nId);
		if(deviceList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.DEVICE_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", deviceList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id获取父节点列表，可根据数据列表生成树，列表顺序从顶层到底层(包括自身节点)
	 * @param id 节点id
	 * @return 节点列表json字符串
	 */
	@RequestMapping(value="getParentsByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getParentsByNodeId(String id) {
		System.out.println(CLASS_NAME + "getParentsByNodeId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//获取节点的父亲节点列表
		List<Map<String,Object>> parentList = ClientUtil.getParentList(nId);
		jsonObject.put("data", parentList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		
		return jsonObject.toString();
	}
	
	/**
	 * 根据类名和节点id查询后代指定类名子节点列表，包括普通节点，可根据该列表生成树
	 * @param id 节点id
	 * @param className  类名组合，如果有多个类名，则中间用,隔开
	 * @param flag 是否包含该节点数据标志，等于空时不包含传入的节点数据，等于1时包含该节点数据
	 * @return 节点后代普通节点和设备列表json字符串
	 */
	@RequestMapping(value="getDescentantsByClassName",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getDescentantsByClassName(String id,String className,String flag) {
		System.out.println(CLASS_NAME + "getDescentantsByClassName" + "  param:" + id + " " + className);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(className == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		List<Map<String,Object>> nodeList = ClientUtil.getDescendantsByClass(nId, className);
		//查询自身节点
		if(flag != null && "1".equals(flag)) {
			//查询该节点
			List<Map<String,Object>> parentList = treeDao.queryNodeByNodeId(nId);
			nodeList.addAll(0, parentList);
		}
		jsonObject.put("data", nodeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据类名和节点id查询后代指定类名子节点列表，包括普通节点，遍历至第一层指定类名节点则结束
	 * @param id 节点id
	 * @param className 类名组合，如果有多个类名，则中间用,隔开
	 * @param flag 是否包含该节点数据标志，等于空时不包含传入的节点数据，等于1时包含该节点数据
	 * @return
	 */
	@RequestMapping(value="getDescentantsToFirstByClassName",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getDescentantsToFirstByClassName(String id,String className,String flag) {
		System.out.println(CLASS_NAME + "getDescentantsToFirstByClassName" + "  param:" + id + "  " + className);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(className == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//获取指定类名的子节点
		List<Map<String,Object>> nodeList = ClientUtil.getDescendantsToFirstByClass(nId, className);
		if(nodeList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CHILD_SPECIFIED_NOT_EXIST);
			return jsonObject.toString();
		}
		if(flag != null && "1".equals(flag)) {
			//查询该节点
			List<Map<String,Object>> parentList = treeDao.queryNodeByNodeId(nId);
			nodeList.addAll(0, parentList);
		}
		jsonObject.put("data", nodeList);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据节点id查找下一级指定类名的节点
	 * @param id 节点id
	 * @param className 类名组合，如果有多个类名，则中间用,隔开
	 * @return
	 */
	@RequestMapping(value="getChildrenByClassName",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getChildrenByClassName(String id,String className) {
		System.out.println(CLASS_NAME + "getChildrenByClassName" + " param:" + id + " " + className);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(className == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		String[] classNames = className.split(",");
		for(int i=0;i<classNames.length;i++) {
			List<Map<String,Object>> nodeList = treeDao.queryChildrenByClassNameWithoutNormal(nId, classNames[i]);
			resultList.addAll(nodeList);
		}
		
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id和类名获取后代的设备(设备，不包括普通节点)
	 * @param id 节点id
	 * @param className 类名组合，如果有多个类名，则中间用,隔开
	 * @param flag 是否包含该节点数据标志，等于空时不包含传入的节点数据，等于1时包含该节点数据
	 * @return 设备列表json字符串
	 */
	@RequestMapping(value="getDeviceByClassName",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getDeviceByClassName(String id,String className,String flag) {
		System.out.println(CLASS_NAME + "getDeviceByClassName" + "  param:" + id + "  " + className);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(className == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		List<Map<String,Object>> nodeList = ClientUtil.getDescendantsByClass(nId, className);
		if(flag != null && "1".equals(flag)) {
			//查询该节点
			List<Map<String,Object>> parentList = treeDao.queryNodeByNodeId(nId);
			nodeList.addAll(0, parentList);
		}
		List<Map<String,Object>> deviceList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<nodeList.size();i++) {
			int type = (Integer)nodeList.get(i).get("node_type");
			if(type == TypeEnum.EQUIPMENT.getId()) {
				deviceList.add(nodeList.get(i));
			}
		}
		jsonObject.put("data", deviceList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id获取指定类名的节点列表(只包括指定类名节点，不包括普通节点)
	 * @param id 节点id
	 * @param className 类名组合，如果有多个类名，则中间用,隔开
	 * @param flag 可选参数，是否包含该节点标志，当该参数为null为不为1时，则只查询指定类名的子节点，
	 *  	 如果该参数为1时，则在该节点和子节点查询
	 * @return 节点json字符串
	 * data参数说明： 类型Array
	 * node_id: 节点id
	 * node_userid: 创建节点的用户id
	 * node_name: 节点的名称
	 * node_pid: 节点的父id,根节点pid为0
	 * node_treeid: 节点所对应的树id,即根节点id
	 * node_url: 节点对应的跳转链接地址
	 * node_type: 节点类型
	 * node_protocol: 节点协议，需要是设备的下一级节点，其他为null
	 * node_class: 节点属性名称，需要设备节点，其他为null
	 * node_sn: 节点设备号，需要设备节点，其他为null
	 */
	@RequestMapping(value="getNodesByClassName",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getNodesByClassName(String id,String className,String flag) {
		System.out.println(CLASS_NAME + "getNodesByClassName" + " param:" + id + " " + className);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(className == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//查找所有的子节点
		List<Map<String,Object>> children = ClientUtil.getDescendantById(nId);
		if("1".equals(flag)) {
			//查询该节点本身
			List<Map<String,Object>> nodeList = treeDao.queryNodeByNodeId(nId);
			children.addAll(0, nodeList);
		}
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		//分割类名组合
		String[] classNames = className.split(",");
		for(Map<String,Object> node : children) {
			for(int i=0;i<classNames.length;i++) {
				if(classNames[i].equals((String)node.get("node_class"))) {
					resultList.add(node);
					break;
				}
			}
			
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	
	/**
	 * 根据节点id和节点类型获取后代子节点列表
	 * @param id  节点id
	 * @param type 节点类型(1、普通节点  2、设备   3、文本  4、图片   5、视频  6、枚举   7、枚举动作  8、表格  9、图表  10、滑块   11、动作   )
	 * @param flag 是否包含该节点数据标志，等于空时不包含传入的节点数据，等于1时包含该节点数据
	 * @return 后代普通节点和指定类型节点，如果没有找到指定类型节点，则全部都是普通节点
	 */
	@RequestMapping(value="getDescentantsByNodeType",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getDescentantsByNodeType(String id,String type,String flag) {
		System.out.println(CLASS_NAME + "getDescentantsByNodeType" + "  param:" + id + "  " + type);
		CodeEnum code = CodeHelper.Strings2Numbers(id,type);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断节点类型是否存在
		int nType = Integer.valueOf(type);
		List<Map<String,Object>> typeList = typeDao.queryTypeById(nType);
		if(typeList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODETYPE_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询后代指定类型的节点列表
		List<Map<String,Object>> nodeList = ClientUtil.getDescentantsByType(nId, Integer.valueOf(type));
		if(nodeList.size() <= 0 ) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CHILD_SPECIFIED_NOT_EXIST);
			return jsonObject.toString();
		}
		
		if(flag != null && "1".equals(flag)) {
			//查询该节点
			List<Map<String,Object>> parentList = treeDao.queryNodeByNodeId(nId);
			nodeList.addAll(0, parentList);
		}
		
		jsonObject.put("data", nodeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id和节点类型获取后代节点,遍历到第一层指定节点类型则结束
	 * @param id 节点id
	 * @param type 节点类型
	 * @param flag flag 是否包含该节点数据标志，等于空时不包含传入的节点数据，等于1时包含该节点数据
	 * @return 后代普通节点和指定类型节点，如果没有找到指定类型节点，则全部都是普通节点
	 */
	@RequestMapping(value="getDescentantsToFirstByNodeType",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getDescentantsToFirstByNodeType(String id,String type,String flag) {
		System.out.println(CLASS_NAME + "getDescentantsToFirstByNodeType" + "  param:" + id + "  " + type);
		CodeEnum code = CodeHelper.Strings2Numbers(id,type);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断节点类型是否存在
		int nType = Integer.valueOf(type);
		List<Map<String,Object>> typeList = typeDao.queryTypeById(nType);
		if(typeList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODETYPE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//获取指定类型的子节点
		List<Map<String,Object>> nodeList = ClientUtil.getDescentantsToFirstByType(nId, nType);
		if(flag != null && "1".equals(flag)) {
			//查询该节点
			List<Map<String,Object>> parentList = treeDao.queryNodeByNodeId(nId);
			nodeList.addAll(0, parentList);
		}
		jsonObject.put("data", nodeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据节点id和节点类型获取下一级指定的类型节点
	 * @param id 节点id 
	 * @param type 节点类型(1、普通节点  2、设备   3、文本  4、图片   5、视频  6、枚举   7、枚举动作  8、表格  9、图表  10、滑块   11、动作   )
	 * @param containsValue 可选参数，是否包含节点对应的数据标志，如果为空，则不查询节点对应的数据
	 * @return
	 * data参数说明：类型Array
	 * node_id: 节点id
	 * node_userid: 创建节点的用户id
	 * node_name: 节点的名称
	 * node_pid: 节点的父id,根节点pid为0
	 * node_treeid: 节点所对应的树id,即根节点id
	 * node_url: 节点对应的跳转链接地址
	 * node_type: 节点类型
	 * node_protocol: 节点协议，需要是设备的下一级节点，其他为null
	 * node_class: 节点属性名称，需要设备节点，其他为null
	 * node_sn: 节点设备号，需要设备节点，其他为null
	 * value: 该节点对应的值
	 */
	@RequestMapping(value="getChildrenByNodeType",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getChildrenByNodeType(String id,String type,String containsValue) {
		System.out.println(CLASS_NAME + "getChildrenByNodeType" + "  param:" + id + "  " + type);
		CodeEnum code = CodeHelper.Strings2Numbers(id,type);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断节点类型是否存在
		int nType = Integer.valueOf(type);
		List<Map<String,Object>> typeList = typeDao.queryTypeById(nType);
		if(typeList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODETYPE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//查询下一级指定类型子节点
		List<Map<String,Object>> nodeList = treeDao.queryChildrenByNodeType(nId, nType);
		if(nodeList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CHILD_SPECIFIED_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询对应的value
		if(containsValue != null) {
			for(Map<String,Object> node : nodeList) {
				Map<String,Object> map = new HashMap<String, Object>();
				List<Map<String,Object>> valueList = valueDao.queryLimitValueByNodeId(((Long)node.get("node_id")).intValue(), 1);
				if(valueList.size() > 0) {
					map = valueList.get(0);
				}
				node.put("value", map);
			}
		}
		
		jsonObject.put("data", nodeList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id和节点类型查询后代所有指定类型节点，不包括普通节点
	 * @param id 节点id
	 * @param type 节点类型(1、普通节点  2、设备   3、文本  4、图片   5、视频  6、枚举   7、枚举动作  8、表格  9、图表  10、滑块   11、动作   )
	 * @return
	 */
	@RequestMapping(value="getNodesByNodeType",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getNodesByNodeType(String id,String type) {
		System.out.println(CLASS_NAME + "getNodesByNodeType" + "  param:" + id + "  " + type);
		CodeEnum code = CodeHelper.Strings2Numbers(id,type);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断节点类型是否存在
		int nType = Integer.valueOf(type);
		List<Map<String,Object>> typeList = typeDao.queryTypeById(nType);
		if(typeList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODETYPE_NOT_EXIST);
			return jsonObject.toString();
		}
		//获取所有的子代节点，根据类型筛选
		List<Map<String,Object>> nodeList = ClientUtil.getDescendantById(nId);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<nodeList.size();i++) {
			int nodeType = (Integer)nodeList.get(i).get("node_type");
			if(nodeType == nType) {
				resultList.add(nodeList.get(i));
			}
		}
		
		if(resultList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CHILD_SPECIFIED_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id和用户id查询该节点后代拥有浏览权限的子节点，如果该用户没有该节点的浏览权限，则返回数据为空
	 * @param nodeid 节点id
	 * @param userid 用户id
	 * @param flag  是否包含该节点数据标志，等于空时不包含传入的节点数据，等于1时包含该节点数据
	 * @return 后代节点列表json字符串
	 * 注：当该节点的后代节点过多时，该接口运行效率低，不推荐使用
	 */
	@RequestMapping(value="getNavigableDescentantsByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getNavigableDescentantsByNodeId(String nodeid,String userid,String flag) {
		System.out.println(CLASS_NAME + "getNavigableDescentantsByNodeId" + "  param:" + nodeid + "  " + userid + " " + flag);
		CodeEnum code = CodeHelper.Strings2Numbers(nodeid,userid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断节点是否存在
		int nNodeid = Integer.valueOf(nodeid);
		if(!treeDao.isExistNode(nNodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断该用户是否该树的创建者，是，则返回所有的子节点
		List<Map<String,Object>> nodeList = treeDao.queryNodeByNodeId(nNodeid);
		if((Integer)nodeList.get(0).get("node_userid") == nUserid) {
			List<Map<String,Object>> childrenList = ClientUtil.getDescendantById(nNodeid);
			if(flag != null && ("1").equals(flag)) {
				childrenList.addAll(0, nodeList);
			}
			jsonObject.put("data", childrenList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//查询有浏览权限的后代节点
		List<User> userList = userDao.queryById(nUserid);
		int group = userList.get(0).getUr_group();
		//查询该节点是否有权限，没有，则子节点也没有
		if(!userGroupPermissionDao.hasPermissionByGroupIdAndNodeId(group, nNodeid, 3)) {
			System.out.println(CLASS_NAME + "父节点无权限");
			return jsonObject.toString();
		}
		
		List<Map<String,Object>> resultList = ClientUtil.getPermissionDescentantsByNodeId(nNodeid, group, 3);
		if(flag != null && "1".equals(flag)) {
			resultList.addAll(0,nodeList);
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id和用户id获取指定操作权限的下一级子节点列表(必须是节点操作权限，如浏览、控制)
	 * @param nodeid 节点id
	 * @param userid 用户id
	 * @param permissionid 权限id  1、控制   3、浏览
	 * @param containsValue 可选参数，是否包含节点对应的数据标志，如果为空，则不查询节点对应的数据
	 * @return 下一级节点列表json字符串
	 */
	@RequestMapping(value="getPermissionChildrenByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getPermissionChildrenByNodeId(String nodeid,String userid,String permissionid,String containsValue) {
		System.out.println(CLASS_NAME + "getPermissionChildrenByNodeId" + "  param:" + nodeid + "  " + userid + "  " + permissionid);
		CodeEnum code = CodeHelper.Strings2Numbers(nodeid,userid,permissionid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断节点是否存在
		int nNodeid = Integer.valueOf(nodeid);
		if(!treeDao.isExistNode(nNodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断权限是否存在
		int nPermissionid = Integer.valueOf(permissionid);
		List<Map<String,Object>> perList = clientPermissionDao.queryByIdAndType(nPermissionid, 1);
		if(perList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.PERMISSION_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询用户用户组
		List<User> userList = userDao.queryUserById(nUserid);
		int groupId = userList.get(0).getUr_group();
		List<Map<String,Object>> childrenList = treeDao.queryChilrenByNodeId(nNodeid);
		
		//判断树是否该用户创建
		List<Tree> nodeList = treeDao.queryById(nNodeid);
		if(nodeList.get(0).getNode_userid() == nUserid) {
			jsonObject.put("data", childrenList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<childrenList.size();i++) {
			int id = ((Long)childrenList.get(i).get("node_id")).intValue();
			if(userGroupPermissionDao.hasPermissionByGroupIdAndNodeId(groupId, id, nPermissionid)) {
				resultList.add(childrenList.get(i));
			}
		}
		if(resultList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CHILD_SPECIFIED_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//查询对应的value
		if(containsValue != null) {
			for(Map<String,Object> node : resultList) {
				Map<String,Object> map = new HashMap<String, Object>();
				List<Map<String,Object>> valueList = valueDao.queryLimitValueByNodeId(((Long)node.get("node_id")).intValue(), 1);
				if(valueList.size() > 0) {
					map = valueList.get(0);
				}
				node.put("value", map);
			}
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id上传资源(图片或视频资源)，成功则返回资源链接地址(注：表单中的input[type=file] name必须为file)
	 * @param id  节点id
	 * @return json字符串
	 * data参数说明,类型Array
	 * data:[{"relative":""},{"complete":""}]    relative:上传成功后资源的相对网站地址    complete:上传成功后资源的完整地址(包括网址和接口)
	 * @throws Exception
	 * 示例：{"code":"0000","msg":"成功","data":[{"complete":"http://localhost:8080/Tree/upload/resource/2018-03-20/res_1521516084653.jpg","relative":"upload/resource/2018-03-20/res_1521516084653.jpg"}]}
	 */
	 @RequestMapping(value="uploadResourceByNodeId",produces="text/json;charset=UTF-8",method=RequestMethod.POST)
	 @ResponseBody
	 public String uploadResourceByNodeId(HttpServletRequest request,String id) throws Exception {
		 	System.out.println(CLASS_NAME + "uploadResourceByNodeId" + "  param:" + id);
		 	CodeEnum code = CodeHelper.String2Number(id);
			JSONObject jsonObject = ClientUtil.getCodeJSON(code);
			if(code != CodeEnum.SUCCESS) {
				return jsonObject.toString();
			}
			//判断节点是否存在
			int nId = Integer.valueOf(id);
			if(!treeDao.isExistNode(nId)) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
				return jsonObject.toString();
			}
			
			//判断节点类型是否图片或视频
			List<Map<String,Object>> nodeList = treeDao.queryNodeByNodeId(nId);
			int type = (Integer)nodeList.get(0).get("node_type");
			System.out.println(CLASS_NAME + "type:" + type);
			if(type != TypeEnum.IMAGE.getId() && type != TypeEnum.VIDEO.getId()) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_NODE_TYPE);
				return jsonObject.toString();
			}
			String path = ClientUtil.fileUpload(request, "resource", "res_");
			
			if(path == null) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_UPLOAD_RESOURCE);
				return jsonObject.toString();
			}
			//将资源地址存入数据库
			NodeAttr nodeAttr = new NodeAttr();
			nodeAttr.setAttr_nodeid(nId);
			nodeAttr.setAttr_type(2);
			nodeAttr.setAttr_value(path);
			List<Map<String,Object>> attrList = nodeAttrDao.queryAttrByNodeIdAndType(nId, 2);
			if(attrList.size() <= 0) {
				//直接添加
				nodeAttrDao.addNodeAttr(nodeAttr);
			}else if(attrList.size() == 1) {
				//修改
				nodeAttrDao.updateByNodeIdAndType(nId, 2, nodeAttr);
			}else  {
				//删除全部数据后添加
				nodeAttrDao.deleteByNodeIdAndType(nId, 2);
				nodeAttrDao.addNodeAttr(nodeAttr);
			}
			
			
			//网站地址
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
			
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("relative", path);
			map.put("complete",basePath +  path);
			resultList.add(map);
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
 		    return jsonObject.toString();
	 }
	
	 /**
	  * 上传资源(图片或者视频资源),，成功则返回资源链接地址,不需要参数(注：表单中的input[type=file] name必须为file)
	  *@return json字符串
	  * data参数说明,类型Array
	  * data:[{"relative":""},{"complete":""}]    relative:上传成功后资源的相对网站地址    complete:上传成功后资源的完整地址(包括网址和接口)
	  * @throws Exception
	  * 示例：{"code":"0000","msg":"成功","data":[{"complete":"http://localhost:8080/Tree/upload/resource/2018-03-20/res_1521516084653.jpg","relative":"upload/resource/2018-03-20/res_1521516084653.jpg"}]}
	  */
	@RequestMapping(value="uploadResource",produces="text/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String uploadResource(HttpServletRequest request) throws Exception {
		System.out.println(CLASS_NAME + "uploadResource");
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		String path = ClientUtil.fileUpload(request, "resource", "res_");
		if(path == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_UPLOAD_RESOURCE);
			return jsonObject.toString();
		}
		//网站地址
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("relative", path);
		map.put("complete",basePath +  path);
		resultList.add(map);
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点获取节点对应的资源链接
	 * @param id 节点id
	 * @return 图片链接json字符串
	 * * data参数说明,类型Array
	  * data:[{"relative":""},{"complete":""}]    relative:资源的相对网站地址    complete:资源的完整地址(包括网址和接口)
	 * 示例：{"code":"0000","msg":"成功","data":[{"complete":"http://localhost:8080/Tree/upload/resource/2018-03-20/res_1521516084653.jpg","relative":"upload/resource/2018-03-20/res_1521516084653.jpg"}]}
	 */
	@RequestMapping(value="getResourceUrlByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getResourceUrlByNodeId(HttpServletRequest request,String id) {
		System.out.println(CLASS_NAME + "getResourceUrlByNodeId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		List<Map<String,Object>> resList = nodeAttrDao.queryAttrByNodeIdAndType(nId, 2);
		if(resList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.RESOURCE_NOT_EXIST);
			return jsonObject.toString();
		}
		//网站地址
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("relative", resList.get(0).get("attr_value"));
		map.put("complete",basePath + resList.get(0).get("attr_value"));
		resultList.add(map);
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	
	
	/********************************************节点数据接口*********************************************************/
	
	
	/**
	 * 根据节点id查询对应的数据列表
	 * @param id 节点id
	 * @return 节点数据(值)json字符串
	 * data 参数说明：类型Array
	 * value_id: 数据id
	 * value_nodeid: 数据对应的节点id
	 * value_data: 数据对应的json字符串，json的键由用户自定义
	 */
	@RequestMapping(value="getValueByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getValueByNodeId(String id) {
		System.out.println(CLASS_NAME + "getValueByNodeId" + " param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		List<Map<String,Object>> valueList = valueDao.queryValueByNodeId(nId);
		if(valueList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.VALUE_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", valueList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id获取最新的节点对应的数据
	 * @param id 节点id
	 * @return 数据json字符串
	 */
	@RequestMapping(value="getLastValueByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getLastValueByNodeId(String id) {
		System.out.println(CLASS_NAME + "getLastValueByNodeId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//查询最新的数据
		List<Map<String,Object>> valueList = valueDao.queryLimitValueByNodeId(nId, 1);
		if(valueList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.VALUE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		jsonObject.put("data", valueList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据节点id更新指定键对应的值，如果该键不存在，可根据参数设置不更新或者往协议添加该键，同时添加值（原有基础上添加，如果该节点没有对应的数据，则新增数据）
	 * @param id 节点id
	 * @param key 数据键名
	 * @param value 数据键名对应的值
	 * @param updateProtocol 可选参数，如果为空或者不为1，则当协议中不存在该键名时，返回错误信息，为1时，当协议中没有该键，则修改协议并且修改数据
	 * @return
	 */
	@RequestMapping(value="updateValueByKey",produces="text/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String updateValueByKey(String id,String key,String value,String updateProtocol) {
		System.out.println(CLASS_NAME + "updateValueByKey" + " param:" + id + " " + key + " " + value + " " + updateProtocol);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(key == null || value == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		List<Tree> nodeList = treeDao.queryById(nId);
		
		//如果传输方式为透传，则返回错误信息
		if(nodeList.get(0).getNode_tstype() == TRANS_PASSTHROUGH) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.KEY_NOT_EXIST);
			return jsonObject.toString();
		}
		
		String procotol = nodeList.get(0).getNode_protocol();
		boolean isExistKey = false;
		if(procotol != null && !("").equals(procotol.trim())) {
			JSONArray jsonArray = JSONArray.fromObject(procotol);
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				if(key.equals(object.get("identification"))) {
					isExistKey = true;
					break;
				}
			}
		}
		//不允许修改协议
		if(!"1".equals(updateProtocol)) {
			//不存在该键，返回错误信息
			if(!isExistKey) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.KEY_NOT_EXIST);
				return jsonObject.toString();
			}
		}else {
			JSONArray jsonArray = null;
			if(procotol == null || ("").equals(procotol.trim())) {
				jsonArray = new JSONArray();
			}else {
				jsonArray = JSONArray.fromObject(procotol);
			}
			if(!isExistKey) {
				//添加该键，并且修改协议
				JSONObject object = new JSONObject();
				object.put("identification", key);
				object.put("name","");
				object.put("nodeId", nId);
				object.put("remark", "");
				jsonArray.add(object);
				nodeList.get(0).setNode_protocol(jsonArray.toString());
				treeDao.updateTree(nodeList.get(0));
				
			}
		}
		
		//修改对应的值
		List<Map<String,Object>> valueList = valueDao.queryLimitValueByNodeId(nId, 1);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = dateFormat.format(new Date());
		
		Value data = new Value();
		data.setValue_nodeid(nId);
		data.setValue_datetime(dateTime);
		
		if(valueList.size() <= 0) {
			//添加数据
			JSONObject object = new JSONObject();
			object.put(key, value);
			data.setValue_data(object.toString());
			
			valueDao.addValue(data);
			
		}else {
			//修改最后一条数据
			JSONObject object = JSONObject.fromObject(valueList.get(0).get("value_data"));
			object.put(key, value);
			data.setValue_id(((Long)valueList.get(0).get("value_id")).intValue());
			data.setValue_data(object.toString());
			valueDao.updateValue(data);
		}
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	
	
	/******************************************会议接口********************************************************/
	
	/**
	 * 用户登录
	 * @param username 用户名
	 * @param password 密码
	 * @return json字符串，成功则传该登录用户信息(密码除外)
	 */
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
		List<Map<String,Object>> userList = userDao.queryByUsernameAndPassword(username, MD5Util.md5(password));
		if(userList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_LOGIN_MESSAGE);
			return jsonObject.toString();
		}
		jsonObject.put("data", userList);
		System.out.println(CLASS_NAME + jsonObject.toString());
//		//存入session
//		HttpSession session = request.getSession();
//		session.setAttribute("USERNAME", userList.get(0).get("ur_username"));
		return jsonObject.toString();
	}
	
	
	/**
	 * 普通用户注册
	 * @param username 用户名
	 * @param password 密码
	 * @param group 用户组id
	 * @return json字符串
	 * data参数说明： 类型Array
	 * 注册信息错误：[{"username":"" , "password":"" , "group":""}]
	 * 注册成功:注册成功的用户信息
	 */
	@RequestMapping(value="register",produces="text/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String register(String username,String password,String group) {
		System.out.println(CLASS_NAME + "  param:" + username + "  " + password + "  " + group);
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_REG_MESSAGE);     //默认注册信息错误
		List<Map<String,Object>> errorList = new ArrayList<Map<String,Object>>();    //错误列表
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
		
		//检查用户组id
		if(group == null) {
			error.put("group", "用户组id不能为空");
		}else if(!NumberUtils.isNumber(group)) {
			error.put("group", "用户组id类型错误");
		}else if(!userGroupDao.isExistGroupById(Integer.valueOf(group))) {
			error.put("grou", "用户组不存在");
		}
		
		errorList.add(error);
		//存在错误
		if(error.size() > 0) {
			jsonObject.put("data", errorList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//注册成功
		jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		User user = new User();
		user.setUr_username(username);
		user.setUr_password(MD5Util.md5(password));
		user.setUr_type(0);
		user.setUr_group(Integer.valueOf(group));
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		user.setUr_datetime(dateFormat.format(new Date()));
		
		//添加用户
		int id = userDao.addUserNew(user);
		System.out.println(CLASS_NAME + "id=" + id);
		//查询用户
		List<Map<String,Object>> userList = userDao.queryUserByUserIdAndType(id, 0);
		jsonObject.put("data", userList);
		System.out.println(CLASS_NAME + jsonObject);
		return jsonObject.toString();
	}
	
	
	
	/**
	 * 根据用户id查询用户信息
	 * @param id 用户id
	 * @return 用户json字符串
	 */
	@RequestMapping(value="getUserById",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getUserById(String id,HttpServletRequest request) {
		System.out.println(CLASS_NAME + "getUserById" + "  param:" + id);
//		HttpSession session = request.getSession();
//		System.out.println(CLASS_NAME + "session用户名：" + session.getAttribute("USERNAME"));
		
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断用户是否存在
		int nId = Integer.valueOf(id);
		
		//查询用户
		List<Map<String,Object>> userList = userDao.queryUserMapById(nId);
		if(userList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		
		jsonObject.put("data", userList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据用户id和密码获取用户信息
	 * @param id  用户id
	 * @param password  密码(md5加密后)
	 * @return
	 * 暂时废弃
	 */
	/*
	@RequestMapping(value="getUserByIdAndPsw",method=RequestMethod.POST,produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getUserByIdAndPsw(String id,String password) {
		System.out.println(CLASS_NAME + "getUserByIdAndPsw" + "  param:" + id + "  " + password);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		if(password == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		
		//判断用户是否存在
		int nId = Integer.valueOf(id);
		
		//查询用户
		List<Map<String,Object>> userList = userDao.queryUserByIdAndPsw(nId, password, 0);
		if(userList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", userList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	*/
	
	
	/**
	 * 根据用户组id获取用户组信息
	 * @param id 用户组id
	 * @return 用户组json字符串
	 * data参数说明,类型Array
	 * urg_id: 用户组id
	 * urg_name : 用户组名称
	 * urg_userid: 创建该用户组的用户id
	 * urg_treeid: 用户组对应的树根节点id
	 */
	@RequestMapping(value="getGroupById",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getGroupById(String id) {
		System.out.println(CLASS_NAME + "getGroupById" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		int nId = Integer.valueOf(id);
		//根据id查询用户组
		List<Map<String,Object>> groupList = userGroupDao.queryGroupById(nId);
		if(groupList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.GROUP_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", groupList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据树根节点id查询用户组列表
	 * @param id 树根节点id
	 * @return 用户组列表json字符串
	 */
	@RequestMapping(value="getGroupByTreeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getGroupByTreeId(String id) {
		System.out.println(CLASS_NAME + "getGroupByTreeId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		int nId = Integer.valueOf(id);
		//根据树id查询用户组
		List<Map<String,Object>> groupList = userGroupDao.queryGroupByTreeId(nId);
		if(groupList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.GROUP_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", groupList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据节点id获取普通子节点和会议子节点(包括它本身)
	 * @param id 节点id
	 * @return 树节点列表json字符串
	 */
	@RequestMapping(value="getMeetingChilrenByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getMeetingChilrenByNodeId(String id) {
		System.out.println(CLASS_NAME + "getMeetingChilrenByNodeId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//查找普通子节点和会议子节点
		List<Map<String,Object>> children = ClientUtil.getMeetingDescendantById(nId);
		//添加该节点
		List<Map<String,Object>> nodeList = treeDao.queryNodeByNodeId(nId);
		children.addAll(0, nodeList);
		jsonObject.put("data", children);
		
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id获取会议列表
	 * @param id 节点id
	 * @return 会议列表Json字符串
	 * data参数说明，类型Array
	 * me_id: 会议id
	 * me_userid: 创建会议的用户id
	 * me_nodeid: 会议对应的节点id
	 * me_title: 会议名称
	 * me_number: 参加会议人数
	 * me_starttime: 会议开始时间，格式为  yyyy-MM-dd HH:mm:ss
	 * me_endtiem: 会议结束时间，格式为yyyy-MM-dd HH:mm:ss
	 * me_status:会议的审核状态，0、未审核  1、已审核
	 */
	@RequestMapping(value="getMeetingByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getMeetingByNodeId(String id) {
		System.out.println(CLASS_NAME + "getMeetingByNodeId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//根据节点查询会议
		List<Map<String,Object>> meetingList = meetingDao.queryMeetingByNodeId(nId);
		if(meetingList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.MEETING_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", meetingList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据节点id和日期查询会议列表
	 * @param id 节点id
	 * @param date 日期，格式为yyyy-MM-dd
	 * @return 会议列表json字符串(包括用户名)
	 */
	@RequestMapping(value="getMeetingByNodeIdAndDate",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getMeetingByNodeIdAndDate(String id,String date) {
		System.out.println(CLASS_NAME + "getMeetingByNodeIdAndDate" + "  param:" + id + "  " + date);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断日期是否符合格式
		if(!ClientUtil.isValidDate(date,"yyyy-MM-dd")) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_DATE_FORMAT);
			return jsonObject.toString();
		}
		
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//日期去除两边空格
		date = date.trim();
		//根据节点id查询会议
		List<Map<String,Object>> meetingList = meetingDao.queryByNodeIdAndDate(nId, date);
		/*
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();        //筛选日期后的会议列表
		for(int i=0;i<meetingList.size();i++) {
			Map<String,Object> meeting = meetingList.get(i);
			String starttime = (String) meeting.get("me_starttime");
			String[] dates = starttime.split(" ");
			System.out.println(CLASS_NAME + dates[0] + "  " + dates[1]);
			String startDate = dates[0].trim();
			if(ClientUtil.isDateEquale(date, starttime)) {
				list.add(meeting);
			}
		}
		*/
		
		if(meetingList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.MEETING_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", meetingList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 获取所有用户列表(返回用户id和用户名)
	 * @return 用户json字符串(ur_id,ur_username)
	 */
	@RequestMapping(value="getUserList",produces="text/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String getUserList() {
		System.out.println(CLASS_NAME + "getUserList");
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		//查询所有用户
		List<Map<String,Object>> userList = userDao.queryAllUsers();
		jsonObject.put("data", userList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	
	/**
	 * 根据标识或日期获取今天或指定日期下一周的日期列表
	 * @param isToday 获取今天的标识,"0"获取今天后七天的日期列表
	 * @param date 指定日期,格式为yyyy-MM-dd
	 * @param offset 相对于指定日期的偏移量  -1代表指定日期的前一天 ，1代表指定日期的后一天 ， 以此类推
	 * @return 日期列表json字符串，包括星期
	 * data参数说明：
	 * date: 每一天的日期字符串 ,格式为yyyy-MM-dd
	 * week: 星期索引 , 0对应周日，1-6对应周一到周六
	 * 示例：{"code":"0000","msg":"成功","data":[{"date":"2018-03-05","week":1},{"date":"2018-03-06","week":2}]}
	 */
	@RequestMapping(value="getWeekDate",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getWeekDate(String isToday,String date,String offset) {
		System.out.println(CLASS_NAME + "getWeekDate" + "  param:" + isToday + "  " + date);
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		if(isToday == null && date == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		//查询本周的一周日期列表
		if(isToday != null && "0".equals(isToday)) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String today = dateFormat.format(new Date());
			try {
				List<Map<String,Object>> dateList = ClientUtil.getAfterWeekWithWeek(today);
				jsonObject.put("data", dateList);
				System.out.println(CLASS_NAME + jsonObject.toString());
				return jsonObject.toString();
			} catch (Exception e) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_UNKNOW);
				return jsonObject.toString();
			}
			
		}
		
		//查询指定日期的一周日期列表
		//检查日期字符串是否符合yyyy-MM-dd格式
		if(!ClientUtil.isValidDate(date,"yyyy-MM-dd")) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_DATE_FORMAT);
			return jsonObject.toString();
		}
		if(offset == null) {
			try {
				List<Map<String,Object>> dateList = ClientUtil.getAfterWeekWithWeek(date);
				jsonObject.put("data", dateList);
				System.out.println(CLASS_NAME + jsonObject.toString());
				return jsonObject.toString();
			} catch (Exception e) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_UNKNOW);
				return jsonObject.toString();
			}
		}
		
		//根据指定日期和偏移量获取对应的后七天日期
		CodeEnum code = CodeHelper.String2Number(offset);
		jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		String offsetDate = ClientUtil.getDateRelatively(date, Integer.valueOf(offset), "yyyy-MM-dd");
		try {
			List<Map<String,Object>> list = ClientUtil.getAfterWeekWithWeek(offsetDate);
			jsonObject.put("data", list);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		} catch (Exception e) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_UNKNOW);
			return jsonObject.toString();
		}
	}
	
	/**
	 * 预约会议
	 * @param ur_id  用户id
	 * @param node_id 节点id
	 * @param me_title 会议标题
	 * @param me_num 会议人数
	 * @param me_starttime 会议开始时间 ，格式为 HH:mm:ss
	 * @param me_endtime 会议结束时间 ，格式为 HH:mm:ss
	 * @param me_description 会议说明
	 * @param idGroup  预约的用户id,中间用,隔开
	 * @param target_date 会议日期，格式为yyyy-MM-dd
	 * @return 
	 */
	@RequestMapping(value="addMeeting",produces="text/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String addMeeting(String ur_id,String node_id,String me_title,String me_number,
			String me_starttime,String me_endtime,String me_description,String idGroup,String target_date) {
		System.out.println(CLASS_NAME + "addMeeting" + " param:" + node_id + "  " + target_date + "  " + me_starttime + " " + me_endtime + " " + idGroup);
		
		//判断必填的参数是否为空
		CodeEnum code = CodeHelper.isStringsNull(me_title,me_starttime,me_endtime,idGroup,target_date);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//检查id字符串是否是数字格式
		code = CodeHelper.Strings2Numbers(ur_id,node_id,me_number);
		if(code != CodeEnum.SUCCESS) {
			jsonObject = ClientUtil.getCodeJSON(code);
			return jsonObject.toString();
		}
		
		//判断用户是否存在
		int nUserid = Integer.valueOf(ur_id);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nNodeid = Integer.valueOf(node_id);
		if(!treeDao.isExistNode(nNodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断日期是否符合yyyt-MM-dd格式
		if(!ClientUtil.isValidDate(target_date,"yyyy-MM-dd")) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_DATE_FORMAT);
			return jsonObject.toString();
		}
		
		//将日期字符转成标准的格式
		target_date = ClientUtil.formatDate(target_date,"yyyy-MM-dd");
		System.out.println(CLASS_NAME + "target_date:" + target_date);
		
		String starttime = target_date + " " + me_starttime.trim();
		String endtime = target_date + " " + me_endtime.trim();
		System.out.println(CLASS_NAME + ClientUtil.isValidDate(starttime, "yyyy-MM-dd HH:mm:ss") + "  " + ClientUtil.isValidDate(endtime, "yyyy-MM-dd HH:mm:ss"));
		//判断开始时间、结束时间是否符合日期格式
		if(!ClientUtil.isValidDate(starttime, "yyyy-MM-dd HH:mm:ss") || !ClientUtil.isValidDate(endtime, "yyyy-MM-dd HH:mm:ss")) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_TIME_FORMAT);
			return jsonObject.toString();
		}
		
		//判断结束时间是否晚于开始时间
		if(ClientUtil.compareDate(starttime, endtime, "yyyy-MM-dd HH:mm:ss") != -1) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_TIME_ORDER);
			return jsonObject.toString();
		}
		
		//格式化时间
		starttime = ClientUtil.formatDate(starttime, "yyyy-MM-dd HH:mm:ss");
		endtime = ClientUtil.formatDate(endtime, "yyyy-MM-dd HH:mm:ss");
		
		//根据节点id和日期查询会议，判断是否有时间冲突
		List<Map<String,Object>> meetingList = meetingDao.queryMeetingByNodeIdAndDate(nNodeid, target_date);
		for(int i=0;i<meetingList.size();i++) {
			//获取开始时间
			String start = (String) meetingList.get(i).get("me_starttime");
			String end = (String) meetingList.get(i).get("me_endtime");
			if(ClientUtil.isBetweenDates(start, end, starttime, "yyyy-MM-dd HH:mm:ss")) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_START_TIME);
				return jsonObject.toString();
			}
		}
		
		//添加会议
		Meeting meeting = new Meeting();
		meeting.setMe_nodeid(nNodeid);
		meeting.setMe_userid(nUserid);
		meeting.setMe_title(me_title);
		meeting.setMe_number(Integer.valueOf(me_number));
		meeting.setMe_starttime(starttime);
		meeting.setMe_endtime(endtime);
		meeting.setMe_description(me_description);
		meeting.setMe_status(0);
		System.out.println(CLASS_NAME + meeting.toString());
		int meetingId = meetingDao.addMeeting(meeting);
		String[] ids = idGroup.split(",");
		for(int i=0;i<ids.length;i++) {
			Conferee conferee = new Conferee();
			conferee.setCon_meid(meetingId);
			conferee.setCon_userid(Integer.valueOf(ids[i]));
			confereeDao.addConferee(conferee);
		}
		
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据指定日期和指定偏移量查询会议，默认查询今天以后6天（包括今天）的所有会议
	 * @param id 节点会议 
	 * @param date 可选参数 ，默认为今天，要查询的日期
	 * @param offset 可选参数，默认为6，日期偏移量 ，比如-2代表前两天 ，2代表后两天
	 * @return 会议json字符串
	 */
	@RequestMapping(value="getMeetingByDates",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getMeetingByDates(String id,String date,String offset) {
		System.out.println(CLASS_NAME + "getMeetingByDates" + " param:" + date + "  " + offset);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//默认日期为今天
		if(date == null) {
			date = dateFormat.format(new Date());
		} else if(ClientUtil.isValidDate(date, "yyyy-MM-dd")) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_TIME_FORMAT);
			return jsonObject.toString();
		}
		//默认获取一周内的会议
		if(offset == null) {
			offset = "6";
		}else if(!NumberUtils.isNumber(offset)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
			return jsonObject.toString();
		}
		int nOffset = Integer.valueOf(offset);
		List<String> dateList = new ArrayList<String>();
		if(nOffset <= 0) {
			for(int i=nOffset;i<0;i++) {
				dateList.add(ClientUtil.getDateRelatively(date, i, "yyyy-MM-dd"));
			}
			dateList.add(date);
		}else {
			for(int i=0;i<=nOffset;i++) {
				dateList.add(ClientUtil.getDateRelatively(date, i, "yyyy-MM-dd"));
			}
		}
		
		List<List<Map<String,Object>>> resultList = new ArrayList<List<Map<String,Object>>>();
		//查询对应日期的会议
		for(int i=0;i<dateList.size();i++) {
			List<Map<String,Object>> list = meetingDao.queryByNodeIdAndDate(nId, dateList.get(i));
			resultList.add(list);
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id和日期(日期可省略，默认为今天)查询对应的星期的所有会议列表
	 * @param id 节点id
	 * @param date 可选参数，日期，格式为yyyy-MM-dd，默认为今天
	 * @return 会议json列表字符串
	 * data参数说明，类型Array
	 * date: 日期字符串，格式为yyyy-MM-dd
	 * meeting: 会议列表
	 * 示例：{"code":"0000","msg":"成功","data":[{"meeting":[],"date":"2018-02-25"},{"meeting":[],"date":"2018-02-26"}]}
	 */
	@RequestMapping(value="getWeekMeetingByDate",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getWeekMeetingByDate(String id,String date) throws Exception {
		System.out.println(CLASS_NAME + "getWeekMeetingByDate" + " param:" + id + " " + date);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//检查节点id是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		//默认为本周
		if(date == null) {
			date = dateFormat.format(new Date());
		}else if(!ClientUtil.isValidDate(date, "yyyy-MM-dd")) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_DATE_FORMAT);
			return jsonObject.toString();
		}
		//获取一周的日期
		List<String> weekList = ClientUtil.getWeekDays(date);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<weekList.size();i++) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("date", weekList.get(i));
			List<Map<String,Object>> meetingList = meetingDao.queryByNodeIdAndDate(nId, weekList.get(i));
			map.put("meeting", meetingList);
			resultList.add(map);
		}
		
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/***************************************与nodeJs交互接口*****************************************************/
	
	/**
	 * 根据节点id查询对应设备的父级或自身设备的sn号(提供给客户端的NodeJs使用，不提供给客户端)
	 * @param nodeid 节点id
	 * @param userid 用户id
	 * @return 返回给nodejs的Json字符串
	 * json格式：{"sn":"" , "userid":"" , "code":"" , "msg":"" , "data":[]}
	 * 参数说明: sn：设备的SN号    userid:用户id  code:状态码    msg:说明     data:数据，为空
	 * 示例：{"code":"0000","msg":"成功","data":[],"sn":"123456","userid":"1"}
	 */
	@RequestMapping(value="getDeviceSNByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getDeviceSNByNodeId(String nodeid,String userid) {
		System.out.println(CLASS_NAME + "getDeviceSNByNodeId" + "  param:" + nodeid + "  " + userid);
		CodeEnum code = CodeHelper.Strings2Numbers(nodeid,userid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nNodeid = Integer.valueOf(nodeid);
		if(!treeDao.isExistNode(nNodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//获取节点,判断节点类型是否设备或设备子类
		List<Map<String,Object>> nodeList = treeDao.queryNodeByNodeId(nNodeid);
		int type = (Integer) nodeList.get(0).get("node_type");
		if(type == TypeEnum.NORMAL.getId()) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_NODE_TYPE);
			return jsonObject.toString();
		}
		List<Map<String,Object>> deviceList = ClientUtil.getDeviceParent(nNodeid);
		if(deviceList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.SN_NOT_EXIST);
			return jsonObject.toString();
		}
		
		jsonObject.put("sn", deviceList.get(0).get("node_sn"));
		jsonObject.put("userid", userid);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 客户端发送指令(触发动作)控制设备，不成功则将错误信息发送给客户端，成功则将自定义协议发送给设备，设备对协议解析处理
	 * @param nodeid
	 * @param userid
	 * @return json字符串
	 * json参数说明：
	 * 不成功：{"code":String,"msg":String,"data":Array,"type":String,"userid":String}
	 * code:标识码,0000为成功         msg:标识码的中文说明    data:数据，返回客户端的数据为空       type:客户端标识           userid:用户id
	 * 
	 * 成功: {"code":String,"msg":String,"data":Array,"type":String,"sn":String}
	 * code:标识码   msg:标识码的中文说明    data:数组,存放自定义协议    type:设备标识,0是发送给客户端，1是发送给设备       sn:设备的SN号
	 * 
	 * 示例:{"code":"0000","msg":"成功","data":[{"code":"if(status==1){status=2}","time":"2018-03-14"}],"type":"1","sn":"123456"}
	 */
	@RequestMapping(value="controlDevice",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String controlDevice(HttpServletRequest request,String nodeid,String userid) {
		System.out.println(CLASS_NAME + "controlDevice" + "  param:" + nodeid);
		CodeEnum code = CodeHelper.Strings2Numbers(nodeid,userid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		jsonObject.put("type", "0");    //发送给设备或客户端的标识，NodeJS负责解析分发
		jsonObject.put("userid", userid);     //按照nodeJS的连接需要，需要将userid传给客户端
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			jsonObject.put("type", "0");
			jsonObject.put("userid", userid);
			System.out.println(CLASS_NAME + "controlDevice  " + jsonObject.toString());
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nNodeId = Integer.valueOf(nodeid);
		if(!treeDao.isExistNode(nNodeId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			jsonObject.put("type", "0");
			jsonObject.put("userid", userid);
			System.out.println(CLASS_NAME + "controlDevice  " + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//判断节点类型是否动作类
		List<Map<String,Object>> nodeList = treeDao.queryNodeByNodeId(nNodeId);
		int type = (Integer) nodeList.get(0).get("node_type");
		System.out.println(CLASS_NAME + "type:" + type);
		if(type != TypeEnum.ENUMACTION.getId() && type != TypeEnum.ACTION.getId() && type != TypeEnum.RANGE.getId()) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_NODE_TYPE);
			jsonObject.put("type", "0");
			jsonObject.put("userid", userid);
			System.out.println(CLASS_NAME + "controlDevice  " + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//查询设备的SN号
		List<Map<String,Object>> deviceList = ClientUtil.getDeviceParent(nNodeId);
		if(deviceList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.SN_NOT_EXIST);
			jsonObject.put("type", "0");
			jsonObject.put("userid", userid);
			System.out.println(CLASS_NAME + "controlDevice  " + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//查询节点对应的数据
		List<Map<String,Object>> valueList = valueDao.queryValueByNodeId(nNodeId);
		List<String> result = new ArrayList<String>();
		if(valueList.size() != 0) {
			result.add((String)valueList.get(0).get("value_data"));
		}
		//添加sn号
		jsonObject.put("sn", deviceList.get(0).get("node_sn"));
		jsonObject.put("data", result);
		jsonObject.put("type", "1");                //分发给设备的标志
		jsonObject.remove("userid");        //移除userid的键，这个不需要发送给设备，只需要发送给客户端
		System.out.println(CLASS_NAME + "controlDevice  " + jsonObject.toString());
		
		//写入日志
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strTime = dateFormat.format(new Date());
		StringBuffer text = new StringBuffer();
		text.append(strTime + ": ");
		//查询用户
		List<User> userList = userDao.queryById(nUserid);
		text.append("用户").append(userList.get(0).getUr_username()).append("(id=" + nUserid).append(") 操作了 ");
		text.append(ClientUtil.getNodeFullName(nNodeId)).append("(id=" + nNodeId + ")");
		//获取log文件夹实际路径
		String logPath = request.getServletContext().getRealPath("") + "/WEB-INF/views/public/log/";
		ClientUtil.appendLog(logPath, text.toString());
		System.out.println(CLASS_NAME + text.toString());
		System.out.println(CLASS_NAME + logPath);
		return jsonObject.toString();
	}
	
	
	/**
	 * NodeJS发送主机信息(ip、端口、项目id)到服务器，保存主机信息
	 * @param idGroup 树根节点id组合，格式为 xx,xx,xx，比如  1,2,3
	 * @param ip ip地址
	 * @param port 端口号
	 * @return
	 */
	@RequestMapping(value="sendHost",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String sendHost(String idGroup,String ip,String port) {
		System.out.println(CLASS_NAME + "sendHost()  params:" + idGroup + " " + ip + " " + port);
		CodeEnum code = CodeHelper.isStringsNull(idGroup,ip,port);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(!NumberUtils.isNumber(port)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
			return jsonObject.toString();
		}
		//检查idGroup是否符合格式
		if(!ClientUtil.isValidIdGroup(idGroup)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//检查idGroup每个树节点是否存在
		String[] ids = idGroup.split(",");
		boolean isExistError = false;
		for(int i=0;i<ids.length;i++) {
			if(!treeDao.isExistRootById(Integer.valueOf(ids[i]))) {
				isExistError = true;
				break;
			}
		}
		if(isExistError) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = dateFormat.format(new Date());
		//将主机信息写入数据库
		for(int i=0;i<ids.length;i++) {
			Host host = new Host();
			host.setHost_treeid(Integer.valueOf(ids[i]));
			host.setHost_ip(ip);
			host.setHost_port(Integer.valueOf(port));
			host.setHost_updatetime(datetime);
			
			//判断是否存在主机信息
			if(hostDao.isExistHostByTreeId(Integer.valueOf(ids[i]))) {
				//更新
				hostDao.updateHostByTreeId(host);
			}else {
				//新增
				hostDao.addHost(host);
			}
		}
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/*******************************************属性模板操作接口***************************************************/
	
	/**
	 * 根据树根节点id查询该树拥有的所有属性模板列表
	 * @param id 树根节点id
	 * @return 属性模板json字符串
	 * data参数说明：类型Array
	 * tp_id: 模板id
	 * tp_name: 模板的名称
	 * tp_data: 模板数据，是一个json格式的字符串，格式为{"name":"","type":"" [,"option":"选项1,选项2"]}，
	 *          键name为属性名称,type为属性类型，分为四种类型:text(文本)、boolean(布尔型)、file(文件类型)、enum(枚举，它的选项对应的键为option)
	 * tp_treeid:  模板对应的节点id
	 * 示例: {"code":"0000","msg":"成功","data":[{"tp_id":17,"tp_name":"11","tp_data":"[{"name":"11","type":"text"}]","tp_treeid":null}]}
	 */
	@RequestMapping(value="getTemplateListByTreeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getTemplateListByTreeId(String id) {
		System.out.println(CLASS_NAME + "getTemplateByNodeId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//根据节点获取模板
		List<Map<String,Object>> tpList = templateDao.queryTemplateByNodeId(nId);
		if(tpList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.TEMPLATE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		jsonObject.put("data", tpList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据模板id和节点id添加或修改模板内容，当有相应的数据，则修改数据，没有则添加数据
	 * @param templateid 模板id
	 * @param nodeid 节点id
	 * @param content 属性内容，参数类型必须为json格式的字符串，如{"key":"value"},key对应的模板的属性名，value对应用户填入的属性值
	 * @param userid 用户id，填写该属性表单的用户
	 * @return 属性内容json字符串，返回添加或修改的属性内容
	 * data参数说明:类型Array
	 * tpc_id： 数据内容id
	 * tpc_tpid: 内容对应的模板id
	 * tpc_nodeid: 内容对应的节点id
	 * tpc_content: 数据内容
	 * tpc_content: 填写该数据内容的用户id
	 * tpc_date: 填写日期
	 * 示例：{"code":"0000","msg":"成功","data":[{"tpc_id":3,"tpc_tpid":17,"tpc_nodeid":169,"tpc_content":"{"key":"value"}","tpc_userid":1,"tpc_date":"2018-03-19"}]}
	 */
	@RequestMapping(value="changeTemplateContent",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String changeTemplateContent(String templateid,String nodeid,String content,String userid) {
		System.out.println(CLASS_NAME + "addTemplateContent" + "  param:" + templateid + " " + nodeid + " " + content + "  " + userid);
		CodeEnum code = CodeHelper.Strings2Numbers(templateid,userid,nodeid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断内容是否符合json格式
		if(!ClientUtil.isValidJson(content,0)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
			return jsonObject.toString();
		}
		
		//判断用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			System.out.println(CLASS_NAME + "controlDevice  " + jsonObject.toString());
			return jsonObject.toString();
		}
		
		int nTpid = Integer.valueOf(templateid);
		//判断节点是否存在
		int nNodeid = Integer.valueOf(nodeid);
		if(!treeDao.isExistNode(nNodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());
		
		TPContent tpcontent = new TPContent();
		tpcontent.setTpc_nodeid(nNodeid);
		tpcontent.setTpc_tpid(nTpid);
		tpcontent.setTpc_userid(nUserid);
		tpcontent.setTpc_date(date);
		tpcontent.setTpc_content(content);
		System.out.println(CLASS_NAME + tpcontent);
		
		//查询模板是否已有模板内容，有则更新，没有则添加
		List<Map<String,Object>> contentList = tPContentDao.queryContentByNodeIdAndTpId(nNodeid, nTpid);
		int id = 0;
		if(contentList.size() <= 0) {
			//添加模板内容
			id = tPContentDao.addContent(tpcontent);
		}else {
			//更新模板内容
			id = ((Long)contentList.get(0).get("tpc_id")).intValue();
			tPContentDao.updateById(id, tpcontent);
		}
		 
		List<Map<String,Object>> resultList = tPContentDao.queryContentById(id);
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id查询对应的模板(不包含用户填写的模板数据内容)
	 * @param id  节点id
	 * @return 模板Json字符串
	 * 示例:{"code":"0000","msg":"成功","data":[{"tp_id":17,"tp_name":"11","tp_data":"[{"name":"11","type":"text"}]","tp_treeid":null}]}
	 */
	@RequestMapping(value="getTemplateByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getTemplateByNodeId(String id) {
		System.out.println(CLASS_NAME + "getTemplateByNodeId" + "   param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//根据节点id查询模板
		List<Map<String,Object>> attrList = nodeAttrDao.queryAttrByNodeIdAndType(nId, 1);
		if(attrList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.TEMPLATE_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询模板信息
		//判断模板id是否有效
		String value = (String)attrList.get(0).get("attr_value");
		code = CodeHelper.String2Number(value);
		if(code != CodeEnum.SUCCESS) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_UNKNOW);
			return jsonObject.toString();
		}
		int tpid = Integer.valueOf(value);
		List<Map<String,Object>> templateList = templateDao.queryTemplateByTpId(tpid);
		if(templateList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.TEMPLATE_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", templateList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id和模板id获取用户填写的模板数据
	 * @param nodeid  节点id
	 * @param templateid 模板id
	 * @return 模板内容json字符串
	 */
	@RequestMapping(value="getTpContentByNodeIdAndTpId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getTpContentByNodeIdAndTpId(String nodeid,String templateid) {
		System.out.println(CLASS_NAME + "getTpContentByNodeIdAndTpId" + "  param:" + nodeid + "  " + templateid);
		CodeEnum code = CodeHelper.Strings2Numbers(nodeid,templateid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断模板是否存在
		int nTpid = Integer.valueOf(templateid);
		List<Map<String,Object>> tpList = templateDao.queryTemplateByTpId(nTpid);
		if(tpList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.TEMPLATE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断节点是否存在
		int nNodeid = Integer.valueOf(nodeid);
		if(!treeDao.isExistNode(nNodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//查询对应的模板内容
		List<Map<String,Object>> contentList = tPContentDao.queryContentByNodeIdAndTpId(nNodeid, nTpid);
		if(contentList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CONTENT_NOT_EXIST);
			return jsonObject.toString();
		}
		
		jsonObject.put("data", contentList);
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据模板id查询模板信息
	 * @param id 模板id
	 * @return 模板json字符串
	 */
	@RequestMapping(value="getTemplateByTpId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getTemplateByTpId(String id) {
		System.out.println(CLASS_NAME + "getTemplateByTpId" + "  param:" + id);
		//检查id有效性
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		int nId = Integer.valueOf(id);
		//检查模板是否存在
		List<Map<String,Object>> tpList = templateDao.queryTemplateByTpId(nId);
		if(tpList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.TEMPLATE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		jsonObject.put("data", tpList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据节点id查询对应的模板和用户填写的模板数据
	 * @param id 节点id
	 * @return 模板、模板数据json字符串，如果模板有对应的数据，则返回模板和模板数据；如果没有对应的数据，则返回模板
	 * data参数说明：类型Array
	 * tpc_id： 数据内容id
	 * tpc_tpid: 内容对应的模板id
	 * tpc_nodeid: 内容对应的节点id
	 * tpc_content: 数据内容
	 * tpc_content: 填写该数据内容的用户id
	 * tpc_date: 填写日期
	 * template: 模板json
	 * 示例：{"code":"0000","msg":"成功","data":[{"tpc_id":3,"tpc_tpid":17,"tpc_nodeid":168,"tpc_content":"{\"key\":\"1\"}","tpc_userid":1,"tpc_date":"2018-03-26",
	 * 		"template":{"tp_id":17,"tp_name":"11","tp_data":"[{\"name\":\"11\",\"type\":\"text\"}]","tp_treeid":null}}]}
	 */
	@RequestMapping(value="getTpContentByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getTpContentByNodeId(String id) {
		System.out.println(CLASS_NAME + "getTpContentByBodeId" + "  param:" + id);
		//检查id有效性
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//检查节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询对应的模板内容
		List<Map<String,Object>> contentList = tPContentDao.queryContentByNodeId(nId);
		//根据节点获取模板
		List<Map<String,Object>> tempList = templateDao.queryByNodeId(nId);
		if(tempList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.TEMPLATE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		if(contentList.size() <= 0) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("template", tempList.get(0));
			resultList.add(map);
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		resultList.add(contentList.get(0));
		resultList.get(0).put("template", tempList.get(0));
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id和模板的键上传文件(注：数据库不保存数据，只返回上传后文件地址,保存数据可在保存用户填写的模板数据接口保存)
	 * @param nodeid 节点id
	 * @param key 模板的键，键必须存在且类型是file
	 * @return 上传后文件的完整地址和相对地址
	 *  relative:上传成功后资源的相对网站地址    complete:上传成功后资源的完整地址(包括网址和接口)
	 *  filename: 原始文件名称
	 * 示例：{"code":"0000","msg":"成功","data":[{"complete":"http://localhost:8080/Tree/upload/template/2018-03-30/temp_1522391649714.jpg","filename":"4.jpg","relative":"upload/template/2018-03-30/temp_1522391649714.jpg"}]}
	 */
	@RequestMapping(value="uploadTpFile",produces="text/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String uploadTpFile(String nodeid,String key,HttpServletRequest request,MultipartFile file) throws Exception {
		System.out.println(CLASS_NAME + "uploadTpFile" + "  param:" + nodeid + " " + key);
		CodeEnum code = CodeHelper.isStringsNull(nodeid,key);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(!NumberUtils.isNumber(nodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nNodeid = Integer.valueOf(nodeid);
		if(!treeDao.isExistNode(nNodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断节点是否有对应的模板
		List<NodeAttr> attrList = nodeAttrDao.findByNodeIdAndType(nNodeid, 1);
		if(attrList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_WIDTHOUT_TEMPLATE);
			return jsonObject.toString();
		}
		int tpid = Integer.valueOf(attrList.get(0).getAttr_value());
		//查询模板是否存在对应的key
		List<Map<String,Object>> tpList = templateDao.queryTemplateByTpId(tpid);
		if(tpList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_WIDTHOUT_TEMPLATE);
			return jsonObject.toString();
		}
		String data = (String) tpList.get(0).get("tp_data");
		JSONArray jsonArray = ClientUtil.getArrayFromString(data);
		if(jsonArray == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.KEY_NOT_EXIST);
			return jsonObject.toString();
		}
		boolean isExistKey = false;
		for(int i=0;i<jsonArray.size();i++) {
			JSONObject dataobject = jsonArray.getJSONObject(i);
			System.out.println(dataobject.toString());
			if(key.equals(dataobject.getString("name")) && ("file").equals(dataobject.getString("type"))) {
				isExistKey = true;
				break;
			}
		}
		if(!isExistKey) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.KEY_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//上传资源
		String path = ClientUtil.fileUpload(request, "template", "temp_");
		if(path == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_UPLOAD_RESOURCE);
			return jsonObject.toString();
		}
		//获取文件原始名称
		String filename = file.getOriginalFilename();
		System.out.println(CLASS_NAME + filename);
		
		//网站地址
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("relative", path);
		map.put("complete",basePath + path);
		map.put("filename", filename);
		resultList.add(map);
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/********************************场景接口********************************************************/
	
	
	/**
	 * 根据树根节点获取场景列表
	 * @param id 树根节点id
	 * @param detail 可选参数，等于空时则不显示场景对应的动作列表和节点列表信息，等于1时显示详细信息
	 * @return 场景列表json字符串
	 * data参数说明，类型Array
	 * sc_id:场景id
	 * sc_name: 场景名称
	 * sc_treeid: 场景对应的树根节点id
	 * sc_action: 场景对应的动作id列表,id之间用","分隔
	 * sc_nodeid: 场景对应的节点id列表,id之间用","分隔
	 * [detail参数不为空时]
	 * action: 类型Array   sc_action中id对应的节点列表详细信息
	 * node: 类型Array  sc_nodeid中id对应的节点列表详细信息
	 * 
	 * 示例：[detail为空] {"code":"0000","msg":"成功","data":[{"sc_id":1,"sc_name":"场景1","sc_treeid":168,"sc_action":"175,176","sc_nodeid":null}]}
	 * [detail不为空]
	 * {"code":"0000","msg":"成功",
	 * 	"data":[{"sc_id":1,"sc_name":"场景1","sc_treeid":168,"sc_action":"176","sc_nodeid":"89",
	 * 	"action":[{"node_id":176,"node_userid":1,"node_name":"关机","node_pid":174,"node_treeid":168,"node_url":"","node_type":11,"node_protocol":null,"node_class":null,"node_sn":null}],
	 * 	"node":[{"node_id":89,"node_userid":1,"node_name":"中国","node_pid":0,"node_treeid":89,"node_url":"","node_type":1,"node_protocol":null,"node_class":null,"node_sn":null}]}]}
	 */
	@RequestMapping(value="getSceneByTreeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getSceneByTreeId(String id,String detail) {
		System.out.println(CLASS_NAME + "getSceneByTreeId" + "  param:" + "id");
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断树根节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//根据树节点查询场景
		List<Map<String,Object>> sceneList = sceneDao.querySceneByTreeId(nId);
		if(sceneList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.SCENE_NOT_EXIST);
			return jsonObject.toString();
		}
		//详细信息，包括节点信息、对应的动作信息
		if(detail != null && "1".equals(detail)) {
			ClientUtil.putDetialToSceneList(sceneList);
		}
		jsonObject.put("data", sceneList);
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据场景id查询场景信息
	 * @param id 场景id
	 * @param detail 可选参数，等于空时则不显示场景对应的动作列表和节点列表信息，等于1时显示详细信息
	 * @return 场景json字符串
	 */
	@RequestMapping(value="getSceneById",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getSceneById(String id,String detail) {
		System.out.println(CLASS_NAME + "  param:" + id + "  " + detail);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		int nId = Integer.valueOf(id);
		List<Map<String,Object>> sceneList = sceneDao.queryById(nId);
		if(sceneList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.SCENE_NOT_EXIST);
			return jsonObject.toString();
		}
		//添加详细信息
		if(detail != null && ("1").equals(detail)) {
			ClientUtil.putDetialToSceneList(sceneList);
		}
		
		jsonObject.put("data", sceneList);
		System.out.println(CLASS_NAME + sceneList.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id获取场景列表
	 * @param id 节点id
	 * @param detail 可选参数，等于空时则不显示场景对应的动作列表和节点列表信息，等于1时显示详细信息
	 * @return 场景json字符串
	 */
	@RequestMapping(value="getSceneByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getSceneByNodeId(String id,String detail) {
		System.out.println(CLASS_NAME + "getSceneByNodeId" + "  param:" + id + "  " + detail);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//根据查询节点查询场景列表
		List<Map<String,Object>> sceneList = sceneDao.queryByNodeId(nId);
		if(sceneList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.SCENE_NOT_EXIST);
			return jsonObject.toString();
		}
		//添加详细信息
		if(detail != null && ("1").equals(detail)) {
			ClientUtil.putDetialToSceneList(sceneList);
		}
		jsonObject.put("data", sceneList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 添加场景，成功则返回添加的场景记录
	 * @param treeid 树根节点id
	 * @param name 场景名称
	 * @param action 场景包含的动作节点id,id之间用","隔开，例如  1,2,3,4
	 * @param nodeid 场景包含的节点id,id之间用","隔开，例如 1,2,3,4
	 * @return 添加的场景记录
	 * 注：对于action和nodeid中的id有效性不做检查
	 */
	@RequestMapping(value="addScene",produces="text/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String addScene(String treeid,String name,String action,String nodeid) {
		System.out.println(CLASS_NAME + "addScene" + "  param:" + treeid + " " + name + " " + action + " " + nodeid);
		CodeEnum code = CodeHelper.isStringsNull(treeid,name,action,nodeid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//检查字符串是否符合格式,检查action和nodeid是否符合1,2,3,这种格式
		if(!NumberUtils.isNumber(treeid) || !ClientUtil.isValidIdGroup(action) || !ClientUtil.isValidIdGroup(nodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
			return jsonObject.toString();
		}
		
		//检查树根节点是否存在
		int nTreeid = Integer.valueOf(treeid);
		if(!treeDao.isExistRootById(nTreeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断场景名称是否存在
		if(sceneDao.isExistByName(name, nTreeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NAME_HAVE_EXIST);
			return jsonObject.toString();
		}
		
		//添加场景
		Scene scene = new Scene();
		scene.setSc_treeid(nTreeid);
		scene.setSc_name(name);
		scene.setSc_action(action);
		scene.setSc_nodeid(nodeid);
		
		int id = sceneDao.addScene(scene);
		//根据场景id查询场景
		List<Map<String,Object>> sceneList = sceneDao.queryById(id);
		jsonObject.put("data", sceneList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据场景id修改场景
	 * @param id 场景id
	 * @param treeid 树根节点id
	 * @param name 场景名称
	 * @param action  场景包含的动作节点id,id之间用","隔开，例如  1,2,3,4
	 * @param nodeid 场景包含的节点id,id之间用","隔开，例如 1,2,3,4
	 * @return
	 */
	@RequestMapping(value="updateSceneById",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String updateSceneById(String id,String treeid,String name,String action,String nodeid) {
		System.out.println(CLASS_NAME + "updateSceneById" + "  param:" + id + " " + treeid + " " + name + " " + action + " " + nodeid);
		CodeEnum code = CodeHelper.isStringsNull(id,treeid,name,action,nodeid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//检查参数格式
		if(!NumberUtils.isNumber(id) || !NumberUtils.isNumber(treeid) || !ClientUtil.isValidIdGroup(action) || !ClientUtil.isValidIdGroup(nodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
			return jsonObject.toString();
		}
		int nId = Integer.valueOf(id);
		//检查场景是否存在
		List<Map<String,Object>> sceneList = sceneDao.queryById(nId);
		if(sceneList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.SCENE_NOT_EXIST);
			return jsonObject.toString();
		}
		int nTreeid = Integer.valueOf(treeid);
		//检查树根节点是否存在
		if(!treeDao.isExistRootById(nTreeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//根据id修改场景
		Scene scene = new Scene();
		scene.setSc_id(nId);
		scene.setSc_treeid(nTreeid);
		scene.setSc_name(name);
		scene.setSc_action(action);
		scene.setSc_nodeid(nodeid);
		System.out.println(CLASS_NAME + scene);
		sceneDao.updateSceneById(scene);
		
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
		
	}
	
	
	
	
	/**********************************权限接口***********************************************/
	
	
	/**
	 * 根据树根节点id和用户id查询用户拥有的管理权限列表,如果用户是该树的创建者，则返回所有的管理权限
	 * @param userid 用户id
	 * @param treeid 树根节点id
	 * @return 管理权限列表json字符串
	 * data参数说明：类型Array
	 * id : 权限id
	 * name: 权限名称，如用户组管理、权限管理等
	 * type: 权限类型，1为节点操作类型   2为管理权限
	 * 示例：{"code":"0000","msg":"成功","data":[{"id":5,"name":"用户组管理"},{"id":6,"name":"权限管理"}]}
	 */
	@RequestMapping(value="getManagePermission",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getManagePermission(String userid,String treeid) {
		System.out.println(CLASS_NAME + "getManagePermission" + "  param:" + userid + "  " + treeid);
		CodeEnum code = CodeHelper.Strings2Numbers(userid,treeid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断树根节点是否存在
		int nTreeid = Integer.valueOf(treeid);
		if(!treeDao.isExistRootById(nTreeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.TREE_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断用户是否创建者，是则拥有全部权限
		List<Tree> treeList = treeDao.queryById(nTreeid);
		if(treeList.get(0).getNode_userid() == nUserid) {
			//查询全部管理权限
			List<Map<String,Object>> permissionList = clientPermissionDao.queryByType(2);
			jsonObject.put("data", permissionList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//根据用户组获取管理权限
		//获取用户
		List<User> userList = userDao.queryById(nUserid);
		int userGroup = userList.get(0).getUr_group();    //用户组
		List<UserGroupPermission> permissionList = userGroupPermissionDao.queryByTreeIdAndGroupId(nTreeid, nTreeid, userGroup);
		if(permissionList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.PERMISSION_NOT_EXIST);
			return jsonObject.toString();
		}
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();     //结果集合
		//获取权限id列表
		String permissIds = permissionList.get(0).getGp_perid();
		//去除后台权限
		String clientPermissions = ClientUtil.getNumberString(permissIds);
		String[] clients = clientPermissions.split(",");
		for(int i=0;i<clients.length;i++) {
			//筛选管理权限，放入结果集合
			List<Map<String,Object>> clientList = clientPermissionDao.queryByIdAndType(Integer.valueOf(clients[i]), 2);
			resultList.addAll(clientList);
		}
		if(resultList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.PERMISSION_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id和用户id查询用户拥有该节点的操作权限列表，如果是该树的创建者，则返回全部操作权限
	 * @param userid 用户id
	 * @param nodeid 节点id
	 * @return 节点操作权限列表json字符串
	 * 示例：{"code":"0000","msg":"成功","data":[{"id":1,"name":"控制"},{"id":2,"name":"查询"},{"id":3,"name":"浏览"}]}
	 */
	@RequestMapping(value="getNodePermission",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getNodePermission(String userid,String nodeid) {
		System.out.println(CLASS_NAME + "getNodePermission" + "  param:" + userid + "  " + nodeid);
		CodeEnum code = CodeHelper.Strings2Numbers(userid,nodeid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		//判断用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断节点id是否存在
		int nNodeid = Integer.valueOf(nodeid);
		if(!treeDao.isExistNode(nNodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断该用户是否树的创建者
		List<Tree> treeList = treeDao.queryById(nNodeid);
		if(treeList.get(0).getNode_userid() == nUserid) {
			//查询全部节点权限
			List<Map<String,Object>> permissionList = clientPermissionDao.queryByType(1);
			jsonObject.put("data", permissionList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		//查询用户对应的用户组
		List<User> userList = userDao.queryById(nUserid);
		int groupId = userList.get(0).getUr_group();
		//查询用户和节点对应的权限
		List<UserGroupPermission> groupPermissionList = userGroupPermissionDao.queryByNodeIdAndGroupId(nNodeid, groupId);
		if(groupPermissionList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.PERMISSION_NOT_EXIST);
			return jsonObject.toString();
		}
		//筛选结果
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		String strPermission = ClientUtil.getNumberString(groupPermissionList.get(0).getGp_perid());
		System.out.println(CLASS_NAME + strPermission);
		String[] permissions = strPermission.split(",");
		for(int i=0;i<permissions.length;i++) {
			List<Map<String,Object>> list = clientPermissionDao.queryByIdAndType(Integer.valueOf(permissions[i]), 1);
			resultList.addAll(list);
		}
		
		if(resultList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.PERMISSION_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id、用户id查询该用户是否拥有所查类型的权限
	 * @param nodeid 节点id
	 * @param userid 用户id
	 * @param type 权限类型(权限id)  1、控制  3、浏览  4、资产管理  5、用户组管理   6、权限管理   7、场景管理  8、用户审核   9、模板管理
	 * @return 结果json字符串
	 * data参数说明：类型Array
	 * result: 拥有权限与否，0、不拥有   1、拥有
	 * 示例：{"code":"0000","msg":"成功","data":[{"result":"0"}]}
	 */
	@RequestMapping(value="hasPermissionByNodeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String  hasPermissionByNodeId(String nodeid,String userid,String type) {
		System.out.println(CLASS_NAME + "hasPermissionByNodeId" + "  param:" + nodeid + "  " + type);
		CodeEnum code = CodeHelper.Strings2Numbers(nodeid,userid,type);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//判断用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断节点id是否存在
		int nNodeid = Integer.valueOf(nodeid);
		if(!treeDao.isExistNode(nNodeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//结果
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("result", "1");
		resultList.add(result);
		
		//判断该用户是否该树创建者
		List<Tree> treeList = treeDao.queryById(nNodeid);
		if(treeList.get(0).getNode_userid() == nUserid) {
			jsonObject.put("data", resultList);
			return jsonObject.toString();
		}
		//获取用户组
		List<User> userList = userDao.queryById(nUserid);
		int groupId = userList.get(0).getUr_group();
		
		//查询是否有权限
		List<UserGroupPermission> list = userGroupPermissionDao.queryByNodeIdAndPermissId(nNodeid, Integer.valueOf(type), groupId);
		if(list.size() <= 0) {
			result.put("result", "0");
			jsonObject.put("data", resultList);
			return jsonObject.toString();
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据标志获取权限列表
	 * @param type 为空或者0时，查询所有的权限，包括节点操作权限和管理权限    为1查询节点操作权限    为2查询管理权限   
	 * @return 权限列表json字符串
	 * data参数说明：类型Array
	 * id : 权限id
	 * name: 权限名称，如用户组管理、权限管理等
	 * type: 权限类型，1为节点操作类型   2为管理权限
	 */
	@RequestMapping(value="getPermissionList",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getPermissionList(String type) {
		System.out.println(CLASS_NAME + "getPermissionList");
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		if(type == null || "0".equals(type)) {
			//查询所有的权限
			List<Map<String,Object>> permissionList = clientPermissionDao.queryAllPermission();
			jsonObject.put("data", permissionList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		//查询节点操作或管理权限
		int nType = Integer.valueOf(type);
		
		List<Map<String,Object>> permissionList = clientPermissionDao.queryByType(nType);
		jsonObject.put("data", permissionList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
		
	}
	
	
	
	/*************************************用户组接口*********************************************/
	
	
	/**
	 * 根据树根节点id查询用户组
	 * @param id 树根节点
	 * @return 用户组列表json字符串
	 * data参数说明，类型Array
	 * urg_id: 用户组id
	 * urg_userid: 用户组创建者id
	 * urg_name: 用户组名称
	 * urg_treeid: 用户组对应的树根节点id
	 */
	@RequestMapping(value="getUserGroupByTreeId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getUserGroupByTreeId(String id) {
		System.out.println(CLASS_NAME + "getUserGroupByTreeId" + "   param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//检查树根节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistRootById(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.TREE_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询用户组列表
		List<Map<String,Object>> groupList = userGroupDao.queryGroupByTreeId(nId);
		if(groupList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.GROUP_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", groupList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据用户组id获取用户列表
	 * @param id 用户组id
	 * @return 用户列表json字符串
	 * data参数说明，类型Array
	 * ur_id: 用户id
	 * ur_username: 用户名
	 * ur_type: 用户类型
	 * ur_datetime: 用户创建日期
	 * ur_group: 用户所属用户组id
	 */
	@RequestMapping(value="getUsersByGroupId",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getUsersByGroupId(String id) {
		System.out.println(CLASS_NAME + "getUsersByGroupId" + "  param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		int nId = Integer.valueOf(id);
		//判断用户组是否存在
		List<Map<String,Object>> groupList = userGroupDao.queryGroupById(nId);
		if(groupList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.GROUP_NOT_EXIST);
			return jsonObject.toString();
		}
		//根据用户组id查询用户
		List<Map<String,Object>> userList = userDao.queryUserByGroupId(nId);
		if(userList.size() <= 0 ) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		jsonObject.put("data", userList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 添加用户组
	 * @param name 用户组名称
	 * @param treeid 树根节点id
	 * @param userid 用户id
	 * @return 刚添加的用户组json字符串
	 * data参数说明,类型Array
	 * urg_id: 用户组id
	 * urg_name: 用户组名称
	 * urg_userid: 创建用户组的用户id
	 * urg_treeid: 用户组所在的树id
	 * 
	 */
	@RequestMapping(value="addUserGroup",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String addUserGroup(String name,String treeid,String userid) {
		System.out.println(CLASS_NAME + "addUserGroup" + "  param:" + name + "  " + treeid + "  " + userid);
		CodeEnum code = CodeHelper.Strings2Numbers(treeid,userid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(name == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		
		//判断用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断树根节点是否存在
		int nTreeid = Integer.valueOf(treeid);
		if(!treeDao.isExistRootById(nTreeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.TREE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断用户是否有权限
		if(!ClientUtil.hasPermissionByNodeId(nTreeid, nUserid, 5)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_WITHOUT_PERMISSION);
			return jsonObject.toString();
		}
		
		//判断名称是否存在
		if(userGroupDao.isExistByName(name, nTreeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NAME_HAVE_EXIST);
			return jsonObject.toString();
		}
		
		//添加用户组
		UserGroup userGroup = new UserGroup();
		userGroup.setUrg_name(name);
		userGroup.setUrg_treeid(nTreeid);
		userGroup.setUrg_userid(nUserid);
		
		System.out.println(CLASS_NAME + userGroup.toString());
		
		int id = userGroupDao.add(userGroup);
		List<Map<String,Object>> userGroupList = userGroupDao.queryGroupById(id);
		jsonObject.put("data", userGroupList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据用户组id删除用户组，并根据用户判断是否有权限
	 * @param groupid 用户组id
	 * @param userid 用户id
	 * @param treeid 树id
	 * @return
	 */
	@RequestMapping(value="deleteUserGroupById",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String deleteUserGroupById(String groupid,String userid,String treeid) {
		System.out.println(CLASS_NAME + "  param:" + groupid + "  " + userid);
		CodeEnum code = CodeHelper.Strings2Numbers(groupid,userid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//检查用户是否存在
		int nUserid = Integer.valueOf(userid);
		if(!userDao.isExistByUserId(nUserid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//检查用户组是否存在
		int nGroupid = Integer.valueOf(groupid);
		if(!userGroupDao.isExistGroupById(nGroupid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.GROUP_NOT_EXIST);
			return jsonObject.toString();
		}
		//检查树根节点是否存在
		int nTreeid = Integer.valueOf(treeid);
		if(!treeDao.isExistRootById(nTreeid)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.TREE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		//判断用户是否有权限删除
		if(!ClientUtil.hasPermissionByNodeId(nTreeid, nUserid, 5)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_WITHOUT_PERMISSION);
			return jsonObject.toString();
		}
		
		//删除用户组
		userGroupDao.deleteUserGroupById(nGroupid);
		//将对应的用户中的用户组id清0
		userDao.updateUserGroup(nGroupid, 0);
		//将权限表有关的用户组清空
		userGroupPermissionDao.deletePermissionByGroupId(nGroupid);
		
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 将多个用户添加到用户组中
	 * @param groupid 用户组id
	 * @param userids 用户id组合，格式为 %d,%d,%d，例如1,2,3,4
	 * @return
	 */
	@RequestMapping(value="addUsersToGroup",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String addUsersToGroup(String groupid,String userids) {
		System.out.println(CLASS_NAME + "addUsersToGroup" + "  param:" + groupid + "  " + userids);
		CodeEnum code = CodeHelper.String2Number(groupid);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//检查用户id组合是否符合格式
		if(userids == null ) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		
		if(("").equals(userids)) {
			return jsonObject.toString();
		}
		if(!ClientUtil.isValidIdGroup(userids)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
			return jsonObject.toString();
		}
		
		//检查用户组是否存在
		int nGroup = Integer.valueOf(groupid);
		if(!userGroupDao.isExistGroupById(nGroup)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.GROUP_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询用户是否存在
		String[] ids = userids.split(",");
		for(int i=0;i<ids.length;i++) {
			int id = Integer.valueOf(ids[i]);
			if(!userDao.isExistByUserId(id)) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.USER_NOT_EXIST);
				return jsonObject.toString();
			}
		}
		
		//修改用户组id
		for(int i=0;i<ids.length;i++) {
			int id = Integer.valueOf(ids[i]);
			userDao.updateGroupById(id, nGroup);
		}
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据条件获取无用户组用户，可搜索用户名、可分页
	 * @param search 可选参数，搜索用户名
	 * @param index 可选参数，页码
	 * @param count 可选参数，每页记录数
	 * @return 用户列表json字符串
	 */
	@RequestMapping(value="getNoGroupUser",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getNoGroupUser(String search,String index,String count) {
		System.out.println(CLASS_NAME + "getNoGroupUser" + "  param:" + search);
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		if(index  != null && count != null) {
			//判断是否都是数字
			if(!NumberUtils.isNumber(index) || !NumberUtils.isNumber(count)) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
				return jsonObject.toString();
			}
		}
		List<Map<String,Object>> userList = userDao.queryUserPageBySearch(search, index, count);
		jsonObject.put("data", userList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据条件查询无用户组用户的总记录数，当分页时，返回总记录数、当前页数、每页记录数、总页数
	 * @param search 可选参数，搜索用户名
	 * @param index 可选参数，页码
	 * @param count 可选参数，每页记录数
	 * @return 
	 * data参数说明，类型Array
	 * total: 总记录数
	 * index: 页码
	 * count: 每页记录数
	 * pagecount: 总页数
	 * 示例：{"code":"0000","msg":"成功","data":[{"total":3,"index":"0","count":"1","pagecount":3}]}
	 */
	@RequestMapping(value="countNoGroupUser",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String countNoGroupUser(String search,String index,String count) {
		System.out.println(CLASS_NAME + "countNoGroupUser" + " param:" + search + " " + index + " " + count);
		JSONObject jsonObject = ClientUtil.getCodeJSON(CodeEnum.SUCCESS);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		resultList.add(map);
		//查询总人数
		int userCount = userDao.countNoGroupUserByCondition(search);
		if(index  != null && count != null) {
			//判断是否都是数字
			if(!NumberUtils.isNumber(index) || !NumberUtils.isNumber(count)) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
				return jsonObject.toString();
			}
			
			map.put("index", index);
			map.put("count", count);
			//计算总页数
			int nCount = Integer.valueOf(count);
			int pageCount = (userCount + (nCount - 1)) / nCount;
			map.put("pagecount", pageCount);
		}
		map.put("total", userCount);
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	
	/***************************************客户端统计数据接口***************************************************/
	
	
	/**
	 * 根据操作码统计开始时间与结束时间之间的数据
	 * @param id 节点id
	 * @param begintime 开始时间，格式为yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss
	 * @param endtime 结束时间，格式为yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss(注：格式为yyyy-MM-dd，查询的数据会包括该天的数据)
	 * @param opcode 操作码   0001-平均值    0002-总量  0003-最大值   0004-最小值    0005-极差
	 * @param key 要查询的数据对应的键(注：如果数据传输方式是透传，则可以不加该参数,传输方式为协议传输，则必须加上)
	 * @return 统计结果json字符串
	 * data参数说明：类型Array
	 * begintime: 开始时间
	 * endtime: 结束时间
	 * opcode: 操作码
	 * opmsg: 操作码说明
	 * result: 统计结果
	 * 
	 * 示例：{"code":"0000","msg":"成功","data":[{"endtime":"2018-3-28 00:00:00","opmsg":"最大值","reuslt":"56.15","begintime":"2018-3-26 00:00:00","opcode":"0003"}]}
	 */
	@RequestMapping(value="getStatisticsData",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getStatisticsData(String id,String begintime,String endtime,String opcode,String key) {
		System.out.println(CLASS_NAME + "getStatisticsData" + "  param:" + id + " " + " " + begintime + " " + endtime + " " + opcode + " " + key);
		//检查id是否有效
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		code = CodeHelper.isStringsNull(begintime,endtime,opcode);
		if(code != CodeEnum.SUCCESS) {
			jsonObject = ClientUtil.getCodeJSON(code);
			return jsonObject.toString();
		}
		//判断时间是否符合格式
		if(!ClientUtil.isValidDate(begintime, "yyyy-MM-dd") && !ClientUtil.isValidDate(begintime, "yyyy-MM-dd HH:mm:ss") || (!ClientUtil.isValidDate(endtime, "yyyy-MM-dd") && !ClientUtil.isValidDate(endtime, "yyyy-MM-dd HH:mm:ss"))) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_DATE_FORMAT);
			return jsonObject.toString();
		}
		//判断操作码是否存在
		opcode = opcode.trim();
		if(!CodeHelper.isExistOpCode(opcode)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.OPCODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		List<Tree> treeList = treeDao.queryById(nId);
		//判断传输方式
		int tstype = treeList.get(0).getNode_tstype();
		if(tstype == TRANS_PROTOCOL) {
			//协议传输
			//判断键是否存在
			String protocol = treeList.get(0).getNode_protocol();
			if(!ClientUtil.isExistKey(protocol, key)) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.KEY_NOT_EXIST);
				return jsonObject.toString();
			}
		}
		
		//获取在开始时间和结束时间内的节点数据
		List<Value> valueList = null;
		
		if(ClientUtil.isValidDate(endtime, "yyyy-MM-dd HH:mm:ss")) {
			//结束时间为yyyy-MM-dd HH:mm:ss格式，则用时间戳查询value列表
			valueList = valueDao.queryBetweenTimeByNodeId(nId, begintime, endtime);
		} else {
			//结束时间为yyyy-MM-dd格式，则用to_days去查询value列表
			valueList = valueDao.queryBetweenDateByNodeId(nId, begintime, endtime);
		}
		List<Map<String,Object>>  dataList = null;
		if(tstype == TRANS_PROTOCOL) {
			//协议传输
			dataList = ClientUtil.getValueJsonData(valueList, key);
		} else {
			//透传
			dataList = ClientUtil.getValueData(valueList);
		}
		System.out.println(CLASS_NAME + dataList.toString());
		System.out.println(CLASS_NAME + valueList.size());
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("begintime", begintime);
		result.put("endtime", endtime);
		result.put("opcode", opcode);
		result.put("opmsg", CodeHelper.getEnumByCode(opcode).getMsg());
		result.put("result", 0);
		resultList.add(result);
		
		//数据保留两位小数
		DecimalFormat df = new DecimalFormat("0.00");
		//根据操作码对数据操作
		if(OperationEnum.AVERAGE.getCode().equals(opcode)) {
			//求平均值
			double count = 0;
			for(int i=0;i<dataList.size();i++) {
				count += (Double)dataList.get(i).get("data");
			}
			double average = 0;
			if(dataList.size() > 0) {
				average = count / dataList.size();
			}
			System.out.println(CLASS_NAME + "average:" + average);
			result.put("result", df.format(average));
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
			
		}
		
		//求总量
		if(OperationEnum.TOTAL.getCode().equals(opcode)) {
			double count = 0;
			for(int i=0;i<dataList.size();i++) {
				count += (Double)dataList.get(i).get("data");
			}
			result.put("result", df.format(count));
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//求最大值和最小值，以及对应的value_id
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		int maxId = 0;
		int minId = 0;
		for(int i=0;i<dataList.size();i++) {
			double data = (Double)dataList.get(i).get("data");
			if(data >= max) {
				max = data;
				maxId = (Integer)dataList.get(i).get("id");
			}
			if(data <= min) {
				min = data;
				minId = (Integer)dataList.get(i).get("id");
			}
		}
		//求最大值
		if(OperationEnum.MAX.getCode().equals(opcode)) {
			if(maxId == 0) {
				max = 0;
			}
			result.put("result", df.format(max));
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//求最小值
		if(OperationEnum.MIN.getCode().equals(opcode)) {
			if(minId == 0) {
				min = 0;
			}
			result.put("result", df.format(min));
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//求极差
		if(OperationEnum.RANGE.getCode().equals(opcode)) {
			double range = 0;
			if(maxId != 0 && minId != 0) {
				range = max - min;
			}
			result.put("result", df.format(range));
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		return jsonObject.toString();
	}
	
	
	/**
	 * 根据节点id查询开始时间和结束时间之间的所有节点数据列表(数值数据)，如果传输方式是协议传输，则返回对应键的数值数据
	 * @param id 节点id
	 * @param begintime  开始时间，格式为yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss
	 * @param endtime 结束时间，格式为yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss(注：格式为yyyy-MM-dd，查询的数据会包括该天的数据)
	 * @param key 要查询的数据对应的键(注：如果数据传输方式是透传，则可以不加该参数,传输方式为协议传输，则必须加上)
	 * @return 节点数据(数值数据)列表json字符串
	 * data参数说明：类型Array
	 * id: 节点数据id
	 * data: 数据值
	 * datetime: 该数据对应的时间
	 * 示例：{"code":"0000","msg":"成功","data":[{"id":112,"data":"18.30","datetime":"2018-03-27 11:07:10"}]}
	 */
	@RequestMapping(value="getValuesBetweenDate",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getValuesBetweenDate(String id,String begintime,String endtime,String key) {
		System.out.println(CLASS_NAME + "getValuesBetweenDate" + " param:" + id + " " + begintime + " " + endtime + " " + key);
		CodeEnum code = CodeHelper.isStringsNull(id,begintime,endtime);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(!NumberUtils.isNumber(id)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
			return jsonObject.toString();
		}
		//判断时间是否符合格式
		if(!ClientUtil.isValidDate(begintime, "yyyy-MM-dd") && !ClientUtil.isValidDate(begintime, "yyyy-MM-dd HH:mm:ss") || (!ClientUtil.isValidDate(endtime, "yyyy-MM-dd") && !ClientUtil.isValidDate(endtime, "yyyy-MM-dd HH:mm:ss"))) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_DATE_FORMAT);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断节点传输方式
		List<Tree> treeList = treeDao.queryById(nId);
		int tstype = treeList.get(0).getNode_tstype();
		if(tstype == TRANS_PROTOCOL) {
			//协议传输
			//判断键是否存在
			String protocol = treeList.get(0).getNode_protocol();
			if(!ClientUtil.isExistKey(protocol, key)) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.KEY_NOT_EXIST);
				return jsonObject.toString();
			}
		}
		
		//获取在开始时间和结束时间内的节点数据
		List<Value> valueList = null;
		
		if(ClientUtil.isValidDate(endtime, "yyyy-MM-dd HH:mm:ss")) {
			//结束时间为yyyy-MM-dd HH:mm:ss格式，则用时间戳查询value列表
			valueList = valueDao.queryBetweenTimeByNodeId(nId, begintime, endtime);
		} else {
			//结束时间为yyyy-MM-dd格式，则用to_days去查询value列表
			valueList = valueDao.queryBetweenDateByNodeId(nId, begintime, endtime);
		}
		DecimalFormat df = new DecimalFormat("0.00");
		List<Map<String,Object>>  dataList = null;
		if(tstype == TRANS_PROTOCOL) {
			//协议传输
			dataList = ClientUtil.getValueJsonData(valueList, key);
		} else {
			//透传
			dataList = ClientUtil.getValueData(valueList);
		}
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<dataList.size();i++) {
			List<Value> value = valueDao.queryValueById((Integer)dataList.get(i).get("id"));
			if(value.size() > 0) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("id", value.get(0).getValue_id());
				map.put("data", df.format((Double)dataList.get(i).get("data")));
				map.put("datetime", value.get(0).getValue_datetime());
				resultList.add(map);
				
			}
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	/*********************************多路视频定制接口***************************************/
	
	
	/**
	 * 根据节点id获取下一级视频节点和对应的值
	 * @param id 节点id
	 * @return 视频节点列表json字符串
	 * data参数说明：类型Array
	 * node_id: 节点id
	 * node_userid: 创建节点的用户id
	 * node_name: 节点的名称
	 * node_pid: 节点的父id,根节点pid为0
	 * node_treeid: 节点所对应的树id,即根节点id
	 * node_url: 节点对应的跳转链接地址
	 * node_type: 节点类型
	 * node_protocol: 节点协议，需要是设备的下一级节点，其他为null
	 * node_class: 节点属性名称，需要设备节点，其他为null
	 * node_sn: 节点设备号，需要设备节点，其他为null
	 * value: 该节点对应的值
	 * 示例：{"code":"0000","msg":"成功","data":[{"node_id":232,"node_userid":1,"node_name":"一路视频","node_pid":229,"node_treeid":189,"node_url":null,"node_type":5,"node_protocol":null,"node_class":null,"node_sn":null,"node_tstype":0
	 * 		,"value":{"value_id":122,"value_nodeid":232,"value_data":"{\"src\":\"rtmp://\"}","value_datetime":"2018-04-08 11:07:08"}}]}
	 */
	@RequestMapping(value="getVideoChildren",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getVideoChildren(String id) {
		System.out.println(CLASS_NAME + "getVideoChildren" + " param:" + id);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		//检查节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//查询下一级的视频节点
		List<Map<String,Object>> videoList = treeDao.queryChildrenByNodeType(nId, TypeEnum.VIDEO.getId());
		if(videoList.size() <= 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.VIDEO_NOT_EXIST);
			return jsonObject.toString();
		}
		for(Map<String,Object> node : videoList) {
			//查询对应的值
			Map<String,Object> map = new HashMap<String, Object>();
			List<Map<String,Object>> valueList = valueDao.queryLimitValueByNodeId(((Long)node.get("node_id")).intValue(), 1);
			if(valueList.size() > 0) {
				map = valueList.get(0);
			}
			node.put("value", map);
 		}
		
		jsonObject.put("data", videoList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	
	/**
	 * 修改节点对应的数据，如果该节点没有对应的数据，则添加数据，有则修改最新的数据
	 * @param id 节点id
	 * @param data 数据json字符串，例如{"src":"rtmp://localhost"}
	 * @return
	 */
	@RequestMapping(value="updateNodeValue",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String updateNodeValue(String id,String data) {
		System.out.println(CLASS_NAME + "updateNodeValue" + " param:" + id + " " + data);
		String urlKey = "url";                         //链接地址键名
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		if(data == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		
		if(!ClientUtil.isValidJson(data,0)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_NODE_TYPE);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		List<Tree> treeList = treeDao.queryById(nId);
		Tree node = treeList.get(0);
		//判断协议是否有url键，没有则添加
		JSONObject urlObject = new JSONObject();
		urlObject.put("identification", urlKey);
		urlObject.put("name", "");
		urlObject.put("nodeId", nId);
		urlObject.put("remark", "");
		boolean isExistUrl = false;
		if(node.getNode_protocol() != null) {
			JSONArray proArray = JSONArray.fromObject(node.getNode_protocol());
			for(int i=0;i<proArray.size();i++) {
				JSONObject object = proArray.getJSONObject(i);
				if(urlKey.equals(object.getString("identification"))) {
					isExistUrl = true;
					break;
				}
			}
			if(!isExistUrl) {
				//不存在url，则修改
				proArray.add(urlObject);
				node.setNode_protocol(proArray.toString());
				treeDao.updateTree(node);
			}
		}else {
			//协议为空，添加协议
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(urlObject);
			node.setNode_protocol(jsonArray.toString());
			treeDao.updateTree(node);
		}
		//判断节点是否存在对应的值，存在则修改最后一条数据，不存在则添加
		List<Map<String,Object>> valueList = valueDao.queryLimitValueByNodeId(nId, 1);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = dateFormat.format(new Date());
		Value value = new Value();
		value.setValue_nodeid(nId);
		value.setValue_data(data);
		value.setValue_datetime(datetime);
		
		if(valueList.size() <= 0) {
			//不存在，则添加
			valueDao.addValue(value);
		}else {
			//修改最后一条数据
			Map<String,Object> map = valueList.get(0);
			int valueId = ((Long)map.get("value_id")).intValue();
			value.setValue_id(valueId);
			valueDao.updateValue(value);
		}
		
		return jsonObject.toString();
	}
	
	
	
}
