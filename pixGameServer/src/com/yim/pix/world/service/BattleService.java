package com.yim.pix.world.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.yim.message.pix.game.MessagePtoto.ArmyMessage;
import com.yim.pix.world.World;
import com.yim.pix.world.entity.Player;
import com.yim.pix.world.entity.battle.Room;
import com.yim.pix.world.handler.BattleHandler;
import com.yim.pix.world.model.army.ArmyTemplate;
import com.yim.service.IService;
import com.yim.service.ServiceContainer;

public class BattleService implements IService{
	
	private static final AtomicInteger rommIdNumber = new AtomicInteger(1);
	
	private Map<Integer, Room> roomMap;
	
	public BattleService() {
		roomMap = new HashMap<>();
	}

	@Override
	public void startup() {
		BattleHandler battleHandler = new BattleHandler();
		World.getInstance().getClientHandlerDispatch().register(battleHandler.getOpcodes(), battleHandler);
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}
	
	public ArmyMessage buildArmyMessage(ArmyTemplate template){
		ArmyMessage.Builder builder = ArmyMessage.newBuilder();
		builder.setArmyId(template.id);
		builder.setArmyType(template.armyType);
		builder.setName(template.armyName);
		builder.setAtk(template.atk);
		builder.setDef(template.def);
		builder.setRound(template.round);
		builder.setLevel(template.level);
		builder.setDescribe(template.describe);
		builder.setIcon(template.icon);
		return builder.build();
	}
	
	/**
	 * 创建房间
	 * @param player1Id
	 * @param player2Id
	 * @return room
	 */
	public Room creatRoom(Player player1,Player player2){
		synchronized (roomMap) {
			Room room = new Room(rommIdNumber.getAndIncrement(),player1,player2);
			this.roomMap.put(room.getRoomId(), room);
			return room;
		}
	}
	
	/**
	 * 根据房间id取得房间
	 * @param id
	 * @return room
	 */
	public Room getRoom(int id){
		synchronized (roomMap) {
			return this.roomMap.get(id);
		}
	}
	

}
