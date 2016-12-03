package com.yim.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends ChannelInboundHandlerAdapter{

	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
		IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
		if (idleStateEvent.state().equals(IdleState.READER_IDLE)) {
			ctx.channel().close();
		}
	}
	
	
}
