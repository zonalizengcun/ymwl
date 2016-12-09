package message;

import com.google.protobuf.MessageLite;

import message.protos.MessagePtoto;
import message.protos.MessagePtoto.ArenaRankReq;
import message.protos.MessagePtoto.ArenaRankRes;
import message.protos.MessagePtoto.BuyPvpProtectReq;
import message.protos.MessagePtoto.BuyPvpProtectRes;
import message.protos.MessagePtoto.ClientLogReq;
import message.protos.MessagePtoto.ClientLogRes;
import message.protos.MessagePtoto.GameRankReq;
import message.protos.MessagePtoto.GameRankRes;
import message.protos.MessagePtoto.GameRankScoreReq;
import message.protos.MessagePtoto.GameRankScoreRes;
import message.protos.MessagePtoto.MonsterLevelReq;
import message.protos.MessagePtoto.MonsterLevelRes;
import message.protos.MessagePtoto.MonsterListReq;
import message.protos.MessagePtoto.MonsterListRes;
import message.protos.MessagePtoto.MonthRegistListReq;
import message.protos.MessagePtoto.MonthRegistListRes;
import message.protos.MessagePtoto.MonthRegistReq;
import message.protos.MessagePtoto.MonthRegistRes;
import message.protos.MessagePtoto.PveSectionReq;
import message.protos.MessagePtoto.PveSectionRes;
import message.protos.MessagePtoto.PvpInfoReq;
import message.protos.MessagePtoto.PvpMapUploadReq;
import message.protos.MessagePtoto.PvpMapUploadRes;
import message.protos.MessagePtoto.PvpMatchReq;
import message.protos.MessagePtoto.PvpMatchRes;
import message.protos.MessagePtoto.PvpOverReq;
import message.protos.MessagePtoto.PvpOverRes;
import message.protos.MessagePtoto.PvpReportRes;
import message.protos.MessagePtoto.ReciveAttachReq;
import message.protos.MessagePtoto.ReciveAttachRes;
import message.protos.MessagePtoto.WeekRegistListReq;
import message.protos.MessagePtoto.WeekRegistListRes;
import message.protos.MessagePtoto.WeekRegistReq;
import message.protos.MessagePtoto.pvpReportReq;

