package creator;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.api.CodeEnum;
import com.creator.api.CodeHelper;
import com.creator.api.OperationEnum;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.value.Value;
import com.creator.db.value.ValueDao;
import com.creator.util.ClientUtil;


/**
 * 云平台定制接口
 * 返回json数据格式规定为：{"code":"" , "msg":"" , "data":[]}
 * 注：code:状态码,String    	msg:状态码的文字说明,String   	data:数据,Array
 */
//解决跨域问题
@CrossOrigin(origins="*",maxAge=3600)
@Controller
@RequestMapping(value="cloud")
public class CloudAction {
	private final static String CLASS_NAME = CloudAction.class.getName() + ": ";
	
	@Autowired
	private TreeDao treeDao;
	
	@Autowired
	private ValueDao valueDao;
	
	
	/**
	 * 根据节点和日期获取当天的数据列表，如果传输方式为协议传输，则必须传递key参数
	 * @param id 节点id
	 * @param key 可选参数，如果传输方式为透传，则不需要key;如果传输方式为协议传输，则必须传递该参数
	 * @param date 可选参数，如果为null，则日期为当天日期，格式为yyyy-MM-dd
	 * @return
	 * 示例：{"code":"0000","msg":"成功","data":[{"value":["2018-03-28 11:07:10","28.3"]},{"value":["2018-03-28 12:07:10","20.3"]},{"value":["2018-03-28 13:07:10","21.3"]}]}
	 */
	@RequestMapping(value="getValueDataByDate",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getValueDataByDate(String id,String key,String date) {
		System.out.println(CLASS_NAME + "getDataByDate" + " param:" + id + " " + key + " " + date);
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(date == null) {
			date = dateFormat.format(new Date());
		}else if(!ClientUtil.isValidDate(date, "yyyy-MM-dd")) {
			//判断时间格式是否符合要求
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.ERROR_DATE_FORMAT);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		List<Tree> nodeList = treeDao.queryById(nId);
		int transType = nodeList.get(0).getNode_tstype();
		
		List<Value> valueList =  valueDao.queryBetweenDateByNodeId(nId, date, date);
		List<Map<String,Object>> list = null;
		//协议传输，检查key是否存在
		if(transType == ClientAction.TRANS_PROTOCOL) {
			if(key == null ) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
				return jsonObject.toString();
			}
			list = ClientUtil.getValueJsonData(valueList, key);
		}else {
			//透传
			list = ClientUtil.getValueData(valueList);
		}
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();    //结果
		for(Map<String,Object> valueMap : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<String> list1 = new ArrayList<String>();
			list1.add(String.valueOf(valueMap.get("datetime")));
			list1.add(String.valueOf(valueMap.get("data")));
			map.put("value", list1);
			resultList.add(map);
		}
		jsonObject.put("data", resultList);
		System.out.println(CLASS_NAME + jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 根据操作码统计数据（总量、平均值、最大值、最小值、极差）
	 * @param id 节点id
	 * @param key 要查询的数据对应的键(注：如果数据传输方式是透传，则可以不加该参数,传输方式为协议传输，则必须加上)
	 * @param opcode
	 * @return 统计结果json字符串
	 * opcode: 操作码    0001-平均值    0002-总量  0003-最大值   0004-最小值    0005-极差
	 * opmsg: 操作码说明
	 * result: 统计结果
	 */
	@RequestMapping(value="getStatisticsData",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String getStatisticsData(String id,String key,String opcode) {
		System.out.println(CLASS_NAME + "getStatisticsData" + " param:" + id + " " + key + " " + opcode);
		//检查id是否有效
		CodeEnum code = CodeHelper.String2Number(id);
		JSONObject jsonObject = ClientUtil.getCodeJSON(code);
		if(code != CodeEnum.SUCCESS) {
			return jsonObject.toString();
		}
		
		if(opcode == null) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.INCOMPLETE_PARAM);
			return jsonObject.toString();
		}
		//判断节点是否存在
		int nId = Integer.valueOf(id);
		if(!treeDao.isExistNode(nId)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.NODE_NOT_EXIST);
			return jsonObject.toString();
		}
		//判断操作码是否存在
		opcode = opcode.trim();
		if(!CodeHelper.isExistOpCode(opcode)) {
			jsonObject = ClientUtil.getCodeJSON(CodeEnum.OPCODE_NOT_EXIST);
			return jsonObject.toString();
		}
		
		List<Tree> treeList = treeDao.queryById(nId);
		//判断传输方式
		int tstype = treeList.get(0).getNode_tstype();
		if(tstype == ClientAction.TRANS_PROTOCOL) {
			//协议传输
			//判断键是否存在
			String protocol = treeList.get(0).getNode_protocol();
			if(!ClientUtil.isExistKey(protocol, key)) {
				jsonObject = ClientUtil.getCodeJSON(CodeEnum.KEY_NOT_EXIST);
				return jsonObject.toString();
			}
		}
		
		//根据节点id查询所有数据
		List<Value> valueList = valueDao.queryByNodeId(nId);
		List<Map<String,Object>> dataList = null;
		if(tstype == ClientAction.TRANS_PROTOCOL) {
			//协议传输
			dataList = ClientUtil.getValueJsonData(valueList, key);
		} else {
			//透传
			dataList = ClientUtil.getValueData(valueList);
		}
		//结果
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("opcode", opcode);
		result.put("opmsg", CodeHelper.getEnumByCode(opcode).getMsg());
		result.put("result", 0);
		resultList.add(result);
		
		//数据保留两位小数
		DecimalFormat df = new DecimalFormat("0.00");
		//根据操作码对数据操作
		if(OperationEnum.AVERAGE.getCode().equals(opcode)) {
			//求平均值
			double count = 0;
			for(int i=0;i<dataList.size();i++) {
				count += (Double)dataList.get(i).get("data");
			}
			double average = 0;
			if(dataList.size() > 0) {
				average = count / dataList.size();
			}
			System.out.println(CLASS_NAME + "average:" + average);
			result.put("result", df.format(average));
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
			
		}
		
		//求总量
		if(OperationEnum.TOTAL.getCode().equals(opcode)) {
			double count = 0;
			for(int i=0;i<dataList.size();i++) {
				count += (Double)dataList.get(i).get("data");
			}
			result.put("result", df.format(count));
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//求最大值和最小值，以及对应的value_id
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		int maxId = 0;
		int minId = 0;
		for(int i=0;i<dataList.size();i++) {
			double data = (Double)dataList.get(i).get("data");
			if(data >= max) {
				max = data;
				maxId = (Integer)dataList.get(i).get("id");
			}
			if(data <= min) {
				min = data;
				minId = (Integer)dataList.get(i).get("id");
			}
		}
		//求最大值
		if(OperationEnum.MAX.getCode().equals(opcode)) {
			if(maxId == 0) {
				max = 0;
			}
			result.put("result", df.format(max));
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//求最小值
		if(OperationEnum.MIN.getCode().equals(opcode)) {
			if(minId == 0) {
				min = 0;
			}
			result.put("result", df.format(min));
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		
		//求极差
		if(OperationEnum.RANGE.getCode().equals(opcode)) {
			double range = 0;
			if(maxId != 0 && minId != 0) {
				range = max - min;
			}
			result.put("result", df.format(range));
			jsonObject.put("data", resultList);
			System.out.println(CLASS_NAME + jsonObject.toString());
			return jsonObject.toString();
		}
		return jsonObject.toString();
	}
}
