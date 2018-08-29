package creator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.common.db.Page;
import com.creator.db.term.Term;
import com.creator.db.term.TermDaoImpl;
import com.creator.db.term.TermDto;
import com.creator.db.timetable.TimeTableDaoImpl;
import com.creator.db.timetable.TimeTableDto;
import com.creator.rest.CodeResult;

@Controller
@RequestMapping(value="term")
public class TermAction {
	//log4j打印
	Logger logger = Logger.getLogger(AdminAction.class);
	
	@Autowired
	private TermDaoImpl termDao;
	@Autowired
	private TimeTableDaoImpl timeTableDao;
	
	@RequestMapping(value={"","index"})
	public String index(HttpServletRequest request,Model model) {
		return "term/index";
	}
	
	/**
	 * 新增学期
	 */
	@RequestMapping(value={"addTerm"})
	@ResponseBody
	public CodeResult addTerm(HttpServletRequest request, Term term, Model model) {
		try {
			termDao.insert(term);
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	@RequestMapping(value={"queryByPage"})
	@ResponseBody
	public CodeResult queryByPage(HttpServletRequest request, Page page, String like) {
		try {
			List<TermDto> ls = termDao.queryByPage(page, like);
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("terms", ls);
			data.put("page", page);
			return CodeResult.ok(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/**
	 * 更新学期
	 */
	@RequestMapping(value={"updateTerm"})
	@ResponseBody
	public CodeResult updateTerm(HttpServletRequest request, Term term, Model model) {
		try {
			termDao.updateById(term);
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/**
	 * 删除学期
	 */
	@RequestMapping(value={"deleteTerm"})
	@ResponseBody
	public CodeResult deleteTerm(HttpServletRequest request, String ids, Model model) {
		try {
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				termDao.deleteById(Integer.parseInt(id));
				timeTableDao.resetTermIdByTermId(Integer.parseInt(id));
			}
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/**
	 * 获取学期列表
	 */
	@RequestMapping(value={"query"})
	@ResponseBody
	public CodeResult query(HttpServletRequest request) {
		try {
			return CodeResult.ok(termDao.query());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
}
