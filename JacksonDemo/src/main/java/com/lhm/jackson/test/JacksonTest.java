package com.lhm.jackson.test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lhm.jackson.entity.Student;

public class JacksonTest {
	
	public static void main(String[] args) {
//		  ObjectMapper mapper = new ObjectMapper();
//		  String jsonString = "{\"name\":\"Mahesh\", \"age\":21}";
//		
//		  //map json to student
//		  try {
//			  //json字符串转java对象
//		     Student student = mapper.readValue(jsonString, Student.class);
//		     System.out.println(student);
//		     // 转换为格式化的json
//		     mapper.enable(SerializationFeature.INDENT_OUTPUT);
//		     //java对象转json字符串
//		     jsonString = mapper.writeValueAsString(student);
//		     System.out.println(jsonString);
//		
//		  } catch (JsonParseException e) {
//		     e.printStackTrace();
//		  } catch (JsonMappingException e) {
//		     e.printStackTrace();
//		  } catch (IOException e) {
//		     e.printStackTrace();
//		  }
		JacksonTest jt = new JacksonTest();
//		jt.test2();
		jt.test3();
	}
	
	public void test2() {
	         try {
	            ObjectMapper mapper = new ObjectMapper();

	            Map<String,Object> studentDataMap = new HashMap<String,Object>(); 
	            int[] marks = {1,2,3};

	            Student student = new Student();
	            student.setAge(10);
	            student.setName("Mahesh");
	            // JAVA Object
	            studentDataMap.put("student", student);
	            // JAVA String
	            studentDataMap.put("name", "Mahesh Kumar");   		
	            // JAVA Boolean
	            studentDataMap.put("verified", Boolean.FALSE);
	            // Array
	            studentDataMap.put("marks", marks);

	            //将map转为json格式并写入文件
	            mapper.writeValue(new File("student.json"), studentDataMap);
	            //result student.json
				//{ 
	            //   "student":{"name":"Mahesh","age":10},
	            //   "marks":[1,2,3],
	            //   "verified":false,
	            //   "name":"Mahesh Kumar"
	            //}
	            //从文件中读出json数据并自动转换为map
	            studentDataMap = mapper.readValue(new File("student.json"), Map.class);

	            System.out.println(studentDataMap.get("student"));
	            System.out.println(studentDataMap.get("name"));
	            System.out.println(studentDataMap.get("verified"));
	            System.out.println(studentDataMap.get("marks"));
	      } catch (JsonParseException e) {
	         e.printStackTrace();
	      } catch (JsonMappingException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	            e.printStackTrace();
	      }
	}
	
	public void test3() {
	         try {
	            ObjectMapper mapper = new ObjectMapper();

	            Map<String,Object> userDataMap = new HashMap<String,Object>();
	            UserData studentData = new UserData(); 
	            int[] marks = {1,2,3};

	            Student student = new Student();
	            student.setAge(10);
	            student.setName("Mahesh");
	            // JAVA Object
	            studentData.setStudent(student);
	            // JAVA String
	            studentData.setName("Mahesh Kumar");
	            // JAVA Boolean
	            studentData.setVerified(Boolean.FALSE);
	            // Array
	            studentData.setMarks(marks);
	            userDataMap.put("studentData1", studentData);
	            System.out.println((UserData)userDataMap.get("studentData1"));
	            mapper.writeValue(new File("student.json"), userDataMap);
	            //{
	            //   "studentData1":
	            //	 {
	            //		"student":
	            //		{
	            //			"name":"Mahesh",
	            //			"age":10
	            //      },
	            //      "name":"Mahesh Kumar",
	            //      "verified":false,
	            //      "marks":[1,2,3]
	            //   }
	            //}
	            userDataMap = mapper.readValue(new File("student.json"), Map.class);
	            //map里面保存的实体变成map，所以要转会实体类
	            UserData result = mapper.convertValue(userDataMap.get("studentData1"),UserData.class);
	            System.out.println(result.getName());
	            System.out.println(result.getVerified());
	            System.out.println(Arrays.toString(result.getMarks()));
	      } catch (JsonParseException e) {
	         e.printStackTrace();
	      } catch (JsonMappingException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	            e.printStackTrace();
	      }
	}
	
}
class UserData {
	   private Student student;
	   private String name;
	   private Boolean verified;
	   private int[] marks;

	   public UserData(){}

	   public Student getStudent() {
	      return student;
	   }
	   public void setStudent(Student student) {
	      this.student = student;
	   }
	   public String getName() {
	      return name;
	   }
	   public void setName(String name) {
	      this.name = name;
	   }
	   public Boolean getVerified() {
	      return verified;
	   }
	   public void setVerified(Boolean verified) {
	      this.verified = verified;
	   }
	   public int[] getMarks() {
	      return marks;
	   }
	   public void setMarks(int[] marks) {
	      this.marks = marks;
	   }
}

