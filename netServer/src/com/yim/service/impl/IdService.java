package com.yim.service.impl;

import com.yim.redis.RedisService;
import com.yim.service.IService;
import com.yim.service.ServiceContainer;

public class IdService implements IService {
	
	public static final String DBNAME = "ID";
	
	public static final String player = "_PLAYER";
	
	public static final String account = "_ACCOUNT";
	
	private RedisService redisService; 

	@Override
	public void startup() {
		this.redisService = ServiceContainer.getInstance().get(RedisService.class);
		redisService.initIntId(DBNAME+player, 1);
	}
	

	@Override
	public void shutdown() {
		
	}
	
	/**
	 * 获取下一个playerid
	 */
	public int getNextPlayerId(){
		return this.redisService.nextIntID(DBNAME+player);
	}
	
	public int getNextAccountId(){
		return this.redisService.nextIntID(DBNAME+account);
	}

}
