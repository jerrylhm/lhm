package com.creator.db.host;

public class Host {
	private Integer host_id;
	private Integer host_treeid;
	private String host_ip;
	private Integer host_port;
	private String host_updatetime;
	public Host() {
		super();
	}
	
	public Host(Integer host_id, Integer host_treeid, String host_ip,
			Integer host_port, String host_updatetime) {
		super();
		this.host_id = host_id;
		this.host_treeid = host_treeid;
		this.host_ip = host_ip;
		this.host_port = host_port;
		this.host_updatetime = host_updatetime;
	}

	public Integer getHost_id() {
		return host_id;
	}
	public void setHost_id(Integer host_id) {
		this.host_id = host_id;
	}
	public String getHost_ip() {
		return host_ip;
	}
	public void setHost_ip(String host_ip) {
		this.host_ip = host_ip;
	}
	public Integer getHost_port() {
		return host_port;
	}
	public void setHost_port(Integer host_port) {
		this.host_port = host_port;
	}
	public String getHost_updatetime() {
		return host_updatetime;
	}
	public void setHost_updatetime(String host_updatetime) {
		this.host_updatetime = host_updatetime;
	}
	
	
	public Integer getHost_treeid() {
		return host_treeid;
	}

	public void setHost_treeid(Integer host_treeid) {
		this.host_treeid = host_treeid;
	}

	@Override
	public String toString() {
		return "Host [host_id=" + host_id + ", host_ip=" + host_ip
				+ ", host_port=" + host_port + ", host_updatetime="
				+ host_updatetime + "]";
	}
	
	
	
}
