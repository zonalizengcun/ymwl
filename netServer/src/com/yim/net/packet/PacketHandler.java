package com.yim.net.packet;

import com.google.protobuf.MessageLite;
import com.yim.net.session.ClientSession;

public interface PacketHandler {

	public void handle(int opcode,MessageLite message,ClientSession session) throws Exception;
	
	public int[] getOpcodes();
}
