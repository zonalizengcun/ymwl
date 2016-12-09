package main;

import org.apache.log4j.Logger;

import net.NettyHttpServer;
import net.manager.ClientManager;
import world.Config;
import world.World;
import world.dao.DaoManager;
import world.service.ArenaBattleService;
import world.service.ChatService;
import world.service.IDService;
import world.service.PlayerService;
import world.service.PvpService;
import world.service.ServiceCache;

public class GameServer {

	private static Logger log = Logger.getLogger(GameServer.class);
	
	public static void main(String[] args)throws Exception{
		Config config = new Config("config.properties");
		config.init();
		World.getDefault().setConfig(config);//加载服务器配置
		World.getDefault().initModel();
		DaoManager.getDefault();
		ClientManager.getDefault().initClients();//初始化玩家token等基本数据(从数据库中读取)
		//JedisTest.test();
		ServiceCache.getDefault().setIDService(new IDService());
		ServiceCache.getDefault().setPlayerService(new PlayerService());
		ServiceCache.getDefault().setChatService(new ChatService());
		ServiceCache.getDefault().setArenaBattleService(new ArenaBattleService());
		ServiceCache.getDefault().setPvpService(new PvpService());
		ServiceCache.getDefault().start();
		
		NettyHttpServer server = new NettyHttpServer(config.gamePort);
		System.out.println("Server Startup OK 8");
		log.info("Server Startup OK");
		server.start();
		
		
		
	}
}