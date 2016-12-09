package world.handler;

import java.io.IOException;

import message.OpCodeEnum;
import message.protos.MessagePtoto.OccupyEnterReq;
import message.protos.MessagePtoto.OccupyEnterRes;
import message.protos.MessagePtoto.OccupyListRes;
import message.protos.MessagePtoto.OccupyScoreReq;
import message.protos.MessagePtoto.OccupyScoreRes;
import message.protos.MessagePtoto.OccupySection;
import net.handler.PacketHandler;
import net.session.Packet;
import net.session.UserEntity;
import world.GameNumber;
import world.dao.DaoManager;
import world.entity.OccupyEntity;
import world.entity.SectionEntity;
import world.service.PlayerService;
import world.service.ServiceCache;

/**
 * 占领模式
 * 
 * @author lizengcun
 *
 */
public class OccupyHandler implements PacketHandler {

	@Override
	public void handle(Packet packet) {
		int opcode = packet.getOpcode();
		if (opcode == OpCodeEnum.occupyEnterReq.getOpcode()) {
			this.enterRequest(packet);
		} else if (opcode == OpCodeEnum.occupyScoreReq.getOpcode()) {
			this.occupyScore(packet);
		} else if(opcode == OpCodeEnum.occupyListReq.getOpcode()){
			getSectionList(packet);
		}
	}

	@Override
	public int[] getOpcodes() {
		return new int[] { OpCodeEnum.occupyEnterReq.getOpcode(), OpCodeEnum.occupyScoreReq.getOpcode(),
				OpCodeEnum.occupyListReq.getOpcode()};
	}
	
	/**
	 * 获取关卡列表信息
	 * @param packet
	 */
	public void getSectionList(Packet packet){
		
		packet.setOpcode(OpCodeEnum.occupyListRes.getOpcode());
		OccupyListRes.Builder response = OccupyListRes.newBuilder();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		for(int i=0;i<GameNumber.occupySectionCnt;i++){
			OccupyEntity entity = DaoManager.getDefault().getOccupyEntity(i);
			OccupySection.Builder builder = OccupySection.newBuilder();
			builder.setSectionId(entity.getSectionId());
			builder.setPlayerId(entity.getPlayerId());
			builder.setAward(entity.getAward());
			builder.setScore(entity.getScore());
			boolean hasUser = true;
			if (entity.getPlayerId() == -1) {
				hasUser = false;
			}else{
				UserEntity userEntity = playerService.getUserEntity(entity.getPlayerId());
				if (userEntity == null) {
					hasUser = false;
				}else{
					builder.setPlayerName(userEntity.getName());
					builder.setPlayerHeadId(userEntity.getHeadId());
				}
				
			}
			if (!hasUser) {
				builder.setPlayerHeadId(-1);
				builder.setPlayerName("");
			}
			response.addSection(builder.build());
		}
		packet.writeData(response.build());
	}

	/**
	 * 占领模式准备进入 信息
	 * 
	 * @param packet
	 */
	public void enterRequest(Packet packet) {
		String token = packet.getToken();
		OccupyEnterReq request = (OccupyEnterReq)packet.getData();
		int sectionId = request.getSectionId();
		
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(token);
		if (userEntity == null) {
			return;
		}
		packet.setOpcode(OpCodeEnum.occupyEnterRes.getOpcode());
		OccupyEnterRes.Builder response = OccupyEnterRes.newBuilder();
		OccupyEntity occupyEntity = DaoManager.getDefault().getOccupyEntity(sectionId);
		boolean isocc = false;
		if (occupyEntity.getPlayerId() != -1) {
			UserEntity occUser = playerService.getUserEntity(occupyEntity.getPlayerId());
			if (occUser!=null) {
				isocc = true;
				response.setOccupyPlayerName(occUser.getName());
				response.setOccupyPlayerHead(occUser.getHeadId());
				response.setOccupyPlayerId(occUser.getId());
			}
		}
		if (!isocc) {
			response.setOccupyPlayerHead(0);
			response.setOccupyPlayerName("");
			response.setOccupyPlayerId(-1);
		} 
		SectionEntity section = null;
		try {
			section = DaoManager.getDefault().getSectionEntity(userEntity.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		int[] scoreInfo = section.getScoreMap().get(sectionId);
		if (scoreInfo != null) {
			response.setMaxScore(scoreInfo[1]);
			response.setLastScore(scoreInfo[0]);
		}else{
			response.setMaxScore(0);
			response.setLastScore(0);
		}
		response.setOccupyPlayerScore(occupyEntity.getScore());
		response.setOccupyaward(occupyEntity.getAward());
		response.setOccupyed(isocc);
		response.setOccupyAwardTime(playerService.getOccupyAwardTime());
		packet.writeData(response.build());
	}

	/**
	 * 上传占领模式分数
	 */
	public void occupyScore(Packet packet) {
		OccupyScoreReq request = (OccupyScoreReq) packet.getData();
		int sectionId = request.getSectionId();
		if (sectionId<0 || sectionId>=GameNumber.occupySectionCnt) {
			return;
		}
		int score = request.getScore();
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		OccupyEntity occupyEntity = DaoManager.getDefault().getOccupyEntity(sectionId);
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		if (score > occupyEntity.getScore()) {
			occupyEntity.setPlayerId(userEntity.getId());
			occupyEntity.setScore(score);
			occupyEntity.setAward(occupyEntity.getAward() + GameNumber.occupyScore);
			DaoManager.getDefault().saveOccupyEntity(occupyEntity);
		}
		SectionEntity sectionEntity = null;
		try {
			sectionEntity = DaoManager.getDefault().getSectionEntity(userEntity.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		int[] scoreInfo = sectionEntity.getScoreMap().get(sectionId);
		if (scoreInfo == null) {
			scoreInfo = new int[]{score,score};
			sectionEntity.getScoreMap().put(sectionId, scoreInfo);
		}
		scoreInfo[0] = score;
		if (score > scoreInfo[1]) {
			scoreInfo[1] = score;
		}
		try {
			DaoManager.getDefault().saveSectionEntity(sectionEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		OccupyScoreRes.Builder response = OccupyScoreRes.newBuilder();
		packet.setOpcode(OpCodeEnum.occupyScoreRes.getOpcode());
		packet.writeData(response.build());
	}
	
	

}
