package com.springdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springdata.dao.TeacherRepository;

@Service
public class TeacherService {

	@Autowired
	private TeacherRepository teacherDao;
	
	@Transactional
	public void updateTeacherName(String name) {
		System.out.println('j');
		teacherDao.updateName(name);
	}
}