public enum OpCodeEnum {
	ErrorMessage(0,MessagePtoto.ErrorMessage.class),
	registReq(1, MessagePtoto.RegistAccReq.class), registRes(2, MessagePtoto.RegistAccRes.class), 
	loginReq(3,MessagePtoto.LoginReq.class), loginRes(4, MessagePtoto.LoginRes.class), 
	giveNameReq(5,MessagePtoto.GiveNameReq.class), giveNameRes(6, MessagePtoto.GiveNameRes.class), 
	headSetReq(7,MessagePtoto.HeadSetReq.class), headSetRes(8,MessagePtoto.HeadSetRes.class), 
	applyFriendReq(9,MessagePtoto.ApplyFriendReq.class), applyFriendRes(10,MessagePtoto.ApplyFriendRes.class), 
	friendListReq(11,MessagePtoto.FriendListReq.class), friendListRes(12,MessagePtoto.FriendListRes.class), 
	agreeFriendReq(13,MessagePtoto.AgreeFriendReq.class), agreeFriendRes(14,MessagePtoto.AgreeFriendRes.class),
	privateChatReq(15,MessagePtoto.PrivateChatReq.class),privateChatRes(16,MessagePtoto.PrivateChatRes.class),
	privateChatListReq(17,MessagePtoto.PrivateChatListReq.class),privateChatListRes(18,MessagePtoto.PrivateChatListRes.class),
	globalChatReq(19,MessagePtoto.GlobalChatReq.class),gloablChatRes(20,MessagePtoto.GlobalChatRes.class),
	globalChatListReq(21,MessagePtoto.GlobalChatListReq.class),globalChatListRes(22,MessagePtoto.GlobalChatListRes.class),
	arenaMapReq(23,MessagePtoto.ArenaMapReq.class),arenaMapRes(24,MessagePtoto.ArenaMapRes.class),
	arenaRankReq(25,ArenaRankReq.class),arenaRankRes(26,ArenaRankRes.class),
	arenaScoreReq(27,MessagePtoto.ArenaScoreReq.class),arenaScoreRes(28,MessagePtoto.ArenaScoreRes.class),
	occupyEnterReq(29,MessagePtoto.OccupyEnterReq.class),occupyEnterRes(30,MessagePtoto.OccupyEnterRes.class),
	occupyScoreReq(31,MessagePtoto.OccupyScoreReq.class),occupyScoreRes(32,MessagePtoto.OccupyScoreRes.class),
	pvpInfoReq(33,PvpInfoReq.class),PvpInfoRes(34,message.protos.MessagePtoto.PvpInfoRes.class),
	pvpMapUploadReq(35,PvpMapUploadReq.class),pvpMapUploadRes(36,PvpMapUploadRes.class),
	pvpMatchReq(37,PvpMatchReq.class),pvpMatchRes(38,PvpMatchRes.class),
	pvpOverReq(39,PvpOverReq.class),pvpOverRes(40,PvpOverRes.class),
	monsterListReq(41,MonsterListReq.class),monsterListRes(42,MonsterListRes.class),
	monsterLevelReq(43,MonsterLevelReq.class),monsterLevelRes(44,MonsterLevelRes.class),
	platRegistReq(45,MessagePtoto.PlatRegistReq.class),platRegistRes(46,MessagePtoto.PlatRegistRes.class),
	gameStartReq(47,MessagePtoto.GameStartReq.class),gameStartRes(48,MessagePtoto.GameStartRes.class),
	occupyListReq(49,MessagePtoto.OccupyListReq.class),occupyListRes(50,MessagePtoto.OccupyListRes.class),
	diamondReq(51,MessagePtoto.DiamondReq.class),diamondRes(52,MessagePtoto.DiamondRes.class),
	pveSectionIdReq(53,MessagePtoto.PveSectionIdReq.class),pveSectionIdRes(54,MessagePtoto.PveSectionIdRes.class),
	pveSectionInfoReq(55,MessagePtoto.PveSectionInfoReq.class),pveSectionInfoRes(56,MessagePtoto.PveSectionInfoRes.class),
	pveSectionDownReq(57,MessagePtoto.PveSectionDownReq.class),pveSectionDownRes(58,MessagePtoto.PveSectionDownRes.class),
	pveSectionReq(59,PveSectionReq.class),pveSectionRes(60,PveSectionRes.class),
	friendDeleteReq(61,MessagePtoto.FriendDeleteReq.class),friendDeleteRes(62,MessagePtoto.FriendDeleteRes.class),
	friendGiftReq(63,MessagePtoto.FriendGiftReq.class),friendGiftRes(64,MessagePtoto.FriendGiftRes.class),
	reciveAttacheReq(65,ReciveAttachReq.class),reciveAttacheRes(66,ReciveAttachRes.class),
	monthRegistListReq(67,MonthRegistListReq.class),monthRegistListRes(68,MonthRegistListRes.class),
	monthRegistReq(69,MonthRegistReq.class),monthRegistRes(70,MonthRegistRes.class),
	weekRegistListReq(71,WeekRegistListReq.class),weekRegistListRes(72,WeekRegistListRes.class),
	weekRegistReq(73,WeekRegistReq.class),weekRegistRes(74,WeekRegistReq.class),
	gameRankScoreReq(75,GameRankScoreReq.class),gameRankScoreRes(76,GameRankScoreRes.class),
	gameRankReq(77,GameRankReq.class),gameRankRes(78,GameRankRes.class),
	pvpReportReq(79,pvpReportReq.class),pvpReportRes(80,PvpReportRes.class),
	clientLogReq(81,ClientLogReq.class),clientLogRes(82,ClientLogRes.class),
	buyPvpProtectReq(83,BuyPvpProtectReq.class),buyPvpProtectRes(84,BuyPvpProtectRes.class);

	private int code;

	private Class<? extends MessageLite> clazz;

	private OpCodeEnum(int code, Class<? extends MessageLite> clazz) {
		this.code = code;
		this.clazz = clazz;
	}

	public int getOpcode() {
		return this.code;
	}

	public static Class<? extends MessageLite> getClass(int code) {
		for (OpCodeEnum opCode : values()) {
			if (opCode.code == code) {
				return opCode.clazz;
			}
		}
		return null;
	}
	
	public static int getOpcodeByClazz(Class<? extends MessageLite> clazz){
		for(OpCodeEnum opCode : values()){
			if (opCode.clazz ==  clazz) {
				return opCode.code;
			}
		}
		return -100000;
	}
}
