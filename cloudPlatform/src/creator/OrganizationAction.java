package creator;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.common.util.StringUtil;
import com.creator.db.address.Address;
import com.creator.db.address.AddressDao;
import com.creator.db.organization.Organization;
import com.creator.db.organization.OrganizationDao;
import com.creator.db.studenttable.StudentTable;
import com.creator.db.studenttable.StudentTableDao;
import com.creator.db.timetable.TimeTable;
import com.creator.db.timetable.TimeTableDao;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.rest.CodeEnum;
import com.creator.rest.CodeResult;
import com.creator.rest.ResultProcessor;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * 组织机构控制器，包括组织管理以及场所管理
 *
 */
@Controller
@RequestMapping(value="organization")
public class OrganizationAction extends BaseAction{
	private final static int ORGANIZATION_TYPE_CLASS = 4;    //班级标识
	
	private final static int ADDRESS_TYPE_NORMAL = 1;               //普通场所节点类型
	private final static int ADDRESS_TYPE_CLASSROOM = 0;            //课室类型
	Logger logger = Logger.getLogger(OrganizationAction.class);
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private AddressDao addressDao;
	
	@Autowired
	private TimeTableDao timeTableDao;
	
	@Autowired
	private StudentTableDao studentTableDao;
	
	@Autowired
	private UserDao userDao;
	
	
	/**
	 * 跳转到组织管理页面
	 */
	@RequestMapping(value="manageIndex")
	public String index() {
		return "organization/manageIndex";
	}
	
	/**
	 * 获取所有的组织
	 */
	@RequestMapping(value="listOrganizations")
	@ResponseBody
	public CodeResult listOrganizations() {
		CodeResult code = restProcessor(new ResultProcessor() {
			
			@Override
			public CodeResult process() {
				//处理请求代码，如果报错会自动返回错误码
				//递归遍历获取组织结构
				List<Organization> orgList = new ArrayList<Organization>();
				listOrganizationInternal(orgList, 0);
				logger.debug("组织结构总数：" + orgList.size());
				return CodeResult.ok(orgList);
			}
		});
		return code;
	}
	
