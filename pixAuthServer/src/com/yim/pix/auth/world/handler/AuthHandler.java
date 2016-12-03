package com.yim.pix.auth.world.handler;

import com.google.protobuf.MessageLite;
import com.yim.message.pix.auth.AuthCodeMaper;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthHeartBeatReq;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthHeartBeatRes;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthLoginReq;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthLoginRes;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthRegistReq;
import com.yim.message.pix.auth.AuthMessagePtoto.AuthRegistRes;
import com.yim.message.pix.game.MessagePtoto.ErrorMessage;
import com.yim.net.packet.PacketHandler;
import com.yim.net.session.ClientSession;
import com.yim.pix.auth.world.entity.User;
import com.yim.pix.auth.world.exception.AuthException;
import com.yim.pix.auth.world.service.AuthService;
import com.yim.service.ServiceContainer;

public class AuthHandler implements PacketHandler {

	@Override
	public void handle(int opcode, MessageLite message, ClientSession session) throws Exception {
		switch (opcode) {
		case AuthCodeMaper.AUTH_HEARTBEAT_REQ:
			AuthHeartBeatReq(message,session);
			break;
		case AuthCodeMaper.AUTH_REGIST_REQ:
			authRegist(message, session);
			break;
		case AuthCodeMaper.AUTH_LOGIN_REQ:
			this.authLogin(message, session);
			break;
			
		}
	}

	@Override
	public int[] getOpcodes() {
		return new int[]{AuthCodeMaper.AUTH_HEARTBEAT_REQ,AuthCodeMaper.AUTH_REGIST_REQ,AuthCodeMaper.AUTH_LOGIN_REQ};
	}
	
	public void AuthHeartBeatReq(MessageLite message,ClientSession session){
		session.send(AuthCodeMaper.AUTH_HEARTBEAT_RES, AuthHeartBeatRes.newBuilder().build());
	}
	
	/**
	 * 注册账号
	 * @param message
	 * @param session
	 */
	public void authRegist(MessageLite message,ClientSession session){
		AuthRegistReq request = (AuthRegistReq)message;
		String accName = request.getAccountName();
		String passWord = request.getPassWord();
		AuthService authService = ServiceContainer.getInstance().get(AuthService.class);
		try {
			User user = authService.creatUser(accName, passWord);
			AuthRegistRes.Builder response = AuthRegistRes.newBuilder();
			response.setAccountId(user.getId());
			response.setAccountName(user.getAccountName());
			response.setPassWord(user.getPassWord());
			session.send(AuthCodeMaper.AUTH_REGIST_RES, response.build());
		} catch (AuthException e) {
			ErrorMessage.Builder builder = ErrorMessage.newBuilder();
			builder.setErrorReason(e.getMessage());
			session.send(AuthCodeMaper.ERROR, builder.build());
		}
		
	}
	
	/**
	 * 登陆账号
	 * @param message
	 * @param session
	 */
	public void authLogin(MessageLite message,ClientSession session){
		AuthLoginReq request = (AuthLoginReq)message;
		String accName = request.getAccountName();
		String passWord = request.getPassWord();
		AuthService authService = ServiceContainer.getInstance().get(AuthService.class);
		try {
			User user = authService.getUser(accName);
			if (user == null) {
				throw new AuthException("账号错误");
			}
			if (user.getPassWord().equals(passWord)) {
				throw new AuthException("密码错误");
			}
			AuthLoginRes.Builder response = AuthLoginRes.newBuilder();
			response.setAccountId(user.getId());
			session.send(AuthCodeMaper.AUTH_LOGIN_RES, response.build());
		} catch (AuthException e) {
			ErrorMessage.Builder builder = ErrorMessage.newBuilder();
			builder.setErrorReason(e.getMessage());
			session.send(AuthCodeMaper.ERROR, builder.build());
		}
	}

}
