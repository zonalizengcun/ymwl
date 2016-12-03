package com.yim.net.session;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.util.AttributeKey;

/**
 * 用来缓存所有clientSession 暂时不需要缓存 暂时无用
 * @author admin
 *
 */
public class SessionContainer {

	protected static final AtomicInteger id_gen = new AtomicInteger(0);
	
	public static final AttributeKey<ClientSession> SERVERSESSIONKEY = AttributeKey.valueOf("SERVERSESSIONKEY");
	
	public static int getSessionId(){
		return id_gen.incrementAndGet();
	}
	
}
