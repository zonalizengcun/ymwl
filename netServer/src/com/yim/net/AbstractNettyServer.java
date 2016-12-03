package com.yim.net;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public abstract class AbstractNettyServer {

		
	private ServerBootstrap serverBootstrap;
	
	
	public AbstractNettyServer(){
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(this.getChannelInitializer());
		serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
		serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
	}
	
	
	public void bind(int port)throws InterruptedException{
		ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(port));
		future.sync();
	}
	
	protected abstract ChannelInitializer<SocketChannel> getChannelInitializer();
	
}
