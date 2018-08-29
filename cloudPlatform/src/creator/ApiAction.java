package creator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.db.address.Address;
import com.creator.db.address.AddressDao;
import com.creator.db.organization.Organization;
import com.creator.db.organization.OrganizationDao;
import com.creator.db.studenttable.StudentTableDao;
import com.creator.db.timetable.TimeTableDao;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.db.usergroup.UserGroupDao;
import com.creator.rest.CodeEnum;
import com.creator.rest.CodeResult;

@CrossOrigin(origins="*",maxAge=3600)
@Controller
@RequestMapping(value = "api")
public class ApiAction {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserGroupDao userGroupDao;
	@Autowired
	private OrganizationDao organizationDao;
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private TimeTableDao timeTableDao;
	@Autowired
	private StudentTableDao studentTableDao;
	
	/**
	 * @exception  根据用户id，返回该用户相关基础信息
	 * @author yangxy
	 * @version 1.0
	 * @param id 用户id
	 * @return code：提示码    msg：信息中文注释   data:返回的用户信息
	 */
	
	@PostMapping(value = "postById")
	@ResponseBody
	public CodeResult postById(String id, String name){
		System.out.println(name);
		System.out.println(id);
		if(!StringUtils.isNumericSpace(id)) {
			return new CodeResult(CodeEnum.ERROR_PARAM);
		}
		List<User> UserList = userDao.findById(Integer.parseInt(id));
		return new CodeResult(CodeEnum.SUCCESS,UserList);
	}
	
	@RequestMapping(value = "QueryUserById/{id}/{name}")
	@ResponseBody
	public CodeResult queryUserById(@PathVariable(name= "id")String id, @PathVariable(name= "name")String name){
		System.out.println(name);
		System.out.println(id);
		if(!StringUtils.isNumericSpace(id)) {
			return new CodeResult(CodeEnum.ERROR_PARAM);
		}
		List<User> UserList = userDao.findById(Integer.parseInt(id));
		return new CodeResult(CodeEnum.SUCCESS, UserList);
	}
	
	@RequestMapping(value = "getById")
	@ResponseBody
	public CodeResult getById(@RequestParam(name= "id")String id, @RequestParam(name= "name")String name){
		System.out.println(name);
		System.out.println(id);
		if(!StringUtils.isNumericSpace(id)) {
			return new CodeResult(CodeEnum.ERROR_PARAM);
		}
		List<User> UserList = userDao.findById(Integer.parseInt(id));
		return new CodeResult(CodeEnum.SUCCESS,UserList);
	}
	
	/**
	 * @exception 根据用户id返回该用户的年级班级，如果存在一对多，中间用"/"隔开
	 * @author yangxy
	 * @version 1.0
	 * @param id   用户id
	 * @return  code：提示码   msg：中文提示信息    data： 返回数据
	 */
	
	@RequestMapping(value = "QueryClassNameByUserId")
	@ResponseBody
	public CodeResult queryClassNameByUserId(String id){
		if(!StringUtils.isNumericSpace(id)) {
			return new CodeResult(CodeEnum.ERROR_PARAM);
		}
		
		List<Map<String, Object>> ClassNameList = userDao.queryClassNameByUserId(Integer.parseInt(id));
		return new CodeResult(CodeEnum.SUCCESS,ClassNameList);
	}
	
	/**
	 * @exception 根据用户id返回该用户所有的权限
	 * @author yangxy
	 * @version 1.0
	 * @param id  用户id
	 * @return  code：提示码    msg：中文提示信息  data：返回数据 
	 */
	
	@RequestMapping(value = "ShowModelByUserId")
	@ResponseBody
	public CodeResult showModelByUserId(String id){
		if(!StringUtils.isNumericSpace(id)){
			return new CodeResult(CodeEnum.ERROR_PARAM);
		}
		
		List<Map<String, Object>> GroupList = userGroupDao.queryUserGroupByUserId(Integer.parseInt(id));
		List<Map<String, Object>> GroupPermissionList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<GroupList.size();i++){
			Map<String, Object> MapPermissionList = userGroupDao.queryPermissionByGroupId(GroupList.get(i).get("uug_ugid"));
		//  GroupList.get(i).put("UserPermission", MapPermissionList);
			GroupPermissionList.add(MapPermissionList);
		}
		
		return new CodeResult(CodeEnum.SUCCESS, GroupPermissionList);
	}
	
	/**
	 * @exception 根据节点id获取该节点以下校务组织架构树
	 * @param nodeId 节点id
	 * @author yangxy
	 * @version 1.0
	 * @return  code：提示码    msg：中文提示信息  data：返回数据 
	 */
	
	private void listOrganizationInternal(List<Organization> orgList,int pid) {
		List<Organization> childList = organizationDao.findByPid(pid);
		if(childList.size() == 0) {
			return;
		}
		orgList.addAll(childList);
		//遍历每一个子节点，获得其子孙节点
		for(Organization child: childList) {
			listOrganizationInternal(orgList, child.getOrg_id());
		}
	}
	
	@RequestMapping(value = "GetOrganization")
	@ResponseBody
	public CodeResult getOrganization(String nodeId){
		if(!StringUtils.isNumericSpace(nodeId)) {
			return new CodeResult(CodeEnum.ERROR_PARAM);
		}
		List<Organization> orgList = new ArrayList<Organization>();
		listOrganizationInternal(orgList, Integer.parseInt(nodeId));
		return new CodeResult(CodeEnum.SUCCESS, orgList);
	}
	
	/**
	 * @exception 根据节点id获取该节点以下地点组织架构树
	 * @param nodeId 节点id
	 * @author yangxy
	 * @version 1.0
	 * @return  code：提示码    msg：中文提示信息  data：返回数据 
	 */
	
	private void listAddressInternal(List<Address> addressList,int pid){
		List<Address> childList = addressDao.findByPid(pid);
		if(childList.size() == 0) {
			return;
		}
		addressList.addAll(childList);
		//遍历每一个子节点，获得其子孙节点
		for(Address child: childList) {
			listAddressInternal(addressList, child.getAdd_id());
		}
	}
	
	@RequestMapping(value = "GetAddress")
	@ResponseBody
	public CodeResult getAddress(String nodeId){
		if(!StringUtils.isNumericSpace(nodeId)) {
			return new CodeResult(CodeEnum.ERROR_PARAM);
		}
		List<Address> addressList = new ArrayList<Address>();
		listAddressInternal(addressList, Integer.parseInt(nodeId));
		return new CodeResult(CodeEnum.SUCCESS,addressList);
	}
	
	
}
