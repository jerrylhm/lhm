package com.springdata.dao;

import org.springframework.data.repository.CrudRepository;

import com.springdata.entity.Teacher;

public interface TeacherCrudRepository extends CrudRepository<Teacher, Integer>{

	Teacher findByClassName(String name);
}
