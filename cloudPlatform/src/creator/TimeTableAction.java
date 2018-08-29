package creator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.common.db.Page;
import com.creator.db.address.Address;
import com.creator.db.address.AddressDaoImpl;
import com.creator.db.term.Term;
import com.creator.db.term.TermDaoImpl;
import com.creator.db.timetable.TimeTable;
import com.creator.db.timetable.TimeTableDaoImpl;
import com.creator.db.timetable.TimeTableDto;
import com.creator.db.ttnode.TTNode;
import com.creator.db.ttnode.TTNodeDaoImpl;
import com.creator.interceptor.AdminInterceptor;
import com.creator.rest.CodeEnum;
import com.creator.rest.CodeResult;
import com.sun.crypto.provider.AESParameters;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 后台管理
 *
 */
@Controller
@RequestMapping(value="timetable")
public class TimeTableAction {
	//log4j打印
	Logger logger = Logger.getLogger(TimeTableAction.class);
	@Autowired
	private TimeTableDaoImpl timeTableDao;
	@Autowired
	private TTNodeDaoImpl ttNodeDao;
	@Autowired
	private AddressDaoImpl addressDao;
	@Autowired
	private TermDaoImpl termDao;
	
	/**
	 * 后台首页
	 */
	@RequestMapping(value={"","index"})
	public String index() {
		return "timetable/index";
	}
	
	@RequestMapping(value={"queryByPage"})
	@ResponseBody
	public CodeResult queryByPage(HttpServletRequest request,Page page,String like) {
		try {
			List<TimeTableDto> ls = timeTableDao.queryByPage(page, like);
			Map<String,Object> data = new HashMap<String,Object>();
			for(TimeTableDto timeTableDto : ls) {
				int termId = timeTableDto.getTt_termid();
				Term term = termDao.findById(termId);
				if(term != null) {
					timeTableDto.setTerm(term);
				}
			}
			data.put("timetable", ls);
			data.put("page", page);
			return CodeResult.ok(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/**
	 * 获取全部课表
	 */
	@RequestMapping(value={"queryTimeTables"})
	@ResponseBody
	public CodeResult queryTimeTables(HttpServletRequest request) {
		try {
			List<TimeTable> tts = timeTableDao.query();
			List<TimeTableDto> data = new ArrayList<>();
			for (TimeTable tt : tts) {
				TimeTableDto dto = new TimeTableDto();
				BeanUtils.copyProperties(tt, dto);
				dto.setNodes(ttNodeDao.queryByttId(dto.getTt_id()));
				data.add(dto);
			}
			return CodeResult.ok(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	/**
	 * 添加课表
	 */
	@Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
	@RequestMapping(value={"addTimeTable"})
	@ResponseBody
	public CodeResult addTimeTable(HttpServletRequest request,String tt_name,Integer tt_termid,Integer tt_num,String nodes) {
		Integer userId = (Integer) request.getSession().getAttribute(AdminInterceptor.SESSION_USERID);
		if(userId == null) {
			return new CodeResult(CodeEnum.ERROR.getCode(), "登录失效!");
		}
		JSONArray ja = JSONArray.fromObject(nodes);
		TimeTableDto data = new TimeTableDto();
		TimeTable tt = new TimeTable();
		tt.setTt_name(tt_name);
		tt.setTt_termid(tt_termid);
		tt.setTt_num(tt_num);
		int ttId = timeTableDao.insert(tt);
		tt.setTt_id(ttId);
		BeanUtils.copyProperties(tt, data);
		logger.debug(tt);
		
		List<TTNode> ls = new ArrayList<>();
		for(int i=0;i<ja.size();i++) {
			JSONObject node = ja.getJSONObject(i);
			TTNode ttnode = new TTNode();
			ttnode.setNode_ttid(ttId);
			ttnode.setNode_index(node.getInt("node_index"));
			ttnode.setNode_start(node.getString("node_start"));
			ttnode.setNode_end(node.getString("node_end"));
			ttNodeDao.insert(ttnode);
			ls.add(ttnode);
			logger.debug(node.toString());
		}
		data.setNodes(ls);
		CodeResult result = CodeResult.ok(data);
		return result;
	}
	
	/**
	 * 删除课表
	 */
	@Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
	@RequestMapping(value={"deleteTimeTable"})
	@ResponseBody
	public CodeResult deleteTimeTable(HttpServletRequest request,String ids) {
		try {
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				if(!id.equals("")) {
					int ttId = Integer.parseInt(id);
					timeTableDao.deleteById(ttId);
					ttNodeDao.deleteByTtid(ttId);
				}
			}
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	@RequestMapping(value={"getNodeByttId"})
	@ResponseBody
	public CodeResult getNodeByttId(HttpServletRequest request,Integer id) {
		try {
			List<TTNode> data = ttNodeDao.queryByttId(id);
			return CodeResult.ok(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
	@Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
	@RequestMapping(value={"updateTimeTable"})
	@ResponseBody
	public CodeResult updateTimeTable(HttpServletRequest request,TimeTable timeTable,String nodes) {
		try {
			timeTableDao.updateById(timeTable);
			logger.debug(timeTable);
			ttNodeDao.deleteByTtid(timeTable.getTt_id());
			JSONArray ja = JSONArray.fromObject(nodes);
			for(int i=0;i<ja.size();i++) {
				JSONObject node = ja.getJSONObject(i);
				TTNode ttnode = new TTNode();
				ttnode.setNode_ttid(node.getInt("node_ttid"));
				ttnode.setNode_index(node.getInt("node_index"));
				ttnode.setNode_start(node.getString("node_start"));
				ttnode.setNode_end(node.getString("node_end"));
				ttNodeDao.insert(ttnode);
				logger.debug(node);
			}
			return CodeResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return CodeResult.error();
		}
	}
	
}
