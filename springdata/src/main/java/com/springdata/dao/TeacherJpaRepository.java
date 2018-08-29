package com.springdata.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springdata.entity.Teacher;

public interface TeacherJpaRepository extends JpaRepository<Teacher, Integer>{
	
	Teacher findByName(String name);
}
