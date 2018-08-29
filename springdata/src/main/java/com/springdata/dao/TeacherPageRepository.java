package com.springdata.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.springdata.entity.Teacher;

public interface TeacherPageRepository extends PagingAndSortingRepository<Teacher, Integer>{

}
