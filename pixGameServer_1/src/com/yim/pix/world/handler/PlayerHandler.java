package com.yim.pix.world.handler;

import org.apache.log4j.Logger;

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
import com.yim.pix.world.model.RacistType;
import com.yim.pix.world.service.PlayerService;
import com.yim.pix.world.service.RacistService;
import com.yim.service.ServiceContainer;
import com.yim.util.DateUtil;

public class PlayerHandler implements PacketHandler {
	
	private static Logger log = Logger.getLogger(PlayerHandler.class);

	@Override
	public void handle(int opcode, MessageLite message, ClientSession session) throws Exception {
		switch (opcode) {
		case GameCodeMaper.HEARTBEATREQ:
			this.heartBeat(message, session);
			break;
		case GameCodeMaper.LOGINREQ:
			this.login(message, session);
			break;
			
		case OpCodeMapper.clientDisConnect://����˳�
			this.logout(message, session);
			break;

		}
	}

	@Override
	public int[] getOpcodes() {
		return new int[]{GameCodeMaper.HEARTBEATREQ,GameCodeMaper.LOGINREQ, OpCodeMapper.clientDisConnect};
	}
	
	/**
	 * �������� ���ط�����ʱ��
	 * @param message
	 * @param session
	 */
	public void heartBeat(MessageLite message, ClientSession session){
		HeartBeatRes.Builder response = HeartBeatRes.newBuilder();
		response.setTime(DateUtil.getSecondStamp());
		session.send(GameCodeMaper.HEARTBEATRES, response.build());
		log.info("[HERATBEAT]");
	}
	/**
	 * ģ���½
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
			Racist racist = racistService.creatRacist(player.getId(), racistType);
			player.setRacist(racist);
			
		}else {
			Racist racist = racistService.getRacist(player.getId(),RacistType.values()[racistType]);
			if (racist == null) {
				racistService.creatRacist(player.getId(), racistType);
			}
			player.setRacist(racistService.getRacist(player.getId(), player.getRacistType()));
		}
		session.setClient(player);
		player.setSession(session);
		EntityManagerFactory.createManager().saveOrUpdate(player);
		
		LoginRes.Builder response = LoginRes.newBuilder();
		response.setName(name);
		response.setPlayerId(player.getId());
		session.send(GameCodeMaper.LOGINRES, response.build());
		log.info("[LOGOUT]PLAYERID["+player.getId()+"]PLAYERNAME["+player.getName()+"]");
	}
	
	/**
	 * ����˳���Ϸ
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
			log.info("[LOGOUT]PLAYERID["+player.getId()+"]PLAYERNAME["+player.getName()+"]");
			
		}
	}
	

}
