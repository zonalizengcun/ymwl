package com.yim.pix.world.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.yim.persist.EntityManager;
import com.yim.persist.EntityManagerFactory;
import com.yim.pix.world.entity.Racist;
import com.yim.service.IService;

public class RacistService implements IService {
	
	private EntityManager entityManager = EntityManagerFactory.createManager();
	
	private Map<Integer, Racist> racists = Collections.synchronizedMap(new HashMap<Integer,Racist>());

	@Override
	public void startup() {
		
	}

	@Override
	public void shutdown() {
		
	}
	
	public Racist loadRacist(int playerId,int racistType){
		Racist racist = this.entityManager.fetch(Racist.class, "from Racist where playerId=? and racistType=?", playerId,racistType);
		this.racists.put(racist.getPlayerId(), racist);
		return racist;
	}
	
	public void saveDB(){
		entityManager.saveOrUpdate(racists.values(), 10);
	}
	
	public Racist creatRacist(int playerId,int racistType){
		Racist racist = new Racist(playerId,racistType);
		this.racists.put(racist.getPlayerId(), racist);
		entityManager.create(racist);
		return racist;
	}
	
	public static void main(String[] args){
		RacistService service = new RacistService();
		//创建6万条数据
		Random random = new Random();
		for(int i=0;i<20000;i++){
			int value = random.nextInt(4);
			service.creatRacist(i, value);
			service.creatRacist(i, (value+2)%4);
			service.creatRacist(i, (value+3)%4);
		}
		
		//测试查询时间
		long start = System.currentTimeMillis();
		service.loadRacist(5002, 2);
		System.out.println((System.currentTimeMillis() - start));
		
	}
	
	/**
	 * 从内存中查询种族信息
	 * @param playerId
	 * @return
	 */
	public Racist getRacist(int playerId){
		return this.racists.get(playerId);
	}
	
	/**
	 * 查询种族信息，如果内存中不存在，从数据库中加载
	 * @param playerId
	 * @param racistType
	 * @return
	 */
	public Racist getRacist(int playerId,int racistType){
		if (this.racists.containsKey(playerId)) {
			return this.racists.get(playerId);
		}else{
			return loadRacist(playerId,racistType);
		}
	}
	
	//玩家下线 从内存中卸载
	public void unLoadRacist(int playerId){
		if (this.racists.containsKey(playerId)) {
			Racist racist = this.racists.remove(playerId);
			entityManager.saveOrUpdate(racist);
		}
	}

}
