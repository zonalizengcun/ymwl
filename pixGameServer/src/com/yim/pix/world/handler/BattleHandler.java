package com.yim.pix.world.handler;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder.Case;

import com.google.protobuf.MessageLite;
import com.yim.message.pix.game.GameCodeMaper;
import com.yim.message.pix.game.MessagePtoto.BattleArmy;
import com.yim.message.pix.game.MessagePtoto.BattleMatchRes;
import com.yim.message.pix.game.MessagePtoto.FormationRes;
import com.yim.message.pix.game.MessagePtoto.FormationSaveReq;
import com.yim.message.pix.game.MessagePtoto.FormationSaveRes;
import com.yim.message.pix.game.MessagePtoto.StartBattleRes;
import com.yim.net.packet.PacketHandler;
import com.yim.net.protocol.OpCodeMapper;
import com.yim.net.session.ClientSession;
import com.yim.pix.world.World;
import com.yim.pix.world.entity.Player;
import com.yim.pix.world.entity.Racist;
import com.yim.pix.world.entity.battle.Room;
import com.yim.pix.world.entity.battle.Square;
import com.yim.pix.world.model.RacistType;
import com.yim.pix.world.model.army.ArmyModel;
import com.yim.pix.world.model.army.ArmyTemplate;
import com.yim.pix.world.service.BattleService;
import com.yim.pix.world.service.MatchService;
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
		case GameCodeMaper.STARTBATTLEREQ:
			this.startBattle(message, session);
			break;
		}
	}

	@Override
	public int[] getOpcodes() {
		return new int[]{GameCodeMaper.FORMATIONREQ,GameCodeMaper.FORMATIONSAVEREQ,
				GameCodeMaper.BATTLEMATCHREQ,GameCodeMaper.STARTBATTLEREQ};
	}
	
	/**
	 * 
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
	 * 战斗匹配
	 * @param message
	 * @param session
	 */
	public void battleMatch(MessageLite message, ClientSession session){
		Player player = (Player)session.getClient();
		if (player != null) {
			MatchService matchService = ServiceContainer.getInstance().get(MatchService.class);
			matchService.addMatcher(player.getId());
			BattleMatchRes.Builder response = BattleMatchRes.newBuilder();
			session.send(GameCodeMaper.BATTLEMATCHRES, response.build());
		}
	}
	
	/**
	 * 开始战斗
	 * @param message
	 * @param session
	 */
	public void startBattle(MessageLite message, ClientSession session){
		Player player = (Player)session.getClient();
		if (player!=null) {
			BattleService battleService = ServiceContainer.getInstance().get(BattleService.class);
			Racist racist = player.getRacist();
			Room room = battleService.getRoom(racist.getRoomId());
			room.initRoom();
			StartBattleRes.Builder respones = StartBattleRes.newBuilder();
			respones.setMaxHp(room.getMaxHp());
			respones.setMaxHpE(room.getMaxHp());
			Square square = room.getSquare(player.getId());
			Square eSquare = room.getESquare(player.getId());
			respones.setHp(square.getHp());
			respones.setHpE(eSquare.getHp());
			respones.setMaxMagic(room.getMaxMagic());
			respones.setMaxMagicE(room.getMaxMagic());
			respones.setMagic(square.getMagic());
			respones.setMagic(eSquare.getMagic());
			respones.setIdleArmy(square.getIdleArmy());
			respones.setIdleArmyE(eSquare.getIdleArmy());
			respones.addAllBattleArmys(square.buildBattleArmys());
			respones.addAllBattleArmysE(eSquare.buildBattleArmys());
			session.send(GameCodeMaper.STARTBATTLERES, respones.build());
		}
	}
	

}
