package com.yim.redis;

import com.yim.service.IService;

public class RedisService implements IService{

	RedisClient redisClient;
	
	private String ip;
	
	private int port;
	
	private String passWord;
	
	public RedisService(String ip,int port,String passWord) {
		this.setIp(ip);
		this.setPort(port);
		this.setPassWord(passWord);
		this.redisClient = new RedisClient(ip, port, passWord);
	}
	
	@Override
	public void startup() {
		this.redisClient.init();
	}

	@Override
	public void shutdown() {
		
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public RedisClient getRedisClient(){
		return this.redisClient;
	}
	
	//初始化一个id
	public void initIntId(String key,int value){
		if (!this.redisClient.exists(key)) {
			this.redisClient.set(key, value+"");
		}
	}
	
	/**
	 * 获取一个全局id
	 * 
	 * @return
	 */
	public int nextIntID(String key) {
		return (int) this.redisClient.incr(key);
	}

}
