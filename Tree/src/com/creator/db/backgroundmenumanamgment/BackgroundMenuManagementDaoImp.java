package com.creator.db.backgroundmenumanamgment;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.creator.db.tree.Tree;
import com.sun.org.apache.bcel.internal.util.BCELifier;

public class BackgroundMenuManagementDaoImp extends JdbcDaoSupport implements BackgroundMenuManagementDao{

	@Override
	public Integer countAllMenu() {
		JdbcTemplate template = super.getJdbcTemplate();
		String sql = "select count(bck_mm_id) from tb_backgroundmenumanagement";
		Integer count = template.queryForObject(sql, Integer.class);
		return count;
	}

	@Override
	public List<BackgroundMenuManagementDto> queryMenuByPage(int num, int index) {
		JdbcTemplate template = super.getJdbcTemplate();
		String sql = "select * from tb_backgroundmenumanagement,tb_tree where bck_mm_treeid=node_id ORDER BY bck_mm_treeid DESC LIMIT "+(index-1)*num+","+num;
		List<BackgroundMenuManagementDto> list = template.query(sql, new BeanPropertyRowMapper<>(BackgroundMenuManagementDto.class));
		return list;
	}

	@Override
	public List<BackgroundMenuManagment> queryMenuById(int bck_mm_id) {
		JdbcTemplate template = super.getJdbcTemplate();
		String sql = "select * from tb_backgroundmenumanagement where bck_mm_id="+bck_mm_id;
		List<BackgroundMenuManagment> list = template.query(sql, new BackgroundMenuManagementMapper());
		return list;
	}

	@Override
	public void insertNewMenu(BackgroundMenuManagment backMenuManagment) {
		JdbcTemplate template = super.getJdbcTemplate();
		String sql = "insert into tb_backgroundmenumanagement(bck_mm_name,bck_mm_treeid,bck_mm_del_flag,bck_mm_state) values (?,?,?,?)";
		template.update(sql,backMenuManagment.getBck_mm_name(),backMenuManagment.getBck_mm_treeid(),backMenuManagment.getBck_mm_del_flag(),backMenuManagment.getBck_mm_state());
		
	}

	@Override
	public void updateMenu(int bck_mm_id,BackgroundMenuManagment backMenuManagment) {
		JdbcTemplate template = super.getJdbcTemplate();
		String sql = "update tb_backgroundmenumanagement set bck_mm_name=?,bck_mm_treeid=?,bck_mm_del_flag=?,bck_mm_state=?";
		template.update(sql,backMenuManagment.getBck_mm_name(),backMenuManagment.getBck_mm_treeid(),backMenuManagment.getBck_mm_del_flag(),backMenuManagment.getBck_mm_state());

	}

	@Override
	public void deleteMenu(int bck_mm_id) {
		JdbcTemplate template = super.getJdbcTemplate();
		String sql = "delete from tb_backgroundmenumanagement where bck_mm_id="+bck_mm_id;
		template.update(sql);
		
	}


}
