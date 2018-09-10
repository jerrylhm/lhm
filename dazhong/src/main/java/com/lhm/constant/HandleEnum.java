package com.lhm.constant;

public enum HandleEnum {
	SUCCESS(200, "请求成功"),
	FAIL(201, "创建成功"),
	ERROR(400, "错误的请求"),
	NOTVALIDATE(401, "未验证"),
	REFUSE(403, "请求被拒绝"),
	NOTFOUND(404, "资源找不到"),
	SYSTEMERROR(500, "系统错误");
	
	private Integer code;
	private String msg;
	
	private HandleEnum(int code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public static final String key = "handle";

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
	
}
