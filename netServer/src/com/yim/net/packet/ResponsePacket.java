package com.yim.net.packet;

import com.google.protobuf.MessageLite;

public class ResponsePacket {
	private int opcode;
	
	private MessageLite messageData;
	
	public ResponsePacket(int opcode ,MessageLite messageLite){
		this.opcode = opcode;
		this.messageData = messageLite;
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
	
	
}
