package creator;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.creator.db.permission.PermissionDao;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.user.UserDao;
import com.creator.db.userhandle.UserHandle;
import com.creator.db.userhandle.UserHandleDao;
import com.creator.db.value.Value;
import com.creator.db.value.ValueDao;

@RequestMapping("docking")
@Controller
public class DockingAction {

	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private TreeDao treeDao;
	@Autowired 
	private UserDao userDao;
	@Autowired
	private ValueDao valueDao;
	@Autowired
	private UserHandleDao userHandleDao;
	
	@RequestMapping(value="getDatas")
	public ResponseEntity<List<Map<String,Object>>> getDatas(HttpServletRequest request,String names,Model model) throws ParseException {
		if(null == names || names.equals("")) {
			return null;
		}
		String[] nameArray = names.split(",");	
		int count = 0;
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		searchValues(nameArray,0,count,result);
		//设置头部信息（防止ie下载json数据）
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("utf-8"));
		headers.setContentType(mediaType);
		return new ResponseEntity<List<Map<String,Object>>>(result,headers,HttpStatus.OK);
	}
	
	@RequestMapping(value="getData")
	public ResponseEntity<List<Map<String,Object>>> getData(HttpServletRequest request,String names,Model model) throws ParseException {
		if(null == names || names.equals("")) {
			return null;
		}
		String[] nameArray = names.split(",");	
		int count = 0;
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(nameArray.length >= 2) {
			searchValue(nameArray,0,count,result);
		}
		//设置头部信息（防止ie下载json数据）
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("utf-8"));
		headers.setContentType(mediaType);
		return new ResponseEntity<List<Map<String,Object>>>(result,headers,HttpStatus.OK);
	}
	
	public void searchValues(String[] nameArray,int pid,int count,List<Map<String,Object>> result) {
		
		List<Tree> trees = treeDao.queryByNameAndPid(nameArray[count++],pid);
		for (Tree tree : trees) {
			if(count == nameArray.length) {
				List<Value> values = valueDao.queryByNodeId(tree.getNode_id());
				for (Value value : values) {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put(value.getValue_key(), value.getValue_data());
					result.add(map);
				}
			}else {
				searchValues(nameArray,tree.getNode_id(),count,result);
			}
		}		
		
	}
	
public void searchValue(String[] nameArray,int pid,int count,List<Map<String,Object>> result) {
		
		List<Tree> trees = treeDao.queryByNameAndPid(nameArray[count++],pid);
		for (Tree tree : trees) {
			if(count == nameArray.length-1) {
				List<Value> values = valueDao.queryByNodeId(tree.getNode_id());
				for (Value value : values) {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put(value.getValue_key(), value.getValue_data());
					if(value.getValue_key().equals(nameArray[count])) {
						result.add(map);	
					}
				}
			}else {
				searchValue(nameArray,tree.getNode_id(),count,result);
			}
		}		
		
	}

@RequestMapping(value="getUserHandle")
public ResponseEntity<UserHandle> getUserHandle(HttpServletRequest request,Integer nodeId,Model model) throws ParseException {
	UserHandle result = userHandleDao.findByNodeId(nodeId);
	//设置头部信息（防止ie下载json数据）
	HttpHeaders headers = new HttpHeaders();
	MediaType mediaType = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("utf-8"));
	headers.setContentType(mediaType);
	return new ResponseEntity<UserHandle>(result,headers,HttpStatus.OK);
}

@RequestMapping(value="getValuesByNodeId")
public ResponseEntity<List<Value>> getValuesByNodeId(HttpServletRequest request,Integer nodeId,Model model) throws ParseException {
	List<Value> result = valueDao.queryByNodeId(nodeId);

	//设置头部信息（防止ie下载json数据）
	HttpHeaders headers = new HttpHeaders();
	MediaType mediaType = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("utf-8"));
	headers.setContentType(mediaType);
	return new ResponseEntity<List<Value>>(result,headers,HttpStatus.OK);
}

@RequestMapping(value="test")
public ResponseEntity<String> test(HttpServletRequest request,String value,Model model) throws ParseException {
    JSONObject j = JSONObject.fromObject(value);
    j.put("jie", "ge");
    value = j.toString();
	//设置头部信息（防止ie下载json数据）
	HttpHeaders headers = new HttpHeaders();
	MediaType mediaType = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("utf-8"));
	headers.setContentType(mediaType);
	return new ResponseEntity<String>(value,headers,HttpStatus.OK);
}

}
