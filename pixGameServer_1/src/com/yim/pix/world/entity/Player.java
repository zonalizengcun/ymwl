package com.yim.pix.world.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yim.net.session.ClientSession;
import com.yim.net.session.IClient;
import com.yim.pix.world.model.RacistType;

@Entity
@Table(name="palyer")
public class Player implements IClient {

	@Id
	private int id;
	
	private String name;
	
	private int accountId;
	
	//当前种族id
	private int racistType;
	
	@Transient
	private ClientSession session;
	
	@Transient
	private Racist racist;
	
	
	public Player(){
		
	}
	
	public Player(int id){
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	public int getId(){
		return this.id;
	}
	
	public ClientSession getSession(){
		return this.session;
	}
	
	public void setSession(ClientSession session){
		this.session = session;
	}

	public RacistType getRacistType() {
		return RacistType.values()[racistType];
	}

	public void setRacistType(int racistType) {
		this.racistType = racistType;
	}

	public Racist getRacist() {
		return racist;
	}

	public void setRacist(Racist racist) {
		this.racist = racist;
	}

	
}
