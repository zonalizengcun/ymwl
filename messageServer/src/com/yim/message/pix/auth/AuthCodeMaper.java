package com.yim.message.pix.auth;

import com.yim.message.pix.auth.AuthMessagePtoto.AuthHeartBeatReq;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthHeartBeatRes;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthLoginReq;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthLoginRes;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthRegistReq;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthRegistRes;
import com.yim.message.pix.auth.AuthMessagePtoto.ErrorMessage;
import com.yim.net.protocol.OpCodeMapper;
/**
 *
 * @author admin
 *
 */
public class AuthCodeMaper extends OpCodeMapper{
	
	public AuthCodeMaper() {
		super();
	}
	
	public static final int ERROR = 0;
	
	public static final int AUTH_HEARTBEAT_REQ = 1;
	public static final int AUTH_HEARTBEAT_RES = 2;
	
	public static final int AUTH_REGIST_REQ = 3;
	public static final int AUTH_REGIST_RES = 4;
	
	public static final int AUTH_LOGIN_REQ = 5;
	public static final int AUTH_LOGIN_RES = 6;

	@Override
	public void initProtocol() {
		this.codeToClazz.put(ERROR, ErrorMessage.class);
		
		this.codeToClazz.put(AUTH_HEARTBEAT_REQ, AuthHeartBeatReq.class);
		this.codeToClazz.put(AUTH_HEARTBEAT_RES, AuthHeartBeatRes.class);
		
		this.codeToClazz.put(AUTH_REGIST_REQ,AuthRegistReq.class);
		this.codeToClazz.put(AUTH_REGIST_RES, AuthRegistRes.class);
		
		this.codeToClazz.put(AUTH_LOGIN_REQ, AuthLoginReq.class);
		this.codeToClazz.put(AUTH_LOGIN_RES, AuthLoginRes.class);
	}

}
