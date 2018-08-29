package creator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.db.permission.PermissionDto;
import com.creator.db.template.Template;
import com.creator.db.template.TemplateDao;
import com.creator.db.template.TemplateDto;
import com.creator.db.template.TpTypeEnum;
import com.creator.db.tpcontent.TPContent;
import com.creator.db.tpcontent.TPContentDao;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.type.TypeDao;
import com.creator.db.value.ValueDao;
import com.fasterxml.jackson.databind.util.BeanUtil;

@Controller
@RequestMapping("/template")
public class TemplateAction {

	@Autowired
	private TreeDao treeDao;
	@Autowired
	private ValueDao valueDao;
	@Autowired
	private TypeDao typeDao;
	@Autowired
	private TemplateDao templateDao;
	@Autowired
	private TPContentDao tpContentDao;
 
	/**
	 * 模板管理首页
	 * @param request
	 * @param templateDto
	 * @param model
	 * @return
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request, TemplateDto templateDto, Model model) {
		 Integer userId = (Integer) request.getSession().getAttribute("USERID");
		 model.addAttribute("like", templateDto.getLike());	   
		   if(templateDto == null){
			   templateDto = new TemplateDto();
		   }
		   templateDto.getPage().setPageNumber(10);	   
		   List<Tree> ls = treeDao.queryTreeNodeByUserId(userId);
		   List<Integer> treesStr = new ArrayList<Integer>();
		   for (Tree tree : ls) {
			   treesStr.add(tree.getNode_id());
		   }
		   templateDto.setTreesStr(StringUtils.join(treesStr.toArray(),","));
		  List<TemplateDto> tps = templateDao.queryByPage(templateDto);
		  model.addAttribute("searchParam", templateDto);

         /*
          * 初始化模板的所属树名称
          */
         for (TemplateDto tp : tps) {
        	 Tree treeNode = treeDao.queryById(tp.getTp_treeid()).get(0);
			tp.setTreeName(treeNode.getNode_name());
			tp.setUserId(treeNode.getNode_userid());
		}

