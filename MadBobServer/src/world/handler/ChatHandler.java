package world.handler;

import java.io.IOException;
import java.util.List;

import com.Language;

import message.OpCodeEnum;
import message.protos.MessagePtoto.Attache;
import message.protos.MessagePtoto.ChatContent;
import message.protos.MessagePtoto.GlobalChatListRes;
import message.protos.MessagePtoto.GlobalChatReq;
import message.protos.MessagePtoto.GlobalChatRes;
import message.protos.MessagePtoto.PrivateChatListRes;
import message.protos.MessagePtoto.PrivateChatReq;
import message.protos.MessagePtoto.PrivateChatRes;
import message.protos.MessagePtoto.ReciveAttachReq;
import message.protos.MessagePtoto.ReciveAttachRes;
import net.handler.PacketHandler;
import net.session.Packet;
import net.session.UserEntity;
import utils.DateUtil;
import world.GameNumber;
import world.dao.DaoManager;
import world.entity.ChatEntity;
import world.entity.PrivateChatEntity;
import world.service.ChatService;
import world.service.PlayerService;
import world.service.ServiceCache;

public class ChatHandler implements PacketHandler{

	@Override
	public void handle(Packet packet) {
		int opcode = packet.getOpcode();
		if (opcode == OpCodeEnum.privateChatReq.getOpcode()) {
			this.privateChat(packet);
		}else if (opcode == OpCodeEnum.privateChatListReq.getOpcode()) {
			this.privateChatList(packet);
		}else if (opcode == OpCodeEnum.globalChatReq.getOpcode()) {
			this.globalChat(packet);
		}else if (opcode == OpCodeEnum.globalChatListReq.getOpcode()) {
			this.globalChatList(packet);
		}else if (opcode == OpCodeEnum.reciveAttacheReq.getOpcode()) {
			getAttache(packet);
		}
	}

	@Override
	public int[] getOpcodes() {
		return new int[]{OpCodeEnum.privateChatReq.getOpcode(),OpCodeEnum.privateChatListReq.getOpcode(),
				OpCodeEnum.globalChatReq.getOpcode(),OpCodeEnum.globalChatListReq.getOpcode(),
				OpCodeEnum.reciveAttacheReq.getOpcode()};
	}
	
	/**
	 * 领取附件
	 * @param packet
	 */
	private void getAttache(Packet packet){
		ReciveAttachReq request = (ReciveAttachReq)packet.getData();
		int chatId = request.getChatId();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		PrivateChatEntity pce = null;
		try {
			pce = DaoManager.getDefault().getPrivateChat(userEntity.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<ChatEntity> chatList = pce.getChats();
		ChatEntity chatEntity = null;
		for(ChatEntity entity : chatList){
			if (entity.getChatId() == chatId) {
				chatEntity  = entity;
				break;
			}
		}
		if (chatEntity == null) {
			packet.sendError("Message invalid !");
			return;
		}
		if (chatEntity.isAttachRecive()) {
			packet.sendError(Language.MAIL_ATTACHE);
			return;
		}
		chatEntity.setAttachRecive(true);
//		ServiceCache.getDefault().getChatService().addAttachAward(chatEntity.getAttach(), userEntity);
		try {
			DaoManager.getDefault().savePrivateChat(userEntity.getId(), pce);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ReciveAttachRes.Builder response = ReciveAttachRes.newBuilder();
		for(int key:chatEntity.getAttach().keySet()){
			Attache.Builder builder = Attache.newBuilder();
			builder.setAttchType(key);
			builder.setCount(chatEntity.getAttach().get(key));
			response.addAttaches(builder.build());
		}
		packet.setOpcode(OpCodeEnum.reciveAttacheRes.getOpcode());
		packet.writeData(response.build());
	}
	
	private void privateChat(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		PrivateChatReq request = (PrivateChatReq)packet.getData();
		int reciverId = request.getRoleId();
		String content = request.getContent();
		ServiceCache.getDefault().getChatService().privateChat(reciverId, userEntity.getId(), content);
		PrivateChatRes.Builder response = PrivateChatRes.newBuilder();
		packet.setOpcode(OpCodeEnum.privateChatRes.getOpcode());
		packet.writeData(response.build());
		
	}
	
	private void privateChatList(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		ChatService chatService = ServiceCache.getDefault().getChatService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		List<ChatEntity> chats = chatService.getPrivateChats(userEntity.getId());
		PrivateChatListRes.Builder response = PrivateChatListRes.newBuilder();
		for(ChatEntity chatEntity : chats){
			ChatContent content = chatService.buildChat(chatEntity);
			response.addContents(content);
		}
		packet.setOpcode(OpCodeEnum.privateChatListRes.getOpcode());
		packet.writeData(response.build());
	}
	
	private void globalChat(Packet packet){
		GlobalChatReq request = (GlobalChatReq)packet.getData();
		String content = request.getContent();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		ChatService chatService = ServiceCache.getDefault().getChatService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		int chatCnt = playerService.getChatTimes(userEntity);
		if (chatCnt >= GameNumber.worldChatLimit) {
			packet.sendError(Language.WORLD_CHAT_LIMIT);
			return;
		}
		userEntity.setChatCnt(userEntity.getChatCnt()+1);
		userEntity.setChatTime(DateUtil.getSecondStamp());
		DaoManager.getDefault().saveUserEntity(userEntity);
		chatService.globalChat(userEntity.getId(),content);
		GlobalChatRes.Builder response = GlobalChatRes.newBuilder();
		packet.setOpcode(OpCodeEnum.gloablChatRes.getOpcode());
		packet.writeData(response.build());
	}
	
	/**
	 * 世界聊天
	 * @param packet
	 */
	private void globalChatList(Packet packet){
		ChatService chatService = ServiceCache.getDefault().getChatService();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		List<ChatEntity> chats = chatService.getGlobalChats();
		GlobalChatListRes.Builder response = GlobalChatListRes.newBuilder();
		response.setChatLimit(GameNumber.worldChatLimit);
		response.setChatCount(GameNumber.worldChatLimit - playerService.getChatTimes(userEntity));
		for(ChatEntity chat : chats){
			ChatContent content = chatService.buildChat(chat);
			response.addContents(content);
		}
		response.setChatLimit(GameNumber.worldChatLimit);
		packet.setOpcode(OpCodeEnum.globalChatListRes.getOpcode());
		packet.writeData(response.build());
	}

}
