package com.creator.common.util;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	
	private static RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private RedisTemplate<String, String> template;
	
	@PostConstruct
	private void init() {
		redisTemplate = template;
	}
	
	public static void add(String key,String value,int ex) {
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.expire(key, ex, TimeUnit.SECONDS);
	}
	
	public static void add(String key,String value) {
		add(key, value,60 * 60);
	}
	
	public static String get(String key) {
		String result = redisTemplate.opsForValue().get(key);
		return result;
	}
	
	public static void delete(String key) {
		redisTemplate.delete(key);
	}
}
