package com.yim.pix.world.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.yim.message.pix.game.GameCodeMaper;
import com.yim.message.pix.game.MessagePtoto.BattleMatchSyn;
import com.yim.message.pix.game.MessagePtoto.PlayerMessage;
import com.yim.pix.world.entity.Matcher;
import com.yim.pix.world.entity.Player;
import com.yim.service.IService;
import com.yim.service.ServiceContainer;
import com.yim.util.DateUtil;

/**
 * 战斗匹配
 * @author admin
 *
 */
public class MatchService implements IService {
	
	/**
	 * 匹配超时时间
	 */
	public static final int TIMEOUT = 40;
	
	private List<Integer> matchers = new ArrayList<>();
	
	private Map<Integer, Matcher> matcherMap = new LinkedHashMap<>();
	
	PlayerService playerService;
	
	public MatchService() {
		this.playerService = ServiceContainer.getInstance().get(PlayerService.class);
	}

	@Override
	public void startup() {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				match();
			}
		}, "battleMatch");

	}

	@Override
	public void shutdown() {
		
	}
	
	public void addMatcher(int playerId){
		synchronized (matchers) {
			matchers.add(playerId);
		}
	}
	
	private void match(){
		while(true){
			
			synchronized (matcherMap) {
				Iterator<Map.Entry<Integer, Matcher>> iterator = matcherMap.entrySet().iterator();
				while (iterator.hasNext()) {//删除匹配超时的玩家
					Map.Entry<Integer, Matcher> entry = iterator.next();
					Matcher matcher = entry.getValue();
					if (DateUtil.getSecondStamp() - matcher.getStartTime() >= TIMEOUT) {
						iterator.remove();
						this.matchFail(entry.getKey());
					}
				}
				if (matcherMap.size() < 2) {//人数不足
					return;
				}
				List<Matcher> tempList = new ArrayList<>(matcherMap.values());
				if (matcherMap.size() %2 == 1) {//有奇数个玩家 不匹配最后一个玩家
					tempList.remove(tempList.size()-1);
				}
				for(int i=0;i<tempList.size();i=i+2){
					this.matchSuccess(tempList.get(i), tempList.get(i+1));
					matcherMap.remove(tempList.get(i).getPlayerId());
					matcherMap.remove(tempList.get(i+1).getPlayerId());
				}
				
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 匹配成功
	 * @param matcher1
	 * @param matcher2
	 */
	private void matchSuccess(Matcher matcher1,Matcher matcher2){
		Player player1 = this.playerService.getPlayer(matcher1.getPlayerId());
		Player player2 = this.playerService.getPlayer(matcher2.getPlayerId());
		PlayerMessage playerMessage1 = PlayerService.buildPlayerMessage(player1);
		PlayerMessage playerMessage2 = PlayerService.buildPlayerMessage(player2);
		BattleMatchSyn.Builder syn = BattleMatchSyn.newBuilder();
		syn.setSuccess(1);
		syn.setPlayerInfo(playerMessage2);
		player1.getSession().send(GameCodeMaper.BATTLEMATCHSYN, syn.build());
		syn.setPlayerInfo(playerMessage1);
		player2.getSession().send(GameCodeMaper.BATTLEMATCHSYN, syn.build());
		ServiceContainer.getInstance().get(BattleService.class).creatRoom(player1, player2);
	}
	
	/**
	 * 通知玩家匹配失败
	 * @param playerId
	 */
	private void matchFail(int playerId){
		Player player = this.playerService.getPlayer(playerId);
		BattleMatchSyn.Builder syn = BattleMatchSyn.newBuilder();
		syn.setSuccess(2);
		player.getSession().send(GameCodeMaper.BATTLEMATCHSYN, syn.build());
	}

}
