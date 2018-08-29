package com.creator.db.scene;

public enum SceneActionEnum {

	ENUMACTION(7,"enumaction"),
	RANGE(10,"range"),
	ACTION(11,"action");
	
	private int id;
	private String name;
	
	private SceneActionEnum(int id,String name) {
		this.id = id;
		this.name = name;
	}	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

