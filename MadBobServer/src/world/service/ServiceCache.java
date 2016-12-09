package world.service;

import world.schedule.Scheduler;

/**
 * 缓存所有service
 * @author admin
 *
 */
public class ServiceCache {
	
	private static ServiceCache instance = new ServiceCache();
	
	private IDService idService;
	
	private PlayerService playerService;
	
	private ChatService chatService;
	
	private ArenaBattleService arenaBattleService;
	
	private PvpService pvpService;
	

	private ServiceCache(){
		
	}
	
	public static ServiceCache getDefault(){
		return instance;
	}

	public PlayerService getPlayerService() {
		return playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}
	
	public void setChatService(ChatService chatService){
		this.chatService = chatService;
	}
	
	public ChatService getChatService(){
		return this.chatService;
	}
	
	
	public ArenaBattleService getArenaBattleService() {
		return arenaBattleService;
	}
	
	public void setArenaBattleService(ArenaBattleService arenaBattleService) {
		this.arenaBattleService = arenaBattleService;
	}
	
	
	
	public void start(){
		this.idService.start();
		this.playerService.start();
		this.chatService.start();
		this.arenaBattleService.start();
		this.pvpService.start();
		Scheduler.getInstance().start();
	}

	public PvpService getPvpService() {
		return pvpService;
	}

	public void setPvpService(PvpService pvpService) {
		this.pvpService = pvpService;
	}
	
	public void setIDService(IDService idService){
		this.idService = idService;
	}
	
	public IDService getIDservice(){
		return this.idService;
	}

	
	
}
