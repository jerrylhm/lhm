package com.creator.common.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StringUtil {
	
	/**
	 * 将数组拼凑成字符串，中间以","分隔
	 */
	public static String arrayToString(Object[] objects) {
		StringBuilder result = new StringBuilder();
		if(objects == null) {
			return "";
		}
		if(objects.length <= 0) {
			return "";
		}
		for(int i=0;i<objects.length;i++) {
			result.append(objects[i]).append(",");
		}
		//去除最后一个","
		String strResult = result.toString();
		if(result.toString().endsWith(",")) {
			strResult = strResult.substring(0, strResult.length() - 1);
		}
		return strResult;
	}
	
	/**
	 * 将数组转set
	 */
	public static <T> Set<T> arrayToSet(T[] objects) {
		Set<T> set = new HashSet<T>();
		if(objects == null) {
			return set;
		}
		for(int i=0;i<objects.length;i++) {
			set.add(objects[i]);
		}
		return set;
	}
	
	/**
	 * 将集合转成字符串,中间以","隔开
	 */
	public static String collectionToString(Collection<?> collection) {
		if(collection == null) {
			return "";
		}
		
		Object[] objects = collection.toArray();
		return arrayToString(objects);
	}
	
	/**
	 * 将以","分隔的字符串，剔除其中一个元素，组成新的字符串
	 * 如将  1,2,3,4    剔除 2返回  1,3,4
	 */
	public static String removeElement(String str,String element) {
		String result = "";
		if(str == null) {
			return result;
		}
		String[] elements = str.split(",");
		if(elements.length == 0) {
			return result;
		}
		
		for(String ele: elements) {
			if(ele.equals(element)) {
				continue;
			}
			result += ele + ",";
		}
		if(result.endsWith(",")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
}
