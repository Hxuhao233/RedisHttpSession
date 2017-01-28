package com.hxuhao.test;

import org.junit.Test;

import com.hxuhao.redis.RedisConfig;
import com.hxuhao.redis.RedisConfig.RedisConnectionConfig;
import com.hxuhao.redis.RedisConfig.RedisServerConfig;
import com.hxuhao.redis.RedisConnection;
import com.hxuhao.redis.RedisManager;

public class RedisTest {
	
	
	// read config file
	//@Test
	public void configTest(){
		RedisConfig config = RedisConfig.create();
		if(config != null){
			RedisConnectionConfig connectionConfig = config.getConnectionConfig();
			RedisServerConfig serverConfig = config.getRedisServers().get(0);
			if(serverConfig != null){
				System.out.println("ip : " + serverConfig.getIp());
				System.out.println("port : " + serverConfig.getPort());
				System.out.println("password : " + serverConfig.getPassword());
			}
			if(connectionConfig != null){
				System.out.println("maxTotal :" + connectionConfig.getMaxTotal());
				System.out.println("maxIdle :" + connectionConfig.getMaxIdle());
				System.out.println("maxWait :" + connectionConfig.getMaxWait());
				System.out.println("timeout :" + connectionConfig.getTimeout());
			}
		}
	}
	
	//@Test
	public void hashOpsTest(){
		RedisConnection connection = RedisManager.getInstance().getConnection();
		System.out.println(connection.hset("hxuhao", "name", new String("何徐昊")));
		String content = (String)connection.hget("hxuhao", "name");
		System.out.println(content);
	}
	
}
