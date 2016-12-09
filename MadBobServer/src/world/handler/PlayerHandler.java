package world.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.Language;

import message.OpCodeEnum;
import message.protos.MessagePtoto;
import message.protos.MessagePtoto.ClientLogReq;
import message.protos.MessagePtoto.ClientLogRes;
import message.protos.MessagePtoto.DiamondReq;
import message.protos.MessagePtoto.DiamondRes;
import message.protos.MessagePtoto.GameRank;
import message.protos.MessagePtoto.GameRankRes;
import message.protos.MessagePtoto.GameRankScoreReq;
import message.protos.MessagePtoto.GameRankScoreRes;
import message.protos.MessagePtoto.GameStartRes;
import message.protos.MessagePtoto.GiveNameReq;
import message.protos.MessagePtoto.GiveNameRes;
import message.protos.MessagePtoto.HeadSetReq;
import message.protos.MessagePtoto.HeadSetRes;
import message.protos.MessagePtoto.HeroMsg;
import message.protos.MessagePtoto.LoginReq;
import message.protos.MessagePtoto.LoginRes;
import message.protos.MessagePtoto.PveSection;
import message.protos.MessagePtoto.PveSectionDownRes;
import message.protos.MessagePtoto.PveSectionIdRes;
import message.protos.MessagePtoto.PveSectionInfoReq;
import message.protos.MessagePtoto.PveSectionInfoRes;
import message.protos.MessagePtoto.PveSectionReq;
import message.protos.MessagePtoto.PveSectionRes;
import net.handler.PacketHandler;
import net.manager.ClientManager;
import net.session.Packet;
import net.session.UserEntity;
import utils.DateUtil;
import utils.TokenUtil;
import world.GameNumber;
import world.dao.DaoManager;
import world.entity.ClientLogEntity;
import world.entity.HeroEntity;
import world.entity.MonthAwardEntity;
import world.entity.PlatEntity;
import world.entity.PlayerClient;
import world.entity.PlayerInfoEntity;
import world.entity.PveEntity;
import world.entity.PveSectionEntity;
import world.entity.WeekAwardEntity;
import world.service.PlayerService;
import world.service.ServiceCache;

public class PlayerHandler implements PacketHandler {

	@Override
	public void handle(Packet packet) {
		int opcode = packet.getOpcode();
		if (opcode == OpCodeEnum.registReq.getOpcode()) {
			this.regist(packet);
		} else if (opcode == OpCodeEnum.loginReq.getOpcode()) {
			this.login(packet);
		} else if(opcode == OpCodeEnum.giveNameReq.getOpcode()){
			this.giveName(packet);
		} else if(opcode == OpCodeEnum.headSetReq.getOpcode()){
			this.setHead(packet);
		} else if (opcode == OpCodeEnum.platRegistReq.getOpcode()) {
			this.platRegist(packet);
		} else if(opcode == OpCodeEnum.diamondReq.getOpcode()){
			this.diamondUpload(packet);
		} else if (opcode == OpCodeEnum.pveSectionIdReq.getOpcode()) {
			this.pveSectionId(packet);
		} else if (opcode == OpCodeEnum.pveSectionInfoReq.getOpcode()) {
			this.upLoadPveInfo(packet);
		} else if (opcode == OpCodeEnum.pveSectionDownReq.getOpcode()) {
			this.downLoadPveInfo(packet);
		} else if (opcode == OpCodeEnum.pveSectionReq.getOpcode()) {
			upLoadPveSection(packet);
		} else if (opcode == OpCodeEnum.gameStartReq.getOpcode()) {
			this.gameStartInfo(packet);
		} else if(opcode == OpCodeEnum.gameRankReq.getOpcode()){
			this.getGameRank(packet);
		} else if (opcode == OpCodeEnum.gameRankScoreReq.getOpcode()) {
			this.gameRankScore(packet);
		} else if (opcode == OpCodeEnum.clientLogReq.getOpcode()) {
			this.clientLog(packet);
		}

	}

