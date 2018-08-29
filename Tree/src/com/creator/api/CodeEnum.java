package com.creator.api;


/**
 * 返回状态码及返回状态码代表的信息
 * 注：正常情况下状态码第一位 ：0、客户端状态码       1、设备状态码 
 *
 */
public enum CodeEnum {
	SUCCESS("0000","成功"),
	ERROR_UNKNOW("1000","未知错误"),
	USER_NOT_EXIST("0001","用户不存在"),
	NODE_NOT_EXIST("0002","节点不存在"),
	INCOMPLETE_PARAM("0003","参数不完整"),
	ERROR_PARAM_TYPE("0004","参数类型错误"),
	VALUE_NOT_EXIST("0005","节点无对应数据"),
	ERROR_DEVICE_STATUSCODE("0006","设置状态码错误"),
	ERROR_NODE_TYPE("0007","节点类型错误"),
	PROTOCOL_NOT_EXIST("0008","协议名不存在"),
	CLASS_NOT_EXIST("0009","类名不存在"),
	NODETYPE_NOT_EXIST("0010","节点类型不存在"),
	CHILD_NOT_EXIST("0011","子节点不存在"),
	ERROR_LOGIN_MESSAGE("0012","用户名或密码错误"),
	GROUP_NOT_EXIST("0013","用户组不存在"),
	ERROR_REG_MESSAGE("0014","注册信息有误"),
	MEETING_NOT_EXIST("0015","会议不存在"),
	ERROR_DATE_FORMAT("0016","日期不符合格式"),
	DEVICE_NOT_EXIST("0017","设备不存在"),
	ERROR_UPLOAD_RESOURCE("0018","资源上传失败"),
	ERROR_TIME_ORDER("0019","开始时间晚于结束时间"),
	ERROR_START_TIME("0020","预约时间段已被预约"),
	ERROR_TIME_FORMAT("0021","时间不符合hh:mm:ss格式"),
	TEMPLATE_NOT_EXIST("0022","模板不存在"),
	CONTENT_NOT_EXIST("0023","模板未填写数据"),
	RESOURCE_NOT_EXIST("0024","节点无对应资源"),
	CHILD_SPECIFIED_NOT_EXIST("0025","指定的子节点不存在"),
	SCENE_NOT_EXIST("0026","场景不存在"),
	TREE_NOT_EXIST("0027","树根节点不存在"),
	PERMISSION_NOT_EXIST("0028","用户无相应权限"),
	NAME_HAVE_EXIST("0029","名称已存在"),
	USER_WITHOUT_PERMISSION("0030","用户无相应权限"),
	OPCODE_NOT_EXIST("0031","操作码不存在"),
	KEY_NOT_EXIST("0032","键名不存在"),
	NODE_WIDTHOUT_TEMPLATE("0033","节点无对应模板"),
	VIDEO_NOT_EXIST("0034","视频节点不存在"),
	
	SN_NOT_EXIST("1001","SN号不存在"),
	ERROR_VALUE("1002","数据为空");
	
	private String code;    //标识码
	private String msg;     //标识码代表的信息
	
	private CodeEnum(String code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	/*
	 * 获取标识码
	 */
	public String getCode() {
		return code;
	}
	
	/*
	 * 获取信息
	 */
	public String getMsg() {
		return msg;
	}
	
	
}
