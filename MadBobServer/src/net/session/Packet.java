package net.session;

import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import message.OpCodeEnum;
import message.protos.MessagePtoto.ErrorMessage;

public class Packet {

	private int opcode;

	private MessageLite data;

	private ChannelHandlerContext ctx;
	
	private boolean keepAlive;
	
	private String token;

	public Packet(int opcode, MessageLite data, ChannelHandlerContext ctx) {
		this.opcode = opcode;
		this.data = data;
		this.ctx = ctx;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public MessageLite getData() {
		return data;
	}

	public void setData(MessageLite data) {
		this.data = data;
	}

	public void writeData(MessageLite data) {
		
		ByteBuf titleBuf = Unpooled.buffer();
		ByteBuf databuf = Unpooled.wrappedBuffer(data.toByteArray());
		titleBuf.writeInt(databuf.readableBytes());
		titleBuf.writeInt(opcode);
		ByteBuf allBuf = Unpooled.wrappedBuffer(titleBuf, databuf);
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.wrappedBuffer(allBuf));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		 if (keepAlive) {
			 response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderNames.KEEP_ALIVE);
		 }
		ctx.write(response);
		ctx.flush();
	}
	
	public void sendError(String errorMsg){
		this.opcode = OpCodeEnum.ErrorMessage.getOpcode();
		ErrorMessage.Builder message = ErrorMessage.newBuilder();
		message.setErrorReason(errorMsg);
		this.writeData(message.build());
	}


	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
