package com.creator.db.equipment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("equipmentDao")
public class EquipmentDaoImpl implements EquipmentDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Equipment> query() {
		String sql = "SELECT * FROM tb_equipment";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Equipment>(Equipment.class));
	}

	@Override
	public Equipment findById(int id) {
		String sql = "SELECT * FROM tb_equipment WHERE eqm_id=?";
		List<Equipment> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Equipment>(Equipment.class), id);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(Equipment obj) {
		String sql = "UPDATE tb_equipment SET eqm_name=?,eqm_type=?,eqm_sn=?,eqm_class=? WHERE eqm_id=?";
		return jdbcTemplate.update(sql, obj.getEqm_name(), obj.getEqm_type(), obj.getEqm_sn(), obj.getEqm_class(), obj.getEqm_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_equipment WHERE eqm_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(Equipment obj) {
		String sql = "INSERT INTO tb_equipment(eqm_name,eqm_type,eqm_sn,eqm_class) VALUES(?,?,?,?)";
		return jdbcTemplate.update(sql, obj.getEqm_name(), obj.getEqm_type(), obj.getEqm_sn(), obj.getEqm_class());
	}

}
