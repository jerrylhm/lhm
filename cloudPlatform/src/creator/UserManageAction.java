package creator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.creator.annotation.ExcelMapping;
import com.creator.common.db.AfterHandleData;
import com.creator.common.db.ExcelDataHandler;
import com.creator.common.db.ExcelDataResult;
import com.creator.common.db.ExcelToEntity;
import com.creator.common.util.MD5Util;
import com.creator.common.util.POIUtil;
import com.creator.constant.UserExcelMap;
import com.creator.db.organization.Organization;
import com.creator.db.organization.OrganizationDao;
import com.creator.db.user.GetUserDto;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.db.usergroup.UserGroup;
import com.creator.db.usergroup.UserGroupDao;
import com.creator.rest.CodeEnum;
import com.creator.rest.CodeResult;
import com.creator.rest.ResultProcessor;

/**
 * 系统用户(用户管理)的增删改查操作控制器
 */
@RestController
@RequestMapping(value="userManage")
public class UserManageAction extends BaseAction {
	
	private int index = 1;
	/*private int count;*/
	private int perPageCount = 9;
	
	private final static String DEFAULT_PWD = "123456";
	private final static String DEFAULT_IMAGE = "upload/head-image/default_image.gif";
	private final static int DEFAULT_USERGROUP = 3;
	
