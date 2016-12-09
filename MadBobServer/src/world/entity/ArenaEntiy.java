package world.entity;

import utils.DateUtil;
import world.GameNumber;

/**
 * 玩家竞技场数据
 * @author admin
 *
 */
public class ArenaEntiy {
	
	private int roleId;
	
	private int maxScore;
	
	private int lastScore;
	
	/**
	 * 上次挑战时间
	 */
	private long lastBattleTime;
	
	
	/**
	 * 当日挑战次数
	 */
	private int battleTimes;
	
	/**
	 * 当前入场券
	 */
	private String ticket;

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public int getLastScore() {
		return lastScore;
	}

	public void setLastScore(int lastScore) {
		this.lastScore = lastScore;
	}

	public long getLastBattleTime() {
		return lastBattleTime;
	}

	public void setLastBattleTime(long lastBattleTime) {
		this.lastBattleTime = lastBattleTime;
	}

	public int getBattleTimes() {
		return battleTimes;
	}

	public void setBattleTimes(int battleTimes) {
		this.battleTimes = battleTimes;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	/**
	 * 获取当日已挑战次数
	 * @return
	 */
	public int getLeaveTimes(){
		if (!DateUtil.isSameDay(this.lastBattleTime, System.currentTimeMillis())) {
			this.battleTimes = 0;
			this.lastBattleTime = System.currentTimeMillis();
		}
		return GameNumber.arenaTimes - this.battleTimes;
	}
	/**
	 * 增加一次当日挑战次数
	 */
	public void addBattleTimes(){
		if (!DateUtil.isSameDay(this.lastBattleTime, System.currentTimeMillis())) {
			this.battleTimes = 0;
		}
		this.lastBattleTime = System.currentTimeMillis();
		this.battleTimes += 1;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	
	
	
	
}
