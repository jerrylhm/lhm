package com.creator.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.creator.api.OperationEnum;
import com.creator.db.value.Value;
import com.creator.db.value.ValueDao;
import com.creator.util.ClientUtil;
import com.creator.util.HttpUtil;

/**
 * 测试客户端请求(只测试post请求)
 * @author 魏彦
 *
 */
public class ClientTest {
	private final static String HOST_ADDRESS = "http://localhost:8080/Tree/client/";
	private final static String CLOUD_ADDRESS = "http://localhost:8080/Tree/cloud/";
	private final static String APP_ADDRESS = "http://localhost:8080/Tree/app/";
	
	@Test
	public void testGetTypeList() throws Exception {
		String host = HOST_ADDRESS + "getNodeTypeList";
		String result = HttpUtil.httpPost(host, "utf-8", null);
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetNodeTypeById() throws Exception {
		String host = HOST_ADDRESS + "getNodeTypeById";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "123");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetTypeChildById() throws Exception {
		String host = HOST_ADDRESS + "getTypeChildById";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","7");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetRootByCreateUserId() throws Exception {
		String host = HOST_ADDRESS + "getRootByCreateUserId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","2");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetRootByUserId() throws Exception {
		String host = HOST_ADDRESS + "getRootByUserId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","3");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetNodeByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getNodeByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","144");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetChildrenByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getChildrenByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","101");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetPermissChildrenByIds() throws Exception {
		String host = HOST_ADDRESS + "getPermissChildrenByIds";
		Map<String,String> map = new HashMap<String, String>();
		map.put("nodeid","144");
		map.put("userid","3");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testLogin() throws Exception {
		String host = HOST_ADDRESS + "login";
		Map<String,String> map = new HashMap<String, String>();
		map.put("username","user1");
		map.put("password","123456");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetUserById() throws Exception {
		String host = HOST_ADDRESS + "getUserById";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","2");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetGroupById() throws Exception {
		String host = HOST_ADDRESS + "getGroupById";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","3");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetAllChildrenByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getAllChildrenByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","89");
		map.put("flag", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetGroupByTreeId() throws Exception {
		String host = HOST_ADDRESS + "getGroupByTreeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","89");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testRegister() throws Exception {
		String host = HOST_ADDRESS + "register";
		Map<String,String> map = new HashMap<String, String>();
		map.put("username","admin12345");
		map.put("group", "1");
		map.put("password", "123456");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetMeetingChilrenByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getMeetingChilrenByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","138");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetMeetingByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getMeetingByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","145");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetMeetingByNodeIdAndDate() throws Exception {
		String host = HOST_ADDRESS + "getMeetingByNodeIdAndDate";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","145");
		map.put("date", "2018-3-13");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetUserByIdAndPsw() throws Exception {
		String host = HOST_ADDRESS + "getUserByIdAndPsw";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","3");
		map.put("password", "4QrcOUm6Wau+VuBX8g+IPg==");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetValueByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getValueByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","104");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetDeviceSNByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getDeviceSNByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("nodeid","102");
		map.put("userid", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetParentDeviceByNodeid() throws Exception {
		String host = HOST_ADDRESS + "getParentDeviceByNodeid";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id","150");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testControlDevice() throws Exception {
		String host = HOST_ADDRESS + "controlDevice";
		Map<String,String> map = new HashMap<String, String>();
		map.put("nodeid","144");
		map.put("userid", "2");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetUserList() throws Exception {
		String host = HOST_ADDRESS + "getUserList";
		Map<String,String> map = new HashMap<String, String>();
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetWeekDate() throws Exception {
		String host = HOST_ADDRESS + "getWeekDate";
		Map<String,String> map = new HashMap<String, String>();
		map.put("isToday", "1");
		map.put("date", "2018-3-5");
		map.put("offset", "2");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testAddMeeting() throws Exception {
		String host = HOST_ADDRESS + "addMeeting";
		Map<String,String> map = new HashMap<String, String>();
		map.put("node_id", "145");
		map.put("target_date", "2018-03-15");
		map.put("me_number", "100");
		map.put("me_starttime", "04:00:10");
		map.put("me_endtime", "12:10:01");
		map.put("me_description", "说明");
		map.put("me_title", "会议标题");
		map.put("ur_id", "1");
		map.put("idGroup", "2,3");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetLastValueByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getLastValueByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "144");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetParents() {
		List<Map<String,Object>> parentlist = ClientUtil.getParentList(99);
		for(int i=0;i<parentlist.size();i++) {
			System.out.println(parentlist.get(i));
		}
	}
	
	@Test
	public void testGetParentsByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getParentsByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "102");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetDescentantsByClassName() throws Exception {
		String host = HOST_ADDRESS + "getDescentantsByClassName";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "149");
		map.put("className","电脑,空调");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetDeviceByClassName() throws Exception {
		String host = HOST_ADDRESS + "getDeviceByClassName";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "89");
		map.put("className","空调");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetTemplateListByTreeId() throws Exception {
		String host = HOST_ADDRESS + "getTemplateListByTreeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "168");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testChangeTemplateContent() throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", "1");
		String host = HOST_ADDRESS + "changeTemplateContent";
		Map<String,String> map = new HashMap<String, String>();
		map.put("templateid", "17");
		map.put("userid", "1");
		map.put("nodeid", "169");
		map.put("content", jsonObject.toString());
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetTemplateByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getTemplateByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "168");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetTpContentByNodeIdAndTpId() throws Exception {
		String host = HOST_ADDRESS + "getTpContentByNodeIdAndTpId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("nodeid", "169");
		map.put("templateid", "17");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetResourceUrlByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getResourceUrlByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "190");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetDescentantsByNodeType() throws Exception {
		String host = HOST_ADDRESS + "getDescentantsByNodeType";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "189");
		map.put("type", "2");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetChildrenByNodeType() throws Exception {
		String host = HOST_ADDRESS + "getChildrenByNodeType";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "195");
		map.put("type", "3");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetNodesByNodeType() throws Exception {
		String host = HOST_ADDRESS + "getNodesByNodeType";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "169");
		map.put("type", "11");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetDescentantsToFirstByClassName() throws Exception {
		String host = HOST_ADDRESS + "getDescentantsToFirstByClassName";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "189");
		map.put("className", "课室");
		map.put("flag", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetDescentantsToFirstByNodeType() throws Exception {
		String host = HOST_ADDRESS + "getDescentantsToFirstByNodeType";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "189");
		map.put("type","3");
		map.put("flag", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetSceneByTreeId() throws Exception {
		String host = HOST_ADDRESS + "getSceneByTreeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "168");
		map.put("detail", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetSceneById() throws Exception {
		String host = HOST_ADDRESS + "getSceneById";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "1");
//		map.put("detail", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetSceneByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getSceneByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "89");
		map.put("detail", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testMatch() {
		/*
		String str = "123,8988,23,212,4343,212,20";
		System.out.println(ClientUtil.isValidIdGroup(str));
		System.out.println(NumberUtils.isNumber(null));
		*/
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		System.out.println(ClientUtil.isValidJson(jsonArray.toString(),1));
	}
	
	@Test
	public void testAddScene() throws Exception {
		String host = HOST_ADDRESS + "addScene";
		Map<String,String> map = new HashMap<String, String>();
		map.put("treeid", "89");
		map.put("name", "场景测试12");
		map.put("action", "");
		map.put("nodeid", "12,123");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testUpdateSceneById() throws Exception {
		String host = HOST_ADDRESS + "updateSceneById";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "1");
		map.put("treeid", "89");
		map.put("name", "场景测试");
		map.put("action", "12");
		map.put("nodeid", "12,45");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetNumberString() {
		String str = "admin_1,admin_2,admin_3,1,4,admin";
		System.out.println(ClientUtil.getNumberString(str));
		OperationEnum[] values = OperationEnum.values();
		for(int i=0;i<values.length;i++) {
			System.out.println(values[i].getCode());
		}
	}
	
	@Test
	public void testGetManagePermission() throws Exception {
		String host = HOST_ADDRESS + "getManagePermission";
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", "2");
		map.put("treeid", "189");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetNodePermission() throws Exception {
		String host = HOST_ADDRESS + "getNodePermission";
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", "1");
		map.put("nodeid", "138");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testHasPermissionByNodeId() throws Exception {
		String host = HOST_ADDRESS + "hasPermissionByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", "2");
		map.put("nodeid", "106");
		map.put("type", "5");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetNavigableDescentantsByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getNavigableDescentantsByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", "2");
		map.put("nodeid", "97");
		map.put("flag", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	

	@Test
	public void testGetPermissionChildrenByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getPermissionChildrenByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", "2");
		map.put("nodeid", "96");
		map.put("permissionid", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetPermissionList() throws Exception {
		String host = HOST_ADDRESS + "getPermissionList";
		Map<String,String> map = new HashMap<String, String>();
		map.put("type", "2");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetUserGroupByTreeId() throws Exception {
		String host = HOST_ADDRESS + "getUserGroupByTreeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "89");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetUsersByGroupId() throws Exception {
		String host = HOST_ADDRESS + "getUsersByGroupId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testAddUserGroup() throws Exception {
		String host = HOST_ADDRESS + "addUserGroup";
		Map<String,String> map = new HashMap<String, String>();
		map.put("name", "测试用户组1");
		map.put("treeid", "89");
		map.put("userid", "3");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testDeleteUserGroupById() throws Exception {
		String host = HOST_ADDRESS + "deleteUserGroupById";
		Map<String,String> map = new HashMap<String, String>();
		map.put("treeid", "89");
		map.put("userid", "3");
		map.put("groupid", "7");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testAddUsersToGroup() throws Exception {
		String host = HOST_ADDRESS + "addUsersToGroup";
		Map<String,String> map = new HashMap<String, String>();
		map.put("groupid", "3");
		map.put("userids", "3,4,5");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetTpContentByNodeId() throws Exception {
		String host = HOST_ADDRESS + "getTpContentByNodeId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "168");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetTemplateByTpId() throws Exception {
		String host = HOST_ADDRESS + "getTemplateByTpId";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "17");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetNoGroupUser() throws Exception {
		String host = HOST_ADDRESS + "getNoGroupUser";
		Map<String,String> map = new HashMap<String, String>();
//		map.put("index", "1");
		map.put("count", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testCountNoGroupUser() throws Exception {
		String host = HOST_ADDRESS + "countNoGroupUser";
		Map<String,String> map = new HashMap<String, String>();
		map.put("search", "a");
		map.put("index", "0");
		map.put("count", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	
	@Test
	public void test() throws Exception {
		String host = HOST_ADDRESS + "test";
		Map<String,String> map = new HashMap<String, String>();
		map.put("search", "a");
		map.put("index", "0");
		map.put("count", "1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetMeetingByDates() throws Exception {
		String host = HOST_ADDRESS + "getMeetingByDates";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "230");
		map.put("offset", "-1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetStatisticsData() throws Exception {
		String host = HOST_ADDRESS + "getStatisticsData";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "231");
		map.put("begintime", "2018-3-27");
		map.put("endtime", "2018-3-27");
		map.put("opcode", "0005");
		map.put("key", "value");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetValuesBetweenDate() throws Exception {
		String host = HOST_ADDRESS + "getValuesBetweenDate";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "178");
		map.put("begintime", "2018-3-27");
		map.put("endtime", "2018-3-27");
		map.put("key", "value");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetWeekDay() throws Exception {
		String str = "2018-03-3";
		List<String> list = ClientUtil.getWeekDays(str);
		System.out.println(list.toString());
	}
	
	@Test
	public void testGetWeekMeetingByDate() throws Exception {
		String host = HOST_ADDRESS + "getWeekMeetingByDate";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "230");
		map.put("date", "2018-03-01");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetNodesByClassName() throws Exception {
		String host = HOST_ADDRESS + "getNodesByClassName";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "189");
		map.put("className", "空调,课室,12");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetVideoChildren() throws Exception {
		String host = HOST_ADDRESS + "getVideoChildren";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "229");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testUpdateNodeValue() throws Exception {
		String host = HOST_ADDRESS + "updateNodeValue";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("url", "rtmp://localhost");
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "233");
		map.put("data", jsonObject.toString());
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testGetChildrenByClassName() throws Exception {
		String host = HOST_ADDRESS + "getChildrenByClassName";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "195");
		map.put("className", "课室,电脑");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	@Test
	public void testUpdateValueByKey() throws Exception {
		String host = HOST_ADDRESS + "updateValueByKey";
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", "233");
		map.put("key", "link");
		map.put("value", "linkvalue");
		map.put("updateProtocol","1");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	
	
	@Test
	public void testAddValue() {
		ApplicationContext context = new ClassPathXmlApplicationContext("Mysql_bean.xml");
		ValueDao valueDao = (ValueDao) context.getBean("valueDao");
		Value value = new Value();
		value.setValue_nodeid(178);
		value.setValue_datetime("2018-04-11 17:30:12");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("value", 80);
		value.setValue_data(jsonObject.toString());
		valueDao.addValue(value);
		System.out.println("添加成功");
	}
	
	
	@Test
	public void testSendHost() throws Exception {
		String host = HOST_ADDRESS + "sendHost";
		Map<String,String> map = new HashMap<String, String>();
		map.put("idGroup", "149,168,189,189");
		map.put("ip", "127.0.0.1");
		map.put("port", "8080");
		String result = HttpUtil.httpPost(host, "utf-8", map);
		
		System.out.println("结果：" + result);
	}
	
	
	
	
}
