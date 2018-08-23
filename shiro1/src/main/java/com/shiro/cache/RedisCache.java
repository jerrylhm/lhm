package com.shiro.cache;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.shiro.util.JedisUtils;

@Component
public class RedisCache<K, V> implements Cache<K, V>{

	@Resource
	private JedisUtils jedisUtils;
	
	public static final String PERFIX = "SHIRO_CACHE_PERFIX_";	
	
	public byte[] getKey(K key) {
		if(key != null && key instanceof String) {
			return (PERFIX + key).getBytes();
		}else {			
			String str = key.toString();
			
			return (PERFIX + str).getBytes();
		}
	}
	
	public V get(K key) throws CacheException {		
		System.out.println("从缓存获取" + key.toString());
		byte[] realKey = getKey(key);
		byte[] value = jedisUtils.get(realKey);
		if(value != null) {
			return (V) SerializationUtils.deserialize(value);				
		}
		return null;
	}

	public V put(K key, V value) throws CacheException {
		System.out.println("放入缓存" + key.toString() + " value:" + value.toString());		
		byte[] byte_key = getKey(key);
		if(value != null) {
			jedisUtils.set(byte_key, SerializationUtils.serialize(value));
			return value;
		}
		return null;
	}

	public V remove(K key) throws CacheException {
		System.out.println("从缓存移除" + key.toString());
		byte[] value = jedisUtils.get(getKey(key));
		jedisUtils.del(getKey(key));
		if(value != null) {
			return (V) SerializationUtils.serialize(value);
		}
		return null;
	}

	public void clear() throws CacheException {
		// TODO Auto-generated method stub
		System.out.println("清空缓存");
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Set<K> keys() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

}
