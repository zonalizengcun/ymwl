package com.yim.pix.world.service;

import java.util.concurrent.TimeUnit;

import com.yim.executor.ExecutorManager;
import com.yim.message.pix.auth.AuthCodeMaper;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthHeartBeatReq;
import com.yim.net.nettyClient.AbstractNettyClient;
import com.yim.net.nettyClient.Connector;
import com.yim.net.packet.PacketHandler;
import com.yim.pix.world.Config;
import com.yim.pix.world.World;
import com.yim.pix.world.handler.AuthPacketHandler;
import com.yim.service.IService;

public class AccountService implements IService{
	
	private Connector connector;
	
	public AccountService() {
	}

	@Override
	public void startup() {
		Config config = World.getInstance().getConfig();
		PacketHandler packetHandler = new AuthPacketHandler();
		this.connector = new Connector(config.authHost, config.authPort, packetHandler);
		AbstractNettyClient nettyClient = World.getInstance().getNettyClient();
		nettyClient.tryConnect(connector);
		ExecutorManager.getDefault().getNormalScheduled().scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				AuthHeartBeatReq.Builder request = AuthHeartBeatReq.newBuilder();
				connector.getClientSession().send(AuthCodeMaper.AUTH_HEARTBEAT_REQ, request.build());
			}
		}, 30, 30, TimeUnit.SECONDS);
	}

	@Override
	public void shutdown() {
		this.connector.disConnect();
	}

}
