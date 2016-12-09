package world.handler;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Set;

import com.Language;

import message.OpCodeEnum;
import message.protos.MessagePtoto.AgreeFriendReq;
import message.protos.MessagePtoto.AgreeFriendRes;
import message.protos.MessagePtoto.ApplyFriendReq;
import message.protos.MessagePtoto.ApplyFriendRes;
import message.protos.MessagePtoto.FriendDeleteReq;
import message.protos.MessagePtoto.FriendDeleteRes;
import message.protos.MessagePtoto.FriendGiftReq;
import message.protos.MessagePtoto.FriendGiftRes;
import message.protos.MessagePtoto.FriendInfo;
import message.protos.MessagePtoto.FriendListRes;
import net.handler.PacketHandler;
import net.session.Packet;
import net.session.UserEntity;
import utils.DateUtil;
import world.GameNumber;
import world.dao.DaoManager;
import world.entity.ChatEntity;
import world.entity.FriendEntity;
import world.entity.PrivateChatEntity;
import world.service.ChatService;
import world.service.PlayerService;
import world.service.ServiceCache;

public class FriendHandler implements PacketHandler {

	@Override
	public void handle(Packet packet) {
		int opcode = packet.getOpcode();
		if (opcode == OpCodeEnum.applyFriendReq.getOpcode()) {
			this.applyFriend(packet);
		} else if (opcode == OpCodeEnum.agreeFriendReq.getOpcode()) {
			this.agreeFriend(packet);
		}else if (opcode == OpCodeEnum.friendListReq.getOpcode()) {
			this.getFriendList(packet);
		}else if(opcode == OpCodeEnum.friendDeleteReq.getOpcode()){
			this.deleteFriend(packet);
		}else if(opcode == OpCodeEnum.friendGiftReq.getOpcode()){
			this.friendGift(packet);
		}
	}

	@Override
	public int[] getOpcodes() {
		return new int[] { OpCodeEnum.applyFriendReq.getOpcode(), OpCodeEnum.agreeFriendReq.getOpcode(),
				OpCodeEnum.friendListReq.getOpcode(),OpCodeEnum.friendDeleteReq.getOpcode(),
				OpCodeEnum.friendGiftReq.getOpcode()};
	}
	
	
	/**
	 * 好友送礼
	 * @param packet
	 */
	private void friendGift(Packet packet){
		FriendGiftReq request = (FriendGiftReq)packet.getData();
		int targetId = request.getTargetId();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		FriendEntity friendEntity = playerService.getFriendEntity(userEntity.getId());
		int giftCnt = playerService.getGiftCnt(friendEntity);
		if (giftCnt >= GameNumber.friendGiftLimit) {
			packet.sendError(Language.GIFT_LIMIT);
			return;
		}
		if (friendEntity.getGiftFriends().contains(targetId)) {
			packet.sendError(Language.GIFT_HASSEND);
			return;
		}
		playerService.sendGift(userEntity, friendEntity, targetId);
		FriendGiftRes.Builder response = FriendGiftRes.newBuilder();
		packet.setOpcode(OpCodeEnum.friendGiftRes.getOpcode());
		packet.writeData(response.build());	
	}
	
