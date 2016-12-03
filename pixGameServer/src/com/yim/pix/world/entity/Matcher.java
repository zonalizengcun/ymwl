package com.yim.pix.world.entity;

public class Matcher {

	private int playerId;
	
	private int startTime;
	
	public Matcher(int playerId,int startTime) {
		this.playerId = playerId;
		this.startTime = startTime;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
}
