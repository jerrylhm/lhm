package com.creator.rest;

import java.util.HashMap;

/**
 * json返回状态类 
 *
 */
public class CodeResult {
	private String code;
	private String msg;
	private Object data;
	
	public CodeResult() {
		code = "";
		msg = "";
		data = (Object) new HashMap<>();
	}
	
	public CodeResult(String code,String msg) {
		this();
		this.code = code;
		this.msg = msg;
	}
	
	public CodeResult(String code,String msg,Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public CodeResult(CodeEnum codeEnum) {
		this.code = codeEnum.getCode();
		this.msg = codeEnum.getMsg();
		data = (Object) new HashMap<>();
	}
	
	public CodeResult(CodeEnum codeEnum, Object data) {
		this.code = codeEnum.getCode();
		this.msg = codeEnum.getMsg();
		this.data = data;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	/*
	 * 成功
	 */
	public static  CodeResult ok() {
		return new CodeResult(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getMsg());
	}
	
	public static  CodeResult ok(Object data) {
		return new CodeResult(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getMsg(),data);
	}
	
	/*
	 * 错误
	 */
	public static  CodeResult error() {
		return new CodeResult(CodeEnum.ERROR.getCode(),CodeEnum.ERROR.getMsg());
	}
	public static CodeResult error(Object data) {
		return new CodeResult(CodeEnum.ERROR.getCode(),CodeEnum.ERROR.getMsg(),data);
	}
	
}
