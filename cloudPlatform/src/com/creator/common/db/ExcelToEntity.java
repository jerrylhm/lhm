package com.creator.common.db;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.creator.annotation.ExcelMapping;

/**
 * Excel实例化类
 * @author ilongli
 *
 */
public class ExcelToEntity {
	
	private static Logger logger = Logger.getLogger(ExcelToEntity.class);
	
	//字段数据映射map集合
	private Map<String, Map<String, String>> attrsMap = null;
	
	//自定义数据处理接口
	private ExcelDataHandler edh = null;
	
	public <T> ExcelDataResult<T> excelToEntity(Class<T> clazz, List<String[]> readExcel) throws Exception {	
		return handleHead(clazz, readExcel);
	}
	
	public <T> ExcelDataResult<T> excelToEntity(Class<T> clazz, List<String[]> readExcel, ExcelDataHandler edh) throws Exception {	
		this.edh = edh;
		return handleHead(clazz, readExcel);
	}
	
	public <T> ExcelDataResult<T> excelToEntity(Class<T> clazz, List<String[]> readExcel, Map<String, Map<String, String>> attrsMap) throws Exception {	
		this.attrsMap = attrsMap;
		handleHead(clazz, readExcel);
		return handleHead(clazz, readExcel);
	}
	
	public <T> ExcelDataResult<T> excelToEntity(Class<T> clazz, List<String[]> readExcel, Map<String, Map<String, String>> attrsMap, ExcelDataHandler edh) throws Exception {	
		this.edh = edh;
		this.attrsMap = attrsMap;
		return handleHead(clazz, readExcel);
	}

	/**
	 * 表头处理
	 * @param clazz
	 * @param readExcel
	 * @param attrsMap
	 * @return
	 * @throws Exception
	 */
	private <T> ExcelDataResult<T> handleHead(Class<T> clazz, List<String[]> readExcel) throws Exception {	
		if(readExcel == null || readExcel.size() == 0) {
			return null;
		}
		
		//实体集合
		List<T> entityList = new ArrayList<T>();
		
		//错误信息集合
		List<ErrorMsgWithPoints> errorList = new ArrayList<ErrorMsgWithPoints>();
		
		//表头字符串组
		String[] table_ths = readExcel.get(0);
		
		//初始化set方法Map集合
		HashMap<String, MethodWithExcelMapping> methodMap = new HashMap<String, MethodWithExcelMapping>();
		
		//获取实体的所有方法
		Method[] methods = clazz.getDeclaredMethods();
				
		//遍历方法获取注解，并做一层封装
		for(Method method : methods) {
			//获取带有ExcelMapping注解的方法
			ExcelMapping annotation = method.getAnnotation(ExcelMapping.class);
			if(annotation != null) {
				methodMap.put(annotation.value(), new MethodWithExcelMapping(annotation, method));
			}
		}
		
		//检查表头数量的正确性
		int th_num = table_ths.length;
		int method_num = methodMap.size();
		if(th_num != method_num) {
			logger.error("表头数量不一致。");
			return null;
		}
		
		//检查表头内容的正确性
		for(String table_th : table_ths) {
			if(methodMap.get(table_th) == null) {
				logger.error("表头内容不一致。");
				return null;
			}
		}

		//开始遍历excel数据
		for(int i = 1; i < readExcel.size(); i ++) {
			//实体数据
			String[] attrs = readExcel.get(i);
			
			//实例化实体
			T t = clazz.newInstance();
			
			//遍历字段
			for(int j = 0; j < attrs.length; j ++) {
				//获取注解
				ExcelMapping anno = methodMap.get(table_ths[j]).getExcelMapping();
				//数据
				String attr  = attrs[j];
				
				//判断数据是否为空
				if(StringUtils.isEmpty(attr)) {
					//判断字段数据是否必须
					if(anno.isRequire()) {
						//报错，此字段数据不能为空
						errorList.add(new ErrorMsgWithPoints("数据不能为空", i, j));
					}
					continue;
				}
				
				//根据表头获取方法
				Method method = methodMap.get(table_ths[j]).getMethod();
				
				//检查该数据是否多重字段
				if(anno.isMulitAttr()) {
					String[] attrGroup = attr.split(anno.separator());
					attr = "";
					boolean isOK = true;
					for(String _attr : attrGroup) {
						//数据验证与映射
						AfterHandleData handleData = handleData(anno, _attr, entityList);
						if(!handleData.isOK()) {
							errorList.add(new ErrorMsgWithPoints(handleData.getErrorMsg(), i, j));
							isOK = false;
							break;
						}
						attr += handleData.getAttr() + ",";
					}
					if(!isOK) {
						continue;
					}
					attr = attr.substring(0, attr.length() - 1);
				} else {
					//数据验证与映射
					AfterHandleData handleData = handleData(anno, attr, entityList);
					if(!handleData.isOK()) {
						errorList.add(new ErrorMsgWithPoints(handleData.getErrorMsg(), i, j));
						continue;
					}
					attr = handleData.getAttr();
				}
			
				//调用方法封装数据
				try {
					method.invoke(t, ConvertUtils.convert(attr, method.getParameterTypes()[0]));
				} catch (Exception e) {
					//参数类型转换失败
					errorList.add(new ErrorMsgWithPoints("数据类型错误[" + method.getParameterTypes()[0] + "]", i, j));
				} 
			}
			//放入集合
			entityList.add(t);
		}
		
		return new ExcelDataResult<T>(readExcel, entityList, errorList);
	}
	
	/**
	 * 数据验证与映射
	 * @param anno
	 * @param attr
	 * @return
	 */
	private <T> AfterHandleData handleData(ExcelMapping anno, String attr, List<T> entityList) {
		//正则验证数据(如果需要)
		if(anno.vertify().length != 0) {
			for(String reg : anno.vertify()) {
				if(!attr.matches(reg)) {
					//验证失败
					return new AfterHandleData(false, null, anno.errorMsg());
				}
			}
		}
		
		//执行自定义数据处理(在字段映射之前，如果有定义)
		if(edh != null) {
			AfterHandleData handleData = edh.handleBeforeMapping(attr, anno, entityList);
			if(!handleData.isOK()) {
				return handleData;
			}
			attr = handleData.getAttr();
		}
		
		//检查数据是否需要字段映射(如果需要)
		if(!StringUtils.isEmpty(anno.attrMapping())) {
			attr = attrsMap.get(anno.attrMapping()).get(attr);
			//检查是否映射失败
			if(attr == null) {
				//映射失败
				return new AfterHandleData(false, null, anno.errorMsg());
			}
		}
		
		//执行自定义数据处理(在字段映射之后，如果有定义)
		if(edh != null) {
			return edh.handleAfterMapping(attr, anno, entityList);
		}
		
		return new AfterHandleData(true, attr, "");
	}
}
