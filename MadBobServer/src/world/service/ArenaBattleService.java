package world.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.Language;

import net.session.UserEntity;
import utils.DateUtil;
import world.GameNumber;
import world.World;
import world.dao.DaoManager;
import world.entity.ArenaEntiy;
import world.entity.ChatEntity;
import world.handler.ArenaBattleHandler;
import world.model.AwardModel;

public class ArenaBattleService implements Iservice{

	@Override
	public void start() {
		World.getDefault().getHandlerManager().Regist(new ArenaBattleHandler());
	}

	@Override
	public void shutdown() {
		
		
	}
	
	/**
	 * 获取当前竞技场地图id
	 * @return
	 */
	public int getArenaMap(){
		long lastTime = DaoManager.getDefault().getArenaMapTime();
		if (!DateUtil.isSameWeek(lastTime,System.currentTimeMillis())) {
			int oldId = DaoManager.getDefault().getArenaMap();
			int[] ids = getAllArenaMapId();
			Random random = new Random();
			int nextId = ids[random.nextInt(ids.length)];
			while (oldId == nextId && ids.length > 1) {
				nextId = random.nextInt(ids.length);
			}
			DaoManager.getDefault().saveArenaMap(nextId, System.currentTimeMillis());
			return nextId;
		}else{
			return DaoManager.getDefault().getArenaMap();
		}
	}
	
	/**
	 * 根据roleid 获取竞技场信息
	 * @param roleId
	 * @return
	 */
	public ArenaEntiy getArenaEntity(int roleId){
		ArenaEntiy arenaEntiy = DaoManager.getDefault().getArenaEntity(roleId);
		if (arenaEntiy == null) {
			arenaEntiy = new ArenaEntiy();
			arenaEntiy.setRoleId(roleId);
			DaoManager.getDefault().saveArenaEntity(new ArenaEntiy());
		}
		return arenaEntiy;
	}
	
	private int[] getAllArenaMapId(){
		String[] strs = GameNumber.ArenaMapIdInfo.split(",");
		int[] ids = new int[strs.length];
		for(int i=0;i<strs.length;i++){
			ids[i] = Integer.parseInt(strs[i]);
		}
		return ids;
	}
	
	/**
	 * 刷新排行榜
	 * @param arenaEntiy
	 */
	public void refreshRank(ArenaEntiy arenaEntiy){
		if (arenaEntiy.getLastScore() <= arenaEntiy.getMaxScore()) {
			return;
		}else{
			arenaEntiy.setMaxScore(arenaEntiy.getLastScore());
		}
		
		List<int[]> rank = DaoManager.getDefault().getArenaRankList(0, GameNumber.arenaRank);
		if (rank.size() < GameNumber.arenaRank) {//排行榜玩家不满的时候，直接加入排行榜
			DaoManager.getDefault().arenaAddRank(arenaEntiy.getRoleId(), arenaEntiy.getMaxScore());
		}else{
			int minScore = rank.get(rank.size()-1)[1];
			if (arenaEntiy.getMaxScore() > minScore) {
				DaoManager.getDefault().arenaAddRank(arenaEntiy.getRoleId(), arenaEntiy.getMaxScore());
				DaoManager.getDefault().remArenaLast(GameNumber.arenaRank, GameNumber.arenaRank+1);
			}
		}	
	}
	
	public void sendAward(){
		AwardModel awardModel = World.getDefault().getAwardModel();
		ChatService chatService = ServiceCache.getDefault().getChatService();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		List<int[][]> awardList = awardModel.getArenaRankList();
		List<int[]> rankList = DaoManager.getDefault().getArenaRankList(0, 10);
		int length = awardList.size()>rankList.size()?rankList.size():awardList.size();
		for(int i=0;i<length;i++){
			int[][] awards = awardList.get(i);
			int[] scoreInfo = rankList.get(i);
			ChatEntity chatEntity = new ChatEntity();
			chatEntity.setChatId(ServiceCache.getDefault().getIDservice().getNextChatId());
			chatEntity.setRoleId(-1);
			chatEntity.setTime(DateUtil.getSecondStamp());
			chatEntity.setContent(Language.ARENA_REWARD);
			chatEntity.setType(1);
			Map<Integer, Integer> map = new HashMap<>();
			for(int[] award:awards){
				map.put(award[0], award[1]);
			}
			chatEntity.setAttach(map);
			chatService.sendChatTo(scoreInfo[0], chatEntity);
			UserEntity userEntity = playerService.getUserEntity(scoreInfo[0]);
			userEntity.setGameScore(userEntity.getGameScore()+awardModel.arenaGameRankScore.get(i));
			DaoManager.getDefault().saveUserEntity(userEntity);
		}
	}

}
