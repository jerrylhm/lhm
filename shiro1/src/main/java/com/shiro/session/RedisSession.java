package com.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.SerializationUtils;

import com.shiro.util.JedisUtils;

public class RedisSession extends AbstractSessionDAO{

	@Resource
	private JedisUtils jedisUtils;
	
	private final String SHIRO_SESSION_SUFFIX = "SHIRO_SESSION_SUFFIX_";
	
	public void setSession(Session session) {
		if(session != null && session.getId() != null) {
			byte[] key = getKey(session.getId());
			byte[] value = SerializationUtils.serialize(session);
			jedisUtils.set(key, value);
		}
	}
	
	public byte[] getKey(Serializable sessionId) {
		return (SHIRO_SESSION_SUFFIX + sessionId).getBytes();
	}
	
	public void update(Session session) throws UnknownSessionException {
		setSession(session);
	}

	public void delete(Session session) {
		if(session != null && session.getId() != null) {
			byte[] key = getKey(session.getId());
			jedisUtils.del(key);
		}
	}

	public Collection<Session> getActiveSessions() {
		Set<byte[]> keys = jedisUtils.getKeys((SHIRO_SESSION_SUFFIX + "*").getBytes());
		Set<Session> result = new HashSet<Session>();		
		for (byte[] key : keys) {
			byte[] value = jedisUtils.get(key);
			Session session = (Session) SerializationUtils.deserialize(value);
			result.add(session);
		}
		return result;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		setSession(session);
		System.out.println("sessionManager:" + session.getId());
		return session.getId();
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if(sessionId != null) {
			byte[] value = jedisUtils.get(getKey(sessionId));
			Session session = (Session) SerializationUtils.deserialize(value);
			return session;
		}		
		return null;
	}

}