	@Override
	public int[] getOpcodes() {
		return new int[] { OpCodeEnum.registReq.getOpcode(), OpCodeEnum.loginReq.getOpcode(),
				OpCodeEnum.giveNameReq.getOpcode(),OpCodeEnum.headSetReq.getOpcode() ,
				OpCodeEnum.platRegistReq.getOpcode(),OpCodeEnum.diamondReq.getOpcode(),
				OpCodeEnum.pveSectionIdReq.getOpcode(),OpCodeEnum.pveSectionInfoReq.getOpcode(),
				OpCodeEnum.pveSectionDownReq.getOpcode(),OpCodeEnum.pveSectionReq.getOpcode(),
				OpCodeEnum.gameStartReq.getOpcode(),OpCodeEnum.gameRankScoreReq.getOpcode(),
				OpCodeEnum.gameRankReq.getOpcode(),OpCodeEnum.clientLogReq.getOpcode()};
	}
	
	
	public void clientLog(Packet packet){
		ClientLogReq request = (ClientLogReq)packet.getData();
		String plat = request.getPlatform();
		String unique = request.getUnique();
		ClientLogEntity logentity = DaoManager.getDefault().getClientLog(plat, unique);
		if (logentity.getRegistTime() == 0) {
			logentity.setRegistTime(DateUtil.getSecondStamp());
		}
		logentity.setLogTimes(logentity.getLogTimes()+1);
		DaoManager.getDefault().savePlatRegist(plat, unique,logentity);
		ClientLogRes.Builder response = ClientLogRes.newBuilder();
		packet.setOpcode(OpCodeEnum.clientLogRes.getOpcode());
		packet.writeData(response.build());
	}
	
	public void getGameRank(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		GameRankRes.Builder response = GameRankRes.newBuilder();
		response.setMyscore(userEntity.getGameScore());
		List<int[]> rankList = DaoManager.getDefault().getGameRankList(0, GameNumber.gameRank);
		for(int[] rankInfo : rankList){
			GameRank.Builder builder = GameRank.newBuilder();
			UserEntity player = playerService.getUserEntity(rankInfo[0]);
			builder.setPlayerId(player.getId());
			builder.setPlayerName(player.getName());
			builder.setPlayerHead(player.getHeadId());
			builder.setScore(rankInfo[1]);
			response.addRanks(builder.build());
		}
		packet.setOpcode(OpCodeEnum.gameRankRes.getOpcode());
		packet.writeData(response.build());
//		GameRank.Builder response = GameRank.newBuilder();
//		response.setScore(userEntity.getGameScore());
		
	}
	
	/**
	 * 上传总排行榜分数
	 */
	public void gameRankScore(Packet packet){
		GameRankScoreReq request = (GameRankScoreReq)packet.getData();
		int score = request.getScore();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		userEntity.setGameScore(userEntity.getGameScore()+score);
		playerService.refreshGameRank(userEntity);
		DaoManager.getDefault().saveUserEntity(userEntity);
		GameRankScoreRes.Builder response = GameRankScoreRes.newBuilder();
		packet.setOpcode(OpCodeEnum.gameRankScoreRes.getOpcode());
		packet.writeData(response.build());
	}
	
