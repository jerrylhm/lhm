package creator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.api.CodeEnum;
import com.creator.api.CodeHelper;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.value.Value;
import com.creator.db.value.ValueDao;
import com.creator.util.ClientUtil;

/**
 * 设备接口，负责接收并设备传过来的数据
 * 返回json数据格式规定为：{"code":"" , "msg":"" , "data":[]}
 * 注：code:状态码    	msg:状态码的文字说明   	data:数据
 *
 */
//解决跨域问题
@CrossOrigin(origins="*",maxAge=3600)
@Controller
@RequestMapping(value="device")
public class DeviceAction {
	private final static String CLASS_NAME = DeviceAction.class.getName() + "：";     //类名，便于打印时区分信息
	
	@Autowired
	private TreeDao treeDao;
	
	@Autowired
	private ValueDao valueDao;
	
	
	/**
	 * 接收设备发送的数据，不成功则将错误信息返回给设备，成功则将信息传给客户端
	 * 根据发送数据的内容，如果value是json格式，则根据value的键与sn相同的设备及其子节点的协议进行对比，匹配则代表该节点是要寻找的节点
	 * 如果value不是json格式，则查找与SN匹配的节点，将数据直接保存给它
	 * @param sn   设备sn号
	 * @param value  设备发送过来的内容（不做解析）
	 * @return
	 * json参数说明 
	 * 不成功，发送给设备：{"code":String,"msg":String,"data":Array,"type":String,"sn":String}
	 * code:标识码,0000为成功         msg:标识码的中文说明    data:数据     type:设备标识，值为1  sn:设备sn号
	 * 示例：{"code":"0003","msg":"参数不完整","data":[],"type":"1","sn":"123897"}
	 * 
	 * 成功，发送给客户端：{"code":String,"msg":String,"data":Array,"type":String,"sn":String , "id": int}
	 * code:标识码,0000为成功         msg:标识码的中文说明    data:数据，格式为{"id":int , "value":String}     type:客户端标识，值为0  sn:设备sn号  id:节点id
	 * 示例：{"code":"0000","msg":"成功","data":[{"id":152,"value":"{\"time\":\"88\",\"state\":\"1\"}"}],"type":"0","sn":"123","id" : 152}
	 */
	@RequestMapping(value="sendValue",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String sendValue(String sn,String value) {
		System.out.println(CLASS_NAME + "sendValue() " + "  param:" + sn + " " + value);
		CodeEnum code = CodeHelper.isStringsNull(sn,value);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		jsonObject.put("type", "1");                 //1代表传给设备
		jsonObject.put("sn", sn);
		
		if(code != CodeEnum.SUCCESS) {
			System.out.println(CLASS_NAME + jsonObject.toString());
			jsonObject.put("type", "1");                 //1代表传给设备
			jsonObject.put("sn", sn);
			return jsonObject.toString();
		}
		//查找sn的节点id
		List<Tree> nodeList = treeDao.queryBySn(sn);
		if(nodeList.size() <= 0) {
			//未查找到sn相对应的节点
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.SN_NOT_EXIST);
			jsonObject.put("type", "1");                 //1代表传给设备
			jsonObject.put("sn", sn);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		Tree node = nodeList.get(0);    //查找到的节点
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = dateFormat.format(new Date());
		if(!ClientUtil.isValidJson(value,0)) {
			//透传数据，直接在sn对应的节点添加数据
			Value nodeValue = new Value();
			nodeValue.setValue_nodeid(node.getNode_id());
			nodeValue.setValue_data(value);
			nodeValue.setValue_datetime(datetime);
			valueDao.addValue(nodeValue);
			
			//返回json数据给客户端
			jsonObject.put("type", "0");
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", node.getNode_id());
			map.put("value", value);
			resultList.add(map);
			jsonObject.put("id", node.getNode_id());
			jsonObject.put("data",resultList);
			
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//协议数据
		/*
		if(!ClientUtil.isValidJson(value)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_PARAM_TYPE);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		*/
		//查找设备子节点
		JSONObject valueJSON = JSONObject.fromObject(value);
		Set<String> keySet = valueJSON.keySet();
		//判断是否有数据
		if(valueJSON.size() == 0) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_VALUE);
			jsonObject.put("type", "1");                 //1代表传给设备
			jsonObject.put("sn", sn);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		List<Tree> childList = treeDao.querySonNode(node.getNode_id());
		childList.add(0, node);                    //将该节点本身也添加进去
		Tree childFind = null; 
		for(Tree child: childList) {
			//协议传输类型，透传类型节点直接忽略
			if(child.getNode_tstype() == ClientAction.TRANS_PROTOCOL) {
				String protocol = child.getNode_protocol();
				if(ClientUtil.isValidJson(protocol,1)) {
					JSONArray jsonArray = JSONArray.fromObject(protocol);
					int matchNum = 0;     //键匹配成功个数
					for(String key : keySet) {
						boolean isExistKey = false;     //是否存在key
						for(int i=0;i<jsonArray.size();i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							if(key.equals(object.get("identification"))) {
								isExistKey = true;
								break;
							}
						}
						if(isExistKey) {
							matchNum ++;           //有相应的key，则匹配数量加1
						}
					}
					System.out.println(CLASS_NAME + "matchNum:" + matchNum + "  valuesize：" + valueJSON.size());
					//协议匹配所有的键，则代表该子节点是要寻找的
					if(matchNum >= valueJSON.size()) {
						childFind = child;
						break;
					}
				}
				
			}
		}
		if(childFind == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.CHILD_NOT_EXIST);
			jsonObject.put("type", "1");                 //1代表传给设备
			jsonObject.put("sn", sn);
			System.out.println(CLASS_NAME + jsonObject);
			return jsonObject.toString();
		}
		
		Value nodeValue = new Value();
		nodeValue.setValue_nodeid(childFind.getNode_id());
		nodeValue.setValue_data(value);
		nodeValue.setValue_datetime(datetime);
		valueDao.addValue(nodeValue);
		//返回json数据给客户端
		jsonObject.put("type", "0");
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", childFind.getNode_id());
		map.put("value", value);
		resultList.add(map);
		jsonObject.put("data",resultList);
		jsonObject.put("id", childFind.getNode_id());
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
}