	//删除好友
	private void deleteFriend(Packet packet){
		FriendDeleteReq request = (FriendDeleteReq)packet.getData();
		int targetId = request.getRoleId();//要删除的好友id
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		FriendEntity friendEntity = playerService.getFriendEntity(userEntity.getId());
		FriendEntity targetFriendEntity = playerService.getFriendEntity(targetId);
		if (targetFriendEntity!=null&&targetFriendEntity.getFriends().contains(userEntity.getId())) {
			targetFriendEntity.getFriends().remove(userEntity.getId());
			try {
				DaoManager.getDefault().saveFriendList(targetId, targetFriendEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (friendEntity.getFriends().contains(targetId)) {
			friendEntity.getFriends().remove(targetId);
			try {
				DaoManager.getDefault().saveFriendList(userEntity.getId(), friendEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		packet.setOpcode(OpCodeEnum.friendDeleteRes.getOpcode());
		FriendDeleteRes.Builder response = FriendDeleteRes.newBuilder();
		packet.writeData(response.build());
	}

	/**
	 * 申请加好友
	 * 
	 * @param packet
	 */
	private void applyFriend(Packet packet) {
		ApplyFriendReq request = (ApplyFriendReq) packet.getData();
		int userId = request.getFriendId();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity fuserEntity = playerService.getUserEntity(userId);//被申请者
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());//申请者
		ApplyFriendRes.Builder response = ApplyFriendRes.newBuilder();
		if (userEntity.getId() == userId) {
			packet.sendError(Language.FRIND_APPLY_INVALID);
			return;
		}
		if (fuserEntity == null) {
			packet.sendError(Language.PLAYER_NOT_EXIST);
			return;
		} else {
			FriendEntity myFriendEntity = playerService.getFriendEntity(userEntity.getId());
			if (myFriendEntity.getFriends().contains(userId)) {
				packet.sendError(Language.FRIEND_REPEAT);
				return;
			}
			if (myFriendEntity.getFriends().size() > GameNumber.friendLimit) {
				packet.sendError(Language.FRIEND_LIMIT);
				return;
			}
			
			FriendEntity friendEntity = playerService.getFriendEntity(fuserEntity.getId());
			if (friendEntity.getFriendApplys().contains(userEntity.getId())) {
				packet.sendError(Language.FRIEND_APPLY_REPEAT);
				return;
			} else {
				friendEntity.addFriendApply(userEntity.getId());
				ChatEntity chatEntity = new ChatEntity();
				chatEntity.setChatId(ServiceCache.getDefault().getIDservice().getNextChatId());
				chatEntity.setRoleId(userEntity.getId());
				chatEntity.setContent(MessageFormat.format("{0}申请加您为好友", userEntity.getName()));
				chatEntity.setTime(DateUtil.getSecondStamp());
				chatEntity.setType(ChatService.chatType_friend);
				PrivateChatEntity pce = null;
				try {
					pce = DaoManager.getDefault().getPrivateChat(fuserEntity.getId());
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (pce == null) {
					pce = new PrivateChatEntity();
				}
				pce.addChat(chatEntity);
				try {
					DaoManager.getDefault().savePrivateChat(fuserEntity.getId(), pce);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					DaoManager.getDefault().saveFriendList(fuserEntity.getId(), friendEntity);
				} catch (IOException e) {
					e.printStackTrace();
				}
				packet.setOpcode(OpCodeEnum.applyFriendRes.getOpcode());
				packet.writeData(response.build());
			}
		}
		
	}

	/**
	 * 同意或拒绝申请
	 * 
	 * @param packet
	 */
	private void agreeFriend(Packet packet) {
		AgreeFriendReq request = (AgreeFriendReq) packet.getData();
		int targetId = request.getRoleId();
		boolean agree = request.getAgree();
		AgreeFriendRes.Builder response = AgreeFriendRes.newBuilder();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity targetUser = playerService.getUserEntity(targetId);
		UserEntity roleUser = playerService.getUserEntity(packet.getToken());
		FriendEntity roleFirendEntity = null;
		try {
			roleFirendEntity = DaoManager.getDefault().getFrindList(roleUser.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!roleFirendEntity.getFriendApplys().contains(targetId)) {
			packet.sendError(Language.FRIEND_APPLY_NOT_EXIST);
			return;	
		} else {
			if (targetUser == null) {
				packet.sendError(Language.PLAYER_NOT_EXIST);
				return;
			} else {
				if (roleFirendEntity.getFriends().size() >= GameNumber.friendLimit) {
					packet.sendError(Language.FRIEND_LIMIT);
					return;
				}
				FriendEntity targetFirendEntity = null;
				try {
					targetFirendEntity = DaoManager.getDefault().getFrindList(targetUser.getId());
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (targetFirendEntity.getFriends().size() >= GameNumber.friendLimit) {
					packet.sendError(Language.FRIEND_OTHER_LIMIT);
					return;
				}
				if (agree) {// 同意
					targetFirendEntity.addFriend(roleUser.getId());
					roleFirendEntity.addFriend(targetUser.getId());
				}
				// 拒绝
				roleFirendEntity.getFriendApplys().remove(targetId);// 移除请求	
				ChatService chatService = ServiceCache.getDefault().getChatService();
				try {
					DaoManager.getDefault().saveFriendList(targetUser.getId(), targetFirendEntity);
					DaoManager.getDefault().saveFriendList(roleUser.getId(), roleFirendEntity);
				} catch (IOException e) {
					e.printStackTrace();
				}
				chatService.deleteChatMessage(roleUser.getId(),ChatService.chatType_friend,targetUser.getId());
			}
		}
		packet.setOpcode(OpCodeEnum.agreeFriendRes.getOpcode());
		packet.writeData(response.build());
	}

	/**
	 * 获取好友列表
	 * @param packet
	 */
	private void getFriendList(Packet packet) {
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		FriendListRes.Builder response = FriendListRes.newBuilder();
		if (userEntity != null) {
			FriendEntity friendEntity = null;
			try {
				friendEntity = DaoManager.getDefault().getFrindList(userEntity.getId());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (friendEntity!=null) {
				response.setGiftLimt(GameNumber.friendGiftLimit);
				response.setGiftTimes(GameNumber.friendGiftLimit - playerService.getGiftCnt(friendEntity));
				Set<Integer> friendIds = friendEntity.getFriends();
				for(int roleId : friendIds){
					FriendInfo friendInfo = playerService.buildFriendInfo(roleId);
					response.addFriends(friendInfo);
				}
			}
		}
		packet.setOpcode(OpCodeEnum.friendListRes.getOpcode());
		packet.writeData(response.build());
	}

}
