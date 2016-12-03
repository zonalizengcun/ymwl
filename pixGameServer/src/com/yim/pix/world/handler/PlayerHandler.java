package com.yim.pix.world.handler;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder.Case;

import com.google.protobuf.MessageLite;
import com.yim.message.pix.game.GameCodeMaper;
import com.yim.message.pix.game.MessagePtoto.HeartBeatRes;
import com.yim.message.pix.game.MessagePtoto.LoginReq;
import com.yim.message.pix.game.MessagePtoto.LoginRes;
import com.yim.net.packet.PacketHandler;
import com.yim.net.protocol.OpCodeMapper;
import com.yim.net.session.ClientSession;
import com.yim.persist.EntityManagerFactory;
import com.yim.pix.world.entity.Player;
import com.yim.pix.world.entity.Racist;
import com.yim.pix.world.service.PlayerService;
import com.yim.pix.world.service.RacistService;
import com.yim.service.ServiceContainer;
import com.yim.util.DateUtil;

public class PlayerHandler implements PacketHandler {

	@Override
	public void handle(int opcode, MessageLite message, ClientSession session) throws Exception {
		switch (opcode) {
		case GameCodeMaper.HEARTBEATREQ:
			
			break;
		case GameCodeMaper.LOGINREQ:
			break;
			
		case OpCodeMapper.clientDisConnect://玩家退出
			
			break;

		}
	}

	@Override
	public int[] getOpcodes() {
		return new int[]{GameCodeMaper.HEARTBEATREQ,GameCodeMaper.LOGINREQ};
	}
	
	/**
	 * 心跳处理 返回服务器时间
	 * @param message
	 * @param session
	 */
	public void heartBeat(MessageLite message, ClientSession session){
		HeartBeatRes.Builder response = HeartBeatRes.newBuilder();
		response.setTime(DateUtil.getSecondStamp());
		session.send(GameCodeMaper.HEARTBEATRES, response.build());
	}
	/**
	 * 模拟登陆
	 * @param message
	 * @param session
	 */
	public void login(MessageLite message, ClientSession session){
		LoginReq request = (LoginReq)message;
		String name = request.getName();
		int racistType = request.getRacist();
		PlayerService playerService = ServiceContainer.getInstance().get(PlayerService.class);
		RacistService racistService = ServiceContainer.getInstance().get(RacistService.class);
		Player player = playerService.getPlayer(name);
		if (player == null) {
			player = playerService.creatPlayer(name);
			player.setRacistType(racistType);
			player.setRacist(racistService.creatRacist(player.getId(), racistType));
			
		}else {
			player.setRacist(racistService.getRacist(player.getId(), player.getRacistType()));
		}
		session.setClient(player);
		player.setSession(session);
		EntityManagerFactory.createManager().saveOrUpdate(player);
		
		LoginRes.Builder response = LoginRes.newBuilder();
		response.setName(name);
		response.setPlayerId(player.getId());
		session.send(GameCodeMaper.LOGINRES, response.build());
	}
	
	/**
	 * 玩家退出游戏
	 * @param message
	 * @param session
	 */
	public void logout(MessageLite message, ClientSession session){
		Player player = (Player)session.getClient();
		if (player!=null) {
			EntityManagerFactory.createManager().update(player);
			if (player.getRacist()!=null) {
				player.setRacist(null);
				EntityManagerFactory.createManager().update(player.getRacist());
			}
			RacistService racistService = ServiceContainer.getInstance().get(RacistService.class);
			racistService.unLoadRacist(player.getId());
			
		}
	}
	

}
