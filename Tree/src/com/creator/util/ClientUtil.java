package com.creator.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.creator.api.CodeEnum;
import com.creator.db.groupPermission.UserGroupPermissionDao;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.type.TypeEnum;
import com.creator.db.user.User;
import com.creator.db.user.UserDao;
import com.creator.db.value.Value;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 客户端接口相关工具类
 *
 */
public class ClientUtil {
	private static TreeDao treeDao;
	private static UserGroupPermissionDao userGroupPermissionDao;
	private static UserDao userDao;
	
	static {
		ApplicationContext context = new ClassPathXmlApplicationContext("Mysql_bean.xml");
		treeDao = (TreeDao) context.getBean("treeDao");
		userGroupPermissionDao = (UserGroupPermissionDao) context.getBean("userGroupPermissionDao");
		userDao = (UserDao) context.getBean("userDao");
		
	}
	/**
	 * 将状态码转成json
	 * @param code
	 * @return json对象,格式：{"code":"" , "msg":"" , "data":[] },数据为空
	 */
	public static JSONObject getCodeJSON(CodeEnum code) {
		JSONObject jsonObject = new JSONObject();
		if(code != null) {
			jsonObject.put("code", code.getCode());
			jsonObject.put("msg", code.getMsg());
			jsonObject.put("data", new ArrayList<Map<String, Object>>());
		}
		return jsonObject;
	}
	
