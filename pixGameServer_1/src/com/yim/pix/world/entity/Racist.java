package com.yim.pix.world.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder.Case;

import org.hibernate.annotations.Type;

import com.yim.pix.world.entity.premryKey.RacistPK;

/**
 * ����
 * 
 * @author lizengcun
 *
 */
@Entity
@Table(name="racist")
@IdClass(value = RacistPK.class)
public class Racist {

	@Id
	private int playerId;
	@Id
	private int racistType;
	
	@Type(type = "com.yim.pix.world.entity.types.HerosType")
	private HeroContainer heroContainer;
	
	/**
	 * 阵型中的英雄模板id
	 */
	private int position0 = -1;
	private int position1 = -1;
	private int position2 = -1;
	private int position3 = -1;
	private int position4 = -1;
	
	
	private int roomId;
	
	

	public Racist() {
		this.heroContainer = new HeroContainer();
	}

	public Racist(int playerId, int racistType) {
		this();
		this.setPlayerId(playerId);
		this.setRacistType(racistType);
	}

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

	public int getPosition0() {
		return position0;
	}

	public void setPosition0(int position0) {
		this.position0 = position0;
	}

	public int getPosition1() {
		return position1;
	}

	public void setPosition1(int position1) {
		this.position1 = position1;
	}

	public int getPosition2() {
		return position2;
	}

	public void setPosition2(int position2) {
		this.position2 = position2;
	}

	public int getPosition3() {
		return position3;
	}

	public void setPosition3(int position3) {
		this.position3 = position3;
	}

	public int getPosition4() {
		return position4;
	}

	public void setPosition4(int position4) {
		this.position4 = position4;
	}
	
	public int getPosition(int pos){
		switch (pos) {
		case 0:
			return position0;
		case 1:
			return position1;
		case 2:
			return position2;
		case 3:
			return position3;
		case 4:
			return position4;
		default:
			return -1;
		}
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	
}
