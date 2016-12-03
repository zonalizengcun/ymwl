package com.yim.net.nettyClient;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.google.protobuf.MessageLite;
import com.yim.net.HeartBeatHandler;
import com.yim.net.PacketProcessor;
import com.yim.net.ProtocolOutHandler;
import com.yim.net.protocol.OpCodeMapper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;

public class DefaultNettyClient extends AbstractNettyClient {
	
	private static final Logger log = Logger.getLogger(DefaultNettyClient.class);
	
	private static final int idleTime = 40;
	
	private OpCodeMapper opCodeMapper;
	
	public DefaultNettyClient(OpCodeMapper opCodeMapper) {
		super();
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
				channel.pipeline().addLast("protocolIn",new ChannelInboundHandlerAdapter(){
					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						Connector connector = ctx.channel().attr(SESSIONKEY).get();
						connector.setChannel(ctx.channel());
						connector.onActive();
					}

					@Override
					public void channelInactive(ChannelHandlerContext ctx) throws Exception {
						final Connector connector = ctx.channel().attr(SESSIONKEY).get();
						connector.onInActive();
						tryConnect(connector);
					}

					@Override
					public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
						log.error(cause.getMessage());
					}

					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						Connector connector = ctx.channel().attr(SESSIONKEY).get();
						if (!ByteBuf.class.isAssignableFrom(msg.getClass())){
							return;
						}
						ByteBuf buffer = (ByteBuf)msg;
						int opcode = buffer.readInt();
						MessageLite messageLite = this.parseProtocol(opcode, this.getReadbleBytes(buffer));
						buffer.release();
						connector.onRecive(opcode, messageLite);
					}
					
					private MessageLite parseProtocol(int opcode, byte[] bytes) throws Exception {
						Class<? extends MessageLite> clazz = opCodeMapper.getClass(opcode);
						if (clazz != null) {
							Method method = clazz.getDeclaredMethod("parseFrom", byte[].class);
							MessageLite messageLite = (MessageLite) method.invoke(null, bytes);
							return messageLite;
						} else {
							throw new Exception("[OPCODEMIS]CODE[" + opcode + "]");
						}
					}
					
					/**
					 * 获取bytebuffer中未读取的字节
					 * 
					 * @param bytebuf
					 * @return
					 */
					private byte[] getReadbleBytes(ByteBuf bytebuf) {
						int lenght = bytebuf.readableBytes();
						if (lenght == 0) {
							return new byte[0];
						} else {
							byte[] arr = new byte[lenght];
							bytebuf.getBytes(bytebuf.readerIndex(), arr);
							return arr;
						}
					}
				});
				channel.pipeline().addLast("protocolOut",new ProtocolOutHandler());
			}
		};
	}
	
	
	
}