	/**
	 * 获取节点所有后代
	 * @param id 节点id
	 * @return
	 */
	public static List<Map<String,Object>> getDescendantById(int id) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		getChilren(id, list);
		return list;
	}
	
	/*
	 * 递归获得子节点列表
	 */
	private static void getChilren(int id,List<Map<String,Object>> list) {
		List<Map<String,Object>> nodeList = treeDao.queryChilrenByNodeId(id);
		
		if(list == null || nodeList.size() <= 0) {
			return ;
		}
		list.addAll(nodeList);
		for(int i=0;i<nodeList.size();i++) {
			int nodeId = ((Long) nodeList.get(i).get("node_id")).intValue();
//			System.out.println("nodeId:" + nodeId);
			getChilren(nodeId,list);
		}
	}
	
	/**
	 * 获取节点的所有会议后代
	 * @param id
	 * @return
	 */
	public static List<Map<String,Object>> getMeetingDescendantById(int id) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		getMeetingChildren(id, list);
		return list;
	}
	
	private static void getMeetingChildren(int id,List<Map<String,Object>> list) {
		List<Map<String,Object>> nodeList = treeDao.queryMeetingChildrenById(id);
		
		if(list == null || nodeList.size() <= 0) {
			return ;
		}
		list.addAll(nodeList);
		for(int i=0;i<nodeList.size();i++) {
			int nodeId = ((Long) nodeList.get(i).get("node_id")).intValue();
//			System.out.println("nodeId:" + nodeId);
			getMeetingChildren(nodeId,list);
		}
	}
	
	/**
	 * 判断字符串是否符合日期格式
	 * @param date 日期字符串
	 * @param format 指定的格式
	 * @return
	 */
	public static boolean isValidDate(String date,String format) {
		if(date == null) {
			return false;
		}
		DateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setLenient(false);      //设置严格的验证方式
		try {
			dateFormat.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 将日期字符格式化成标准的日期格式，比如2018-3-14 转成  2018-03-14
	 * @param strDate
	 * @return
	 */
	public static String formatDate(String strDate,String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		String formatDate = "";
		try {
			Date date = dateFormat.parse(strDate.trim());
			formatDate = dateFormat.format(date);
		} catch (ParseException e) {
			return "";
		}
		return formatDate;
	}
	
	/**
	 * 判断两个日期字符串所对应的日期是否相等
	 * @param strDate1
	 * @param strDate2
	 * @return
	 */
	public static boolean isDateEquale(String strDate1,String strDate2) {
		if(strDate1 == null || strDate2 == null) {
			return false;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = dateFormat.parse(strDate1);
			Date date2 = dateFormat.parse(strDate2);
			if(date1.getTime() != date2.getTime()) {
				return false;
			}
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 判断比较者是否在开始时间和结束时间之间
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param compare 比较时间
	 * @param format 指定格式
	 * @return
	 */
	public static boolean isBetweenDates(String begin,String end,String compare,String format) {
		if(begin == null || end == null || compare == null || format == null) {
			return false;
		}
		
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			long beginTime = dateFormat.parse(begin).getTime();
			long endTime = dateFormat.parse(end).getTime();
			long compareTime = dateFormat.parse(compare).getTime();
			
			if(compareTime >= beginTime  && compareTime <= endTime) {
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
		
		return false;
		
	}
	
	/**
	 * 比较两个日期字符串的大小关系
	 * @param strDate1 
	 * @param strDate2
	 * @param format 指定的格式
	 * @return 0 相等     1 第一个日期大于第二个日期    -1 第一个日期小于第二个日期     2 出错
	 */
	public static int compareDate(String strDate1,String strDate2,String format) {
		if(strDate1 == null || strDate2 == null) {
			return 2;
		}
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			Date date1 = dateFormat.parse(strDate1);
			Date date2 = dateFormat.parse(strDate2);
			if(date1.getTime() > date2.getTime()) {
				return 1;
			}else if(date1.getTime() < date2.getTime()) {
				return -1;
			}
		} catch (ParseException e) {
			return 2;
		}
		
		return 0;
	}
	
	
	/**
	 * 获取指定日期的相对日期
	 * @param strDate 指定日期字符串
	 * @param offset 日期偏移量  -1、0、1分别代表昨天、今天 、明天，以此类推
	 * @param format 指定的日期格式
	 * @return
	 */
	public static String getDateRelatively(String strDate,int offset,String format) {
		if(strDate == null || format == null) {
			return "";
		}
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			Date date = dateFormat.parse(strDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
			calendar.set(Calendar.DAY_OF_YEAR, dayOfYear + offset);
			return dateFormat.format(calendar.getTime());
		} catch (ParseException e) {
			return "";
		}
		
	}
	
	/**
	 * 根据节点id查询父级设备(包括自身)
	 * @param nodeId 节点id
	 * @return
	 */
	public static List<Map<String,Object>> getDeviceParent(int nodeId) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		getDevice(nodeId, list);
		return list;
	}
	
	/*
	 * 递归获取父级设备
	 */
	private static void getDevice(int nodeId,List<Map<String,Object>> list) {
		List<Map<String,Object>> nodeList = treeDao.queryNodeByNodeId(nodeId);
//		System.out.println("size:" + nodeList.size());
		if(list == null || nodeList.size() <= 0) {
			return;
		}
		int type = (Integer) nodeList.get(0).get("node_type");
		System.out.println("id:" + nodeList.get(0).get("node_id"));
		if(type ==  TypeEnum.EQUIPMENT.getId()) {
			list.addAll(nodeList);
			return;
		}
		
		getDevice((Integer)nodeList.get(0).get("node_pid"), list);
		
	}
	
	/**
	 * 根据节点id获取父节点(包括自身),顺序从顶层到底
	 * @param nodeId
	 * @return
	 */
	public static List<Map<String,Object>> getParentList(int nodeId) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		getParent(nodeId, list);
		return list;
	}
	
	/*
	 * 递归获取父节点
	 */
	private static void getParent(int nodeId,List<Map<String,Object>> list) {
		//获取父节点
		List<Map<String,Object>> parentList = treeDao.queryNodeByNodeId(nodeId);
		if(list == null || parentList.size() <= 0) {
			return ;
		}
		list.addAll(0,parentList);
		int parentId = (Integer)parentList.get(0).get("node_pid");
		System.out.println("parentId:" + parentId);
		getParent(parentId, list);
	}
	
	/**
	 * 根据节点id获取该节点对应的完整名称
	 * @param nodeId
	 * @return
	 */
	public static String getNodeFullName(int nodeId) {
		List<Map<String,Object>> nodeList = getParentList(nodeId);
		StringBuffer result = new StringBuffer();
		for(int i=0;i<nodeList.size();i++) {
			result.append(nodeList.get(i).get("node_name") + " ");
		}
		return result.toString();
	}
	
	
	/**
	 * 根据节点id和类名称获取符合类名的后代设备列表(包括普通树节点)
	 * @param nodeId
	 * @param className
	 * @return
	 */
	public static List<Map<String,Object>> getDescendantsByClass(int nodeId,String className) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		getChildrenByClass(nodeId, className, list);
		return list;
	}
	
	/*
	 * 递归获取指定类名设备
	 */
	private static void getChildrenByClass(int nodeId,String className,List<Map<String,Object>> list) {
		List<Map<String,Object>> childrenList = treeDao.queryChildrenByClassNameGroup(nodeId, className);
//		System.out.println("size:" + childrenList.size());
		if(list == null || childrenList.size() <= 0) {
			return ;
		}
		list.addAll(childrenList);
		for(int i=0; i<childrenList.size(); i++) {
			int id = ((Long)childrenList.get(i).get("node_id")).intValue();
			getChildrenByClass(id, className, list);
		}
	}
	
	/**
	 * 根据节点id和类名称获取符合类名的后代节点，找到第一层则不往下寻找(包括普通树节点)
	 * @param nodeId 节点id
	 * @param className 类名称
	 * @return
	 */
	public static List<Map<String,Object>> getDescendantsToFirstByClass(int nodeId,String className) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		getChilrenToFirstByClass(nodeId, className, list);
		return list;
	}
	
	/*
	 * 递归获取指定类名子类
	 */
	private static void getChilrenToFirstByClass(int nodeId,String className,List<Map<String,Object>> list) {
		List<Map<String,Object>> childrenList = treeDao.queryChildrenByClassNameGroup(nodeId, className);
	//	System.out.println("size:" + childrenList.size());
		if(list == null || childrenList.size() <= 0) {
			return ;
		}
		list.addAll(childrenList);
		for(int i=0; i<childrenList.size(); i++) {
			//判断类名称是否指定名称，是则不往下遍历
//			System.out.println(childrenList.get(i).get("node_class"));
			if(className.equals(childrenList.get(i).get("node_class"))) {
				continue;
			}
			int id = ((Long)childrenList.get(i).get("node_id")).intValue();
			getChilrenToFirstByClass(id, className, list);
		}
	}
	
	/**
	 * 根据节点id和类型获取指定类型节点和普通节点(不包括自身)
	 * @param nodeId
	 * @param type
	 * @return
	 */
	public static List<Map<String,Object>> getDescentantsByType(int nodeId,int type) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		getChildrenByType(nodeId, type, list);
		return list;
	}
	
	/*
	 * 递归获取指定类型的子节点
	 */
	private static void getChildrenByType(int nodeId,int type,List<Map<String,Object>> list) {
		List<Map<String,Object>> children = treeDao.queryChildrenByNodeTypeOrNormal(nodeId, type);
		if(list == null || children.size() <= 0) {
			return ;
		}
		list.addAll(children);
		for(int i=0;i<children.size();i++) {
			
			int id = ((Long)children.get(i).get("node_id")).intValue();
//			System.out.println("id:" + id);
			getChildrenByType(id, type, list);
		}
	}
	
	/**
	 * 根据节点id和类型获取指定类型节点和普通节点，但到达指定类型的第一层就停止往下遍历(不包括自身)
	 * @param nodeId
	 * @param type
	 * @return
	 */
	public static List<Map<String,Object>> getDescentantsToFirstByType(int nodeId,int type) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		getChildrenToFirstByType(nodeId, type, list);
		return list;
	}
	
	/*
	 * 递归获取指定类型的子节点，到达指定类型第一层就停止
	 */
	private static void getChildrenToFirstByType(int nodeId,int type,List<Map<String,Object>> list) {
		List<Map<String,Object>> children = treeDao.queryChildrenByNodeTypeOrNormal(nodeId, type);
		if(list == null || children.size() <= 0) {
			return ;
		}
		list.addAll(children);
		for(int i=0;i<children.size();i++) {
			if((Integer)children.get(i).get("node_type") == type) {
				continue;
			}
			int id = ((Long)children.get(i).get("node_id")).intValue();
//			System.out.println("id:" + id);
			getChildrenToFirstByType(id, type, list);
		}
	}
	
	public static List<Map<String,Object>> getPermissionDescentantsByNodeId(int nodeid,int groupid,int permission) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		getPermissionChildren(nodeid, groupid, permission, list);
		return list;
	}
	
	public static void getPermissionChildren(int nodeid,int groupid,int permission,List<Map<String,Object>> list) {
		if(list == null) {
			return;
		}
		List<Map<String,Object>> nodeList = treeDao.queryChilrenByNodeId(nodeid);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<nodeList.size();i++) {
			int id = ((Long)nodeList.get(0).get("node_id")).intValue();
			//查询是否有权限
			if(userGroupPermissionDao.hasPermissionByGroupIdAndNodeId(groupid, id, permission)) {
				resultList.add(nodeList.get(i));
			}
		}
		if(resultList.size() <= 0) {
			return;
		}
		
		list.addAll(0, resultList);
		for(int i=0;i<resultList.size();i++) {
			int id = ((Long)resultList.get(0).get("node_id")).intValue();
			getPermissionChildren(id, groupid, permission, list);
		}
	}
	
	
	/**
	 * 文件上传通用方法
	 * @param request request请求
	 * @param filePath  文件夹名称，在public/upload里的文件夹
	 * @param filePrefix  文件名称前缀，比如user_ ,完整文件名称为 user_ + 时间戳 + 后缀名
	 * @return 有文件上传，返回文件路径(截取public后面的路径),没有文件上传，则返回null
	 * @throws Exception
	 */
	public static String fileUpload(HttpServletRequest request,String filePath,String filePrefix) throws Exception {
		//图片上传
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
		String Path = request.getServletContext().getRealPath("");
		String BasePath = Path + "/WEB-INF/views/public/upload/"+ filePath +"/";
		
		//文件夹名称
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dirFilePath = BasePath + dateFormat.format(new Date());
	//	System.out.println("路径：" + BasePath);
//		System.out.println("文件夹路径：" +dirFilePath);
		String FilePath = null;
		System.out.println("filePath:" + filePath);
		if(multipartResolver.isMultipart(request)){
			//判断当前文件夹是否存在，不存在则创建
			File dirFile = new File(dirFilePath);
			if(!dirFile.exists() || !dirFile.isDirectory()) {
				boolean isCreate = dirFile.mkdir();
				if(!isCreate) {
					return null;
				}
			}
			dirFilePath += "/";
			
			 MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
			 Iterator<String> iter=multiRequest.getFileNames();
			 String suffix = "";
			 
			 while(iter.hasNext()){
				 MultipartFile file=multiRequest.getFile(iter.next());
				 if(file!=null && file.getOriginalFilename()!=""){
					 	//判断是否有后缀名
	                	if(file.getOriginalFilename().contains(".")) {
	                		suffix = "."+file.getOriginalFilename().split("\\.")[(file.getOriginalFilename().split("\\.")).length-1];
	                	}
	                    String path = dirFilePath + filePrefix + System.currentTimeMillis() + suffix;
	                    System.out.println("图片路径：" + path);
	                    //上传
	                	FilePath = "upload"+path.split("upload")[1];
	                    file.transferTo(new File(path));
	                }
			 }
		}
//		System.out.println("FilePath:" + FilePath);
		return FilePath;
	}
	
	/**
	 * 根据日期获取该日期后七天的日期(包括该日期)
	 * @param date  日期(yyyy-MM-dd)
	 * @return 后七天的日期列表(包括传入的日期)
	 */
	public static List<Date> getAfterWeek(Date date) {
		List<Date> dateList = new ArrayList<Date>();
		int dayNum = 7;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		for(int i=0;i<dayNum;i++) {
			calendar.set(Calendar.DAY_OF_YEAR, dayOfYear + i);
			dateList.add(calendar.getTime());
		}
		return dateList;
	}
	
	/**
	 * 根据日期字符串获取该日期后七天的日期和星期索引
	 * @param strDate 日期字符串
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,Object>> getAfterWeekWithWeek(String strDate) throws Exception {
		List<Map<String,Object>> dateList = new ArrayList<Map<String,Object>>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//字符串转日期
		Date date = dateFormat.parse(strDate);
		List<Date> list = getAfterWeek(date);
		Calendar calendar = Calendar.getInstance();
		for(int i=0;i<list.size();i++) {
			Map<String,Object> map = new HashMap<String, Object>();
			calendar.setTime(list.get(i));
			map.put("date", dateFormat.format(list.get(i)));
			map.put("week", calendar.get(Calendar.DAY_OF_WEEK) - 1);
			dateList.add(map);
//			System.out.println(map);
		}
		return dateList;
	}
	
	
	/**
	 * 根据日期字符串获取该日期对应的星期日期列表
	 * @param str 日期字符串
	 * @return
	 * @throws Exception
	 */
	public static List<String> getWeekDays(String str) throws Exception {
		List<String> weekList = new ArrayList<String>();
		if(str == null) {
			return weekList;
		}
		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse(str);
		calendar.setTime(date);
		int dayOfWeek = calendar.get(calendar.DAY_OF_WEEK);
		for(int i=dayOfWeek-1;i>=0;i--) {
			calendar.add(Calendar.DATE, -i);
			weekList.add(df.format(calendar.getTime()));
			calendar.setTime(date);
		}
		
		for(int i=1;i<=7-dayOfWeek;i++) {
			calendar.add(Calendar.DATE, i);
			weekList.add(df.format(calendar.getTime()));
			calendar.setTime(date);
		}
		
		return weekList;
	}
	
	/**
	 * 添加日志到指定文件夹，以yyyy-MM-dd.txt命名日志文件
	 * @param path
	 * @param text
	 * @return
	 */
	public static boolean appendLog(String path,String text) {
		if(path == null || text == null) {
			return false;
		}
		File file = new File(path);
		if(!file.exists()) {
			return false;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String filename = dateFormat.format(new Date());
		String fullPath = path + filename + ".txt";
		return writeTextToFile(fullPath, text);
	}
	
	/**
	 * 以追加的形式在指定路径文本追加文本
	 * @param path 文件路径
	 * @param text 要追加的文本内容
	 * @return
	 */
	public static boolean writeTextToFile(String path,String text) {
		if(path == null || text == null) {
			return false;
		}
		File file = new File(path);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}
		BufferedWriter  writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file, true));     //追加
			writer.write(text + "\r\n");   //追加内容并换行
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true; 
	}
	
	/**
	 * 判断字符串是否符合json格式
	 * @param json 字符串
	 * @param flag 0为json对象  1为json数组
	 * @return
	 */
	public static boolean isValidJson(String json,int flag) {
		if(json == null) {
			return false;
		}
		try {
			if(flag == 0) {
				JSONObject.fromObject(json);
			}else if(flag == 1) {
				JSONArray.fromObject(json);
			} else {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
		return true;
 	}
	
	
	/**
	 * 查询场景列表中的action、node详细信息，填入原来的列表中
	 * @param sceneList
	 */
	public static void putDetialToSceneList(List<Map<String,Object>> sceneList) {
		for(int i=0;i<sceneList.size();i++) {
			Map<String,Object> scene = sceneList.get(i);
			//动作列表
			String sc_action = (String) scene.get("sc_action");
			List<Map<String,Object>> actionList = new ArrayList<Map<String,Object>>();
			if(sc_action != null && !("").equals(sc_action)) {
				String[] actions = sc_action.split(",");
				for(int j=0;j<actions.length;j++) {
					actionList.addAll(treeDao.queryNodeByNodeId(Integer.valueOf(actions[j])));
				}
			}
			scene.put("action", actionList);
			//节点列表
			String sc_nodeid = (String) scene.get("sc_nodeid");
			List<Map<String,Object>> nodeList = new ArrayList<Map<String,Object>>();
			if(sc_nodeid != null && !("").equals(sc_nodeid)) {
				String[] nodes = sc_nodeid.split(",");
				for(int j=0;j<nodes.length;j++) {
					nodeList.addAll(treeDao.queryNodeByNodeId(Integer.valueOf(nodes[j])));
				}
			}
			scene.put("node", nodeList);
		}
	}
	
	/**
	 * 判断字符串是否符合xx,xx,xx格式，例如1,3,4,5
	 * @param str
	 * @return
	 */
	public static boolean isValidIdGroup(String str) {
		if(str == null) {
			return false;
		}
		if(str.endsWith(",")) {
			return false;
		}
		String pattern = "^([0-9]+[,]{0,1})*$";
		if(!str.matches(pattern)) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 将xx,xx格式字符串中的数字提取出来，组成新的字符串,如admin_1,1,3,admin 组成  1,3
	 * @param str
	 * @return
	 */
	public static String getNumberString(String str) {
		if(str == null || ("").equals(str)) {
			return "";
		}
		String[] strNums = str.split(",");
		String result = "";
		for(int i=0;i<strNums.length;i++) {
			if(NumberUtils.isNumber(strNums[i])) {
				result += strNums[i] + ",";
			}
		}
		//判断是否包含","
		if(result.length() != 0) {
			//去除最后一个","
			result = result.substring(0, result.length()-1);
		}
		return result;
	}
	
	/**
	 * 根据用户id、节点id查询该用户是否拥有该节点的某个权限
	 * @param nodeId
	 * @param userId
	 * @param permissionId
	 * @return
	 */
	public static boolean hasPermissionByNodeId(int nodeId,int userId,int permissionId) {
		//判断该树是否该用户创建
		List<Tree> treeList = treeDao.queryById(nodeId);
		if(treeList.size() <= 0) {
			return false;
		}
		if(treeList.get(0).getNode_userid() == userId) {
			return true;
		}
		//根据用户查询用户组
		List<User> userList = userDao.queryById(userId);
		if(userList.size() <= 0) {
			return false;
		}
		return userGroupPermissionDao.hasPermissionByGroupIdAndNodeId(userList.get(0).getUr_group(), nodeId, permissionId);
	}
	
	
	/**
	 * 判断json字符串判断是否包含想要的协议键
	 * @param json
	 * @param key
	 * @return
	 */
	public static boolean isExistKey(String json,String key) {
		if(json == null || key == null) {
			return false;
		}
		try {
			JSONArray jsonArray = JSONArray.fromObject(json);
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if(jsonObject.getString("identification").equals(key)) {
					return true;
				}
			}
		}catch(Exception e) {
			return false;
		}
		return false;
	}
	
	
	/**
	 * 根据键值获取节点数据中对应的数字，存入list中
	 * @param list 节点数据list
	 * @param key 键值
	 * @return map中键对应value的id,值对应查找的键的数值
	 */
	public static List<Map<String,Object>> getValueJsonData(List<Value> list,String key) {
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		if(list == null || key == null) {
			return dataList;
		}
		for(int i=0;i<list.size();i++) {
			String value = list.get(i).getValue_data();
			if(isValidJson(value,0)) {
				//获取对应的键值
				JSONObject jsonObject = JSONObject.fromObject(value);
				Object object =  jsonObject.get(key);
				String data = "";
				if(object != null) {
					data = String.valueOf(object);
				}
				if(NumberUtils.isNumber(data)) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", list.get(i).getValue_id());
					map.put("data", Double.valueOf(data));
					map.put("datetime", list.get(i).getValue_datetime());
					dataList.add(map);
				}
			}
		}
		return dataList;
	}
	
	/**
	 * 将节点数据中的是数字的值存入list
	 * @param list
	 * @return
	 */
	public static List<Map<String,Object>> getValueData(List<Value> list) {
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		if(list == null) {
			return dataList;
		}
		for(int i=0;i<list.size();i++) {
			String value = list.get(i).getValue_data();
			if(NumberUtils.isNumber(value)) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", list.get(i).getValue_id());
				map.put("data", Double.valueOf(value));
				map.put("datetime", list.get(i).getValue_datetime());
				dataList.add(map);
			}
		}
		return dataList;
	}
	
	/**
	 * 将字符串转换成json数组，如果字符串不符合格式，则返回Null
	 * @param json 字符串
	 * @return
	 */
	public static JSONArray getArrayFromString(String json) {
		JSONArray jsonArray = null;
		try {
			jsonArray = JSONArray.fromObject(json);
		} catch(Exception e) {
			return null;
		}
		return jsonArray;
	}
}
