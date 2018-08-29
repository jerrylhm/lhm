package com.creator.db.type;
/**
 * 用于返回文档操作结果的枚举类
 *
 */
public enum TypeEnum {
	NORMAL(1,"normal"),
	EQUIPMENT(2,"equipment"),
	TEXT(3,"text"),
	IMAGE(4,"image"),
	VIDEO(5,"video"),
	ENUM(6,"enum"),
	ENUMACTION(7,"enumaction"),
	TABLE(8,"table"),
	CHART(9,"chart"),
	RANGE(10,"range"),
	ACTION(11,"action"),
	MEETING(12,"meeting");
	
    private Integer id;
	private String name;
	
	private TypeEnum(Integer id,String name) {
		this.id=id;
		this.name=name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}



}
