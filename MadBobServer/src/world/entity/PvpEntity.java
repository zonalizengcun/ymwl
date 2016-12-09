package world.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.NEW;

import world.GameNumber;

public class PvpEntity {

	//玩家id
	private int playerId;
	
	//当前分数
	private int score;
	
	//被保护开始时间
	private int protectedTime;
	
	//精华数量
	private int jinghua;
	
	//战报
	private List<PvpReport> pvpReport = new LinkedList<>();
	
	//英雄等级
	private List<Integer> heroLevels = new ArrayList<>(GameNumber.pvpMonsterNumber);
	
	/**
	 * 地图信息
	 */
	private PvpMapEntity pvpMapEntity;
	
	/**
	 * 玩家pvp怪物
	 */
	private List<MonsterEntity> monsters = new ArrayList<>();
	
	private int pvpEnemy = -1;
	
	public PvpEntity(){
		
	}
	
	
	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		if (score <= 100) {
			this.score = 100;
		}
	}

	public int getProtectedTime() {
		return protectedTime;
	}

	public void setProtectedTime(int protectedTime) {
		this.protectedTime = protectedTime;
	}

	public PvpMapEntity getPvpMapEntity() {
		return pvpMapEntity;
	}

	public void setPvpMapEntity(PvpMapEntity pvpMapEntity) {
		this.pvpMapEntity = pvpMapEntity;
	}


	public List<PvpReport> getPvpReport() {
		return pvpReport;
	}


	public void setPvpReport(List<PvpReport> pvpReport) {
		this.pvpReport = pvpReport;
	}


	public List<MonsterEntity> getMonsters() {
		return monsters;
	}


	public void setMonsters(List<MonsterEntity> monsters) {
		this.monsters = monsters;
	}


	public int getJinghua() {
		return jinghua;
	}


	public void setJinghua(int jinghua) {
		this.jinghua = jinghua;
	}


	public List<Integer> getHeroLevels() {
		return heroLevels;
	}


	public void setHeroLevels(List<Integer> heroLevels) {
		this.heroLevels = heroLevels;
	}


	public int getPvpEnemy() {
		return pvpEnemy;
	}


	public void setPvpEnemy(int pvpEnemy) {
		this.pvpEnemy = pvpEnemy;
	}

	
	
	
	
}
