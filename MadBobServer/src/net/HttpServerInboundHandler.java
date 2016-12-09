package net;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import message.OpCodeEnum;
import net.session.Packet;
import net.session.SessionProcesser;
import net.session.clients.RegistSession;
import net.session.clients.TokenSession;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

	private static Logger log = Logger.getLogger(HttpServerInboundHandler.class);

	private SessionProcesser sessionProcesser;

	public HttpServerInboundHandler(SessionProcesser sessionProcesser) {
		this.sessionProcesser = sessionProcesser;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		FullHttpRequest request = (FullHttpRequest) msg;
		ByteBuf buf = request.content();
		if (!buf.isReadable()) {
			return;
		}
		int opcode = buf.readInt();
		byte[] tokendBytes = new byte[buf.readShort()];
		buf.readBytes(tokendBytes);
		String token = new String(tokendBytes, "utf-8");
		MessageLite messageLite = this.parseProtocol(opcode, this.getReadbleBytes(buf));
		buf.release();// 读取完毕
		if (opcode == OpCodeEnum.registReq.getOpcode()||opcode == OpCodeEnum.platRegistReq.getOpcode()||opcode == OpCodeEnum.clientLogReq.getOpcode()) {//注册协议
			Packet packet = new Packet(opcode, messageLite, ctx);
			if (HttpUtil.isKeepAlive(request)) {
				packet.setKeepAlive(true);
			}
			sessionProcesser.insertSession(new RegistSession(packet));
		}else{//普通协议
			Packet packet = new Packet(opcode, messageLite, ctx);
			packet.setToken(token);
			if (HttpUtil.isKeepAlive(request)) {
				packet.setKeepAlive(true);
			}
			sessionProcesser.insertSession(new TokenSession(packet, token));			
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error(cause.getMessage());
		ctx.close();
	}

	private MessageLite parseProtocol(int opcode, byte[] bytes) throws Exception {
		Class<? extends MessageLite> clazz = OpCodeEnum.getClass(opcode);
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
