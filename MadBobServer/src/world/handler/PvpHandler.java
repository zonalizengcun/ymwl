package world.handler;

import java.util.List;
import java.util.Random;

import com.Language;

import message.OpCodeEnum;
import message.protos.MessagePtoto.Adornment;
import message.protos.MessagePtoto.BuyPvpProtectRes;
import message.protos.MessagePtoto.Monster;
import message.protos.MessagePtoto.MonsterLevelReq;
import message.protos.MessagePtoto.MonsterLevelRes;
import message.protos.MessagePtoto.MonsterListRes;
import message.protos.MessagePtoto.PvpInfoRes;
import message.protos.MessagePtoto.PvpMapInfo;
import message.protos.MessagePtoto.PvpMapUploadReq;
import message.protos.MessagePtoto.PvpMapUploadRes;
import message.protos.MessagePtoto.PvpMatchRes;
import message.protos.MessagePtoto.PvpOverReq;
import message.protos.MessagePtoto.PvpOverRes;
import message.protos.MessagePtoto.PvpReportRes;
import net.handler.PacketHandler;
import net.session.Packet;
import net.session.UserEntity;
import utils.DateUtil;
import world.GameNumber;
import world.World;
import world.dao.DaoManager;
import world.entity.AdornmentEntity;
import world.entity.MonsterEntity;
import world.entity.PvpEntity;
import world.entity.PvpMapEntity;
import world.entity.PvpReport;
import world.model.AwardModel;
import world.model.PvpRobotTemplate;
import world.service.PlayerService;
import world.service.PvpService;
import world.service.ServiceCache;

public class PvpHandler implements PacketHandler {

	@Override
	public void handle(Packet packet) {
		int opcode = packet.getOpcode();
		if (opcode == OpCodeEnum.pvpInfoReq.getOpcode()) {
			this.getPvpInfo(packet);
		}else if (opcode == OpCodeEnum.pvpMapUploadReq.getOpcode()) {
			uploadMapInfo(packet);
		}else if (opcode == OpCodeEnum.pvpOverReq.getOpcode()) {
			this.pvpBattleOver(packet);
		}else if (opcode == OpCodeEnum.pvpMatchReq.getOpcode()) {
			this.pvpMatch(packet);
		}else if (opcode == OpCodeEnum.monsterListReq.getOpcode()) {
			this.getMonsterList(packet);
		}else if (opcode == OpCodeEnum.monsterLevelReq.getOpcode()) {
			this.monsterLevel(packet);
		}else if (opcode == OpCodeEnum.pvpReportReq.getOpcode()) {
			this.getPvpReport(packet);
		}else if (opcode == OpCodeEnum.buyPvpProtectReq.getOpcode()) {
			this.buyPvpProtect(packet);
		}
		
	}

	@Override
	public int[] getOpcodes() {
		return new int[]{OpCodeEnum.pvpInfoReq.getOpcode(),
				OpCodeEnum.pvpMapUploadReq.getOpcode(),OpCodeEnum.pvpMatchReq.getOpcode(),
				OpCodeEnum.pvpOverReq.getOpcode(),
				OpCodeEnum.monsterListReq.getOpcode(),OpCodeEnum.monsterLevelReq.getOpcode(),
				OpCodeEnum.pvpReportReq.getOpcode(),OpCodeEnum.buyPvpProtectReq.getOpcode()};
	}
	
	public void buyPvpProtect(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		PvpService pvpService = ServiceCache.getDefault().getPvpService();
		PvpEntity pvpEntity = pvpService.getPvpEntity(userEntity.getId());
		int newTime = pvpService.getProtectedTime(pvpEntity)+GameNumber.pvpBuyTime;
		pvpEntity.setProtectedTime(DateUtil.getSecondStamp()+newTime);
		DaoManager.getDefault().savePvpEntity(pvpEntity);
		BuyPvpProtectRes.Builder response = BuyPvpProtectRes.newBuilder();
		response.setLevelTime(newTime);
		packet.setOpcode(OpCodeEnum.buyPvpProtectRes.getOpcode());
		packet.writeData(response.build());
	}
	
