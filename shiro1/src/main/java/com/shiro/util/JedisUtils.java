package com.shiro.util;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

import com.shiro.handler.JedisHandler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisUtils {

	@Resource
	private JedisPool jedisPool;
	
	public Jedis getResource() {
        Enhancer enhancer = new Enhancer();
        //设置代理的父类，就设置需要代理的类
        enhancer.setSuperclass(Jedis.class);
        //设置自定义的代理方法
        Callback callback = new JedisHandler(jedisPool);
        enhancer.setCallback(callback);
 
        Object o = enhancer.create();
        Jedis jedis = null;
        if (o instanceof Jedis){
            jedis = (Jedis) o;
        }
        return jedis;
	}
	
	public void set(byte[] key,byte[] value,int expire) {
		Jedis jedis = getResource();
		jedis.set(key, value);
		jedis.expire(key, expire);
	}
	
	public void set(byte[] key,byte[] value) {
		Jedis jedis = getResource();
		jedis.set(key, value);
		jedis.expire(key, 60*60);
	}
	
	public byte[] get(byte[] key) {
		Jedis jedis = getResource();
		return jedis.get(key);
	}
	
	public void del(byte[] key) {
		Jedis jedis = getResource();
		jedis.del(key);
	}
	
	public Set<byte[]> getKeys(byte[] pattern) {
		Jedis jedis = getResource();
		return jedis.keys(pattern);
	}
}
