package com.yim.net;

import com.google.protobuf.MessageLite;
import com.yim.net.packet.ResponsePacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

public class ProtocolOutHandler extends ChannelOutboundHandlerAdapter {

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof ResponsePacket) {
			ResponsePacket responsePacket = (ResponsePacket) msg;
			MessageLite messageLite = responsePacket.getMessageData();
			byte[] contentBuf = messageLite.toByteArray();
			ByteBuf outBuffer = ctx.alloc().buffer(contentBuf.length + 4);
			outBuffer.writeInt(responsePacket.getOpcode());
			if (contentBuf != null) {
				outBuffer.writeBytes(contentBuf);
			}
			ctx.write(outBuffer, promise);
		} else {
			ctx.write(msg, promise);
		}

	}
}
