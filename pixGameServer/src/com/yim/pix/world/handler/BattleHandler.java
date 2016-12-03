package com.yim.pix.world.handler;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder.Case;

import com.google.protobuf.MessageLite;
import com.yim.message.pix.game.GameCodeMaper;
import com.yim.message.pix.game.MessagePtoto.FormationRes;
import com.yim.message.pix.game.MessagePtoto.FormationSaveReq;
import com.yim.message.pix.game.MessagePtoto.FormationSaveRes;
import com.yim.net.packet.PacketHandler;
import com.yim.net.session.ClientSession;
import com.yim.pix.world.World;
import com.yim.pix.world.entity.Player;
import com.yim.pix.world.entity.Racist;
import com.yim.pix.world.model.RacistType;
import com.yim.pix.world.model.army.ArmyModel;
import com.yim.pix.world.model.army.ArmyTemplate;
import com.yim.pix.world.service.BattleService;
import com.yim.service.ServiceContainer;

public class BattleHandler implements PacketHandler{

	@Override
	public void handle(int opcode, MessageLite message, ClientSession session) throws Exception {
		switch (opcode) {
		case GameCodeMaper.FORMATIONREQ:
			this.getFormation(message, session);
			break;
		case GameCodeMaper.FORMATIONSAVEREQ:
			this.saveFormation(message, session);
			break;
		case GameCodeMaper.BATTLEMATCHREQ:
			this.battleMatch(message, session);
			break;
		}
	}

	@Override
	public int[] getOpcodes() {
		return new int[]{GameCodeMaper.FORMATIONREQ,GameCodeMaper.FORMATIONSAVEREQ,GameCodeMaper.BATTLEMATCHREQ};
	}
	
	/**
	 * 获取阵型
	 * @param message
	 * @param session
	 */
	public void getFormation(MessageLite message, ClientSession session){
		Player player = (Player)session.getClient();
		Racist racist = player.getRacist();
		if (racist != null) {
			BattleService battleService = ServiceContainer.getInstance().get(BattleService.class);
			FormationRes.Builder response = FormationRes.newBuilder();
			response.setPosition0(racist.getPosition0());
			response.setPosition1(racist.getPosition1());
			response.setPosition2(racist.getPosition2());
			response.setPosition3(racist.getPosition3());
			response.setPosition4(racist.getPosition4());
			ArmyModel armyModel = World.getInstance().getArmyModel();
			Collection<ArmyTemplate> templates = armyModel.getArmyTemplates(RacistType.values()[racist.getRacistType()]);
			for(ArmyTemplate template : templates){
				response.addArmys(battleService.buildArmyMessage(template));
			}
			session.send(GameCodeMaper.FORMATIONRES, response.build());
		}
	}
	
	/**
	 * 保存阵型
	 */
	public void saveFormation(MessageLite message, ClientSession session){
		Player player = (Player)session.getClient();
		Racist racist = player.getRacist();
		if (racist != null) {
			FormationSaveReq request = (FormationSaveReq)message;
			racist.setPosition0(request.getPosition0());
			racist.setPosition1(request.getPosition1());
			racist.setPosition2(request.getPosition2());
			racist.setPosition3(request.getPosition3());
			racist.setPosition4(request.getPosition4());
			FormationSaveRes.Builder response = FormationSaveRes.newBuilder();
			session.send(GameCodeMaper.FORMATIONRES, response.build());
		}
	}
	
	/**
	 * 匹配战斗
	 * @param message
	 * @param session
	 */
	public void battleMatch(MessageLite message, ClientSession session){
		
	}
	
	

}
