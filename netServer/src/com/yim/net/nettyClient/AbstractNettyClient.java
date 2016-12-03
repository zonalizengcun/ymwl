package com.yim.net.nettyClient;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

public abstract class AbstractNettyClient {
	private static final int WORKERTHREADNUMBER = Runtime.getRuntime().availableProcessors();
	
	protected final AttributeKey<Connector> SESSIONKEY = AttributeKey.valueOf("SESSIONKEY");
	
	private EventLoopGroup workerGroup = null;
	
	private final Bootstrap bootstrap = new Bootstrap();
	
	public AbstractNettyClient() {
		workerGroup = new NioEventLoopGroup(WORKERTHREADNUMBER);
		bootstrap.group(workerGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(this.getChannelInitializer());
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);//10s
	}
	
	protected abstract ChannelInitializer<SocketChannel> getChannelInitializer();
	
	public void tryConnect(final Connector connector){
		if (connector.isConnect()) {
			return;
		}
		final String host = connector.getRemoteHost();
		final int port = connector.getRemotePort();
		final int interval = connector.getReconnectInterval();
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
		future.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture f) throws Exception {
				if (f.isSuccess()) {
					f.channel().attr(SESSIONKEY).set(connector);
				} else {
					f.channel().eventLoop().schedule(new Runnable() {
						public void run() {
							tryConnect(connector);
						}
					}, interval, TimeUnit.SECONDS);
				}
			}
		});
	}
}
