package world.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Descriptors.EnumDescriptor;

import net.session.UserEntity;
import net.sf.json.JSONObject;
import world.GameNumber;
import world.entity.ArenaEntiy;
import world.entity.ChatEntity;
import world.entity.ClientLogEntity;
import world.entity.FriendEntity;
import world.entity.MonthAwardEntity;
import world.entity.OccupyEntity;
import world.entity.PlatEntity;
import world.entity.PlayerInfoEntity;
import world.entity.PrivateChatEntity;
import world.entity.PveEntity;
import world.entity.PvpEntity;
import world.entity.SectionEntity;
import world.entity.WeekAwardEntity;

public class DaoManager {

	private static DaoManager daoManager = new DaoManager();

	private RedisClient redisClient;

	private DaoManager() {
		this.redisClient = new RedisClient();
		this.redisClient.init();
	}

	private static final String USERKEY = "USERMAP";
	
	private static final String IDSERVICEKEY = "IDSERVICE";

	private static final String ID_TOKEN = "USERID_TOKEN";

	private static final String ACC_TOKEN = "ACC_TOKEN";
	
	private static final String PLAT_MAP = "PLAT_MAP";//记录
	
	private static final String PLAYERINFO="PLAYERINFO";
	

	/**
	 * 好友列表
	 */
	private static final String FRIEND_LIST = "FRIEND_LIST";

	/**
	 * 世界聊天
	 */
	private static final String GLOBAL_CHAT_KEY = "GLOBAL_CHAT_KEY";

	/**
	 * 私聊留言
	 */
	private static final String PRIVATE_CHAT = "PRIVATE_CHAT";
	
	/**
	 * 竞技场地图id
	 */
	private static final String ARENA_MAP = "ARENA_MAPID";
	
	/**
	 * 竞技场刷新时间
	 */
	private static final String ARENA_MAP_TIME = "ARENA_MAPTIME";
	
	/**
	 * 竞技场排行
	 */
	private static final String ARENA_RANK = "ARENA_RANK";
	
	/**
	 * 总排行榜
	 */
	private static final String GAME_RANK = "GAME_RANK";
	
	/**
	 * 竞技场玩家信息
	 */
	private static final String ARENA_INFO = "ARENA_INFO";

	
	/**
	 * 占领模式关卡信息
	 */
	private static final String OCCOUPY_SECTION = "OCCOUPY_SECTION";
	
	private static final String OCCOUPY_SECTION_INFO = "OCCOUPY_SECTION_INFO";
	
	/**
	 * pvp地图信息
	 */
	private static final String PVP_INFO = "PVP_INFO";
	
	/**
	 * pvp排行榜
	 */
	private static final String PVP_RANK = "PVP_RANK";
	
	/**
	 * pve关卡数据
	 */
	private static final String PVE_ENTITY = "PVE_ENTITY";
	
	
	/**
	 * 月签奖励
	 */
	private static final String MONTH_AWARD = "MONTH_AWARD";
	/**
	 * 周签到奖励
	 */
	private static final String WEEK_AWARD = "WEEK_AWARD";
	
	private static final String PLAT_REGIST = "PLATREGIST_";

	public static DaoManager getDefault() {
		return daoManager;
	}
	
	public PlatEntity getPlatEntity(String platName,String platToken){
		String platInfo = this.redisClient.hget(PLAT_MAP, platName+"###"+platToken);
		if (platInfo == null || platInfo.equals("")) {
			return null;
		}
		JSONObject jsonObject = JSONObject.fromObject(platInfo);
		PlatEntity platEntity = (PlatEntity) JSONObject.toBean(jsonObject, PlatEntity.class);
		return platEntity;
	}

	public void savePlatEntity(PlatEntity platEntity){
		String platInfo = JSONObject.fromObject(platEntity).toString();
		this.redisClient.hset(PLAT_MAP, platEntity.getKey(), platInfo);
	}
	public Set<String> getTokens() {
		Set<String> tokens = this.redisClient.hgetAll(USERKEY).keySet();
		return tokens;
	}

	public void saveUserEntity(UserEntity user) {
		String userString = JSONObject.fromObject(user).toString();
		this.redisClient.hset(USERKEY, user.getToken(), userString);
		// id和token的映射
		this.redisClient.hset(ID_TOKEN, user.getId() + "", user.getToken());
		this.redisClient.hset(ACC_TOKEN, user.getAccountName(), user.getToken());
	}

