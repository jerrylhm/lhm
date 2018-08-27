package com.creator.common.db;

import java.util.List;

import com.creator.annotation.ExcelMapping;

/**
 * 用于自定义Excel数据处理的接口
 * @author ilongli
 *
 */
public interface ExcelDataHandler {
	
	/**
	 * 处理数据，在字段映射之前
	 * @param attr	传入的数据
	 * @param anno	数据set方法的注解
	 * @return		若返回null值，则表示该数据有误，不可用
	 */
	<T> AfterHandleData handleBeforeMapping(String attr, ExcelMapping anno, List<T> entityList);
	
	/**
	 * 处理数据，在字段映射之后
	 * @param attr	传入的数据
	 * @param anno	数据set方法的注解
	 * @return		若返回null值，则表示该数据有误，不可用
	 */
	<T> AfterHandleData handleAfterMapping(String attr, ExcelMapping anno, List<T> entityList);
}
