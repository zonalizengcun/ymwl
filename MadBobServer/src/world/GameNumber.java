package world;

import java.io.FileInputStream;
import java.util.Properties;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

public class GameNumber {
	
	public static int friendLimit = 100;
	
	public static int globalChatLimit = 100;
	
	public static int privateChatLimit = 20;
	
	public static int chatCotentLimit = 200;
	
	public static String ArenaMapIdInfo = "";
	
	public static int arenaTimes = 0;
	//竞技场排行榜人数
	public static int arenaRank = 10;
	//游戏总排行榜人数
	public static int gameRank = 10;
	
	public static int occupyMoney = 20;
	
	public static int occupyScore = 2;
	//玩家初始积分
	public static int pvpStartScore = 100;
	
	public static int pvpGurdTime = 1800000;
	
	public static int pvpMatchupDis = 20;
	
	public static int pvpMatchdownDis = 10;
	
	public static int pvpDecScore = 100;
	
	public static int pvpWinScore = 200;
	
	public static int pvpWinJinghua = 30;
	
	public static int pvpLoseJinghua = 10;
	//占领模式关卡数量
	public static int occupySectionCnt = 84;
	
	public static int friendGiftLimit = 5;
	
	public static int[][] friendGifts;
	
	public static int worldChatLimit = 5;
	
	//怪物升级精华数量消耗
	public static int monsterJinghua = 200;
	//初始精华数量
	public static int pvpJinghua = 300;
	//pvp怪物最高等级
	public static int pvpMonsterLevel = 2;
	//pvp怪物数量
	public static int pvpMonsterNumber = 12;
	
	public static int pvpProtectPrice = 10;
	
	public static int pvpBuyTime = 600;
	
	public static void load(String path){
		try {
			Properties properties = new Properties();
			FileInputStream fileInputStream = new FileInputStream(path);
			properties.load(fileInputStream);
			fileInputStream.close();
			friendLimit = Integer.parseInt(properties.getProperty("friendLimit"));
			globalChatLimit = Integer.parseInt(properties.getProperty("chatCotentLimit"));
			privateChatLimit = Integer.parseInt(properties.getProperty("privateChatLimit"));
			chatCotentLimit = Integer.parseInt(properties.getProperty("chatCotentLimit"));
			ArenaMapIdInfo = properties.getProperty("arenaMapId");
			arenaTimes = Integer.parseInt(properties.getProperty("arenaTimes"));
			arenaRank = Integer.parseInt(properties.getProperty("arenaRank"));
			gameRank = Integer.parseInt(properties.getProperty("gameRank"));
			occupyMoney = Integer.parseInt(properties.getProperty("occupyMoney"));
			occupyScore = Integer.parseInt(properties.getProperty("occupyScore"));
			pvpStartScore = Integer.parseInt(properties.getProperty("pvpStartScore"));
			pvpGurdTime = Integer.parseInt(properties.getProperty("pvpGurdTime"));
			pvpMatchupDis = Integer.parseInt(properties.getProperty("pvpMatchupDis"));
			pvpMatchdownDis = Integer.parseInt(properties.getProperty("pvpMatchdownDis"));
			pvpWinScore = Integer.parseInt(properties.getProperty("pvpWinScore"));
			occupySectionCnt = Integer.parseInt(properties.getProperty("occupySectionCnt"));
			friendGiftLimit = Integer.parseInt(properties.getProperty("friendGiftLimit"));
			String friendGiftStr = properties.getProperty("friendGifts");
			String[] giftstrs = friendGiftStr.split(";");
			friendGifts = new int[giftstrs.length][2];
			for(int i=0;i<giftstrs.length;i++){
				friendGifts[i][0] = Integer.parseInt(giftstrs[i].split(",")[0].trim());
				friendGifts[i][1] = Integer.parseInt(giftstrs[i].split(",")[1].trim());
			}
			worldChatLimit = Integer.parseInt(properties.getProperty("worldChatLimit"));
			
			monsterJinghua = Integer.parseInt(properties.getProperty("monsterJinghua"));
			pvpJinghua = Integer.parseInt(properties.getProperty("pvpJinghua"));
			pvpMonsterLevel = Integer.parseInt(properties.getProperty("pvpMonsterLevel"));
			pvpMonsterNumber = Integer.parseInt(properties.getProperty("pvpMonsterNumber"));
			pvpLoseJinghua = Integer.parseInt(properties.getProperty("pvpLoseJinghua"));
			pvpWinJinghua = Integer.parseInt(properties.getProperty("pvpWinJinghua"));
			pvpProtectPrice = Integer.parseInt(properties.getProperty("pvpProtectPrice"));
			pvpBuyTime = Integer.parseInt(properties.getProperty("pvpBuyTime"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