	/**
	 * 根据组织id获取子节点列表
	 */
	@RequestMapping(value="getChildren")
	@ResponseBody
	public CodeResult getChildren(final Integer id) {
		logger.debug("id:" + id);
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//请求处理代码
				List<Organization> orgList = organizationDao.findByPid(id);
				return CodeResult.ok(orgList);
			}
		});
		return code;
	}
	
	/**
	 * 根据组织名称和父id判断是否存在
	 */
	@RequestMapping(value="existsOrganization")
	@ResponseBody
	public CodeResult existsOrganization(final String name,final Integer pid) {
		logger.debug("name:" + name + " pid:" + pid);
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				
				//处理请求代码
				Map<String,Object> map = new HashMap<String,Object>();
				Organization organization = new Organization();
				organization.setOrg_name(name);
				organization.setOrg_pid(pid);
				if(organizationDao.isExistByPidAndName(organization)) {
					map.put("result", 0);
				}else {
					map.put("result", 1);
				}
				return CodeResult.ok(map);
				
			}
		});
		return code;
	}
	
	/**
	 * 添加组织结构
	 */
	@RequestMapping(value="addOrganization")
	@ResponseBody
	public CodeResult addOrganization(final String name,final Integer pid,final Integer type,
			@RequestParam(defaultValue="0")final Integer flag) {
		logger.debug("name:" + name + " pid:" + pid + " type:" + type + "  flag:" + flag);
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				
				//处理请求代码
				Organization organization = new Organization();
				organization.setOrg_name(name);
				organization.setOrg_pid(pid);
				organization.setOrg_type(type);
				organization.setOrg_flag(flag);
				int id = organizationDao.insertReturnWithId(organization);
				//根据id查找组织
				Organization org = organizationDao.findById(id);
				if(org == null) {
					return CodeResult.error();
				}
				return CodeResult.ok(org);
			}
		});
		
		return code;
	}
	
	/**
	 * 根据id删除组织及其子级组织
	 */
	@RequestMapping(value="deleteOrganization")
	@ResponseBody
	public CodeResult deleteOrganization(final Integer id) {
		logger.debug("id:" + id);
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				
				//处理请求代码
				Organization org = organizationDao.findById(id);
				if(org == null) {
					return CodeResult.error();
				}
				List<Organization> orgList = new ArrayList<Organization>();
				orgList.add(org);
				listOrganizationInternal(orgList, id);
				//删除
				logger.debug("要删除的列表：" + orgList.size());
				for(Organization organization: orgList) {
					int orgId = organization.getOrg_id();
					organizationDao.deleteById(orgId);
					
					if(organization.getOrg_type() == ORGANIZATION_TYPE_CLASS) {
						//删除用户的班级
						List<User> userList = userDao.findByClassId(orgId);
						logger.debug("查询到的班级为" + orgId + "的用户：" + userList.size());
						
						for(User user: userList) {
							String classidStr = user.getUr_classid();
							String classidNew = StringUtil.removeElement(classidStr, String.valueOf(orgId));
							if(StringUtils.isBlank(classidNew)) {
								classidNew = "0";
							}
							//更新用户班级
							user.setUr_classid(classidNew);
							userDao.updateClassId(user);
						}
						
						
						//删除学生课表的班级
						List<StudentTable> tableList = studentTableDao.queryByClassId(orgId);
						for(StudentTable table: tableList) {
							String classidStr = table.getSt_orgid();
							String classidNew = StringUtil.removeElement(classidStr, String.valueOf(orgId));
							//更新课表班级
							table.setSt_orgid(classidNew);
							studentTableDao.updateClassIdById(table);
						}
					}
					
				}
				
				return CodeResult.ok(org);
			}
		});
		return code;
	}
	
	/**
	 * 根据类型获取组织列表
	 */
	@RequestMapping(value="getOrgByType")
	@ResponseBody
	public CodeResult getOrgByType(final Integer type) {
		logger.debug("type:" + type);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				//根据类型查找组织
				List<Organization> orgList = organizationDao.findByType(type);
				List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
				for(Organization org: orgList) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("organization", org);
					//查找父级节点
					Organization orgParent = organizationDao.findById(org.getOrg_pid());
					if(orgParent != null) {
						map.put("parent", orgParent);
					}
					resultList.add(map);
				}
				return CodeResult.ok(resultList);
			}
		});
		return codeResult;
	}
	
	/**
	 * 修改组织结构
	 */
	@RequestMapping(value="updateOrganization")
	@ResponseBody
	public CodeResult updateOrganization(final Integer id,final String name,final Integer type,
			final Integer pid,@RequestParam(defaultValue="0")final Integer flag) {
		logger.debug("id:" + id + "  name:" + name + "  type:" + type + "  pid:" + pid + "  flag:" + flag);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				Organization org = new Organization();
				org.setOrg_id(id);
				org.setOrg_name(name);
				org.setOrg_pid(pid);
				org.setOrg_type(type);
				org.setOrg_flag(flag);
				
				//修改组织
				organizationDao.updateById(org);
				return CodeResult.ok(org);
			}
		});
		return codeResult;
	}
	
	/**
	 * 根据id组合删除组织
	 */
	@RequestMapping(value="deleteOrgByIds")
	@ResponseBody
	public CodeResult deleteOrgByIds(final String ids) {
		logger.debug("ids:" + ids);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				String[] idGroup = ids.split(",");
				int deleteNum = 0;
				for(int i=0;i<idGroup.length;i++) {
					if(NumberUtils.isNumber(idGroup[i])) {
						//删除
						deleteOrganization(Integer.valueOf(idGroup[i]));
						deleteNum ++;
					}
				}
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("num", deleteNum);
				return CodeResult.ok(map);
			}
		});
		return codeResult;
		
	}
	
	/*
	 * 递归遍历获取组织结构
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
	
	/**
	 * 进入场所管理首页
	 */
	@RequestMapping(value="addressIndex")
	public String addressIndex() {
		return "organization/addressIndex";
	}
	
	/**
	 * 获取所有场所
	 */
	@RequestMapping(value="listAddress")
	@ResponseBody
	public CodeResult listAddress() {
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				List<Address> addressList = new ArrayList<Address>();
				listAddressInternal(addressList, 0);
				logger.debug("场所总数：" + addressList.size());
				return CodeResult.ok(addressList);
			}
		});
		return code;
	}
	
	/*
	 * 递归获取场所列表
	 */
	public void listAddressInternal(List<Address> addressList,int pid) {
		List<Address> childList = addressDao.findByPid(pid);
		if(childList.size() <= 0) {
			return;
		}
		addressList.addAll(childList);
		for(Address address: childList) {
			listAddressInternal(addressList, address.getAdd_id());
		}
	}
	
	/**
	 * 获取课表列表
	 */
	@RequestMapping(value="listTimeTable")
	@ResponseBody
	public CodeResult listTimeTable() {
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				
				//处理请求代码
				//获取课表列表
				List<TimeTable> timeTableList = timeTableDao.listTimeTable();
				return CodeResult.ok(timeTableList);
			}
		});
		
		return codeResult;
	}
	
	/**
	 * 根据id分页获取场所子节点
	 */
	@RequestMapping(value="getAddressChildren")
	@ResponseBody
	public CodeResult getAddressChildren(final Integer id,@RequestParam(defaultValue="1")final Integer index,
			@RequestParam(defaultValue="10")final Integer count) {
		logger.debug("id:" + id + "  index:" + index + "  count:" + count);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求
				//分页查询
				List<Address> addressList = addressDao.findByPidPage(id, index, count);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("address", addressList);
				int addressCount = addressDao.countChildren(id);
				int pageCount = (addressCount + (count - 1)) / count;
				map.put("addressCount", addressCount);
				map.put("pageCount", pageCount);
				map.put("index", index);
				map.put("count", count);
				return CodeResult.ok(map);
			}
		});
		
		return  codeResult;
	}
	
	/**
	 * 新增场所
	 */
	@RequestMapping(value="addAddress")
	@ResponseBody
	public CodeResult addAddress(final String name,final Integer pid,final String camera,
			final Integer timetable,final Boolean isClassRoom) {
		logger.debug("name:" + name + "  pid:" + pid + "  camera:" + camera + "  timetable:" + timetable + "  isClassRoom" + isClassRoom);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				//判断是否课室
				int ttid = 0;
				String ip = "";
				int type = ADDRESS_TYPE_NORMAL;
				if(isClassRoom) {
					//课室
					ttid = timetable;
					ip = camera;
					type = ADDRESS_TYPE_CLASSROOM;
				}
				Address address = new Address();
				address.setAdd_name(name);
				address.setAdd_pid(pid);
				address.setAdd_type(type);
				address.setAdd_ttid(ttid);
				address.setAdd_camera(ip);
				
				int id = addressDao.insertReturnWidthId(address);
				//根据id查询
				Address addressInsert = addressDao.findById(id);
				if(addressInsert == null) {
				     return CodeResult.error();
				}
				return CodeResult.ok(addressInsert);
			}
		});
		return codeResult;
	}
	
	/**
	 * 编辑场所
	 */
	@RequestMapping(value="updateAddress")
	@ResponseBody
	public CodeResult updateAddress(final Integer id,final String name,final Integer pid,final String camera,
			final Integer timetable,final Boolean isClassRoom) {
		logger.debug("id:" + id + "  name:" + name + "  pid:" + pid + "  camera:" + camera + "  timetable:" + timetable + "  isClassRoom" + isClassRoom);
		
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				int tt_id = 0;
				String ip = "";
				int type = ADDRESS_TYPE_NORMAL;
				if(isClassRoom) {
					tt_id = timetable;
					ip = camera;
					type = ADDRESS_TYPE_CLASSROOM;
					//删除子孙节点
					deleteChildren(id);
				}else {
					//删除自身对应的学生课表
					studentTableDao.deleteByCrid(id);
				}
				//修改节点
				Address address = new Address();
				address.setAdd_id(id);
				address.setAdd_pid(pid);
				address.setAdd_name(name);
				address.setAdd_type(type);
				address.setAdd_ttid(tt_id);
				address.setAdd_camera(ip);
				addressDao.updateById(address);
				return CodeResult.ok(address);
			}
		});
		return codeResult;
	}
	
	/**
	 * 根据场所id删除子孙节点
	 */
	private int deleteChildren(int id) {
		int deleteNum = 0;
		List<Address> addressList = new ArrayList<Address>();
		listAddressInternal(addressList, id);
		for(Address address : addressList) {
			addressDao.deleteById(address.getAdd_id());
			deleteNum ++;
			
			//教室节点，删除对应的学生课表
			if(address.getAdd_type() == ADDRESS_TYPE_CLASSROOM) {
				studentTableDao.deleteByCrid(address.getAdd_id());
			}
		}
		return deleteNum;
	}
	
	/**
	 * 根据id组合删除场所以及子孙节点
	 */
	@RequestMapping(value="deleteAddress")
	@ResponseBody
	public CodeResult deleteAddress(final String idGroup) {
		logger.debug("idGroup:" + idGroup);
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				int deleteNum = 0;      //删除的节点数
				String[] ids = idGroup.split(",");
				for(int i=0;i<ids.length;i++) {
					//删除子孙节点
					deleteNum += deleteChildren(Integer.valueOf(ids[i]));
					//删除自己
					addressDao.deleteById(Integer.valueOf(ids[i]));
					//删除自身对应的学生课表
					studentTableDao.deleteByCrid(Integer.valueOf(ids[i]));
					deleteNum ++;
				}
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("num", deleteNum);
				return CodeResult.ok(map);
			}
		});
		
		return codeResult;
	}
	
	/**
	 * 根据课表id获取课表信息
	 */
	@RequestMapping(value="getTimeTableByTtid")
	@ResponseBody
	public CodeResult getTimeTableByTtid(final Integer id) {
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				TimeTable timeTable = timeTableDao.findById(id);
				if(timeTable == null) {
					return CodeResult.error();
				}
				return CodeResult.ok(timeTable);
			}
		});
		
		return codeResult;
	}
	
	/**
	 * 批量设置课室课表
	 * flag参数说明：1、代表批量修改id下一级课室课表
	 *  2、批量修改id子孙课室节点课表
	 *  3、批量修改全部课室节点课表
	 */
	@RequestMapping(value="batchSetTimeTable")
	@ResponseBody
	public CodeResult batchSetTimeTable(final Integer id,@RequestParam(defaultValue="0")final Integer timetable,
			@RequestParam(defaultValue="1")final Integer flag) {
		logger.debug("id:" + id + "  timetable:" + timetable + "  flag:" + flag);
		
		CodeResult codeResult = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//处理请求代码
				List<Address> resultList = new ArrayList<Address>();
				if(flag == 1) {
					//查找id下一级课室
					resultList = addressDao.findByPidAndType(id, ADDRESS_TYPE_CLASSROOM);
				}else if(flag == 2) {
					//查找子孙节点
					List<Address> addressList = new ArrayList<Address>();
					listAddressInternal(addressList, id);
					for(Address address: addressList) {
						if(address.getAdd_type() == ADDRESS_TYPE_CLASSROOM) {
							resultList.add(address);
						}
					}
				}else if(flag == 3) {
					//查找全部子节点
					List<Address> addressList = new ArrayList<Address>();
					listAddressInternal(addressList, 0);
					for(Address address: addressList) {
						if(address.getAdd_type() == ADDRESS_TYPE_CLASSROOM) {
							resultList.add(address);
						}
					}
				}else {
					return CodeResult.error();
				}
				//修改课室
				logger.debug("resultList:" + resultList.size());
				for(Address address : resultList) {
					address.setAdd_ttid(timetable);
					addressDao.updateById(address);
				}
				return CodeResult.ok(resultList);
			}
		});
		return codeResult;
	}
	
	/**
	 * 根据班级id获取班级详细名称（学院+专业+班级）
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="getDetailByClassId")
	@ResponseBody
	public CodeResult getDetailByClassId(String ids) {
		try {
			if(ids == null) {
				return new CodeResult(CodeEnum.ERROR.getCode(), "班级id为空");
			}
			Map<String, Object> data = new HashMap<>();
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				String name = "";
				name = composeClassDetailName(Integer.parseInt(id), name);
				logger.debug(name);
				data.put(id, name);
			}
			return CodeResult.ok(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
		
	}
	
	/**
	 * 根据班级id组合班级详细名称
	 * @param classId 班级id
	 * @param name 组成的名称
	 */
	public String composeClassDetailName(int classId, String name) {
		Organization org = organizationDao.findById(classId);
		name =  org.getOrg_name() + name;
		logger.debug(name);
		Integer pid = org.getOrg_pid();
		if(pid != null && pid != 0) {
			name = composeClassDetailName(pid, name);
		}
		return name;
	}
}