	/**
	 * 获取战报
	 * @param packet
	 */
	public void getPvpReport(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		PvpService pvpService = ServiceCache.getDefault().getPvpService();
		PvpEntity pvpEntity = pvpService.getPvpEntity(userEntity.getId());
		PvpReportRes.Builder response = PvpReportRes.newBuilder();
		for(PvpReport report : pvpEntity.getPvpReport()){
			message.protos.MessagePtoto.PvpReport.Builder builder = message.protos.MessagePtoto.PvpReport.newBuilder();
			builder.setName(report.getName());
			builder.setPlayerId(report.getPlayerId());
			builder.setWin(report.isSuccess());
			builder.setTime(report.getTime());
			builder.setHeadId(report.getHeadId());
			response.addReports(builder.build());
		}
		packet.setOpcode(OpCodeEnum.pvpReportRes.getOpcode());
		packet.writeData(response.build());
	}
	
	/**
	 * 怪物升级操作
	 * @param packet
	 */
	public void monsterLevel(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		PvpService pvpService = ServiceCache.getDefault().getPvpService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		PvpEntity pvpEntity = pvpService.getPvpEntity(userEntity.getId());
		MonsterLevelReq request = (MonsterLevelReq)packet.getData();
		int monsterId = request.getMonsterId();
		int operate = request.getOperation();
		if (monsterId > GameNumber.pvpMonsterNumber) {
			return;
		}
		int heroLevel = pvpEntity.getHeroLevels().get(monsterId);
		if (operate == 0) {//怪物升级
			if (pvpEntity.getJinghua() < GameNumber.monsterJinghua) {
				packet.sendError(Language.JINGHUA_NOTENOUGH);
				return;
			}
			if (heroLevel >= GameNumber.pvpMonsterLevel) {
				packet.sendError(Language.MONSTER_LEVEL_LIMIT);
				return;
			}
			pvpEntity.setJinghua(pvpEntity.getJinghua() - GameNumber.monsterJinghua);
			pvpEntity.getHeroLevels().set(monsterId, heroLevel+1);
		}else if (operate == 1) {
			if (heroLevel == 0) {
				packet.sendError(Language.MONSTER_LEVEL_ZERO);
				return;
			}
			pvpEntity.getHeroLevels().set(monsterId, heroLevel-1);
		}
		DaoManager.getDefault().savePvpEntity(pvpEntity);
		MonsterLevelRes.Builder response = MonsterLevelRes.newBuilder();
		response.setJinghua(pvpEntity.getJinghua());
		response.setLevel(pvpEntity.getHeroLevels().get(monsterId));
		response.setMonsterId(monsterId);
		packet.setOpcode(OpCodeEnum.monsterLevelRes.getOpcode());
		packet.writeData(response.build());
	}
	
	/**
	 * 获取怪物列表
	 * @param packet
	 */
	public void getMonsterList(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		PvpService pvpService = ServiceCache.getDefault().getPvpService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		PvpEntity pvpEntity = pvpService.getPvpEntity(userEntity.getId());
		MonsterListRes.Builder response = MonsterListRes.newBuilder();
		List<Integer> heroList = pvpEntity.getHeroLevels();
		for(int i=0;i<heroList.size();i++){
			Monster.Builder builder = Monster.newBuilder();
			builder.setMonsterId(i);
			builder.setLevel(heroList.get(i));
			response.addMonsters(builder.build());
		}
		packet.setOpcode(OpCodeEnum.monsterListRes.getOpcode());
		packet.writeData(response.build());
	}
	
	/**
	 * 获取当前pvp信息
	 * @param packet
	 */
	private void getPvpInfo(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		PvpService pvpService = ServiceCache.getDefault().getPvpService();
		PvpEntity pvpEntity = pvpService.getPvpEntity(userEntity.getId());
		PvpInfoRes.Builder response = PvpInfoRes.newBuilder();
		response.setScore(pvpEntity.getScore());
		response.setGurdTime(pvpService.getProtectedTime(pvpEntity));
		response.setJinghua(pvpEntity.getJinghua());
		List<Integer> heroList = pvpEntity.getHeroLevels();
		for(int i=0;i<heroList.size();i++){
			Monster.Builder builder = Monster.newBuilder();
			builder.setMonsterId(i);
			builder.setLevel(heroList.get(i));
			response.addMonsters(builder.build());
		}
		packet.setOpcode(OpCodeEnum.PvpInfoRes.getOpcode());
		packet.writeData(response.build());
	}
	
