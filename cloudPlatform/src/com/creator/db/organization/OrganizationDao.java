package com.creator.db.organization;

import java.util.List;

import com.creator.common.db.CrudDao;

public interface OrganizationDao extends CrudDao<Organization>{

	public List<Organization> queryByOrgType(int type);
	
	/**
	 * 根据父节点查询组织结构
	 */
	public List<Organization> findByPid(int pid);
	
	/**
	 * 根据父id和名称判断是否存在相同节点
	 */
	public boolean isExistByPidAndName(Organization org);
	
	/**
	 * 添加组织结构并返回id
	 */
	public int insertReturnWithId(Organization org);
	
	/**
	 * 根据类型查找组织
	 */
	public List<Organization> findByType(int type);
	
	/**
	 * 拼接所有关联的组织
	 */
	public List<Organization> getAllWithConcat();
}
 