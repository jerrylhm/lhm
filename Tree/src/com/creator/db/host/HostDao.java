package com.creator.db.host;

import java.util.List;

public interface HostDao {
	
	/**
	 * 添加NodeJs主机信息
	 * @param host
	 */
	public void addHost(Host host);
	
	/**
	 * 根据树id查询主机信息是否存在
	 */
	public boolean isExistHostByTreeId(int treeId);
	
	/**
	 * 根据树id更新主机信息
	 */
	public void updateHostByTreeId(Host host);
	
	public List<Host> queryByTreeId(int id);
}
