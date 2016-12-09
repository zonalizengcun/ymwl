package world.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.Language;

import message.protos.MessagePtoto.Attache;
import message.protos.MessagePtoto.ChatContent;
import net.session.UserEntity;
import utils.DateUtil;
import world.GameNumber;
import world.World;
import world.dao.DaoManager;
import world.entity.ChatEntity;
import world.entity.PrivateChatEntity;
import world.handler.ChatHandler;

public class ChatService implements Iservice{

	@Override
	public void start() {
		World.getDefault().getHandlerManager().Regist(new ChatHandler());	
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}
	
	public static final int chatType_Award = 1;
	public static final int chatType_friend = 2;
	
	/**
	 * 私聊
	 * @param reciver 接受者
	 * @param senderId 发送者
	 * @param content 消息内容
	 */
	public void privateChat(int reciver,int senderId,String content){
		ChatEntity chatEntity = new ChatEntity();
		chatEntity.setRoleId(senderId);
		chatEntity.setContent(content);
		PrivateChatEntity pce = null;
		try {
			pce = DaoManager.getDefault().getPrivateChat(reciver);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (pce == null) {
			pce = new PrivateChatEntity();
		}
		if (pce.getChats().size() < GameNumber.privateChatLimit) {
			pce.addChat(chatEntity);
			try {
				DaoManager.getDefault().savePrivateChat(reciver, pce);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<ChatEntity> getPrivateChats(int roleId){
		PrivateChatEntity pce = null;
		try {
			pce = DaoManager.getDefault().getPrivateChat(roleId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (pce == null) {
			pce = new PrivateChatEntity();
		}
		pce.setReadTime(DateUtil.getSecondStamp());
		try {
			DaoManager.getDefault().savePrivateChat(roleId, pce);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pce.getChats();
		
	}
	
	//检查是否有新消息 或者有附件未领取
	public boolean checkNew(int roleId){
		PrivateChatEntity pce = null;
		try {
			pce = DaoManager.getDefault().getPrivateChat(roleId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pce == null) {
			return false;
		}
		boolean hasNewChat = false;
		for(ChatEntity chatEntity : pce.getChats()){
			if (chatEntity.getAttach().size()>0 && !chatEntity.isAttachRecive()) {
				hasNewChat = true;
				break;
			}
			if (chatEntity.getTime() > pce.getReadTime()) {
				hasNewChat = true;
				break;
			}
		}
		return hasNewChat;
	}
	
	public ChatContent buildChat(ChatEntity chatEntity){
		ChatContent.Builder builder = ChatContent.newBuilder();
		builder.setChatId(chatEntity.getChatId());
		builder.setType(chatEntity.getType());
		builder.setSenderRoleId(chatEntity.getRoleId());
		if (chatEntity.getRoleId() == -1) {
			builder.setSenderName(Language.SYSTEM_MESSAGE);
			builder.setSenderHead(0);
		}else{
			UserEntity sender = ServiceCache.getDefault().getPlayerService().getUserEntity(chatEntity.getRoleId());
			builder.setSenderName(sender.getName());
			builder.setSenderHead(sender.getHeadId());
		}
		builder.setContent(chatEntity.getContent());
		for(int type:chatEntity.getAttach().keySet()){
			Attache.Builder attBuilder = Attache.newBuilder();
			attBuilder.setAttchType(type);
			attBuilder.setCount(chatEntity.getAttach().get(type));
			builder.addAttaches(attBuilder.build());
		}
		builder.setState(chatEntity.isAttachRecive());
		return builder.build();
	}
	
	public void globalChat(int senderId,String content){
		ChatEntity chatEntity = new ChatEntity();
		chatEntity.setRoleId(senderId);
		chatEntity.setContent(content);
		chatEntity.setTime(DateUtil.getSecondStamp());
		DaoManager.getDefault().saveGlobalChat(chatEntity);
	}
	
	public List<ChatEntity> getGlobalChats(){
		List<ChatEntity> chats = DaoManager.getDefault().getGlobalChat();
		return chats;
	}
	
	public void sendChatTo(int reciverId,ChatEntity chatEntity){
		PrivateChatEntity pce = null;
		try {
			pce = DaoManager.getDefault().getPrivateChat(reciverId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pce == null) {
			pce = new PrivateChatEntity();
		}
		pce.getChats().add(chatEntity);
		try {
			DaoManager.getDefault().savePrivateChat(reciverId, pce);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 删除邮件
	 * @param reciverId 收件人
	 * @param type	邮件类型
	 * @param senderId 发件人
	 */
	public void deleteChatMessage(int reciverId,int type,int senderId){
		PrivateChatEntity pce = null;
		try {
			pce = DaoManager.getDefault().getPrivateChat(reciverId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (pce == null) {
			return;
		}
		List<ChatEntity> list = pce.getChats();
		LinkedList<ChatEntity> newList = new LinkedList<>();
		for(ChatEntity chatEntity : list){
			if (chatEntity.getType() == type&& senderId == chatEntity.getRoleId()) {
				continue;
			}else{
				newList.add(chatEntity);
			}
		}
		pce.setChats(newList);
		try {
			DaoManager.getDefault().savePrivateChat(reciverId, pce);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
