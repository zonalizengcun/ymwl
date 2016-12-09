package world.entity;

public class WeekAwardEntity {
	
	private int userId;
	
	//是否是第一周
	private boolean first = true;
	
	//上次领奖时间
	private int lastAwardTime;
	
	//已领奖次数
	private int awardTimes;
	
	//第一周奖励的英雄id
	private int awardHeroId = -1;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public int getLastAwardTime() {
		return lastAwardTime;
	}

	public void setLastAwardTime(int lastAwardTime) {
		this.lastAwardTime = lastAwardTime;
	}

	public int getAwardTimes() {
		return awardTimes;
	}

	public void setAwardTimes(int awardTimes) {
		this.awardTimes = awardTimes;
	}

	public int getAwardHeroId() {
		return awardHeroId;
	}

	public void setAwardHeroId(int awardHeroId) {
		this.awardHeroId = awardHeroId;
	}
	
	
}
