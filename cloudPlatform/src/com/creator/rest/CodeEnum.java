package com.creator.rest;

/**
 * 状态码
 *
 */
public enum CodeEnum {
	SUCCESS("0000","成功"),
	ERROR("1000","错误"),
	ERROR_PARAM("1001","参数类型错误"),
	ERROR_RESULTNULL("1002","结果为空");
	
	private String code;
	private String msg;
	private CodeEnum(String code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
