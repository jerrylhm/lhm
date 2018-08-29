package com.springdata.test;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.springdata.dao.TeacherCrudRepository;
import com.springdata.dao.TeacherJpaRepository;
import com.springdata.dao.TeacherPageRepository;
import com.springdata.dao.TeacherRepository;
import com.springdata.dao.TeacherSpcRepositoryi;
import com.springdata.entity.Teacher;
import com.springdata.service.TeacherService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring.xml"})
public class SpringTest {
    //用于加载spring配置文件
    private ApplicationContext context = null;
 
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private TeacherCrudRepository teacherCrudRepository;
    
    @Autowired
    private TeacherJpaRepository teacherJpaRepository;
    
    @Autowired
    private TeacherPageRepository teacherPageRepository;
    
    @Autowired
    private TeacherSpcRepositoryi teacherSpcRepository;
    
//    @Before
    public void getContext(){
        context = new ClassPathXmlApplicationContext("spring.xml");
        //通过类名进行注入
        teacherRepository = (TeacherRepository) context.getBean("teacherRepository");
        teacherService = (TeacherService) context.getBean(TeacherService.class);
    }
 
    /**
     * 直接执行这个测试方法，然后就再去看一下数据库就会发生对应实体中的内容到数据库中了
     */
//    @Test
    public void testCreateTableAuto(){

    }
 
    /**
     * 测试springdata中的findByName方法(没有任何的实现，这就是springdata的强大)
     */
//    @Test
    public void testSpringDataFindName(){
        Teacher teacher = teacherRepository.findByName("hh");
        System.out.println(teacher);
    }
 
    /**
     * 测试使用springdata进行模糊匹配
     */
//    @Test
    public void testSpringDataLike(){
        List<Teacher> teachers = teacherRepository.findByClassNameLike("%1%");
        for (Teacher teacher:teachers) {
        	System.out.println(teacher);
        }
    }

    @Test
    public void testQuery() {
//    	System.out.println(teacherRepository.query1());
//    	System.out.println(teacherRepository.query2("hh"));
//    	System.out.println(teacherRepository.query3("hh"));
//    	System.out.println(teacherRepository.query4());
//    	teacherService.updateTeacherName("faker");
//    	System.out.println(teacherCrudRepository.findAll());
    	
//    	Teacher t = new Teacher();
//    	t.setName("faker");
//    	t.setClassName("class1");
//    	System.out.println(teacherCrudRepository.save(t));
    	
//    	teacherCrudRepository.delete(2);
    	
//    	System.out.println(teacherJpaRepository.findAll());
    	
//    	Pageable pageable = new PageRequest(0, 5);
//    	System.out.println(teacherPageRepository.findAll(pageable).getContent());
    	
//    	Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
//    	Pageable pageable = new PageRequest(0, 5, sort);
//    	System.out.println(teacherPageRepository.findAll(pageable).getContent());
    	
//    	System.out.println(teacherPageRepository.exists(1));
//    	System.out.println(teacherPageRepository.findAll());
//    	System.out.println(teacherSpcRepository.findAll());
    	
    	Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
    	Pageable pageable = new PageRequest(0, 5, sort);
    	Specification<Teacher> specification = new Specification<Teacher>() {			
			public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> path_name = root.get("name");
				Path<String> path_classname = root.get("className");
				Predicate predicate = cb.and(cb.like(path_name, "%fa%"), cb.like(path_classname, "%1%"));
				return predicate;
			}
		};
		System.out.println(teacherSpcRepository.findAll(specification, pageable));
    }
}


