package com.yim.pix.auth.world.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user")
@Entity
public class User {

	@Id
	private int id;
	
	private String accountName;
	
	private String passWord;
	
	public User(){}
	
	public User(int id){
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	
	
}
