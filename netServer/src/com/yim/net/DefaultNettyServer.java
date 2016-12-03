package com.yim.net;

import com.yim.net.protocol.OpCodeMapper;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class DefaultNettyServer extends AbstractNettyServer {
	
	private static int idleTime = 40;
	
	private PacketProcessor packetProcessor;
	
	private OpCodeMapper opCodeMapper;

	public DefaultNettyServer(PacketProcessor packetProcessor,OpCodeMapper opCodeMapper) {
		this.packetProcessor = packetProcessor;
		this.opCodeMapper = opCodeMapper;
	}
	@Override
	protected ChannelInitializer<SocketChannel> getChannelInitializer() {
		
		return new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel channel) throws Exception {
				
				channel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(204800, 0, 4,0,4));
				channel.pipeline().addLast("fieldPrepender",new LengthFieldPrepender(4,false));
				channel.pipeline().addLast("IdleHandler", new IdleStateHandler(idleTime, 0, 0));
				channel.pipeline().addLast("hearbeat",new HeartBeatHandler());
				channel.pipeline().addLast("protocolIn",new ProtocolInHandler(packetProcessor,opCodeMapper));
				channel.pipeline().addLast("protocolOut",new ProtocolOutHandler());
				
			}
		};
	}

}
