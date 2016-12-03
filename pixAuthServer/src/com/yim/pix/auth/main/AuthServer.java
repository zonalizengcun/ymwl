package com.yim.pix.auth.main;

import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import com.yim.executor.ExecutorManager;
import com.yim.message.pix.auth.AuthCodeMaper;
import com.yim.net.AbstractNettyServer;
import com.yim.net.DefaultNettyServer;
import com.yim.net.PacketProcessor;
import com.yim.pix.auth.Config;
import com.yim.pix.auth.world.World;
import com.yim.pix.auth.world.service.AuthService;
import com.yim.redis.RedisService;
import com.yim.service.ServiceContainer;
import com.yim.service.impl.IdService;

public class AuthServer {
	
	private static Logger log = Logger.getLogger(AuthServer.class);

	public static void main(String[] args) throws Exception{
		Config config = new Config("config.properties");
		config.init();
		World.getInstance().setConfig(config);
		ServiceContainer.getInstance().register(RedisService.class, new RedisService(config.jedisIp, config.jedisPort, config.jedisPass));
		ServiceContainer.getInstance().register(IdService.class, new IdService());
		ServiceContainer.getInstance().register(AuthService.class, new AuthService());
		
		Executor logicExecutor = ExecutorManager.getDefault().getAuthLogicExecutor();
		PacketProcessor packetProcessor = new PacketProcessor(logicExecutor, World.getInstance().getHandlerDispatch());
		World.getInstance().setAuthCodeMaper(new AuthCodeMaper());
		AbstractNettyServer nettyServer = new DefaultNettyServer(packetProcessor, World.getInstance().getAuthCodeMaper());
		nettyServer.bind(config.authPort);
		
		log.info("AUTH SERVER START OK");
	}
}
