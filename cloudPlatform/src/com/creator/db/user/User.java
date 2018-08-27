package com.creator.db.user;

import com.creator.annotation.ExcelMapping;

public class User {
	private Integer ur_id;
	private String ur_username;
	private String ur_password;
	private String ur_image;
	private String ur_email;
	private String ur_nickname;
	private String ur_classid;
	private String ur_createdate;
	private Integer ur_sex;
	private String ur_phone;
	private String ur_type;
	private Integer ur_status;
	public Integer getUr_id() {
		return ur_id;
	}
	public void setUr_id(Integer ur_id) {
		this.ur_id = ur_id;
	}
	
	public String getUr_username() {
		return ur_username;
	}
	@ExcelMapping(value = "用户名", vertify = {"^[a-zA-Z0-9]{5,16}$"}, errorMsg = "用户名必须为5~16位英文或数字组合")
	public void setUr_username(String ur_username) {
		this.ur_username = ur_username;
	}
	
	public String getUr_password() {
		return ur_password;
	}
	@ExcelMapping(value = "密码", vertify = "^[\\S]{5,16}$", errorMsg = "密码必须5到16位，且不能出现空格")
	public void setUr_password(String ur_password) {
		this.ur_password = ur_password;
	}
	
	public String getUr_image() {
		return ur_image;
	}
	public void setUr_image(String ur_image) {
		this.ur_image = ur_image;
	}
	
	public String getUr_email() {
		return ur_email;
	}
	@ExcelMapping(value = "邮箱", vertify = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$", errorMsg = "邮箱格式不正确", isRequire = false)
	public void setUr_email(String ur_email) {
		this.ur_email = ur_email;
	}
	
	public String getUr_nickname() {
		return ur_nickname;
	}
	@ExcelMapping(value = "昵称", vertify = "[\\u4e00-\\u9fa5A-z0-9]+", errorMsg = "昵称必须为英文、数字或汉字组合")
	public void setUr_nickname(String ur_nickname) {
		this.ur_nickname = ur_nickname;
	}
	
	public String getUr_classid() {
		return ur_classid;
	}
	@ExcelMapping(value = "班级", errorMsg = "该班级不存在", isRequire = false, isMulitAttr = true)
	public void setUr_classid(String ur_classid) {
		this.ur_classid = ur_classid;
	}
	
	public String getUr_createdate() {
		return ur_createdate;
	}
	public void setUr_createdate(String ur_createdate) {
		this.ur_createdate = ur_createdate;
	}
	
	public Integer getUr_sex() {
		return ur_sex;
	}
	@ExcelMapping(value = "性别", vertify = "^([男|女]){1}$", attrMapping = "ur_sex", errorMsg = "性别只能选择\"男\"或\"女\"")
	public void setUr_sex(Integer ur_sex) {
		this.ur_sex = ur_sex;
	}
	
	public String getUr_phone() {
		return ur_phone;
	}
	@ExcelMapping(value = "手机", vertify = "^1\\d{10}$", errorMsg = "手机号格式不正确", isRequire = false)
	public void setUr_phone(String ur_phone) {
		this.ur_phone = ur_phone;
	}
	
	public String getUr_type() {
		return ur_type;
	}
	@ExcelMapping(value = "身份", vertify = "^([教师|学生|家长])+$", attrMapping = "ur_type", errorMsg = "身份只能选择\"教师\"、\"学生\"和\"家长\"", isMulitAttr = true)
	public void setUr_type(String ur_type) {
		this.ur_type = ur_type;
	}
	
	public Integer getUr_status() {
		return ur_status;
	}
	public void setUr_status(Integer ur_status) {
		this.ur_status = ur_status;
	}
	@Override
	public String toString() {
		return "\nUser [ur_id=" + ur_id + ", ur_username=" + ur_username
				+ ", ur_password=" + ur_password + ", ur_image=" + ur_image
				+ ", ur_email=" + ur_email + ", ur_nickname=" + ur_nickname
				+ ", ur_classid=" + ur_classid + ", ur_createdate="
				+ ur_createdate + ", ur_sex=" + ur_sex + ", ur_phone="
				+ ur_phone + ", ur_type=" + ur_type + ", ur_status="
				+ ur_status + "]";
	}
	
	
}
