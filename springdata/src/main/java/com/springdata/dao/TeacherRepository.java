package com.springdata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.springdata.entity.Teacher;

public interface TeacherRepository extends Repository<Teacher, Integer>{
	/**
     * 根据名字查询老师
     * @param name
     * @return
     */
    Teacher findByName(String name);
 
    /**
     * 根据班级名称进行查询老师（这里用到模糊匹配like）
     * @param classNumber
     * @return
     */
    List<Teacher> findByClassNameLike(String className);
    
    @Query("select t from Teacher t")
    List<Teacher> query1();
    
    @Query("select t from Teacher t where t.name=?1")
    List<Teacher> query2(String name);
    
    @Query("select t from Teacher t where t.name=:name")
    List<Teacher> query3(@Param("name") String name);
    
    /**
     * 设置nativeQuery为 true之后可以使用原生的sql语句
     */
    @Query(nativeQuery = true, value = "SELECT * FROM teacher")
    List<Teacher> query4();
    
    /**
     * 修改类操作必须添加@Modifying注解，service层方法添加@Transactional
     * 返回值可以是void 或者 int/Integer
     */
    @Modifying
    @Query("update Teacher t set t.name = ?1")
    int updateName(String name);
}
