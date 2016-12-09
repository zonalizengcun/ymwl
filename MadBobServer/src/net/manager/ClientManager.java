package net.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.session.AbstractClient;
import world.dao.DaoManager;
import world.entity.PlayerClient;

public class ClientManager {
	private Map<String, AbstractClient> clients = new HashMap<String, AbstractClient>();
	
	private static ClientManager manager = new ClientManager();
	
	public ClientManager(){
		
	}
	
	public static ClientManager getDefault(){
		return manager;
	}
	
	
	public void initClients(){
		//TODO 从数据库中加载所有client数据
		synchronized (clients) {
			Set<String> tokens = DaoManager.getDefault().getTokens();
			for(String token : tokens){
				clients.put(token, new PlayerClient(token));
			}
		}
	}
	
	public void addClient(AbstractClient client){
		synchronized (clients) {			
			this.clients.put(client.getToken(), client);
		}
	}
	
	public AbstractClient getClient(String token){
		synchronized (clients) {
			return this.clients.get(token);
		}
	}
	
}
