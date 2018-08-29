package creator;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.db.meeting.Meeting;
import com.creator.db.meeting.MeetingDao;

@Controller
@RequestMapping("meetingManagement")
public class MeetingManagementAction {
	
	@Autowired
	MeetingDao meetingDao;
	
	@RequestMapping(value="index")
	public String index(Model model){
		
		model.addAttribute("title", "会议审核");
		model.addAttribute("href", "meetingManagement/meetingAudit");
		
		return "audit/auditManage";
	}
	
	@RequestMapping(value="meetingAudit")
	public String meetingAudit(Model model,HttpServletRequest request,String page){
		int index;
		if(page==null || page==""){
			index=1;
		}else{
			index = Integer.parseInt(page);
		}
		
		int userid = (int) request.getSession().getAttribute("USERID");
		
		List<Map<String, Object>> list_meeting = meetingDao.queryMeetingByUserId(userid, index, 10);
		
		int count = meetingDao.countMeetingByUserId(userid);
		
		int countPage = (count+9)/10;
		model.addAttribute("countPage", countPage);
		model.addAttribute("index", index);
		model.addAttribute("list_meeting", list_meeting);
		
		
		return "audit/meetingAudit";
	}
	
	@RequestMapping(value="updateMeeting")
	@ResponseBody
	public String updateMeeting(Model model,String id,String title,String start,String end,String num,String status,String del_flag,String ids){
		
		
	
		if(del_flag==null || del_flag==""){		
			if(status==null || status==""){
				List<Map<String, Object>> list_single = meetingDao.queryMeetingByMeId(Integer.parseInt(id));
				Meeting me = new Meeting();
				me.setMe_userid((Integer) list_single.get(0).get("me_userid"));
				me.setMe_title(title);
				me.setMe_nodeid((Integer) list_single.get(0).get("me_nodeid"));
				me.setMe_number(Integer.parseInt(num));
				me.setMe_starttime(start);
				me.setMe_endtime(end);
				me.setMe_description((String) list_single.get(0).get("me_description"));
				me.setMe_status((Integer) list_single.get(0).get("me_status"));		
				
				meetingDao.updateMeeting(Integer.parseInt(id), me);
				return "1";
				
			}else{
				List<Map<String, Object>> list_single = meetingDao.queryMeetingByMeId(Integer.parseInt(id));
				if(meetingDao.compareMeetingDate((String)list_single.get(0).get("me_starttime"),(String)list_single.get(0).get("me_endtime") ,(Integer) list_single.get(0).get("me_userid"),((Integer)list_single.get(0).get("me_nodeid")).toString()).size()>0 && status.equals("1")){
					System.out.println("hello");
					return "0";
					
				}else{
					
					Meeting me = new Meeting();
					me.setMe_userid((Integer) list_single.get(0).get("me_userid"));
					me.setMe_title((String) list_single.get(0).get("me_title"));
					me.setMe_nodeid((Integer) list_single.get(0).get("me_nodeid"));
					me.setMe_number((Integer) list_single.get(0).get("me_number"));
					me.setMe_starttime((String) list_single.get(0).get("me_starttime"));
					me.setMe_endtime((String) list_single.get(0).get("me_endtime"));
					me.setMe_description((String) list_single.get(0).get("me_description"));
					me.setMe_status(Integer.parseInt(status));		
					
					meetingDao.updateMeeting(Integer.parseInt(id), me);
					return "3";
				}
				
			}	
		}else{
				meetingDao.deleteMeetingAlot(ids);
				return "2";
		}
		
		
	}
	
	
	
}