	/**
	 * 上传地图信息
	 * @param packet
	 */
	private void uploadMapInfo(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		if (userEntity == null) {
			return;
		}
		PvpMapUploadReq request = (PvpMapUploadReq)packet.getData();
		PvpMapInfo mapInfo = request.getMapInfo();
		PvpService pvpService = ServiceCache.getDefault().getPvpService();
		PvpEntity pvpEntity = pvpService.getPvpEntity(userEntity.getId());
		PvpMapEntity mapEntity = new PvpMapEntity();
		mapEntity.setBossId(mapInfo.getBossId());
		mapEntity.setMapId(mapInfo.getMapId());
		mapEntity.setMoney(mapInfo.getMoney());
		List<Adornment> adorments = mapInfo.getAdornmentsList();
		for(Adornment adornment : adorments){
			AdornmentEntity adornmentEntity = new AdornmentEntity();
			adornmentEntity.setAdornId(adornment.getAdornId());
			adornmentEntity.setPointId(adornment.getPointId());
			mapEntity.getAdornments().add(adornmentEntity);
		}
		List<Monster> monsters = mapInfo.getMonstersList();
		for(Monster monster : monsters){
			MonsterEntity monsterEntity = new MonsterEntity();
			int monsterId = monster.getMonsterId();
			monsterEntity.setMonsterId(monsterId);
			monsterEntity.setLevel(pvpEntity.getHeroLevels().get(monsterId));
			mapEntity.getMonsters().add(monsterEntity);
		}
		pvpEntity.setPvpMapEntity(mapEntity);
		DaoManager.getDefault().savePvpEntity(pvpEntity);
		DaoManager.getDefault().addPvpRank(userEntity.getId(), pvpEntity.getScore());
		PvpMapUploadRes.Builder response = PvpMapUploadRes.newBuilder();
		packet.setOpcode(OpCodeEnum.pvpMapUploadRes.getOpcode());
		packet.writeData(response.build());
	}
	
	/**
	 * pvp匹配对手
	 * @param packet
	 */
	public void pvpMatch(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		if (userEntity == null) {
			return;
		}
		PvpService pvpService = ServiceCache.getDefault().getPvpService();
		PvpEntity enemyEntity = pvpService.matchPlayer(userEntity.getId());
		PvpMatchRes.Builder response = PvpMatchRes.newBuilder();
		if (enemyEntity == null) {//如果没有匹配到玩家
			AwardModel awardModel = World.getDefault().getAwardModel();
			List<PvpRobotTemplate> robotList = awardModel.getPvpRobotTemps();
			Random random = new Random();
			PvpRobotTemplate robotTemplate = robotList.get(random.nextInt(robotList.size()));
			response.setPlayerId(-1);
			response.setHeadId(robotTemplate.headId);
			response.setName(robotTemplate.name);
			PvpMapInfo.Builder builder = PvpMapInfo.newBuilder();
			builder.setMapId(robotTemplate.mapId);
			builder.setBossId(robotTemplate.bossId);
			builder.setMoney(robotTemplate.money);
			for(int[] monster : robotTemplate.monsters){
				Monster.Builder monsterBuilder = Monster.newBuilder();
				monsterBuilder.setMonsterId(monster[0]);
				monsterBuilder.setLevel(monster[1]);
				builder.addMonsters(monsterBuilder.build());
			}
			for(int[] adorment : robotTemplate.adornments){
				Adornment.Builder adorBuilder= Adornment.newBuilder();
				adorBuilder.setAdornId(adorment[1]);
				adorBuilder.setPointId(adorment[0]);
				builder.addAdornments(adorBuilder.build());
			}
			response.setMapInfo(builder.build());
			
		}else{
			PvpEntity myPvpEntity = pvpService.getPvpEntity(userEntity.getId());
			myPvpEntity.setPvpEnemy(enemyEntity.getPlayerId());
			DaoManager.getDefault().savePvpEntity(myPvpEntity);
			response.setPlayerId(enemyEntity.getPlayerId());
			UserEntity enemyUserEntity = playerService.getUserEntity(enemyEntity.getPlayerId());
			response.setHeadId(enemyUserEntity.getHeadId());
			response.setName(enemyUserEntity.getName());
			PvpMapInfo pvpMapInfo = pvpService.buildPvpMapProto(enemyEntity.getPvpMapEntity());
			response.setMapInfo(pvpMapInfo);
		}
		packet.setOpcode(OpCodeEnum.pvpMatchRes.getOpcode());
		packet.writeData(response.build());
		
	}
	
