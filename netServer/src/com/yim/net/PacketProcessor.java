package com.yim.net;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import com.yim.net.packet.RequestPacket;
import com.yim.net.packet.PacketHandlerDispatch;
import com.yim.net.session.ClientSession;
import com.yim.net.session.SessionTask;

/**
 * 消息处理
 * @author admin
 *
 */
public class PacketProcessor implements Runnable{
	
	private Executor executor;
	
	private PacketHandlerDispatch handlerDispatch;
	
	private Logger logger = Logger.getLogger(PacketProcessor.class);
	
	private ArrayBlockingQueue<RequestPacket> packets = new ArrayBlockingQueue<RequestPacket>(1024);
	
	
	public PacketProcessor(Executor executor,PacketHandlerDispatch handlerDispatch) {
		this.executor = executor;
		this.handlerDispatch = handlerDispatch;
		Thread thread = new Thread(this,"packetQueue");
		thread.start();
	}
	
	public void insertPacket(RequestPacket packet){
		try {
			this.packets.put(packet);
		} catch (InterruptedException e) {
			logger.error("[SESSIONPUT]FAIL[线程中断]");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				RequestPacket packet = this.packets.take();
				ClientSession session = packet.getClientSession();
				session.insertPacket(packet);
				if (session.isProcessing()) {
					continue;
				}
				session.setProcessing(true);
				this.executor.execute(new SessionTask(session, handlerDispatch));
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("[MESSAGEPACKTAKE]FAIL[线程中断]");
			}
		}
		
	}

}
