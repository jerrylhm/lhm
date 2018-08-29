package com.creator.api;


/**
 * 操作枚举，包括求平均值、求总量、最高值、最低值等
 *
 */
public enum OperationEnum {
	AVERAGE("0001","平均值"),
	TOTAL("0002","总量"),
	MAX("0003","最大值"),
	MIN("0004","最小值"),
	RANGE("0005","极差");
	
	
	private String code;      //操作码
	private String msg;      //操作说明
	private OperationEnum(String code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
}
