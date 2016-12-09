package com.yim.pix.world.handler;

import com.google.protobuf.MessageLite;
import com.yim.message.pix.auth.AuthCodeMaper;
import com.yim.net.packet.PacketHandler;
import com.yim.net.session.ClientSession;

public class AuthPacketHandler implements PacketHandler{

	@Override
	public void handle(int opcode, MessageLite message, ClientSession session) throws Exception {
		switch(opcode){
		case AuthCodeMaper.AUTH_HEARTBEAT_RES:
			break;
		case AuthCodeMaper.AUTH_REGIST_RES:
			break;
		case AuthCodeMaper.AUTH_LOGIN_RES:
			break;
		}
		
	}

	@Override
	public int[] getOpcodes() {
		return null;
	}
	

}
