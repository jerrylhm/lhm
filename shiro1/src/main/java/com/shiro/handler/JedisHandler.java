package com.shiro.handler;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.InvocationHandler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisHandler implements InvocationHandler {
	
    private JedisPool jedisPool;
 
    public JedisHandler(JedisPool jedisPool){
        this.jedisPool = jedisPool;
    }
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Object invoke = method.invoke(jedis, args);
            return invoke;
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
	}
}
