package world.service;

import world.dao.DaoManager;

public class IDService implements Iservice{
	
	private static String ChatKey = "CHAT";
	
	private static String UserKey = "USER";

	@Override
	public void start() {
		DaoManager.getDefault().initIntId(UserKey, 1100);
	}

	@Override
	public void shutdown() {
		
	}
	
	public int getNextUserId(){
		return DaoManager.getDefault().nextIntID(UserKey);
	}
	
	public int getNextChatId(){
		return DaoManager.getDefault().nextIntID(ChatKey);
	}

}
