package com.yim.net;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.google.protobuf.MessageLite;
import com.yim.net.packet.RequestPacket;
import com.yim.net.protocol.OpCodeMapper;
import com.yim.net.session.ClientSession;
import com.yim.net.session.SessionContainer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ProtocolInHandler extends ChannelInboundHandlerAdapter{
	
	
	private static Logger log = Logger.getLogger(ProtocolInHandler.class);
	
	private PacketProcessor packetProcessor;
	
	private OpCodeMapper opCodeMapper;
	
	public ProtocolInHandler(PacketProcessor packetProcessor,OpCodeMapper opCodeMapper){
		this.packetProcessor = packetProcessor;
		this.opCodeMapper = opCodeMapper;
	}

	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//创建session
		ClientSession clientSession = new ClientSession(SessionContainer.getSessionId(),ctx.channel());
		ctx.channel().attr(SessionContainer.SERVERSESSIONKEY).set(clientSession);
		
	}
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ClientSession clientSession = ctx.channel().attr(SessionContainer.SERVERSESSIONKEY).get();
		RequestPacket requestPacket = new RequestPacket(OpCodeMapper.clientDisConnect, null, clientSession);
		this.packetProcessor.insertPacket(requestPacket);
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error(cause.getMessage());
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (!ByteBuf.class.isAssignableFrom(msg.getClass())){
			return;
		}
		ByteBuf buffer = (ByteBuf)msg;
		ClientSession clientSession = ctx.channel().attr(SessionContainer.SERVERSESSIONKEY).get();
		int opcode = buffer.readInt();
		MessageLite messageLite = this.parseProtocol(opcode, this.getReadbleBytes(buffer));
		buffer.release();
		RequestPacket requestPacket = new RequestPacket(opcode,messageLite,clientSession);
		this.packetProcessor.insertPacket(requestPacket);
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
}
