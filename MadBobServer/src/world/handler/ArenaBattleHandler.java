package world.handler;

import java.util.List;

import com.Language;

import message.OpCodeEnum;
import message.protos.MessagePtoto.ArenaMapRes;
import message.protos.MessagePtoto.ArenaRank;
import message.protos.MessagePtoto.ArenaRankRes;
import message.protos.MessagePtoto.ArenaScoreReq;
import message.protos.MessagePtoto.ArenaScoreRes;
import message.protos.MessagePtoto.RankAward;
import net.handler.PacketHandler;
import net.session.Packet;
import net.session.UserEntity;
import world.GameNumber;
import world.World;
import world.dao.DaoManager;
import world.entity.ArenaEntiy;
import world.model.AwardModel;
import world.service.ArenaBattleService;
import world.service.PlayerService;
import world.service.ServiceCache;

/**
 * 竞技场
 * @author admin
 *
 */
public class ArenaBattleHandler implements PacketHandler {

	@Override
	public void handle(Packet packet) {
		int opcode = packet.getOpcode();
		if (opcode == OpCodeEnum.arenaMapReq.getOpcode()) {
			this.mapRequest(packet);
		}else if (opcode == OpCodeEnum.arenaScoreReq.getOpcode()){
			this.arenaScore(packet);
		}else if(opcode == OpCodeEnum.arenaRankReq.getOpcode()){
			this.getArenaRank(packet);
		}
	}

	@Override
	public int[] getOpcodes() {
		return new int[]{OpCodeEnum.arenaMapReq.getOpcode(),
				OpCodeEnum.arenaScoreReq.getOpcode(),OpCodeEnum.arenaRankReq.getOpcode()};
	}
	
	/**
	 * 竞技场请求当前地图
	 * @param packet
	 */
	public void mapRequest(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		ArenaBattleService arenaService = ServiceCache.getDefault().getArenaBattleService();
		int mapId = arenaService.getArenaMap();
		ArenaEntiy arenaEntiy = arenaService.getArenaEntity(userEntity.getId());
		ArenaMapRes.Builder response = ArenaMapRes.newBuilder();
		response.setMapId(mapId);
		response.setTimes(arenaEntiy.getLeaveTimes());
		response.setMaxScore(arenaEntiy.getMaxScore());
		response.setLasScore(arenaEntiy.getLastScore());
		response.setMaxTimes(GameNumber.arenaTimes);
		packet.setOpcode(OpCodeEnum.arenaMapRes.getOpcode());
		packet.writeData(response.build());
	}
	
	/**
	 * 进入竞技场战斗
	 * @param packet
	 */
//	public void enterArena(Packet packet){
//		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
//		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
//		ArenaBattleService arenaService = ServiceCache.getDefault().getArenaBattleService();
//		ArenaEntiy arenaEntiy = arenaService.getArenaEntity(userEntity.getId());
//		if (arenaEntiy.getLeaveTimes() < 0) {
//			packet.sendError(Language.ARENA_TIMES_FULL);
//			return;
//		}
//		String ticket = TokenUtil.generateToken();
//		arenaEntiy.setTicket(ticket);
//		ArenaEnterRes.Builder response = ArenaEnterRes.newBuilder();
//		response.setTicket(ticket);
//		packet.writeData(response.build());
//	}
	
	/**
	 * 上传竞技场分数
	 * @param packet
	 */
	public void arenaScore(Packet packet){
		ArenaScoreReq request = (ArenaScoreReq)packet.getData();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		ArenaBattleService arenaService = ServiceCache.getDefault().getArenaBattleService();
		ArenaEntiy arenaEntiy = arenaService.getArenaEntity(userEntity.getId());
		if (arenaEntiy.getLeaveTimes()<=0) {
			packet.sendError(Language.ARENA_SCORE_INVALID);
			return;
		}else{
			arenaEntiy.setLastScore(request.getScore());
			arenaService.refreshRank(arenaEntiy);
			arenaEntiy.setBattleTimes(arenaEntiy.getBattleTimes()+1);
			DaoManager.getDefault().saveArenaEntity(arenaEntiy);
			ArenaScoreRes.Builder response = ArenaScoreRes.newBuilder();
			packet.setOpcode(OpCodeEnum.arenaScoreRes.getOpcode());
			packet.writeData(response.build());
		}
	}
	
	/**
	 * 获取竞技场排行榜
	 * @param packet
	 */
	public void getArenaRank(Packet packet){
		List<int[]> list = DaoManager.getDefault().getArenaRankList(0, GameNumber.arenaRank);
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		ArenaRankRes.Builder response = ArenaRankRes.newBuilder();
		AwardModel awardModel = World.getDefault().getAwardModel();
		int rank = 0;
		for(int[] rankInfo : list){
			ArenaRank.Builder rankBuilder = ArenaRank.newBuilder();
			UserEntity player = playerService.getUserEntity(rankInfo[0]);
			rankBuilder.setPlayerId(player.getId());
			rankBuilder.setHeadId(player.getHeadId());
			rankBuilder.setName(player.getName());
			rankBuilder.setScore(rankInfo[1]);
			int[][] award = awardModel.getArenaRankAward(rank);
			for(int i=0;i<award.length;i++){
				RankAward.Builder builder = RankAward.newBuilder();
				builder.setType(award[i][0]);
				builder.setCount(award[i][1]);
				rankBuilder.addAwards(builder.build());
			}
			response.addRanks(rankBuilder.build());
			rank++;
		}
		packet.setOpcode(OpCodeEnum.arenaRankRes.getOpcode());
		packet.writeData(response.build());
	}
	
	

}
