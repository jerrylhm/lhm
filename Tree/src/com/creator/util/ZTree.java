package com.creator.util;

import com.creator.db.type.Type;

public class ZTree {

	private int id;
	private int pId;
	private String name;
	private boolean open;
	private String url;
	private String myUrl;
	//是否禁用节点
	private boolean chkDisabled;
	//节点图标
	private String icon;
	//跳转窗口
	private String target;
	//是否有权限
	private boolean hasP;
	//是否是创建者
	private boolean isCreator;
	//节点类型
	private Type type;
	//节点title
	private String title;
	//设备sn
	private String sn;
	//设备所属类
	private String nodeClass;
	//设备模板id
	private String tpId; 
	//场景
	private String scene;
	//传输类型
	private Integer tsType;
	public ZTree() {
		this.open = false;
		this.url="";
		this.tpId = "-1";
	}	
    
	public boolean isChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public boolean isIsCreator() {
		return isCreator;
	}

	public void setIsCreator(boolean isCreator) {
		this.isCreator = isCreator;
	}

	public boolean isHasP() {
		return hasP;
	}

	public void setHasP(boolean hasP) {
		this.hasP = hasP;
	}

	public String getMyUrl() {
		return myUrl;
	}

	public void setMyUrl(String myUrl) {
		this.myUrl = myUrl;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	
	public boolean isCreator() {
		return isCreator;
	}

	public void setCreator(boolean isCreator) {
		this.isCreator = isCreator;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getNodeClass() {
		return nodeClass;
	}

	public void setNodeClass(String nodeClass) {
		this.nodeClass = nodeClass;
	}
	
	public String getTpId() {
		return tpId;
	}

	public void setTpId(String tpId) {
		this.tpId = tpId;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public Integer getTsType() {
		return tsType;
	}

	public void setTsType(Integer tsType) {
		this.tsType = tsType;
	}

	@Override
	public String toString() {
		return "ZTree [id=" + id + ", pId=" + pId + ", name=" + name
				+ ", open=" + open + ", url=" + url + ", myUrl=" + myUrl
				+ ", chkDisabled=" + chkDisabled + ", icon=" + icon
				+ ", target=" + target + ", hasP=" + hasP + ", isCreator="
				+ isCreator + ", type=" + type + ", title=" + title + ", sn="
				+ sn + ", nodeClass=" + nodeClass + ", tpId=" + tpId
				+ ", scene=" + scene + ", tsType=" + tsType + "]";
	}







	


	
	




		
}
