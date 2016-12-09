package world.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import message.protos.MessagePtoto.FriendInfo;
import message.protos.MessagePtoto.PlayerInfo;
import net.session.UserEntity;
import utils.DateUtil;
import world.GameNumber;
import world.World;
import world.dao.DaoManager;
import world.entity.ArenaEntiy;
import world.entity.ChatEntity;
import world.entity.FriendEntity;
import world.entity.PrivateChatEntity;
import world.handler.AwardHandler;
import world.handler.ChatHandler;
import world.handler.FriendHandler;
import world.handler.OccupyHandler;
import world.handler.PlayerHandler;

public class PlayerService implements Iservice {

	@Override
	public void start() {
		World.getDefault().getHandlerManager().Regist(new PlayerHandler());
		World.getDefault().getHandlerManager().Regist(new FriendHandler());
		World.getDefault().getHandlerManager().Regist(new ChatHandler());
		World.getDefault().getHandlerManager().Regist(new OccupyHandler());
		World.getDefault().getHandlerManager().Regist(new AwardHandler());
	}

	@Override
	public void shutdown() {
		
	}
	
	public UserEntity getUserEntity(String token){
		UserEntity userEntity = DaoManager.getDefault().findUserEntity(token);
		return userEntity;
	}
	
	public UserEntity getUserEntity(int id){
		UserEntity userEntity = DaoManager.getDefault().findUserEntity(id);
		return userEntity;
	}
	
	
	public UserEntity getUserByAcc(String account){
		return DaoManager.getDefault().findUserByAcc(account);
	}
	
	public PlayerInfo buildPlayerInfo(UserEntity userEntity){
		PlayerInfo.Builder builder = PlayerInfo.newBuilder();
		builder.setToken(userEntity.getToken());
		builder.setUserId(userEntity.getId());
		builder.setPveSectionId(userEntity.getMaxPveSection());
		return builder.build();
	}
	
	
	public FriendInfo buildFriendInfo(int roleId){
		UserEntity userEntity = getUserEntity(roleId);
		FriendInfo.Builder friendInfo = FriendInfo.newBuilder();
		friendInfo.setRoleId(userEntity.getId());
		friendInfo.setHeadId(userEntity.getHeadId());
		friendInfo.setName(userEntity.getName());
		return friendInfo.build();
	}
	
	public FriendEntity getFriendEntity(int roleId){
		FriendEntity friendEntity = null;
		if (!DaoManager.getDefault().isInitFrindList(roleId)) {
			friendEntity = new FriendEntity();
			friendEntity.setRoleId(roleId);
			try {
				DaoManager.getDefault().saveFriendList(roleId, friendEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				friendEntity = DaoManager.getDefault().getFrindList(roleId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return friendEntity;
	}
	
	/**
	 * 获得当日送礼次数
	 * @param friendEntity
	 * @return
	 */
	public int getGiftCnt(FriendEntity friendEntity){
		if (!DateUtil.isSameDay(friendEntity.getGiftTime()*1000L, System.currentTimeMillis())) {
			friendEntity.setGiftCnt(0);
			friendEntity.getGiftFriends().clear();
			try {
				DaoManager.getDefault().saveFriendList(friendEntity.getRoleId(), friendEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return friendEntity.getGiftCnt();
	}
	/**
	 * 获取当日聊天次数
	 * @param userEntity
	 * @return
	 */
	public int getChatTimes(UserEntity userEntity){
		if (!DateUtil.isSameDay(userEntity.getChatTime()*1000L, System.currentTimeMillis())) {
			userEntity.setChatCnt(0);
			userEntity.setChatTime(DateUtil.getSecondStamp());
			DaoManager.getDefault().saveUserEntity(userEntity);
		}
		return userEntity.getChatCnt();
	}
	
	/**
	 * 送礼
	 */
	public void sendGift(UserEntity userEntity,FriendEntity friendEntity,int reciverId){
		if (!DateUtil.isSameDay(friendEntity.getGiftTime()*1000L, System.currentTimeMillis())) {
			friendEntity.setGiftCnt(0);
			friendEntity.getGiftFriends().clear();
		}
		friendEntity.setGiftCnt(friendEntity.getGiftCnt()+1);
		friendEntity.getGiftFriends().add(reciverId);
		ChatEntity chatEntity = new ChatEntity();
		chatEntity.setType(ChatService.chatType_Award);
		chatEntity.setRoleId(userEntity.getId());
		chatEntity.setContent(MessageFormat.format("{0}送您好礼", userEntity.getName()));
		chatEntity.setChatId(ServiceCache.getDefault().getIDservice().getNextChatId());
		chatEntity.setTime(DateUtil.getSecondStamp());
		HashMap<Integer, Integer> reward = new HashMap<>();
		int index = new Random().nextInt(GameNumber.friendGifts.length);
		reward.put(GameNumber.friendGifts[index][0], GameNumber.friendGifts[index][1]);
//		reward.put(GameNumber.friendGifts[index+1][0], GameNumber.friendGifts[index+1][1]);
//		reward.put(GameNumber.friendGifts[index+2][0], GameNumber.friendGifts[index+2][1]);
		chatEntity.setAttach(reward);
		PrivateChatEntity reciverPce = null;
		try {
			reciverPce = DaoManager.getDefault().getPrivateChat(reciverId);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		reciverPce.addChat(chatEntity);
		friendEntity.getGiftFriends().add(reciverId);
		friendEntity.setGiftTime(DateUtil.getSecondStamp());
		try {
			DaoManager.getDefault().savePrivateChat(reciverId, reciverPce);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			DaoManager.getDefault().saveFriendList(friendEntity.getRoleId(), friendEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getOccupyAwardTime(){
		long nowTime = System.currentTimeMillis();
		int time1 = DateUtil.getNextTime(nowTime, 4);
		int time2 = DateUtil.getNextTime(nowTime, 7);
		return time1<time2?time1:time2;
	}
	
	/**
	 * 刷新总排行榜
	 * @param arenaEntiy
	 */
	public void refreshGameRank(UserEntity userEntity){
		
		List<int[]> rank = DaoManager.getDefault().getGameRankList(0, GameNumber.gameRank);
		if (rank.size() < GameNumber.gameRank) {//排行榜玩家不满的时候，直接加入排行榜
			DaoManager.getDefault().addGameRank(userEntity.getId(), userEntity.getGameScore());
		}else{
			int minScore = rank.get(rank.size()-1)[1];
			if (userEntity.getGameScore() > minScore) {
				DaoManager.getDefault().addGameRank(userEntity.getId(), userEntity.getGameScore());
				DaoManager.getDefault().remGameLast(GameNumber.gameRank, GameNumber.gameRank+1);
			}
		}	
	}
	
}
