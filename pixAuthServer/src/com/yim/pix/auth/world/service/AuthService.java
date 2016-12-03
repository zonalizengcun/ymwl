package com.yim.pix.auth.world.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.LayoutFocusTraversalPolicy;

import com.yim.message.pix.auth.AuthCodeMaper;
import com.yim.persist.EntityManager;
import com.yim.persist.EntityManagerFactory;
import com.yim.pix.auth.world.World;
import com.yim.pix.auth.world.entity.User;
import com.yim.pix.auth.world.exception.AuthException;
import com.yim.pix.auth.world.handler.AuthHandler;
import com.yim.service.IService;
import com.yim.service.ServiceContainer;
import com.yim.service.impl.IdService;

public class AuthService implements IService{
	
	private EntityManager entityManager = EntityManagerFactory.createManager();
	
	private Map<Integer, User> id2User = new HashMap<>();
	
	private Map<String, User> accName2User = new HashMap<>();
	
	private Object lock = new Object();

	@Override
	public void startup() {
		this.readDB();
		AuthHandler authHandler = new AuthHandler();
		World.getInstance().getHandlerDispatch().register(authHandler.getOpcodes(), authHandler);
		
	}

	@Override
	public void shutdown() {
		saveDB();
		
	}
	
	public void readDB(){
		List<User> userList = this.entityManager.query(User.class, "from User");
		synchronized (lock) {
			for(User user : userList){
				this.id2User.put(user.getId(), user);
				this.accName2User.put(user.getAccountName(), user);
			}
		}
	}
	
	public void saveDB(){
		this.entityManager.saveOrUpdate(this.id2User.values(), 10);
	}
	
	public User getUser(String accountName){
		synchronized (lock) {
			return this.accName2User.get(accountName);
		}
	}
	
	/**
	 * 创建一个user
	 * @param accountName
	 * @param passWord
	 * @return 0 创建成功 1账号已存在
	 */
	public User creatUser(String accountName,String passWord) throws AuthException{
		User user = null;
		synchronized (lock) {
			if (this.accName2User.containsKey(accountName)) {
				throw new AuthException("账号名已存在");
			}
			IdService idService = ServiceContainer.getInstance().get(IdService.class);
			int accId = idService.getNextAccountId();
			user = new User(accId);
			user.setAccountName(accountName);
			user.setPassWord(passWord);
			this.id2User.put(user.getId(), user);
			this.accName2User.put(user.getAccountName(), user);
		}
		if (user != null) {
			this.entityManager.create(user);
		}
		return user;
		
	}
	
	

}
