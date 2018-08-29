package com.springdata.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springdata.entity.Teacher;

public interface TestDao extends CrudRepository<Teacher, Integer>{

	@Query(nativeQuery = true,value = "SELECT * FROM WHERE name = 'faker'")
	<S extends Teacher> S test(S entity);
}
