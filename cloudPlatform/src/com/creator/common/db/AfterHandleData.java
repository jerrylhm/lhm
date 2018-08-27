package com.creator.common.db;

/**
 * 自定义处理数据后的封装类
 * @author ilongli
 *
 */
/**
 * @author 李隆威
 *
 */
public class AfterHandleData {

	private boolean isOK;		//数据是否有误
	
	private String attr;		//处理后的数据
	
	private String errorMsg;	//数据出错的错误信息
	
	public AfterHandleData(boolean isOK, String attr, String errorMsg) {
		this.isOK = isOK;
		this.attr = attr;
		this.errorMsg = errorMsg;
	}

	public boolean isOK() {
		return isOK;
	}

	public String getAttr() {
		return attr;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
}
