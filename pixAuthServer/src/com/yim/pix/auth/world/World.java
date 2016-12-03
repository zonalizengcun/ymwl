package com.yim.pix.auth.world;

import com.yim.net.packet.PacketHandlerDispatch;
import com.yim.net.protocol.OpCodeMapper;
import com.yim.pix.auth.Config;


public class World {
	
	public static World instance;
	
	private PacketHandlerDispatch handlerDispatch = new PacketHandlerDispatch();
	
	private OpCodeMapper authCodeMaper;
	
	private Config config;
	
	public static World getInstance(){
		if (instance == null) {
			World.instance = new World();
		}
		return World.instance;
	}
	
	public World(){
		
	}
	
	public PacketHandlerDispatch getHandlerDispatch(){
		return this.handlerDispatch;
	}
	
	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public OpCodeMapper getAuthCodeMaper() {
		return authCodeMaper;
	}

	public void setAuthCodeMaper(OpCodeMapper authCodeMaper) {
		this.authCodeMaper = authCodeMaper;
	}
	
}