	//客户端启动时获取游戏基本信息
	public void gameStartInfo(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		packet.setOpcode(OpCodeEnum.gameStartRes.getOpcode());
		GameStartRes.Builder response = GameStartRes.newBuilder();
		response.setOccupyAwardTime(playerService.getOccupyAwardTime());
		response.setHasMessage(ServiceCache.getDefault().getChatService().checkNew(userEntity.getId())?1:0);
		
		//当天是否已领取七日奖励
		WeekAwardEntity awardEntity = DaoManager.getDefault().getWeekAward(userEntity.getId());
		boolean todayGetAward = true;//今天是否已经领取
		if (!DateUtil.isSameDay(awardEntity.getLastAwardTime()*1000L, System.currentTimeMillis())) {
			todayGetAward = false;
		}
		response.setWeekAwardGet(todayGetAward);
		MonthAwardEntity monthAward = DaoManager.getDefault().getMonthAward(userEntity.getId());
		if (!DateUtil.isSameMonth(monthAward.getLastAwardTime()*1000L, System.currentTimeMillis())) {
			monthAward.getAwardDays().clear();
			monthAward.setLastAwardTime(DateUtil.getSecondStamp());
			DaoManager.getDefault().saveMonthAward(monthAward);
		}
		int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1;
		if (monthAward.getAwardDays().contains(today)) {
			response.setMonthAwardGet(true);
		}else{
			response.setMonthAwardGet(false);
		}
		packet.writeData(response.build());
	}
	
	
	/**
	 * 客户端上传同步钻石数量
	 * @param packet
	 */
	public void diamondUpload(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		DiamondReq request = (DiamondReq)packet.getData();
		int diamond = request.getCount();
		userEntity.setMoney(diamond);
		DaoManager.getDefault().saveUserEntity(userEntity);
		packet.setOpcode(OpCodeEnum.diamondRes.getOpcode());
		DiamondRes.Builder response = DiamondRes.newBuilder();
		packet.writeData(response.build());
	}
	
	/**
	 * 请求单机模式 最高关卡id
	 * @param packet
	 */
	public void pveSectionId(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		packet.setOpcode(OpCodeEnum.pveSectionIdRes.getOpcode());
		PveSectionIdRes.Builder builder = PveSectionIdRes.newBuilder();
		builder.setSectionId(userEntity.getMaxPveSection());
		packet.writeData(builder.build());
	}
	
