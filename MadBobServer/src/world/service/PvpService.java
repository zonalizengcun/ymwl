package world.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import message.protos.MessagePtoto.Adornment;
import message.protos.MessagePtoto.Monster;
import message.protos.MessagePtoto.PvpMapInfo;
import utils.DateUtil;
import world.GameNumber;
import world.World;
import world.dao.DaoManager;
import world.entity.AdornmentEntity;
import world.entity.MonsterEntity;
import world.entity.PvpEntity;
import world.entity.PvpMapEntity;
import world.handler.PvpHandler;

public class PvpService implements Iservice{

	@Override
	public void start() {
		World.getDefault().getHandlerManager().Regist(new PvpHandler());
	}

	@Override
	public void shutdown() {
		
	}
	
	/**
	 * 生成玩家pvp信息
	 * @param playerId
	 */
	public PvpEntity getPvpEntity(int playerId){
		PvpEntity pvpEntity = DaoManager.getDefault().getPvpEntity(playerId);
		if (pvpEntity == null) {
			pvpEntity = new PvpEntity();
			pvpEntity.setPlayerId(playerId);
			pvpEntity.setScore(GameNumber.pvpStartScore);
			pvpEntity.setJinghua(GameNumber.pvpJinghua);
			for(int i=0;i<GameNumber.pvpMonsterNumber;i++){
				pvpEntity.getHeroLevels().add(0);
			}
			DaoManager.getDefault().savePvpEntity(pvpEntity);
		}
		return pvpEntity;
	}
	

	
	/**
	 * 构建mapinfo
	 * @param mapEntity
	 * @return
	 */
	public PvpMapInfo buildPvpMapProto(PvpMapEntity mapEntity){
		PvpMapInfo.Builder mapbuilder = PvpMapInfo.newBuilder();
		mapbuilder.setBossId(mapEntity.getBossId());
		mapbuilder.setMapId(mapEntity.getMapId());
		List<MonsterEntity> monsterEntitys = mapEntity.getMonsters();
		for(MonsterEntity monsterEntity : monsterEntitys){
			Monster.Builder monsterbuild = Monster.newBuilder();
			monsterbuild.setMonsterId(monsterEntity.getMonsterId());
			monsterbuild.setLevel(monsterEntity.getLevel());
			mapbuilder.addMonsters(monsterbuild.build());
		}
		List<AdornmentEntity> adornmentEntities = mapEntity.getAdornments();
		for(AdornmentEntity adornmentEntity : adornmentEntities){
			Adornment.Builder builder = Adornment.newBuilder();
			builder.setAdornId(adornmentEntity.getAdornId());
			builder.setPointId(adornmentEntity.getPointId());
			mapbuilder.addAdornments(builder.build());
		}
		mapbuilder.setMoney(mapEntity.getMoney());
		return mapbuilder.build();
	}
	
	/**
	 * 获取玩家剩余保护时间
	 * @return
	 */
	public int getProtectedTime(PvpEntity pvpEntity){
		int time = pvpEntity.getProtectedTime() - DateUtil.getSecondStamp();
		return time<0?0:time;
	}
	
	public PvpEntity matchPlayer(int playerId){
		Long rankindex_long = DaoManager.getDefault().getPvpRank(playerId);//玩家当前排名
		long length = DaoManager.getDefault().getPvpRankLength();
		Random random = new Random();
		boolean upOrDown = random.nextBoolean();
		List<int[]> rankList = DaoManager.getDefault().getPvpRankList(0, (int)length);
		int rankindex = rankList.size();
		if (rankindex_long != null) {
			rankindex = (int)rankindex_long.longValue();
		}
		
		Map<Integer, PvpEntity> entityMap = DaoManager.getDefault().getPvpEntityMap();
		if (upOrDown) {
			for(int i=rankindex-1;i>=0;i--){
				int[] rankInfo = rankList.get(i);
				PvpEntity pvpEntity = entityMap.get(rankInfo[0]);
				if (pvpEntity!=null&&pvpEntity.getPvpMapEntity()!=null&&getProtectedTime(pvpEntity)<=0) {
					return pvpEntity;
				}
			}
			for(int i= rankindex+1;i<rankList.size();i++){
				if (i>=rankList.size()) {
					break;
				}
				int[] rankInfo = rankList.get(i);
				PvpEntity pvpEntity = entityMap.get(rankInfo[0]);
				if (pvpEntity!=null&&pvpEntity.getPvpMapEntity()!=null&&getProtectedTime(pvpEntity)<=0) {
					return pvpEntity;
				}
			}
		}else{
			for(int i= rankindex+1;i<rankList.size();i++){
				if (i >= rankList.size()) {
					break;
				}
				int[] rankInfo = rankList.get(i);
				PvpEntity pvpEntity = entityMap.get(rankInfo[0]);
				if (pvpEntity!=null&&pvpEntity.getPvpMapEntity()!=null&&getProtectedTime(pvpEntity)<=0) {
					return pvpEntity;
				}
			}
			for(int i=rankindex-1;i>=0;i--){
				int[] rankInfo = rankList.get(i);
				PvpEntity pvpEntity = entityMap.get(rankInfo[0]);
				if (pvpEntity!=null&&pvpEntity.getPvpMapEntity()!=null&&getProtectedTime(pvpEntity)<=0) {
					return pvpEntity;
				}
			}
		}
		return null;
	}
	
	
	
	
	
	
}
