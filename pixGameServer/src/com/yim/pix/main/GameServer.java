package com.yim.pix.main;

import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import com.yim.executor.ExecutorManager;
import com.yim.message.pix.auth.AuthCodeMaper;
import com.yim.message.pix.game.GameCodeMaper;
import com.yim.net.AbstractNettyServer;
import com.yim.net.DefaultNettyServer;
import com.yim.net.PacketProcessor;
import com.yim.net.nettyClient.AbstractNettyClient;
import com.yim.net.nettyClient.DefaultNettyClient;
import com.yim.pix.world.Config;
import com.yim.pix.world.World;
import com.yim.pix.world.entity.Racist;
import com.yim.pix.world.service.AccountService;
import com.yim.pix.world.service.PlayerService;
import com.yim.pix.world.service.RacistService;
import com.yim.redis.RedisService;
import com.yim.service.ServiceContainer;
import com.yim.service.impl.IdService;


public class GameServer {
	
	private static Logger log = Logger.getLogger(GameServer.class);

	public static void main(String[] args) throws Exception{
		Config config = new Config("config.properties");
		config.init();
		World.getInstance().setConfig(config);
		World.getInstance().buildModles();
		ServiceContainer.getInstance().register(RedisService.class, new RedisService(config.jedisIp, config.jedisPort, config.jedisPass));
		ServiceContainer.getInstance().register(IdService.class, new IdService());
		ServiceContainer.getInstance().register(PlayerService.class, new PlayerService());
		ServiceContainer.getInstance().register(RacistService.class, new RacistService());
		AbstractNettyClient nettyClient = new DefaultNettyClient(new AuthCodeMaper());
		World.getInstance().setNettyClient(nettyClient);
		ServiceContainer.getInstance().register(AccountService.class, new AccountService());
		
		Executor logicExecutor = ExecutorManager.getDefault().getNetExecutor();
		PacketProcessor packetProcessor = new PacketProcessor(logicExecutor, World.getInstance().getClientHandlerDispatch());
		AbstractNettyServer clientNettyServer = new DefaultNettyServer(packetProcessor, new GameCodeMaper());
		clientNettyServer.bind(config.gamePort);
		
		log.info("GAME SERVER START OK");
	}
}
