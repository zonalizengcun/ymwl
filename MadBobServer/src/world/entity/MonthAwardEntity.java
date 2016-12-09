package world.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 月签到奖励
 * @author lizengcun
 */
public class MonthAwardEntity {
	
	private int userId;
	
	private List<Integer> awardDays = new ArrayList<>();
	
	private int lastAwardTime;
	
	

	public List<Integer> getAwardDays() {
		return awardDays;
	}

	public void setAwardDays(List<Integer> awardDays) {
		this.awardDays = awardDays;
	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getLastAwardTime() {
		return lastAwardTime;
	}

	public void setLastAwardTime(int lastAwardTime) {
		this.lastAwardTime = lastAwardTime;
	}
	
	
	
}