	/**
	 * pvp战斗结束
	 * @param packet
	 */
	private void pvpBattleOver(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		if (userEntity == null) {
			return;
		}
		PvpOverReq request = (PvpOverReq)packet.getData();
		PvpService pvpService = ServiceCache.getDefault().getPvpService();
		PvpEntity pvpEntity = pvpService.getPvpEntity(userEntity.getId());
		PvpEntity enemyEntity = null;
		if (pvpEntity.getPvpEnemy() != -1) {
			enemyEntity = DaoManager.getDefault().getPvpEntity(pvpEntity.getPvpEnemy());
		}
		
		boolean win = request.getWin();
		PvpOverRes.Builder response = PvpOverRes.newBuilder();
		int oldScore = pvpEntity.getScore();
		int oldJinghua = pvpEntity.getJinghua();
		if (win) {
			pvpEntity.setScore(pvpEntity.getScore() + GameNumber.pvpWinScore);
			pvpEntity.setJinghua(pvpEntity.getJinghua() + GameNumber.pvpWinJinghua);
			enemyEntity.setProtectedTime(DateUtil.getSecondStamp()+GameNumber.pvpGurdTime);
			if (enemyEntity!=null) {
				PvpReport pvpReport = new PvpReport();
				pvpReport.setPlayerId(enemyEntity.getPlayerId());
				UserEntity enemyUser = playerService.getUserEntity(enemyEntity.getPlayerId());
				pvpReport.setHeadId(enemyUser.getHeadId());
				pvpReport.setName(enemyUser.getName());
				pvpReport.setSuccess(win);
				pvpReport.setTime(DateUtil.getSecondStamp());
				enemyEntity.getPvpReport().add(pvpReport);
				DaoManager.getDefault().savePvpEntity(enemyEntity);
			}
		} else{
			pvpEntity.setScore(pvpEntity.getScore() - GameNumber.pvpDecScore);
			pvpEntity.setJinghua(pvpEntity.getJinghua() + GameNumber.pvpWinJinghua);
			if (enemyEntity!=null) {
				PvpReport pvpReport = new PvpReport();
				pvpReport.setPlayerId(enemyEntity.getPlayerId());
				UserEntity enemyUser = playerService.getUserEntity(enemyEntity.getPlayerId());
				pvpReport.setHeadId(enemyUser.getHeadId());
				pvpReport.setName(enemyUser.getName());
				pvpReport.setSuccess(win);
				pvpReport.setTime(DateUtil.getSecondStamp());
				enemyEntity.getPvpReport().add(pvpReport);
				DaoManager.getDefault().savePvpEntity(enemyEntity);
			}
		}
		DaoManager.getDefault().savePvpEntity(pvpEntity);
		DaoManager.getDefault().addPvpRank(userEntity.getId(), pvpEntity.getScore());
		response.setScoreChange(pvpEntity.getScore() - oldScore);
		response.setJinghuaChange(pvpEntity.getJinghua() - oldJinghua);
		response.setScore(pvpEntity.getScore());
		response.setJinghua(pvpEntity.getJinghua());
		
		packet.setOpcode(OpCodeEnum.pvpOverRes.getOpcode());
		packet.writeData(response.build());
	}
	
	
	
	

}
