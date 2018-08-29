package creator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.common.db.Page;
import com.creator.db.organization.Organization;
import com.creator.db.organization.OrganizationDaoImpl;
import com.creator.db.subject.Subject;
import com.creator.db.subject.SubjectDaoImpl;
import com.creator.db.subject.SubjectDto;
import com.creator.rest.CodeResult;

@Controller
@RequestMapping(value="subject")
public class SubjectAction {
	//log4j打印
	Logger logger = Logger.getLogger(StudentTableAction.class);
	@Autowired
	private SubjectDaoImpl subjectDao;
	@Autowired
	private OrganizationDaoImpl organizationDao;
	
	/**
	 * 后台首页
	 */
	@RequestMapping(value={""})
	public String index() {
		return "subject/index";
	}
	
	/**
	 * 分页查询学科
	 */
	@RequestMapping(value={"queryByPage"})
	@ResponseBody
	public CodeResult queryByPage(HttpServletRequest request,Page page,String like,Integer majorId) {
		try {
			List<SubjectDto> ls;
			if(majorId == null || majorId.equals("")) {
				ls = subjectDao.queryByPage(page, like);
			}else {
				ls = subjectDao.queryByPage(page, like, majorId);
			}

			for (SubjectDto subjectDto : ls) {
				List<Organization> majors = new ArrayList<>();
				String orgIds = subjectDto.getSub_orgid();
				if(orgIds != null) {
					String[] orgArray = orgIds.split(",");
					for (String orgId : orgArray) {
						if(!orgId.equals("")) {
							Organization major = organizationDao.findById(Integer.parseInt(orgId));
							if(major != null) {
								majors.add(major);
							}
						}
					}
				}
				subjectDto.setMajors(majors);
			}
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("subject", ls);
			data.put("page", page);
			logger.debug(ls.toString());
			logger.debug(page);
			return CodeResult.ok(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/**
	 * 添加学科
	 */
	@RequestMapping(value={"addSubject"})
	@ResponseBody
	public CodeResult addSubject(HttpServletRequest request,Subject subject,String orgs) {
		try {
			if(subject.getSub_type() == 1) {
				subject.setSub_orgid(orgs);
			}
			subjectDao.insert(subject);
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/**
	 * 更新学科
	 */
	@RequestMapping(value={"updateSubject"})
	@ResponseBody
	public CodeResult updateSubject(HttpServletRequest request,Subject subject,String orgs) {
		try {
			if(subject.getSub_type() == 1) {
				subject.setSub_orgid(orgs);
			}else {
				subject.setSub_orgid(null);
			}
			logger.debug(subject);
			subjectDao.updateById(subject);
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/**
	 * 删除学科
	 */
	@RequestMapping(value={"deleteSubject"})
	@ResponseBody
	public CodeResult deleteSubject(HttpServletRequest request,String ids) {
		try {
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				if(!id.equals("")) {
					int ttId = Integer.parseInt(id);
					subjectDao.deleteById(ttId);
				}
			}
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
}
