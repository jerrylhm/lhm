package com.creator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel导入实例set方法映射注解
 * @author ilongli
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelMapping {
	String value();							//对应的表头
	
	String[] vertify() default {};			//正则表达式验证
	
	String errorMsg() default "";			//验证失败后的错误提示信息
	
	boolean isMulitAttr() default false;	//是否多重字段，如("1,2,3")
	
	String separator() default ",";			//多重字段的分割符，默认为","	
	
	String attrMapping() default "";		//字段映射的key值，如果该值为空则不会进行字段映射
	
	boolean isRequire() default true;		//是否为必要字段，默认为true
}
