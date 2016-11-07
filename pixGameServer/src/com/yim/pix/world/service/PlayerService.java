package com.yim.pix.world.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.yim.message.pix.game.MessagePtoto.PlayerMessage;
import com.yim.persist.EntityManager;
import com.yim.persist.EntityManagerFactory;
import com.yim.pix.world.entity.Player;
import com.yim.service.IService;
import com.yim.service.ServiceContainer;
import com.yim.service.impl.IdService;

public class PlayerService implements IService{
	
	private EntityManager entityManager = EntityManagerFactory.createManager();
	
	private Map<Integer, Player> id2Players = new HashMap<>();
	
	private Map<Integer, Player> acc2Players = new HashMap<>();
	
	private Map<String, Player> name2Players = new HashMap<>();
	
	private Object lock = new Object();

	@Override
	public void startup() {
		readDB();
		
	}

	@Override
	public void shutdown() {
		
		
	}
	
	public void readDB(){
		List<Player> players = entityManager.query(Player.class, "from Player");
		synchronized (lock) {
			for(Player player : players){
				this.id2Players.put(player.getId(), player);
				this.acc2Players.put(player.getAccountId(), player);
				this.name2Players.put(player.getName(), player);
			}
		}
	}
	
	public void saveDB(){
		entityManager.update(id2Players.values(),10);
	}
	
	public Player getPlayer(String name){
		synchronized (lock) {
			return this.name2Players.get(name);
		}
	}
	public Player getPlayer(int id){
		synchronized (lock) {
			return this.id2Players.get(id);
		}
	}
	
	public Player getPlayerByAcc(int accId){
		synchronized (lock) {
			return this.acc2Players.get(accId);
		}
	}
	
	public Player creatPlayer(String name){
		IdService idService = ServiceContainer.getInstance().get(IdService.class);
		int playerid = idService.getNextPlayerId();
		Player player = null;
		synchronized (lock) {
			if (this.name2Players.containsKey(name)) {
				return this.name2Players.get(name);
			}
			
			player = new Player(playerid);
			player.setName(name);
			player.setAccountId(playerid);
			this.id2Players.put(player.getId(), player);
			this.name2Players.put(player.getName(), player);
			this.acc2Players.put(player.getAccountId(), player);
		}
		if (player!=null) {
			this.entityManager.create(player);
		}
		return player;
	}
	
	public static PlayerMessage buildPlayerMessage(Player player){
		PlayerMessage.Builder builder = PlayerMessage.newBuilder();
		builder.setPlayerId(player.getId());
		builder.setName(player.getName());
		builder.setRacist(player.getRacistType().ordinal());
		return builder.build();
	}

}
