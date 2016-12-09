package test;

import java.util.List;

import world.Config;
import world.World;
import world.dao.DaoManager;
import world.entity.ChatEntity;

public class JedisTest {

	public static void main(String[] args)throws Exception{
		Config config = new Config("config.properties");
		config.init();
		World.getDefault().setConfig(config);
		DaoManager.getDefault();
		testRank();
	}
	
	private static void testJson(){
		ChatEntity chatEntity = new ChatEntity();
		chatEntity.setRoleId(10000);
		chatEntity.setContent("test message 9999");
		DaoManager.getDefault().saveGlobalChat(chatEntity);
		List<ChatEntity> result = DaoManager.getDefault().getGlobalChat();
		for(ChatEntity entity:result){
			System.out.println(entity.getRoleId());
			System.out.println(entity.getContent());
			System.out.println(entity.getTime());
		}
	}
	
	private static void testRank(){
//		Random random = new Random();
//		for(int i=0;i<20;i++){
//			DaoManager.getDefault().arenaAddRank(i, random.nextInt(1000));
//		}
		
		List<int[]> list = DaoManager.getDefault().getArenaRankList(0, 30);
//		list.stream().forEach(info -> {
//		System.out.println(info[0]+","+info[1]);});
		System.out.println();
	}
	
}
