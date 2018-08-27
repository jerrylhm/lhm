package com.creator.db.address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository("addressDao")
public class AddressDaoImpl implements AddressDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Address> query() {
		String sql = "SELECT * FROM tb_address";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Address>(Address.class));
	}

	@Override
	public Address findById(int id) {
		String sql = "SELECT * FROM tb_address WHERE add_id=?";
		List<Address> ls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Address>(Address.class), id);
		if(ls.size() > 0) {
			return ls.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int updateById(Address obj) {
		String sql = "UPDATE tb_address SET add_name=?,add_camera=?,add_type=?,add_pid=? WHERE add_id=?";
		return jdbcTemplate.update(sql, obj.getAdd_name(), obj.getAdd_camera(), obj.getAdd_type(), obj.getAdd_pid(), obj.getAdd_id());
	}

	@Override
	public int deleteById(int id) {
		String sql = "DELETE FROM tb_address WHERE add_id=?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int insert(Address obj) {
		String sql = "INSERT INTO tb_address(add_name,add_camera,add_type,add_pid) VALUES(?,?,?,?)";
		return jdbcTemplate.update(sql, obj.getAdd_name(), obj.getAdd_camera(), obj.getAdd_type(), obj.getAdd_pid());
	}
	
	
	/*
	 * 根据父id查询子节点列表
	 */
	@Override
	public List<Address> findByPid(int pid) {
		String SQL = "SELECT * FROM tb_address WHERE add_pid=?";
		List<Address> addressList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<Address>(Address.class),pid);
		return addressList;
	}

	
	/*
	 * 分页查询子节点列表
	 */
	@Override
	public List<Address> findByPidPage(int pid, int index, int count) {
		String SQL = "SELECT * FROM tb_address WHERE add_pid=? LIMIT ?,?";
		List<Address> addressList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<Address>(Address.class),pid,count*(index-1),count);
		return addressList;
	}

	
	/*
	 *根据id获取子节点个数
	 */
	@Override
	public int countChildren(int pid) {
		String SQL = "SELECT COUNT(add_id) FROM tb_address WHERE add_pid=?";
		int count = jdbcTemplate.queryForObject(SQL, new Object[]{pid}, Integer.class);
		return count;
	}

	
	/*
	 * 添加场所并返回数据库id
	 */
	@Override
	public int insertReturnWidthId(final Address address) {
		final String SQL = "INSERT INTO tb_address(add_name,add_camera,add_type,add_pid) VALUES(?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement statement = conn.prepareStatement(SQL,new String[]{"add_name","add_camera","add_type","add_pid"});
				statement.setString(1, address.getAdd_name());
				statement.setString(2, address.getAdd_camera());
				statement.setInt(3, address.getAdd_type());
				statement.setInt(4, address.getAdd_pid());
				
				return statement;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	/*
	 * 根据id和类型获取子节点
	 */
	@Override
	public List<Address> findByPidAndType(int pid, int type) {
		String SQL = "SELECT * FROM tb_address WHERE add_pid=? AND add_type=?";
		List<Address> addressList = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<Address>(Address.class),pid,type);
		return addressList;
	}
	
}
