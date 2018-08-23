package com.shiro.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.shiro.entity.User;
import com.shiro.service.EhcacheService;
import com.shiro.service.EhcacheServiceImpl;
import com.shiro.util.JedisUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/spring.xml","/spring/spring-ehcache.xml"})
public class Test {

	@Autowired
	private JedisUtils jedisUtils;
	@Autowired
	private EhcacheService ehcacheService;
	
	
//	@org.junit.Test
	public void test() {
		User user = new User();
		user.setUsername("faker");
		String objJson = JSON.toJSONString(user);
		
		Map<String, User> map = new HashMap<String, User>(); 
		map.put("xixi", user);
		map.put("haha", user);
		String mapJson = JSON.toJSONString(map,SerializerFeature.DisableCircularReferenceDetect);
		System.out.println(objJson);
		System.out.println(mapJson.toString());
	}

//	@org.junit.Test
	public void test1() {
//		System.out.println(new String(jedisUtils.get("name".getBytes())));
		
		Set<byte[]> keys = jedisUtils.getKeys(("*").getBytes());
		for (byte[] bs : keys) {
			System.out.println(new String(bs));
		}

	}
	
//	@org.junit.Test
	public void test2() {
	    // 1. 创建缓存管理器
	    CacheManager cacheManager = CacheManager.create("./src/main/resources/ehcache.xml");

	    // 2. 获取缓存对象
	    Cache cache = cacheManager.getCache("HelloWorldCache");

	    // 3. 创建元素
	    Element element = new Element("key1", "value1");

	    // 4. 将元素添加到缓存
	    cache.put(element);

	    // 5. 获取缓存
	    Element value = cache.get("key1");
	    System.out.println(value);
	    System.out.println(value.getObjectValue());

	    // 6. 删除元素
	    cache.remove("key1");

	    User dog = new User();
	    dog.setUsername("赤勾史");
	    Element element2 = new Element("goushi", dog);
	    cache.put(element2);
	    Element value2 = cache.get("goushi");
	    User dog2 = (User) value2.getObjectValue();
	    System.out.println(dog2);

	    System.out.println(cache.getSize());

	    // 7. 刷新缓存
	    cache.flush();

	    // 8. 关闭缓存管理器
	    cacheManager.shutdown();
	}
	

	@org.junit.Test
    public void testTimestamp() throws InterruptedException{
        System.out.println("第一次调用：" + ehcacheService.getTimestamp("param"));
        Thread.sleep(2000);
        System.out.println("2秒之后调用：" + ehcacheService.getTimestamp("param"));
        Thread.sleep(4000);
        System.out.println("再过4秒之后调用：" + ehcacheService.getTimestamp("param"));
    }
	
//    @Test
    public void testCache(){
        String key = "zhangsan";
        String value = ehcacheService.getDataFromDB(key); // 从数据库中获取数据...
        ehcacheService.getDataFromDB(key);  // 从缓存中获取数据，所以不执行该方法体
        ehcacheService.removeDataAtDB(key); // 从数据库中删除数据
        ehcacheService.getDataFromDB(key);  // 从数据库中获取数据...（缓存数据删除了，所以要重新获取，执行方法体）
    }
}