         /*
          * 获取所有树节点列表
          */
         List<Map<String,Object>> trees = treeDao.queryRootByUserId(userId);
         model.addAttribute("trees", trees);
         model.addAttribute("templates", tps);
		return "template/index";
	}	
	
	/**
	 * 添加模板
	 * @param request
	 * @param template
	 * @param model
	 * @return
	 */
	@RequestMapping("addTemplate")
	@ResponseBody
	public String addTemplate(HttpServletRequest request,Template template,Model model) {
		 if(templateDao.add(template)==1) {
			 return "1";
		 }else {
			 return "0";
		 }
	}
	
	/**
	 * 删除模板
	 * @param request
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("deleteTemplate")
	@ResponseBody
	public String deleteTemplate(HttpServletRequest request,Integer id,Model model) {
		 if(templateDao.delete(id)==1) {
			 return "1";
		 }else {
			 return "0";
		 }
	}
	
	/**
	 * 根据id获取模板
	 * @param request
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("getTemplate")
	@ResponseBody
	public Template getTemplate(HttpServletRequest request,Integer id,Model model) {
		 List<Template> templates = templateDao.findById(id);
		 Template template;
		 if(templates.size() > 0) {
			 template = templates.get(0);
			 return template;
		 }
		 return null;
	}
	
	/**
	 * 更新模板的名称
	 * @param request
	 * @param id
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping("updateTemplateName")
	@ResponseBody
	public String updateTemplateName(HttpServletRequest request,Integer id, String name,Model model) {
		 if(templateDao.updateTemplateName(id, name) == 1) {
			 return "1";
		 }else {
			 return "0";
		 }
	}
	
	/**
	 * 删除模板的属性
	 * @param request
	 * @param id
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping("deletetTemplateAttr")
	@ResponseBody
	public String deletetTemplateAttr(HttpServletRequest request,Integer id, String name,Model model) {
		 List<Template> templates = templateDao.findById(id);
		 if(templates.size() > 0) {
			 Template template = templates.get(0);
			 JSONArray ja = JSONArray.fromObject(template.getTp_data());
			 for(int i=0;i<ja.size();i++) {
				 //模板属性json对象
				 JSONObject jo = ja.getJSONObject(i);
				 if(jo.get("name").equals(name)) {
					 ja.remove(jo);
					 template.setTp_data(ja.toString());
					 templateDao.update(template);
					 updateTPCWhenDelete(template.getTp_id(), name);
					 return "1";
				 }
			 }
		 }
		 return "0";
	}
	
	/**
	 * 删除模板属性时更新模板内容
	 * @param tpId
	 * @param attrName
	 */
	public void updateTPCWhenDelete(int tpId, String attrName) {
		List<TPContent> tpcs = tpContentDao.findByTpId(tpId);
		if(tpcs.size() > 0) {
			for (TPContent tpContent : tpcs) {
				JSONObject jo = JSONObject.fromObject(tpContent.getTpc_content());
						jo.remove(attrName);
						tpContent.setTpc_content(jo.toString());
						tpContentDao.update(tpContent);
						return;
			}
		}
	}
	
	/**
	 * 添加模板属性
	 * @param request
	 * @param id
	 * @param option
	 * @param name
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping("addTemplateAttr")
	@ResponseBody
	public String addTemplateAttr(HttpServletRequest request,Integer id, String option, String name, String type, Model model) {
		 List<Template> templates = templateDao.findById(id);
		 if(templates.size() > 0) {
			 Template template = templates.get(0);
			 JSONArray ja = JSONArray.fromObject(template.getTp_data());
			 //模板熟悉json对象
		     JSONObject jo = new JSONObject();
             jo.put("name", name);
             jo.put("type", type);
             if(type.equals("enum")) {
            	 jo.put("option", option);
             }
             ja.add(jo);
             template.setTp_data(ja.toString());
             templateDao.update(template);
             
             int tpId = template.getTp_id();
             updateTPCWhenAdd(tpId, name);
             return "1";
		 }else {
			 return "0";
		 }
	}
	
	/**
	 * 新增模板属性时更新模板内容
	 * @param tpId
	 * @param attrName
	 * @param type
	 * @param option
	 */
	public void updateTPCWhenAdd(int tpId, String attrName) {
		List<TPContent> tpcs = tpContentDao.findByTpId(tpId);
		if(tpcs.size() > 0) {
			for (TPContent tpContent : tpcs) {
				String content = tpContent.getTpc_content();
				if(content == null) {
					content = "{}";
				}
				JSONObject jo = JSONObject.fromObject(content);
				jo.put(attrName, "");
				tpContent.setTpc_content(jo.toString());
				tpContentDao.update(tpContent);
			}
		}
	}
	
	/**
	 * 获取模板内容的json数据
	 * @param attrName
	 * @param type
	 * @param option
	 * @return
	 */
	public JSONObject getTPCJSONObject(String attrName, String type, String option) {
		JSONObject jo = new JSONObject();
		jo.put("name", attrName);
		jo.put("type", type);
		if(type.equals(TpTypeEnum.TEXT.getName())) {
			jo.put("content", "");
		}else if(type.equals(TpTypeEnum.FILE.getName())) {
			jo.put("url", "");
		}else if(type.equals(TpTypeEnum.BOOLEAN.getName())) {
			jo.put("checked", "");
		}else if(type.equals(TpTypeEnum.ENUM.getName())) {
			jo.put("option", option);
			jo.put("selected", "");
		}
		return jo;
	}
	
	/**
	 * 更新模板属性
	 * @param request
	 * @param id
	 * @param option
	 * @param name
	 * @param type
	 * @param oldName
	 * @param oldType
	 * @param model
	 * @return
	 */
	@RequestMapping("updateTemplateAttr")
	@ResponseBody
	public String updateTemplateAttr(HttpServletRequest request,Integer id, String option, String name, String type, String oldName, String oldType, Model model) {
		 List<Template> templates = templateDao.findById(id);
		 if(templates.size() > 0) {
			 Template template = templates.get(0);
			 JSONArray ja = JSONArray.fromObject(template.getTp_data());
			 for(int i=0;i<ja.size();i++) {
				 //模板属性json对象
				 JSONObject jo = ja.getJSONObject(i);
				 if(jo.get("name").equals(oldName)) {
					 ja.remove(jo);
					 JSONObject newAttr = new JSONObject();
					 newAttr.put("name", name);
					 newAttr.put("type", type);
					 if(type.equals("enum")) {
						 newAttr.put("option", option);
					 }
					 ja.add(newAttr);
					 template.setTp_data(ja.toString());
					 templateDao.update(template);
					 updateTPCWhenUpdate(template.getTp_id(), name, type, oldName, oldType, option);
				 }
			 }
			 return "1";
		 }
		 return "0";
	}
	
	/**
	 * 更新模板属性时更新模板内容
	 * @param tpId
	 * @param name
	 * @param type
	 * @param oldName
	 * @param oldType
	 * @param option
	 */
	public void updateTPCWhenUpdate(int tpId, String name, String type, String oldName, String oldType, String option) {
		List<TPContent> tpcs = tpContentDao.findByTpId(tpId);
		if(tpcs.size() > 0) {
			for (TPContent tpContent : tpcs) {
				//模板内容json对象
				JSONObject jo = JSONObject.fromObject(tpContent.getTpc_content());
				        String value = "";
						if(!type.equals(oldType)) {
							value = "";
						}else {
							value = jo.getString(oldName); 	                        
						}
						jo.remove(oldName);
						jo.put(name, value);						
				tpContent.setTpc_content(jo.toString());
				tpContentDao.update(tpContent);
			}
		}
	}
}
