package com.springdata.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.springdata.entity.Teacher;

public interface TeacherSpcRepositoryi extends PagingAndSortingRepository<Teacher , Integer> ,JpaSpecificationExecutor<Teacher >{

}
