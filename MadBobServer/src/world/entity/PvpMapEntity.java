package world.entity;

import java.util.ArrayList;
import java.util.List;

public class PvpMapEntity {
	//怪物地图id
	private int mapId;
	
	private int bossId;
	
	private int money;
	
	//地图中怪物信息
	private List<MonsterEntity>  monsters = new ArrayList<>();
	//地图中装饰物信息
	private List<AdornmentEntity> adornments = new ArrayList<>();
	
	public PvpMapEntity(){
		
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public List<MonsterEntity> getMonsters() {
		return monsters;
	}

	public void setMonsters(List<MonsterEntity> monsters) {
		this.monsters = monsters;
	}

	public List<AdornmentEntity> getAdornments() {
		return adornments;
	}

	public void setAdornments(List<AdornmentEntity> adornments) {
		this.adornments = adornments;
	}

	public int getBossId() {
		return bossId;
	}

	public void setBossId(int bossId) {
		this.bossId = bossId;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
	
	
}
