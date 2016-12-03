package com.yim.net.session;

import com.yim.net.packet.RequestPacket;
import com.yim.net.packet.PacketHandlerDispatch;
/**
 * session中消息队列以及处理
 * @author admin
 *
 */
public class SessionTask implements Runnable {
	
	private ClientSession clientSession;
	
	private PacketHandlerDispatch handlerDispatch;
	
	
	public SessionTask(ClientSession clientSession,PacketHandlerDispatch handlerDispatch) {
		this.clientSession = clientSession;
		this.handlerDispatch = handlerDispatch;
	}

	@Override
	public void run() {
		while(true){
			RequestPacket messagePacket = clientSession.pollPacket();
			if (messagePacket == null) {
				clientSession.setProcessing(false);
				break;
			}
			try {
				this.handlerDispatch.handle(messagePacket.getOpcode(), messagePacket.getMessageData(), clientSession);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