	/**
	 * 上传所有单机模式所有关卡信息
	 * @param packet
	 */
	public void upLoadPveInfo(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		PveSectionInfoReq request = (PveSectionInfoReq)packet.getData();
		List<MessagePtoto.PveSection> seclist = request.getSectionList();
		List<Boolean> cinemalsList = request.getCinemasList();
		List<HeroMsg> herosList = request.getHerosList();
		List<Integer> propsList = request.getPropList();
		List<Integer> propPieceList = request.getPropPieceList();
		List<Integer> storyPieces = request.getStoryPiecesList();
		
		PlayerInfoEntity playerInfoEntity = DaoManager.getDefault().getPlayerInfo(userEntity.getId());
		playerInfoEntity.setCinemas(cinemalsList);
		playerInfoEntity.setPropPieces(propsList);
		playerInfoEntity.setProps(propPieceList);
		playerInfoEntity.setStoryPieces(storyPieces);
		List<HeroEntity> heroList = new ArrayList<>(); 
		for(HeroMsg hero : herosList){
			HeroEntity heroEntity = new HeroEntity();
			heroEntity.setHeroId(hero.getHeroId());
			heroEntity.setEnable(hero.getEnable());
			heroEntity.setExp(hero.getExp());
			heroEntity.setLevel(hero.getLevel());
			heroEntity.setPieces(hero.getPiececountList());
			heroList.add(heroEntity);
		}
		playerInfoEntity.setHeros(heroList);
		DaoManager.getDefault().savePlayerInfo(playerInfoEntity);
		PveEntity pveEntity = null;
		try {
			pveEntity = DaoManager.getDefault().getPveEntity(userEntity.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		pveEntity.getSectionMap().clear();
		int maxSection = 0;
		for(MessagePtoto.PveSection sec : seclist){
			if (sec.getSectionId() > maxSection) {
				maxSection = sec.getSectionId();
			}
			PveSectionEntity entity = new PveSectionEntity();
			entity.setSectionId(sec.getSectionId());
			entity.setEasyStar(sec.getEasyStar());
			entity.setHardStar(sec.getHardStar());
			pveEntity.getSectionMap().put(entity.getSectionId()+"", entity);
		}
		userEntity.setMaxPveSection(maxSection);
		DaoManager.getDefault().saveUserEntity(userEntity);
		try {
			DaoManager.getDefault().savePveEntity(pveEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		packet.setOpcode(OpCodeEnum.pveSectionInfoRes.getOpcode());
		PveSectionInfoRes.Builder response = PveSectionInfoRes.newBuilder();
		packet.writeData(response.build());
	}
	
	/**
	 * 上传所有单机模式某个关卡信息
	 */
	public void upLoadPveSection(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		PveSectionReq request = (PveSectionReq)packet.getData();
		PveEntity pveEntity = null;
		try {
			pveEntity = DaoManager.getDefault().getPveEntity(userEntity.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		PveSection pveSection = request.getSection();
		int secId = pveSection.getSectionId();//关卡id
		if (secId<0 || secId >= GameNumber.occupySectionCnt) {
			packet.sendError(Language.SECTION_NOTEXIST);
			return;
		}
		PveSectionEntity sectionEntity = new PveSectionEntity();
		sectionEntity.setSectionId(secId);
		sectionEntity.setEasyStar(pveSection.getEasyStar());
		sectionEntity.setHardStar(pveSection.getHardStar());
		pveEntity.getSectionMap().put(secId+"", sectionEntity);
		if (userEntity.getMaxPveSection() < secId) {
			userEntity.setMaxPveSection(secId);
			DaoManager.getDefault().saveUserEntity(userEntity);
		}
		try {
			DaoManager.getDefault().savePveEntity(pveEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		packet.setOpcode(OpCodeEnum.pveSectionRes.getOpcode());
		PveSectionRes.Builder response = PveSectionRes.newBuilder();
		packet.writeData(response.build());
	}
	
	/**
	 * 客户端下载pve数据
	 * @param packet
	 */
	public void downLoadPveInfo(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		PveEntity pveEntity = null;
		try {
			pveEntity = DaoManager.getDefault().getPveEntity(userEntity.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		packet.setOpcode(OpCodeEnum.pveSectionDownRes.getOpcode());
		PveSectionDownRes.Builder response = PveSectionDownRes.newBuilder();
		Map<String,PveSectionEntity> sectionMap = pveEntity.getSectionMap();
		for(PveSectionEntity sectionEntity : sectionMap.values()){
			PveSection.Builder builder = PveSection.newBuilder();
			builder.setSectionId(sectionEntity.getSectionId());
			builder.setEasyStar(sectionEntity.getEasyStar());
			builder.setHardStar(sectionEntity.getHardStar());
			response.addSection(builder.build());
		}
		
		PlayerInfoEntity playerInfoEntity = DaoManager.getDefault().getPlayerInfo(userEntity.getId());
		response.addAllCinemas(playerInfoEntity.getCinemas());
		response.addAllStoryPieces(playerInfoEntity.getStoryPieces());
		response.addAllProp(playerInfoEntity.getProps());
		response.addAllPropPiece(playerInfoEntity.getPropPieces());
		for(HeroEntity heroEntity : playerInfoEntity.getHeros()){
			HeroMsg.Builder builder = HeroMsg.newBuilder();
			builder.setHeroId(heroEntity.getHeroId());
			builder.setEnable(heroEntity.isEnable());
			builder.setExp(heroEntity.getExp());
			builder.setLevel(heroEntity.getLevel());
			builder.addAllPiececount(heroEntity.getPieces());
			response.addHeros(builder.build());
		}
		response.setMaxPveSection(userEntity.getMaxPveSection());
		packet.writeData(response.build());
	}
	
	
	/**
	 * 第三方平台注册
	 * @param packet
	 */
	public void platRegist(Packet packet){
		MessagePtoto.PlatRegistReq request = (MessagePtoto.PlatRegistReq)packet.getData();
		String platToken = request.getPlatToken();
		String platName = request.getPlatName();
		boolean hasRole = true;
		PlatEntity platEntity = DaoManager.getDefault().getPlatEntity(platName, platToken);
		if (platEntity == null) {
			hasRole = false;
			platEntity = new PlatEntity();
			platEntity.setPlatName(platName);
			platEntity.setPlatToken(platToken);
			String token = TokenUtil.generateToken();
			packet.setToken(token);
			platEntity.setToken(token);
			DaoManager.getDefault().savePlatEntity(platEntity);
		}
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(platEntity.getToken());
		if (userEntity == null) {
			userEntity = new UserEntity();
			userEntity.setToken(platEntity.getToken());
			userEntity.setAccountName("");
			userEntity.setPassWord("");
			userEntity.setId(ServiceCache.getDefault().getIDservice().getNextUserId());
			DaoManager.getDefault().saveUserEntity(userEntity);
		}
		PlayerClient player = new PlayerClient(platEntity.getToken());
		ClientManager.getDefault().addClient(player);
		MessagePtoto.PlatRegistRes.Builder response = MessagePtoto.PlatRegistRes.newBuilder();
		response.setHasRole(hasRole);
		response.setPlayer(playerService.buildPlayerInfo(userEntity));
		packet.setOpcode(OpCodeEnum.platRegistRes.getOpcode());
		packet.writeData(response.build());
	}

	/**
	 * 注册
	 * 
	 * @param packet
	 */
	private void regist(Packet packet) {
		MessagePtoto.RegistAccReq request = (MessagePtoto.RegistAccReq) packet.getData();
		String accountName = request.getAccName().trim();
		String accountPass = request.getPassWord().trim();
		String token = TokenUtil.generateToken();
		packet.setToken(token);
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		if (playerService.getUserByAcc(accountName) != null) {
			packet.sendError(Language.ACCOUNT_EXIST);
			return;
		}
		PlayerClient player = new PlayerClient(token);
		ClientManager.getDefault().addClient(player);
		UserEntity userEntity = new UserEntity();
		userEntity.setToken(token);
		userEntity.setAccountName(accountName);
		userEntity.setPassWord(accountPass);
		userEntity.setId(ServiceCache.getDefault().getIDservice().getNextUserId());
		DaoManager.getDefault().saveUserEntity(userEntity);
		MessagePtoto.RegistAccRes.Builder builder = MessagePtoto.RegistAccRes.newBuilder();
		builder.setToken(token);
		packet.writeData(builder.build());
	}

	/**
	 * 登陆
	 * 
	 * @param packet
	 */
	private void login(Packet packet) {
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		LoginReq request = (LoginReq) packet.getData();
		String account = request.getAccountName().trim();
		String pass = request.getPassWord().trim();
		LoginRes.Builder builder = LoginRes.newBuilder();
		if (userEntity != null) {
			if (userEntity.getAccountName().equals(account) && userEntity.getPassWord().equals(pass)) {
				packet.writeData(builder.build());
			} else {
				packet.sendError(Language.ACCOUNT_ERROR);
			}
		} else {
			packet.sendError(Language.PLAYER_NOT_EXIST);
		}
	}

	/**
	 * 昵称命名
	 * 
	 * @param packet
	 */
	private void giveName(Packet packet) {
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		GiveNameReq request = (GiveNameReq) packet.getData();
		String name = request.getName();
		if (!name.trim().equals("")) {
			userEntity.setName(name);
		}
		DaoManager.getDefault().saveUserEntity(userEntity);
		GiveNameRes.Builder response = GiveNameRes.newBuilder();
		packet.setOpcode(OpCodeEnum.giveNameRes.getOpcode());
		packet.writeData(response.build());
	}
	
	/**
	 * 设置头像
	 * @param packet
	 */
	private void setHead(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		HeadSetReq request = (HeadSetReq)packet.getData();
		int headId = request.getHeadId();
		userEntity.setHeadId(headId);
		DaoManager.getDefault().saveUserEntity(userEntity);
		HeadSetRes.Builder response = HeadSetRes.newBuilder();
		packet.setOpcode(OpCodeEnum.headSetRes.getOpcode());
		packet.writeData(response.build());
	}
	
	

}
