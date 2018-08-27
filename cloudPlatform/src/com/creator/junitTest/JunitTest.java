package com.creator.junitTest;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.creator.db.equipment.EquipmentDaoImpl;
import com.creator.db.index.Index;
import com.creator.db.index.IndexDaoImpl;
import com.creator.db.term.Term;
import com.creator.db.term.TermDaoImpl;
import com.creator.db.timetable.TimeTable;
import com.creator.db.timetable.TimeTableDaoImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:Mysql_bean.xml"})
public class JunitTest {
 
//	@Autowired
//	private EquipmentDaoImpl equipmentDao;
	@Autowired
	private TermDaoImpl termDao;
	@Autowired
	private TimeTableDaoImpl timeTableDao;
	@Autowired
	private IndexDaoImpl IndexDao;
	
	@Test
	public void test() {
		System.out.println(IndexDao.queryExist(36, 1, 3, 2, 2));
	}
	
}
