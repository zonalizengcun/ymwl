package world.schedule.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.Language;

import net.HttpServerInboundHandler;
import net.session.UserEntity;
import utils.DateUtil;
import world.dao.DaoManager;
import world.entity.ChatEntity;
import world.entity.OccupyEntity;
import world.schedule.Action;
import world.schedule.ActionImpl;
import world.service.ArenaBattleService;
import world.service.ChatService;
import world.service.PlayerService;
import world.service.ServiceCache;

/**
 * 系统24点刷新数据
 * @author admin
 *
 */
public class RefreshAt24 implements ActionImpl{
	
	private static Logger log = Logger.getLogger(RefreshAt24.class);
	
	private Action action;
	
	public RefreshAt24(Action action) {
		this.action = action;
	}

	@Override
	public Action getAction() {
		return this.action;
	}


	@Override
	public void exec() throws Exception {
		long time = System.currentTimeMillis();
		this.occupyReward();
		ArenaBattleService arenaBattleService = ServiceCache.getDefault().getArenaBattleService();
		try {
			arenaBattleService.sendAward();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("[REFRESHAT24]OK[USERTIME]"+String.valueOf(System.currentTimeMillis()-time));
	}
	
	private void occupyReward(){
		try{
			List<OccupyEntity> list = DaoManager.getDefault().getAllOccupyEntity();
			ChatService chatService = ServiceCache.getDefault().getChatService();
			PlayerService playerService = ServiceCache.getDefault().getPlayerService();
			for(OccupyEntity entity:list){
				if (entity.getPlayerId()!=-1 && entity.getAward()!=0) {
					ChatEntity chatEntity = new ChatEntity();
					chatEntity.setChatId(ServiceCache.getDefault().getIDservice().getNextChatId());
					chatEntity.setRoleId(-1);
					chatEntity.setTime(DateUtil.getSecondStamp());
					chatEntity.setContent(Language.OCCUPY_AWARD);
					chatEntity.setType(1);
					Map<Integer, Integer> map = new HashMap<>();
					map.put(1000, entity.getAward());
					chatEntity.setAttach(map);
					chatService.sendChatTo(entity.getPlayerId(), chatEntity);
					UserEntity userEntity = playerService.getUserEntity(entity.getPlayerId());
					userEntity.setGameScore(userEntity.getGameScore()+10);
					DaoManager.getDefault().saveUserEntity(userEntity);
				}
			}
			DaoManager.getDefault().deleteOccupy();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void over() throws Exception {
		
	}

	
}
