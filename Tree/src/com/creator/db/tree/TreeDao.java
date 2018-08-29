package com.creator.db.tree;

import java.util.List;
import java.util.Map;

public interface TreeDao {
	List<Tree> queryById(int id);
    List<Tree> querySonNode(int id);
    List<Tree> queryByTreeId(int treeid);
    List<Tree> queryByNameAndPid(String name,int pid);
    List<Tree> queryBySn(String sn);
    List<Tree> queryTreeNodeByUserId(int userId);
    List<TreeDto> queryTreeNodeByPage(TreeDto treeDto);
    /**
     * 查找所有树节点
     * @return
     */
    List<Tree> queryTreeNode();
    int addTree(Tree tree);
    int updateTree(Tree tree);
    int deleteTree(int id);
    
    /**
     * 根据用户id删除树节点
     */
    public void deleteNodeByUserid(int userid);
    
    /**
     * 根据用户id查询其创建的树根节点
     */
    public List<Map<String,Object>> queryCreateRootByUserId(int userId);
    
    /**
     * 根据用户id查询有权限的树根节点
     */
    public List<Map<String,Object>> queryRootByUserId(int userId);
    
    /**
     * 判断节点是否存在
     */
    public boolean isExistNode(int nodeId);
    
    /**
     * 根据节点id查询节点
     */
    public List<Map<String,Object>> queryNodeByNodeId(int nodeId);
    
    /**
     * 根据节点id查询子节点
     */
    public List<Map<String,Object>> queryChilrenByNodeId(int nodeId);
    
    /**
     * 根据节点id和用户id查询有权限的子节点
     */
    public List<Map<String,Object>> queryChilrenByNodeIdAndUserId(int nodeId,int userId);
    
    /**
     * 根据节点id查询会议和普通子节点
     */
    public List<Map<String,Object>> queryMeetingChildrenById(int nodeId);
    
    /**
     * 根据节点id和类型名称查询子节点（包括普通节点类型）
     */
    public List<Map<String,Object>> queryChildrenByClassName(int nodeId,String className);
    
    /**
     * 根据树id和类名查询设备
     */
    public List<Map<String,Object>> queryDeviceByTreeIdAndClassName(int treeId,String className);
    
    /**
     * 根据节点id和节点类型查询子节点(包括普通类型节点)
     */
    public List<Map<String,Object>> queryChildrenByNodeTypeOrNormal(int nodeId,int type);
    
    /**
     * 根据节点id和节点类型查询子节点(不包括普通类型节点)
     */
    public List<Map<String,Object>> queryChildrenByNodeType(int nodeId,int type);
    
    /**
     * 根据节点id判断树根节点是否存在
     */
    public boolean isExistRootById(int id);
    
    /*********************************************************/
    
    /**
     * 根据节点id和类名组合查询子节点(包括普通节点)
     */
    public List<Map<String,Object>> queryChildrenByClassNameGroup(int nodeId,String classNameGroup);
    
    /**
     * 根据节点id和类名查询子节点（不包括普通节点）
     */
    public List<Map<String,Object>> queryChildrenByClassNameWithoutNormal(int nodeId,String className);
    
}