	private final static String XLS_TYPE = "application/vnd.ms-excel";
	private final static String XLSX_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	
	//log4j打印
	Logger logger = Logger.getLogger(UserManageAction.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private UserGroupDao userGroupDao;
	
	/**
	 * 查找单个用户信息
	 * @param id	用户id
	 * @return
	 */
	@GetMapping("{id}")
	public CodeResult getOne(@PathVariable("id") final Integer id) {
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				List<User> userList = userDao.findById(id);
				return CodeResult.ok(userList);
			}
		});
		return code;
	}
	
	/**
	 * 检查用户名是否已经被占用
	 * @param username	用户名
	 * @return
	 */
	@GetMapping("isExist/{username}")
	public CodeResult isExist(@PathVariable("username") final String username) {
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				int result = userDao.isExist(username);
				return CodeResult.ok(result);
			}
		});
		return code;
	}
	
	/**
	 * 分页获取所有用户
	 * @param dto	分页查询数据传输中间类
	 * @return
	 */
	@PostMapping("getAll")
	public CodeResult getAll(final GetUserDto dto) {
		if(dto.getPage() == null){
			index = 1;
		} else {
			index = dto.getPage();
		}
			
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				List<Map<String, Object>> userList = userDao.queryAllUserByNeed(index, perPageCount, dto.getQuery(), dto.getDate(), dto.getRole(), dto.getStatus(), dto.getUg_id(), dto.getOrderBy(), dto.getIsDESC());
				int countNum = userDao.countAllUserByNeed(dto.getQuery(), dto.getDate(), dto.getRole(), dto.getStatus(), dto.getUg_id());
				List<UserGroup> userGroupList = userGroupDao.query();
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("users", userList);
				result.put("usergroups", userGroupList);
				result.put("curPage", index);
				result.put("perPageCount", perPageCount);
				result.put("totalPage", (countNum + perPageCount - 1)/perPageCount);
				result.put("totalCount", countNum);
				return CodeResult.ok(result);
			}
		});
		return code;
	}
	
	/**
	 * 删除用户
	 * @param ids	用户id字符串
	 * @return
	 */
	@DeleteMapping("{ids}")
	public CodeResult deleteSelect(@PathVariable("ids") final String ids) {
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				if(!StringUtils.isEmpty(ids)) {
					String[] idGroup = ids.split(",");
					for(String id : idGroup) {
						//删除用户组
						if(userGroupDao.deleteByUrid(Integer.parseInt(id)) == -1) {
							return new CodeResult(CodeEnum.ERROR.getCode(), "用户删除失败。");
						}
						userDao.deleteById(Integer.parseInt(id));
					}
				}
				return CodeResult.ok();
			}
		});
		return code;
	}
	
	/**
	 * 更新用户
	 * @param id			用户id
	 * @param user			用户信息
	 * @param isResetPwd	是否重置密码
	 * @param ur_group		用户组id组
	 * @return
	 */
	@PutMapping("{id}")
	public CodeResult updateOne(@PathVariable("id") final Integer id, final User user, final String isResetPwd, final String ur_group) {
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				List<User> userList = userDao.findById(id);
				if(userList.size() != 0) {
					User _user = userList.get(0);
					//必填
					_user.setUr_nickname(user.getUr_nickname());
					_user.setUr_classid(StringUtils.isEmpty(user.getUr_classid())?"0":user.getUr_classid());
					_user.setUr_type(user.getUr_type());
					_user.setUr_sex(user.getUr_sex());
					//选填
					_user.setUr_phone(user.getUr_phone());
					_user.setUr_email(user.getUr_email());
					//重置密码
					if(!StringUtils.isEmpty(isResetPwd) && isResetPwd.equals("on")) {
						_user.setUr_password(MD5Util.md5(DEFAULT_PWD));
					}
					int result = userDao.updateUser(_user);
					if(result == 0) {
						return new CodeResult(CodeEnum.ERROR.getCode(), "用户更新失败。");
					}
				}
				
				//更新用户组
				if(!StringUtils.isEmpty(ur_group)) {
					//删除用户组
					if(userGroupDao.deleteByUrid(id) == -1) {
						return new CodeResult(CodeEnum.ERROR.getCode(), "用户更新失败。");
					}
					
					String[] ug_ids = ur_group.split(",");
					for(String ug_id : ug_ids) {
						if(userGroupDao.addUserGroupToUser(id, Integer.parseInt(ug_id)) == -1) {
							return new CodeResult(CodeEnum.ERROR.getCode(), "用户更新失败。");
						}
					}
				}
				
				return CodeResult.ok();
			}
		});
		return code;
	}
	
	/**
	 * 新增用户
	 * @param user		用户信息
	 * @param ur_group	用户组id组
	 * @return
	 */
	@PostMapping("")
	public CodeResult addOne(final User user, final String ur_group) {		
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				if(saveUser(user, ur_group)) {
					return CodeResult.ok();
				} else {
					return new CodeResult(CodeEnum.ERROR.getCode(), "新增用户失败。");
				}
			}
		});
		
		return code;
	}
	
	/**
	 * 批量用户导入
	 * @param userList	用户集合
	 * @return
	 */
	@PostMapping("importUsers")
	public CodeResult importUsers(@RequestBody final List<User> userList) {
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				//判空
				if(userList != null && userList.size() != 0) {
					//错误集合
					Map<Integer, Integer> errorList = new HashMap<Integer, Integer>();
					//要导入的用户总数
					int totalCount = userList.size();
					//循环遍历
					for(int n = 0; n < totalCount; n++) {
						//判断用户名是否重复 
						int exist = userDao.isExist(userList.get(n).getUr_username());
						if(exist == 1) {
							errorList.put(n, 1);
							break;
						}
						//设置用户组和角色对应
						String ur_group = userList.get(n).getUr_type();
						//保存用户
						if(!saveUser(userList.get(n), ur_group)) {
							errorList.put(n, 2);
						}
					}
					return CodeResult.ok(errorList);
				} else {
					return new CodeResult(CodeEnum.ERROR.getCode(), "没有要新增的用户。");
				}
			}
		});
		
		return code;
	}
	
	/**
	 * 保存用户的抽取方法
	 * @param user		用户对象
	 * @param ur_group	权限组
	 * @return	成功返回true，失败返回false
	 */
	private boolean saveUser(User user, String ur_group) {
		//密码MD5加密
		user.setUr_password(MD5Util.md5(user.getUr_password()));
		//默认头像
		user.setUr_image(DEFAULT_IMAGE);
		//如果班级为空，则设置为"0"
		if(StringUtils.isEmpty(user.getUr_classid())) {
			user.setUr_classid("0");
		}
		//设置注册日期
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		user.setUr_createdate(format.format(new Date()));
		//设置用户审核状态为0(未审核)
		user.setUr_status(0);
		
		//添加用户信息到数据库
		int ur_id = userDao.addUser(user);
		
		if(ur_id == -1) {
			return false;
		}
		
		//添加用户组
		if(!StringUtils.isEmpty(ur_group)) {			
			String[] ug_ids = ur_group.split(",");
			for(String ug_id : ug_ids) {
				if(userGroupDao.addUserGroupToUser(ur_id, Integer.parseInt(ug_id)) == -1) {
					return false;
				}
			}
		} else {
			//如果用户组为空，默认会给予一个默认用户组
			if(userGroupDao.addUserGroupToUser(ur_id, DEFAULT_USERGROUP) == -1) {
				return false;
			}
		}
		return true;
	}
	
	
	
	/**
	 * 根据年级id返回所有班级
	 * @param id	年级id
	 * @return
	 */
	@GetMapping("getClass/{id}")
	public CodeResult getClassById(@PathVariable("id") final Integer id) {
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				List<Organization> classList = organizationDao.findByPid(id);
				return CodeResult.ok(classList);
			}
		});
		return code;
	}
	
	/**
	 * 获取所有年级
	 * @return
	 */
	@GetMapping("getAllGrade/{id}")
	public CodeResult getAllGrade(@PathVariable("id") final Integer id) {
		CodeResult code = restProcessor(new ResultProcessor() {
			@Override
			public CodeResult process() {
				List<Organization> gradeList = organizationDao.queryByOrgType(3);
				int targetGradeId = 0;
				if(id != 0) {
					Organization org = organizationDao.findById(id);
					if(org != null) {
						targetGradeId = org.getOrg_pid();
					}
				} else if(gradeList.size() != 0) {
					targetGradeId = gradeList.get(0).getOrg_id();
				}
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("gradeList", gradeList);
				result.put("targetGradeId", targetGradeId);
				return CodeResult.ok(result);
			}
		});
		return code;
	}
	
	/**
	 * excel文件上传
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@PostMapping("xlsFileUpload")
	public CodeResult xlsFileUpload(@RequestParam("file") CommonsMultipartFile file) throws Exception {
		
		//判断文件是否存在
		if(!file.isEmpty()) {
			//获取文件类型
			String contentType = file.getContentType();
			//判断文件类型(只能是.xls或者.xlsx)
			if(XLS_TYPE.equals(contentType) || XLSX_TYPE.equals(contentType)) {
				//调用工具读取Excel表格数据
				List<String[]> readExcel = POIUtil.readExcel(file);
				
				//获取所有班级数据
				List<Organization> org_list = organizationDao.getAllWithConcat();

				//检查并转换数据
				ExcelDataResult<?> result = new ExcelToEntity().excelToEntity(User.class, readExcel, UserExcelMap.attrsMap, new ExcelDataHandler() {
					@Override
					public <T> AfterHandleData handleBeforeMapping(String attr, ExcelMapping anno, List<T> entityList) {
						switch (anno.value()) {
						//拦截班级数据
						case "班级":
							//遍历检查是否存在该班级
							for(Organization organization : org_list) {
								if(organization.getOrg_name().equals(attr)) { 
									//存在该班级，返回该班级的id
									return new AfterHandleData(true, organization.getOrg_id().toString(), null);
								}
							}
							//返回错误信息
							return new AfterHandleData(false, null, anno.errorMsg());
						//拦截用户名数据
						case "用户名":
							//检查用户名是否重复
							//先检查用户名在数据库中是否重复
							if(userDao.isExist(attr) == 1) {
								return new AfterHandleData(false, null, "该用户名已被使用。");
							}
							//再检查用户名在待导入的用户中是否重复(向前检查)
							for(T t : entityList) {
								User user = (User) t;
								String ur_username = user.getUr_username();
								if(StringUtils.isEmpty(ur_username)) {
									continue;
								}
								if(ur_username.equals(attr)) {
									return new AfterHandleData(false, null, "用户名重复。");
								}
							}
							return new AfterHandleData(true, attr, null);
						default:
							//放行其它数据
							return new AfterHandleData(true, attr, null);
						}
					}

					@Override
					public <T> AfterHandleData handleAfterMapping(String attr, ExcelMapping anno, List<T> entityList) {
						return new AfterHandleData(true, attr, null);
					}
				});
				
				if(result == null) {
					return new CodeResult(CodeEnum.ERROR.getCode(), "Excel文件格式错误，请参考模板进行修改。");
				}
				
				return CodeResult.ok(result);
				
			} else {
				//文件类型错误
				return new CodeResult(CodeEnum.ERROR.getCode(), "只能上传.xls或.xlsx类型的EXCEL文件。");
			}
		} else {
			//文件为空
			return new CodeResult(CodeEnum.ERROR.getCode(), "请先选择文件。");
		}
	}
}
