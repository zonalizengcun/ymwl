package net.handler;

import java.util.HashMap;

import org.apache.log4j.Logger;

import net.session.Packet;

public class PacketHandlerManager {
	
	private Logger logger = Logger.getLogger(PacketHandlerManager.class);
	
	private HashMap<Integer, PacketHandler> packetsMap = new HashMap<Integer, PacketHandler>();
	
	public PacketHandlerManager(){
		
	}
	
	public void Regist(PacketHandler handler){
		for(int opcode : handler.getOpcodes()){
			this.packetsMap.put(opcode, handler);
		}
	}
	
	public void unRegist(PacketHandler handler){
		for(int opcode : handler.getOpcodes()){
			this.packetsMap.remove(opcode);
		}
	}
	
	public void unRegist(int opcode){
		this.packetsMap.remove(opcode);
	}
	
	public void handle(Packet packet){
		PacketHandler handler = this.packetsMap.get(packet.getOpcode());
		if (handler == null) {
			logger.error("[OPCODEMISTAKE]opcode["+packet.getOpcode()+"]");
			return;
		}
		long start = System.currentTimeMillis();
		handler.handle(packet);
		logger.debug("[OPCODETIME]"+String.valueOf(System.currentTimeMillis()-start));
	}
}
