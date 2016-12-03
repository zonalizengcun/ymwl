package com.yim.net.packet;

import com.google.protobuf.MessageLite;
import com.yim.net.session.ClientSession;

public class RequestPacket {
	
	private int opcode;
	
	private MessageLite messageData;
	
	private ClientSession clientSession;
	
	public RequestPacket(int opcode,MessageLite messageData,ClientSession clientSession){
		this.opcode = opcode;
		this.messageData = messageData;
		this.clientSession = clientSession;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public MessageLite getMessageData() {
		return messageData;
	}

	public void setMessageData(MessageLite messageData) {
		this.messageData = messageData;
	}

	public ClientSession getClientSession() {
		return clientSession;
	}

	public void setClientSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}
	
	
	
}
