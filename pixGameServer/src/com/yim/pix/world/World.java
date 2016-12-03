package com.yim.pix.world;

import java.io.IOException;

import com.yim.net.nettyClient.AbstractNettyClient;
import com.yim.net.packet.PacketHandlerDispatch;
import com.yim.pix.world.model.army.ArmyModel;
import com.yim.pix.world.model.army.ArmyModleBuilder;

import jxl.read.biff.BiffException;


public class World {
	
	public static World instance;
	
	/**
	 * 用来分发客户端到服务器的协议
	 */
	public PacketHandlerDispatch clientHandlerDispatch = new PacketHandlerDispatch();
	
	private AbstractNettyClient nettyClient;
	
	private Config config;
	
	
	private ArmyModel armyModel;
	
	public static World getInstance(){
		if (instance == null) {
			World.instance = new World();
		}
		return World.instance;
	}
	
	public World(){
		
	}
	
	public void buildModles() throws BiffException, IOException{
		ArmyModleBuilder armyModleBuilder = new ArmyModleBuilder();
		armyModleBuilder.build();
		this.armyModel = armyModleBuilder.getArmyModel();
	}
	
	public PacketHandlerDispatch getClientHandlerDispatch(){
		return this.clientHandlerDispatch;
	}
	
	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public AbstractNettyClient getNettyClient() {
		return nettyClient;
	}

	public void setNettyClient(AbstractNettyClient nettyClient) {
		this.nettyClient = nettyClient;
	}
	
	public ArmyModel getArmyModel(){
		return this.armyModel;
	}
	
}
