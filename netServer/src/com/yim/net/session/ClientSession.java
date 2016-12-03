package com.yim.net.session;

import java.util.concurrent.ArrayBlockingQueue;

import com.google.protobuf.MessageLite;
import com.yim.net.packet.RequestPacket;
import com.yim.net.packet.ResponsePacket;

import io.netty.channel.Channel;
//客户端会话
public class ClientSession {
	
	private int sessionId;
	
	private ArrayBlockingQueue<RequestPacket> queue = new ArrayBlockingQueue<RequestPacket>(512);
	
	private volatile boolean isProcessing;
	
	private Channel channel;
	
	private IClient client;
	
	public ClientSession(int sessionId,Channel channel){
		this.sessionId = sessionId;
		this.setChannel(channel);
	}
	
	
	public void insertPacket(RequestPacket packet){
		synchronized (queue) {
			try {
				this.queue.put(packet);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public RequestPacket pollPacket(){
		RequestPacket packet;
		synchronized (queue) {
			packet = queue.poll();
		}
		return packet;
    }
	
	
	public boolean isProcessing(){
		return this.isProcessing;
	}
	
	public void setProcessing(boolean processing){
		this.isProcessing = processing;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}


	public Channel getChannel() {
		return channel;
	}


	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public void send(int opcode,MessageLite messageLite){
		ResponsePacket responsePacket = new ResponsePacket(opcode, messageLite);
		this.channel.writeAndFlush(responsePacket);
	}
	
	public IClient getClient(){
		return this.client;
	}
	
	public void setClient(IClient client){
		this.client = client;
	}
}
