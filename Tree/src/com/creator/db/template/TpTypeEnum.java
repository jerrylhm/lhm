package com.creator.db.template;

public enum TpTypeEnum {

	TEXT("文本","text"),
	BOOLEAN("布尔型","boolean"),
	FILE("文件","file"),
	ENUM("枚举","enum");
	
    private String cnName;
	private String name;
	
	private TpTypeEnum(String cnName,String name) {
		this.cnName=cnName;
		this.name=name;
	}

	public String getCnName() {
		return cnName;
	}

	public String getName() {
		return name;
	}
}
