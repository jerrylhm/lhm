package com.creator.common.util;

public class LoginResult {
	
	private String type;
	private String url;
	private String paramJsonStr;
	private String method;
	private String token;
	private boolean result;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getParamJsonStr() {
		return paramJsonStr;
	}
	public void setParamJsonStr(String paramJsonStr) {
		this.paramJsonStr = paramJsonStr;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