	public UserEntity findUserEntity(String token) {
		String userString = this.redisClient.hget(USERKEY, token);
		if (userString == null || userString.equals("")) {
			return null;
		}
		JSONObject jsonObject = JSONObject.fromObject(userString);
		UserEntity userEntity = (UserEntity) JSONObject.toBean(jsonObject, UserEntity.class);
		return userEntity;
	}

	public UserEntity findUserByAcc(String account) {
		if (!this.redisClient.hexiste(ACC_TOKEN, account)) {
			return null;
		}
		String token = this.redisClient.hget(ACC_TOKEN, account);
		return this.findUserEntity(token);

	}

	public UserEntity findUserEntity(int id) {
		if (!this.redisClient.hexiste(ID_TOKEN, id + "")) {
			return null;
		}
		String token = this.redisClient.hget(ID_TOKEN, id + "");
		return this.findUserEntity(token);
	}
	
	/**
	 * 保存玩家附加信息
	 * @param playerInfoEntity
	 */
	public void savePlayerInfo(PlayerInfoEntity playerInfoEntity){
		ObjectMapper objectMapper = new ObjectMapper();
		String valueStr;
		try {
			valueStr = objectMapper.writeValueAsString(playerInfoEntity);
			this.redisClient.hset(PLAYERINFO, playerInfoEntity.getRoleId()+"", valueStr);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取玩家附加信息
	 * @param roleId
	 * @return
	 */
	public PlayerInfoEntity getPlayerInfo(int roleId){
		if (!this.redisClient.hexiste(PLAYERINFO, roleId+"")) {
			PlayerInfoEntity playerInfoEntity = new PlayerInfoEntity();
			playerInfoEntity.setRoleId(roleId);
			this.savePlayerInfo(playerInfoEntity);
			return playerInfoEntity;
		}
		String valueStr = this.redisClient.hget(PLAYERINFO, roleId+"");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			PlayerInfoEntity playerInfoEntity = objectMapper.readValue(valueStr, PlayerInfoEntity.class);
			return playerInfoEntity;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	

	public void saveFriendList(int roleId, FriendEntity friend) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String valueStr = objectMapper.writeValueAsString(friend);
		this.redisClient.hset(FRIEND_LIST, roleId+"", valueStr);
	}

	public FriendEntity getFrindList(int roleId) throws IOException{
		String valueStr = this.redisClient.hget(FRIEND_LIST, roleId+"");
		if (valueStr == null) {
			FriendEntity friendEntity = new FriendEntity();
			friendEntity.setRoleId(roleId);
			return friendEntity;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		FriendEntity friendEntity = objectMapper.readValue(valueStr, FriendEntity.class);
		return friendEntity;
	}

	public boolean isInitFrindList(int roleId) {
		return this.redisClient.hexiste(FRIEND_LIST, roleId+"");
	}

	public void saveGlobalChat(ChatEntity chatEntity) {
		String chatString = JSONObject.fromObject(chatEntity).toString();
		long length = this.redisClient.lLen(GLOBAL_CHAT_KEY);
		if (length >= GameNumber.globalChatLimit) {
			this.redisClient.rPop(GLOBAL_CHAT_KEY);
		}
		this.redisClient.lpush(GLOBAL_CHAT_KEY, chatString);
	}

	public List<ChatEntity> getGlobalChat() {
		List<String> strList = this.redisClient.lrange(GLOBAL_CHAT_KEY, 0, GameNumber.globalChatLimit);
		List<ChatEntity> chatList = new ArrayList<ChatEntity>();
		for (String str : strList) {
			ChatEntity chatEntity = (ChatEntity) JSONObject.toBean(JSONObject.fromObject(str), ChatEntity.class);
			chatList.add(chatEntity);
		}
		return chatList;
	}

	/**
	 * 保存留言信息
	 * @param roleId
	 * @param chatEntity
	 */
	public void savePrivateChat(int roleId, PrivateChatEntity chatEntity)throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String chatStr = objectMapper.writeValueAsString(chatEntity);
		this.redisClient.hset(PRIVATE_CHAT, roleId + "", chatStr);
	}

	public PrivateChatEntity getPrivateChat(int roleId) throws IOException {
		if (!this.redisClient.hexiste(PRIVATE_CHAT, roleId + "")) {
			PrivateChatEntity pcEntity = new PrivateChatEntity();
			this.savePrivateChat(roleId, pcEntity);
			return pcEntity;
		}
		String value = this.redisClient.hget(PRIVATE_CHAT, roleId + "");
		ObjectMapper objectMapper = new ObjectMapper();
		PrivateChatEntity chatEntity = objectMapper.readValue(value, PrivateChatEntity.class);
		return chatEntity;
	}
	
	/**
	 * 保存竞技场地图数据
	 */
	public void saveArenaMap(int map,long time){
		this.redisClient.set(ARENA_MAP, map+"");
		this.redisClient.set(ARENA_MAP_TIME,time+"");
	}
	
	public int getArenaMap(){
		if (!this.redisClient.exists(ARENA_MAP)) {
			this.saveArenaMap(0, System.currentTimeMillis());
			return 0;
		}
		return Integer.parseInt(this.redisClient.get(ARENA_MAP));
	}
	
	public long getArenaMapTime(){
		if (!this.redisClient.exists(ARENA_MAP_TIME)) {
			return 0;
		}
		return Long.parseLong(this.redisClient.get(ARENA_MAP_TIME));
	}
	
	/**
	 * 保存竞技场玩家信息
	 * @param arenaEntiy
	 */
	public void saveArenaEntity(ArenaEntiy arenaEntiy){
		String arenainfo = JSONObject.fromObject(arenaEntiy).toString();
		this.redisClient.hset(ARENA_INFO, arenaEntiy.getRoleId()+"", arenainfo);
	}
	
	/**
	 * 获取竞技场玩家信息
	 * @param roleId
	 * @return
	 */
	public ArenaEntiy getArenaEntity(int roleId){
		if (!this.redisClient.hexiste(ARENA_INFO, roleId+"")) {
			return null;
		}
		String arenainfo = this.redisClient.hget(ARENA_INFO, roleId+"");
		ArenaEntiy arenaEntity = (ArenaEntiy)JSONObject.toBean(JSONObject.fromObject(arenainfo),ArenaEntiy.class);
		return arenaEntity;
	}
	
	/**
	 * 增加排名
	 * @param roleId
	 * @param score
	 */
	public void arenaAddRank(int roleId,long score){
		this.redisClient.zAdd(ARENA_RANK, score, roleId+"");
	}
	
	/**
	 * 移除竞技场排行榜最后一名
	 */
	public void remArenaLast(long start,long end){
		this.redisClient.zRemRangeByRank(ARENA_RANK, start, end);
	}
	
	/**
	 * 获取排名列表 【id,分数】
	 * @param start
	 * @param end
	 * @return
	 */
	public List<int[]> getArenaRankList(int start,int end){
		Set<String> members = this.redisClient.zReverRange(ARENA_RANK, start, end);
		List<int[]> ranks = new ArrayList<int[]>();
		for(String member : members){
//			int rank = (int)this.redisClient.zRevrank(ARENA_RANK, member);
			int score = (int)this.redisClient.zScore(ARENA_RANK, member);
			ranks.add(new int[]{Integer.parseInt(member),score});
		}
		return ranks;
	}
	
	public void saveSectionEntity(SectionEntity sectionEntity) throws IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		String valuestr = objectMapper.writeValueAsString(sectionEntity);
		this.redisClient.hset(OCCOUPY_SECTION_INFO, sectionEntity.getUserId()+"", valuestr);
	}
	
	public SectionEntity getSectionEntity(int userId) throws IOException{
		if (!this.redisClient.hexiste(OCCOUPY_SECTION_INFO, userId+"")) {
			SectionEntity sectionEntity = new SectionEntity();
			sectionEntity.setUserId(userId);
			this.saveSectionEntity(sectionEntity);
			return sectionEntity;
		}
		String valuestr = this.redisClient.hget(OCCOUPY_SECTION_INFO,userId+"");
		ObjectMapper objectMapper = new ObjectMapper();
		SectionEntity sectionEntity = objectMapper.readValue(valuestr, SectionEntity.class);
		return sectionEntity;
		
	}
	
	public void saveOccupyEntity(OccupyEntity occupyEntity){
		String valuestr = JSONObject.fromObject(occupyEntity).toString();
		this.redisClient.hset(OCCOUPY_SECTION, occupyEntity.getSectionId()+"", valuestr);
	}
	
	public OccupyEntity getOccupyEntity(int sectionId){
		if (!this.redisClient.hexiste(OCCOUPY_SECTION, sectionId+"")) {
			OccupyEntity occupyEntity = new OccupyEntity();
			occupyEntity.setSectionId(sectionId);
			String valuestr = JSONObject.fromObject(occupyEntity).toString();
			this.redisClient.hset(OCCOUPY_SECTION, sectionId+"", valuestr);
			return occupyEntity;
		}

		String valueStr = this.redisClient.hget(OCCOUPY_SECTION, sectionId+"");
		OccupyEntity occupyEntity = (OccupyEntity)JSONObject.toBean(JSONObject.fromObject(valueStr),OccupyEntity.class);
		return occupyEntity;
	}
	
	/**
	 * 获取所有占领信息
	 * @return
	 */
	public List<OccupyEntity> getAllOccupyEntity(){
		List<String> list = this.redisClient.hValues(OCCOUPY_SECTION);
		List<OccupyEntity> occList = new ArrayList<>(list.size());
		for(String valueStr : list){
			OccupyEntity occupyEntity = (OccupyEntity)JSONObject.toBean(JSONObject.fromObject(valueStr),OccupyEntity.class);
			occList.add(occupyEntity);
		}
		return occList;
	}
	

	
	/**
	 * 获取pvp数据
	 * @param playerId
	 * @return
	 */
	public PvpEntity getPvpEntity(int playerId){
		if (!this.redisClient.hexiste(PVP_INFO, playerId+"")) {
			return null;
		}
		String pvpString = this.redisClient.hget(PVP_INFO, playerId+"");
		ObjectMapper objectMapper = new ObjectMapper();
		PvpEntity pvpEntity = null;
		try {
			pvpEntity = objectMapper.readValue(pvpString, PvpEntity.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pvpEntity;
		
	}
	
	public Map<Integer, PvpEntity> getPvpEntityMap(){
		Map<Integer,PvpEntity> map = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			
			if (this.redisClient.exists(PVP_INFO)) {
				List<String> values = this.redisClient.hValues(PVP_INFO);
				for(String value : values){
					PvpEntity pvpEntity = objectMapper.readValue(value, PvpEntity.class);
					map.put(pvpEntity.getPlayerId(), pvpEntity);
				}
			}
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 保存pvp数据
	 * @param pvpEntity
	 */
	public void savePvpEntity(PvpEntity pvpEntity){
		ObjectMapper objectMapper = new ObjectMapper();
		String valueStr;
		try {
			valueStr = objectMapper.writeValueAsString(pvpEntity);
			this.redisClient.hset(PVP_INFO, pvpEntity.getPlayerId()+"", valueStr);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addPvpRank(int playerId,int score){
		this.redisClient.zAdd(PVP_RANK, score, playerId+"");
	}
	
	/**
	 * 获取某个排名段的所有玩家排名
	 * @param start
	 * @param end
	 * @return
	 */
	public List<int[]> getPvpRankList(int start ,int end){
		Set<String> members = this.redisClient.zReverRange(PVP_RANK, start, end);
		List<int[]> ranks = new ArrayList<int[]>();
		for(String member : members){
			int score = (int)this.redisClient.zScore(PVP_RANK, member);
			ranks.add(new int[]{Integer.parseInt(member),score});
		}
		return ranks;
	}
	
	/**
	 * 根据排名找玩家id
	 * @param rankIndex
	 * @return
	 */
	public int getPvpRankPlayer(long rankIndex){
		Set<String> members = this.redisClient.zRange(PVP_RANK, rankIndex, rankIndex);
		if (members.size() == 0) {
			return -1;
		}else{
			return Integer.parseInt(members.iterator().next());
		}
	}
	
	public Long getPvpRank(int playerId){
		Long rank = this.redisClient.zRevrank(PVP_RANK, playerId+"");
		return rank;
	}
	
	/**
	 * 获取当前排行榜长度
	 * @return
	 */
	public long getPvpRankLength(){
		return this.redisClient.zCard(PVP_RANK);
	}

	/**
	 * 保存pve信息
	 * @param pveEntity
	 * @throws JsonProcessingException 
	 */
	public void savePveEntity(PveEntity pveEntity) throws IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		String valueStr = objectMapper.writeValueAsString(pveEntity);
		this.redisClient.hset(PVE_ENTITY, pveEntity.getPlayerId()+"", valueStr);
	}
	
	/**
	 * 获取pve信息
	 * @param playerId
	 * @return
	 * @throws IOException 
	 */
	public PveEntity getPveEntity(int playerId) throws IOException{
		if (!this.redisClient.hexiste(PVE_ENTITY, playerId+"")) {
			PveEntity pveEntity = new PveEntity();
			pveEntity.setPlayerId(playerId);
			this.savePveEntity(pveEntity);
			return pveEntity;
		}
		String valueStr = this.redisClient.hget(PVE_ENTITY, playerId+"");
		ObjectMapper objectMapper = new ObjectMapper();
		PveEntity entity = objectMapper.readValue(valueStr, PveEntity.class);
		return entity;
	}
	
	/**
	 * 清除占领模式内容
	 * @param key
	 */
	public void deleteOccupy(){
		this.redisClient.del(OCCOUPY_SECTION);
		this.redisClient.del(OCCOUPY_SECTION_INFO);
	}
	
	/**
	 * 获取一个全局id
	 * 
	 * @return
	 */
	public int nextIntID(String type) {
		return (int) this.redisClient.incr(IDSERVICEKEY+type);
	}

	public int currentIntID(String type) {
		return Integer.parseInt(this.redisClient.get(IDSERVICEKEY+type));
	}
	//初始化一个id
	public void initIntId(String type,int value){
		if (!this.redisClient.exists(IDSERVICEKEY+type)) {
			this.redisClient.set(IDSERVICEKEY+type, value+"");
		}
	}
	
	public void saveMonthAward(MonthAwardEntity monthAwardEntity){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String valuestr = objectMapper.writeValueAsString(monthAwardEntity);
			this.redisClient.hset(MONTH_AWARD, monthAwardEntity.getUserId()+"", valuestr);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public MonthAwardEntity getMonthAward(int userId){
		if (!this.redisClient.hexiste(MONTH_AWARD, userId+"")) {
			MonthAwardEntity monthAwardEntity = new MonthAwardEntity();
			monthAwardEntity.setUserId(userId);
			this.saveMonthAward(monthAwardEntity);
			return monthAwardEntity;
		}
		String valueStr = this.redisClient.hget(MONTH_AWARD, userId+"");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			MonthAwardEntity awardEntity = objectMapper.readValue(valueStr, MonthAwardEntity.class);
			return awardEntity;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void saveWeekAward(WeekAwardEntity weekAwardEntity){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String valuestr = objectMapper.writeValueAsString(weekAwardEntity);
			this.redisClient.hset(WEEK_AWARD, weekAwardEntity.getUserId()+"", valuestr);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public WeekAwardEntity getWeekAward(int userId){
		if (!this.redisClient.hexiste(WEEK_AWARD, userId+"")) {
			WeekAwardEntity weekAwardEntity = new WeekAwardEntity();
			weekAwardEntity.setUserId(userId);
			this.saveWeekAward(weekAwardEntity);
			return weekAwardEntity;
		}
		String valueStr = this.redisClient.hget(WEEK_AWARD, userId+"");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			WeekAwardEntity awardEntity = objectMapper.readValue(valueStr, WeekAwardEntity.class);
			return awardEntity;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取排名列表 【id,分数】
	 * @param start
	 * @param end
	 * @return
	 */
	public List<int[]> getGameRankList(int start,int end){
		Set<String> members = this.redisClient.zReverRange(GAME_RANK, start, end);
		List<int[]> ranks = new ArrayList<int[]>();
		for(String member : members){
			int score = (int)this.redisClient.zScore(GAME_RANK, member);
			ranks.add(new int[]{Integer.parseInt(member),score});
		}
		return ranks;
	}
	
	/**
	 * 增加排名
	 * @param roleId
	 * @param score
	 */
	public void addGameRank(int roleId,long score){
		this.redisClient.zAdd(GAME_RANK, score, roleId+"");
	}
	
	/**
	 * 移除总排行榜最后一名
	 */
	public void remGameLast(long start,long end){
		this.redisClient.zRemRangeByRank(GAME_RANK, start, end);
	}
	
	public void savePlatRegist(String plat,String unique,ClientLogEntity clientLogEntity){
		String valueStr = JSONObject.fromObject(clientLogEntity).toString();
		this.redisClient.hset(PLAT_REGIST+plat, unique, valueStr);
	}
	
	public ClientLogEntity getClientLog(String plat,String unique){
		if (!this.redisClient.hexiste(PLAT_REGIST+plat, unique)) {
			ClientLogEntity clientLogEntity = new ClientLogEntity();
			this.savePlatRegist(plat, unique, clientLogEntity);
			return clientLogEntity;
		}
		String valueStr = this.redisClient.hget(PLAT_REGIST+plat, unique);
		ClientLogEntity entity = (ClientLogEntity)JSONObject.toBean(JSONObject.fromObject(valueStr),ClientLogEntity.class);
		return entity;
	}
}
