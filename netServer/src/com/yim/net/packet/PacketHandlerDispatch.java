package com.yim.net.packet;

import java.util.HashMap;

import com.google.protobuf.MessageLite;
import com.yim.net.session.ClientSession;

public class PacketHandlerDispatch {
	
	private HashMap<Integer, PacketHandler> handlers = new HashMap<Integer, PacketHandler>();
	
	public PacketHandlerDispatch(){
		
	}
	
	public void register(int opcode, PacketHandler packetHandler) {
		handlers.put(opcode, packetHandler);
	}
	
	public void register(int[] opcodes, PacketHandler packetHandler) {
		for(int opcode : opcodes) {
			register(opcode, packetHandler);
		}
	}
	
	public void unregister(int opcode){
		handlers.remove(opcode);
	}
	
	public void unregister(int[] opcodes){
		for(int opcode : opcodes) {
			unregister(opcode);
		}
	}
	
	public void handle(int opcode,MessageLite message,ClientSession clientSession) throws Exception{
		PacketHandler handler = handlers.get(opcode);
		if (handler != null) {
			handler.handle(opcode, message, clientSession);
		}
	}
}
