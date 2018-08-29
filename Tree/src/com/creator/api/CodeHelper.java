package com.creator.api;

import org.apache.commons.lang.math.NumberUtils;


/**
 * 状态码辅助类
 *
 */
public class CodeHelper {
	
	/**
	 * 判断字符串是否为空或者不是数字，返回相应的状态码
	 * @param str 字符串参数
	 * @return 状态码
	 */
	public static CodeEnum String2Number(String str) {
		if(str == null) {
			return CodeEnum.INCOMPLETE_PARAM;
		}
		if(!NumberUtils.isNumber(str)) {
			return CodeEnum.ERROR_PARAM_TYPE;
		}
		return CodeEnum.SUCCESS;
	}
	
	/**
	 * 判断多个字符串是否为空或者不是数字，返回相应的状态码
	 * @param strs 不定参数，多个字符串
	 * @return 状态码
	 */
	public static CodeEnum Strings2Numbers(String... strs) {
		//判断参数是否为空
		for(String str : strs) {
			if(str == null) {
				return CodeEnum.INCOMPLETE_PARAM;
			}
		}
		//判断参数是否数字
		for(String str : strs) {
			if(!NumberUtils.isNumber(str)) {
				return CodeEnum.ERROR_PARAM_TYPE;
			}
		}
		
		return CodeEnum.SUCCESS;
	}
	
	/**
	 * 判断多个字符串是否为空，返回相应的状态码
	 * @param strs 不定参数，多个字符串
	 * @return 状态码
	 */
	public static CodeEnum isStringsNull(String... strs) {
		//判断参数是否为空
		for(String str : strs) {
			if(str == null) {
				return CodeEnum.INCOMPLETE_PARAM;
			}
		}
		return CodeEnum.SUCCESS;
	}
	
	
	/**
	 * 判断操作枚举中是否存在该操作码
	 * @param code
	 * @return
	 */
	public static boolean isExistOpCode(String code) {
		if(code == null) {
			return false;
		}
		OperationEnum[] enums = OperationEnum.values();
		for(int i=0;i<enums.length;i++) {
			if(code.trim().equals(enums[i].getCode())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据操作码获取操作枚举
	 * @param code
	 * @return
	 */
	public static OperationEnum getEnumByCode(String code) {
		OperationEnum[] enums = OperationEnum.values();
		for(int i=0;i<enums.length;i++) {
			if(code.trim().equals(enums[i].getCode())) {
				return enums[i];
			}
		}
		return null;
	}
	
}
