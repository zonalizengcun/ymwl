package com.yim.net.nettyClient;

import com.google.protobuf.MessageLite;
import com.yim.net.packet.PacketHandler;
import com.yim.net.session.ClientSession;

import io.netty.channel.Channel;

public class Connector {
	private String remoteHost = "";
	private int remotePort = 0;
	private int reconnectInterval = 10;
	private Channel channel;
	private volatile boolean connect;
	private ClientSession clientSession;
	
	
	private PacketHandler packetHandler;
	
	public Connector(String host,int port,PacketHandler packetHandler){
		this.remoteHost = host;
		this.remotePort = port;
		this.packetHandler = packetHandler;
	}
	
	public Connector(String host,int port,int interval,PacketHandler packetHandler){
		this.remoteHost = host;
		this.remotePort = port;
		this.reconnectInterval = interval;
		this.packetHandler = packetHandler;
	}
	
	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public int getReconnectInterval() {
		return reconnectInterval;
	}

	public void setReconnectInterval(int reconnectInterval) {
		this.reconnectInterval = reconnectInterval;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void onRecive(int opcode,MessageLite message) throws Exception{
		ClientSession clientSession = new ClientSession(-1, channel);
		this.packetHandler.handle(opcode, message, clientSession);
	}
	
	public void onActive(){
		this.connect = true;
		this.clientSession = new ClientSession(-1, channel);
	}
	
	public void onInActive(){
		this.connect = false;
	}
	
	/**
	 * 主动断开链接
	 */
	public void disConnect(){
		this.channel.disconnect();
		this.connect = false;
	}
	
	public boolean isConnect(){
		return connect;
	}

	public ClientSession getClientSession() {
		return clientSession;
	}

	public void setClientSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}
	
}
