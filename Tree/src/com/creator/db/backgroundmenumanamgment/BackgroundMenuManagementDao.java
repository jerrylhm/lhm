package com.creator.db.backgroundmenumanamgment;

import java.util.List;

public interface BackgroundMenuManagementDao {

	//查询所有后台菜单的数量
	public Integer countAllMenu();
	
	//分页查询
	public List<BackgroundMenuManagementDto> queryMenuByPage(int num,int index);
	
	//根据主键id查询对应的记录
	public List<BackgroundMenuManagment> queryMenuById(int bck_mm_id);
	
	//插入新的后台菜单
	public void insertNewMenu(BackgroundMenuManagment backMenuManagment);
	
	//更新后台菜单的数据
	public void updateMenu(int bck_mm_id,BackgroundMenuManagment backMenuManagment);
	
	//根据id删除对应菜单
	public void deleteMenu(int bck_mm_id);
	
}
