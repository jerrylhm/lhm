package com.creator.common.db;

import java.lang.reflect.Method;

import com.creator.annotation.ExcelMapping;

/**
 * 方法与映射注解的封装类
 * @author ilongli
 *
 */
public class MethodWithExcelMapping {
	
	private ExcelMapping excelMapping;		//映射注解
	
	private Method method;					//方法
	
	public MethodWithExcelMapping() {}

	public MethodWithExcelMapping(ExcelMapping excelMapping, Method method) {
		this.excelMapping = excelMapping;
		this.method = method;
	}

	public ExcelMapping getExcelMapping() {
		return excelMapping;
	}

	public Method getMethod() {
		return method;
	}
}
