package com.creator.db.address;

import java.util.List;

import com.creator.common.db.CrudDao;

public interface AddressDao extends CrudDao<Address>{
	/**
	 * 根据父id查询子节点列表
	 */
	public List<Address> findByPid(int pid);
	
	/**
	 * 分页查询子节点列表
	 */
	public List<Address> findByPidPage(int pid,int index,int count);
	
	/**
	 * 根据id获取子节点个数
	 */
	public int countChildren(int pid);
	
	/**
	 * 添加场所并返回数据库id
	 */
	public int insertReturnWidthId(Address address);
	
	/**
	 * 根据id和类型获取子节点
	 */
	public List<Address> findByPidAndType(int pid,int type);
}
