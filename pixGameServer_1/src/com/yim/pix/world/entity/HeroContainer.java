package com.yim.pix.world.entity;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.yim.io.Stream;

public class HeroContainer {

	private Map<Integer, Hero> heros = new LinkedHashMap<>(1);
	
	public Hero getHero(int instanceId){
		return heros.get(instanceId);
	}
	
	public List<Hero> getHeros(){
		return new LinkedList<Hero>(heros.values());
	}
	
	public void addHero(Hero hero){
		heros.put(hero.getInstanceId(), hero);
	}
	
	public Hero removeHero(int heroId){
		return heros.remove(heroId);
	}
	
	public void clear(){
		heros.clear();
	}
	
	/**
	 * ������д�뵽���ݿ���
	 */
	public byte[] getBytes(){
		Stream stream = new Stream();
		stream.writeInt(heros.size());
		for(Hero hero : heros.values()){
			hero.writeToStream(stream);
		}
		return stream.toArray();
	}
	
	/**
	 * ���������ݿ��ж���������
	 */
	public static HeroContainer parse(byte[] value){
		if(value==null||value.length==0){
			return new HeroContainer();
		}
		HeroContainer heroContainer = new HeroContainer();
		Stream stream = new Stream(value);
		int num = stream.readInt();
		for(int i=0;i<num;i++){
			Hero hero = Hero.readFromStream(stream);
			heroContainer.addHero(hero);
		}
		return heroContainer;
	}
	
}
