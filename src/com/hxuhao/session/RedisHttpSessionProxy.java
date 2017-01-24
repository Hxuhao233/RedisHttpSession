package com.hxuhao.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.hxuhao.redis.RedisConnection;
import com.hxuhao.redis.RedisManager;



public class RedisHttpSessionProxy implements InvocationHandler{

	
	private Object target;
	
	public Object bind(Object target){
		this.target = target;
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RedisHttpSession redisHttpSession = (RedisHttpSession) target;
		RedisConnection connection = redisHttpSession.getRedisConnection();
		if(!connection.isConnected()){
			connection.close();
			redisHttpSession.setRedisConnection(RedisHttpSessionRepository.getInstance().getRedisConnection());
		}
		
		if(!redisHttpSession.isValidated()){
			Object result = method.invoke(proxy, args);
			if(!method.equals("invalidate")){
				redisHttpSession.setLastAccessTime(System.currentTimeMillis());
				redisHttpSession.refresh();
			}
			return result;
		}else{
			throw new IllegalStateException("this session has been invalidated!");
		}

		
	}

}
