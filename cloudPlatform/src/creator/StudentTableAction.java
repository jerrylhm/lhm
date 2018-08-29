package creator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.common.util.CalendarUtil;
import com.creator.db.address.Address;
import com.creator.db.address.AddressDaoImpl;
import com.creator.db.index.Index;
import com.creator.db.index.IndexDaoImpl;
import com.creator.db.organization.Organization;
import com.creator.db.organization.OrganizationDaoImpl;
import com.creator.db.studenttable.StudentTable;
import com.creator.db.studenttable.StudentTableDaoImpl;
import com.creator.db.studenttable.StudentTableDto;
import com.creator.db.term.Term;
import com.creator.db.term.TermDaoImpl;
import com.creator.db.timetable.TimeTable;
import com.creator.db.timetable.TimeTableDaoImpl;
import com.creator.db.user.User;
import com.creator.db.user.UserDaoImpl;
import com.creator.rest.CodeEnum;
import com.creator.rest.CodeResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 后台管理
 *
 */
@Controller
@RequestMapping(value="studenttable")
public class StudentTableAction {
	//log4j打印
	Logger logger = Logger.getLogger(StudentTableAction.class);
	@Autowired
	private TimeTableDaoImpl timeTableDao;
	@Autowired
	private OrganizationDaoImpl organizationDao;
	@Autowired
	private StudentTableDaoImpl studentTableDao;
	@Autowired
	private AddressDaoImpl addressDao;
	@Autowired
	private UserDaoImpl userDao;
	@Autowired
	private IndexDaoImpl indexDao;
	@Autowired
	private TermDaoImpl termDao;
	
	/**
	 * 后台首页
	 */
	@RequestMapping(value={"","index"})
	public String index() {
		return "studenttable/index";
	}

	/**
	 * 获取班级列表
	 */
	@RequestMapping(value={"getOrgs"})
	@ResponseBody
	public CodeResult getOrgs(HttpServletRequest request) {
		try {
			List<Organization> orgs = organizationDao.queryByOrgType(4);
			JSONArray data = new JSONArray();
			for (Organization org : orgs) {
				JSONObject jo = new JSONObject();
				String className = createOrgName(org);
				if(className != null) {
					jo.put("name", className);
					jo.put("ele", org);
					data.add(jo);
				}
			}
			return CodeResult.ok(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/**
	 * 获取教师列表
	 */
	@RequestMapping(value={"getTeachers"})
	@ResponseBody
	public CodeResult getTeachers(HttpServletRequest request) {
		try {
			List<User> teachers = userDao.queryTeachers();
			return CodeResult.ok(teachers);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	//生成班级名称（专业+年级+班级）
	public String createOrgName(Organization org) {
		Organization grade = organizationDao.findById(org.getOrg_pid());
		if(grade == null) {
			return null;
		}
		Organization major = organizationDao.findById(grade.getOrg_pid());
		if(major == null) {
			return null;
		}
		return major.getOrg_name()+grade.getOrg_name()+org.getOrg_name();
	}
	
	/**
	 * 检查该时段该在教室是否有课（适用于新增时检查）
	 * @return 有则返回true，没有则返回false
	 */
	public boolean checkExistForAdd(Integer ttId, Integer week, Integer addId, Integer index, Integer day) {
		List<Index> ls = indexDao.queryExist(ttId, week, addId, index, day);
		if(ls.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检查该时段该在教室是否有课（适用于新增时检查）
	 * @return 有则返回true，没有则返回false
	 */
	public boolean checkExistForUpdate(Integer stId, Integer ttId, Integer week, Integer addId, Integer index, Integer day) {
		List<Index> ls = indexDao.queryExist(ttId, week, addId, index, day);
		if(ls.size() > 0) {
			for (Index index2 : ls) {
				if(index2.getIndex_stid() != stId) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 添加课程
	 */
	@Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
	@RequestMapping(value={"addStudentTable"})
	@ResponseBody
	public CodeResult addStudentTable(HttpServletRequest request,
			StudentTable st,Integer st_index_begin,Integer st_index_end,Integer day) {
		try {			
			String[] weekArray = st.getSt_weeks().split(",");
			for (String week : weekArray) {
				for(int i=st_index_begin;i<=st_index_end;i++) {
					if(!week.equals("") && checkExistForAdd(st.getSt_ttid(), Integer.parseInt(week), st.getSt_addid(), i, day)) {
						return new CodeResult(CodeEnum.ERROR.getCode(), "第" + week + "周第" + i + "节课已被占用");
					}
				}
			}
			
			int stid = studentTableDao.insert(st);
			for(int i=st_index_begin;i<=st_index_end;i++) {
				Index index = new Index();
				index.setIndex_stid(stid);
				index.setIndex_day(day);
				index.setIndex_index(i);
				indexDao.insert(index);
			}
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	@RequestMapping(value={"getStudentTable"})
	@ResponseBody
	public CodeResult getStudentTable(HttpServletRequest request,
			Integer ttId, Integer addId, Integer week,Integer orgId) {
		try {	
			TimeTable tt = timeTableDao.findById(ttId);
			Term term = termDao.findById(tt.getTt_termid());
			if(term == null) {
				return new CodeResult(CodeEnum.ERROR.getCode() ,"作息表:" + tt.getTt_name() + "未绑定学期");
			}
			Date firstDate = term.getTerm_start();
			if(week == null) {
				week = CalendarUtil.getWeekForNow(firstDate);
			}
			
			List<Map<String, Object>> ymd = CalendarUtil.getDatesOfWeek(firstDate, week);
			List<StudentTableDto> dtos = studentTableDao.queryByParam(week, ttId, addId, orgId);
			for (StudentTableDto studentTableDto : dtos) {
				List<Index> indexs = indexDao.queryByStId(studentTableDto.getSt_id());
				studentTableDto.setIndexs(indexs);	
			}
			Map<String, Object> data = new HashMap<>();
			data.put("sts", dtos);
			data.put("ymd", ymd);
			data.put("week", week);
			return CodeResult.ok(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/**
	 * 更新课程
	 */
	@Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
	@RequestMapping(value={"updateStudentTable"})
	@ResponseBody
	public CodeResult updateStudentTable(HttpServletRequest request,
			StudentTable st,Integer st_index_begin,Integer st_index_end,Integer day) {
		try {	
			logger.debug(st);
			logger.debug(st_index_begin);
			logger.debug(st_index_end);
			logger.debug(day);
			studentTableDao.deleteById(st.getSt_id());
			indexDao.deleteByStId(st.getSt_id());
			
			String[] weekArray = st.getSt_weeks().split(",");
			for (String week : weekArray) {
				for(int i=st_index_begin;i<=st_index_end;i++) {
					if(!week.equals("") && checkExistForUpdate(st.getSt_id(), st.getSt_ttid(), Integer.parseInt(week), st.getSt_addid(), i, day)) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return new CodeResult(CodeEnum.ERROR.getCode(), "第" + week + "周第" + i + "节课已被占用");
					}
				}
			}
			
			int stid = studentTableDao.insert(st);
			for(int i=st_index_begin;i<=st_index_end;i++) {
				Index index = new Index();
				index.setIndex_stid(stid);
				index.setIndex_day(day);
				index.setIndex_index(i);
				indexDao.insert(index);
			}
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return CodeResult.error();
		}
	}
}
