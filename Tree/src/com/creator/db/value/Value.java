package com.creator.db.value;

public class Value {
	private Integer value_id;
	private Integer value_nodeid;
	private String value_data;
	private String value_key;
	private String value_datetime;

	public Value() {
		super();
	}
	
	public Value(Integer value_id, Integer value_nodeid, String value_data,
			String value_key, String value_datetime) {
		super();
		this.value_id = value_id;
		this.value_nodeid = value_nodeid;
		this.value_data = value_data;
		this.value_key = value_key;
		this.value_datetime = value_datetime;
	}

	public Integer getValue_id() {
		return value_id;
	}
	public void setValue_id(Integer value_id) {
		this.value_id = value_id;
	}
	public Integer getValue_nodeid() {
		return value_nodeid;
	}
	public void setValue_nodeid(Integer value_nodeid) {
		this.value_nodeid = value_nodeid;
	}
	public String getValue_data() {
		return value_data;
	}
	public void setValue_data(String value_data) {
		this.value_data = value_data;
	}
	public String getValue_key() {
		return value_key;
	}
	public void setValue_key(String value_key) {
		this.value_key = value_key;
	}

	public String getValue_datetime() {
		return value_datetime;
	}

	public void setValue_datetime(String value_datetime) {
		this.value_datetime = value_datetime;
	}
	
	
	
	
}
