package com.yim.pix.world.entity.premryKey;

import java.io.Serializable;

public class RacistPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int playerId;
	
	private int racistType;
	
	public RacistPK(){}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getRacistType() {
		return racistType;
	}

	public void setRacistType(int racistType) {
		this.racistType = racistType;
	}
	
	@Override
	public boolean equals(Object object){
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (object instanceof RacistPK) {
			RacistPK pk = (RacistPK)object;
			if (pk.playerId == this.playerId && pk.racistType == this.racistType) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		int result = 17;
		result = 37*result + playerId;
		result = 37*result + racistType;
		return result;
	}
}
