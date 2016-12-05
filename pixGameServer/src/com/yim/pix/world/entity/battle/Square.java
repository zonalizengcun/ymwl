package com.yim.pix.world.entity.battle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.ObjectUtils.Null;

import com.yim.message.pix.game.MessagePtoto.BattleArmy;
import com.yim.pix.world.World;
import com.yim.pix.world.entity.Player;
import com.yim.pix.world.entity.Racist;

import io.netty.channel.epoll.AbstractEpollStreamChannel;
import jxl.read.biff.BiffException;

/**
 * 阵型信息
 * 
 * @author lizengcun
 *
 */
public class Square {

	/**
	 * key: the instanceId of army,value army
	 */
	private Map<Integer, Army> armyMap;
	
	private Army[][] x_Armies = new Army[8][6];
	private Army[][] y_Armies = new Army[6][8];
	private AtomicInteger poolInstance = new AtomicInteger(0);
	
	private Player player;
	
	/**
	 * 魔力值
	 */
	private int magic;
	
	/**
	 * 血量
	 */
	private int hp;
	

	public Square(Player player) {
		this.armyMap = new HashMap<>();
		this.player = player;
	}
	/**
	 * 初始化阵型信息
	 */
	public void initSquare(){
		this.initArmys();
	}
	
	public Map<Integer, Army> getArmyMap(){
		return this.armyMap;
	}
	
	/**
	 * 获取当前血量
	 * @return
	 */
	public int getHp(){
		return hp;
	}
	
	/**
	 * 获取当前控线士兵数量
	 * @return
	 */
	public int getIdleArmy(){
		return 48 - this.armyMap.size();
	}
	
	/**
	 * 初始化兵种信息
	 */
	private void initArmys(){
		Racist racist = player.getRacist();
		Random random = new Random();
		List<Army> armys = new ArrayList<>();
		//先从普通兵中随机27个
		for(int i=0;i<27;i++){
			int position = random.nextInt(3);
			int armyTempId = racist.getPosition(position);
			Army army = new Army();
			army.setInstanceId(poolInstance.getAndIncrement());
			army.setTemplateId(armyTempId);
			army.setColor(position);
			armys.add(army);
			this.armyMap.put(army.getInstanceId(), army);
		}

		//从普通兵和高级兵中随机5个
		for (int i = 0; i < 5; i++) {
			int position = random.nextInt(5);//�����е�λ��
			int armyTempId = racist.getPosition(position);
			if (armyTempId == -1) {
				position = random.nextInt(3);
				armyTempId = racist.getPosition(position);
			}
			Army army = new Army();
			army.setInstanceId(poolInstance.getAndIncrement());
			army.setTemplateId(armyTempId);
			if (army.getTemplate().level > 0) {//����Ǹ߼��� ���һ����ɫ
				army.setColor(random.nextInt(3));
			}
			armys.add(army);
			this.armyMap.put(army.getInstanceId(), army);
		}
		Collections.shuffle(armys);
		
		this.initArmyXY(random,armys);
		this.checkColor();
	}
	/**
	 * 设置xy坐标
	 */
	private void initArmyXY(Random random,List<Army> armys){
		for(Army army : armys){
			armySetXY(random, army);
			cacheXyArmy(army);
		}
	}
	
	/**
	 * 检查士兵颜色 是否可以组成特效
	 */
	private void checkColor(){
		int color1 = -1;
		int color2 = -1;
		for(Army[] armys : x_Armies){
			for(Army army : armys){
				if (army == null) {
					color1 = -1;
					color2 = -1;
					continue;
				}
				if (army.getTemplate().level != 0) {
					color1 = -1;
					color2 = -1;
					continue;
				}
				if (color1 == army.getColor() && color2 == army.getColor()) {
					int position = (color1+1)%3;
					int templateId = this.player.getRacist().getPosition(position);
					army.setTemplateId(templateId);
					army.setTemplate(World.getInstance().getArmyModel().getArmyTemplate(templateId));
					army.setColor(position);
				}
				color1 = color2;
				color2 = army.getColor();
			}
		}
		color1 = -1;
		color2 = -1;
		for(Army[] armys : y_Armies){
			for(Army army : armys){
				if (army == null) {
					color1 = -1;
					color2 = -1;
					continue;
				}
				if (army.getTemplate().level != 0) {
					color1 = -1;
					color2 = -1;
					continue;
				}
				if (color1 == army.getColor() && color2 == army.getColor()) {
					int position = (color1+1)%3;
					int templateId = this.player.getRacist().getPosition(position);
					army.setTemplateId(templateId);
					army.setTemplate(World.getInstance().getArmyModel().getArmyTemplate(templateId));
					army.setColor(position);
				}
				color1 = color2;
				color2 = army.getColor();
			}
		}
		for(Army army : this.armyMap.values()){
			if (army.getTemplate().level != 0) {
				if ( army.getY() >= 3) {
					continue;
				}
				Army next = x_Armies[army.getX()][army.getY()+2];
				Army next1 = x_Armies[army.getX()][army.getY()+2];
				if (next!=null && next1 != null ) {
					if (next.getColor() == next1.getColor()&&next.getColor() == army.getColor()) {
						army.setColor((army.getColor()+1)%3);
					}
				}
			}
		}
	}
	
	/**
	 * 将士兵添加到数组
	 * @param army
	 */
	private void cacheXyArmy(Army army){
		if (army.getTemplate().level == 0) {
			x_Armies[army.getX()][army.getY()] = army;
			y_Armies[army.getY()][army.getX()] = army;
		}else if (army.getTemplate().level == 1) {
			x_Armies[army.getX()][army.getY()] = army;
			y_Armies[army.getY()][army.getX()] = army;
			x_Armies[army.getX()][army.getY()+1] = army;
			y_Armies[army.getY()+1][army.getX()] = army;
		}else if (army.getTemplate().level == 2) {
			x_Armies[army.getX()][army.getY()] = army;
			y_Armies[army.getY()][army.getX()] = army;
			x_Armies[army.getX()][army.getY()+1] = army;
			y_Armies[army.getY()+1][army.getX()] = army;
			x_Armies[army.getX()+1][army.getY()] = army;
			y_Armies[army.getY()][army.getX()+1] = army;
			x_Armies[army.getX()+1][army.getY()+1] = army;
			y_Armies[army.getY()+1][army.getX()+1] = army;
		}
	}
	/**
	 * 设置士兵xy坐标
	 * @param random
	 * @param army
	 */
	private void armySetXY(Random random ,Army army){
		short x = (short)random.nextInt(8);//ʿ����x����
		short size = getYSize(x);//x��Ӧ��һ���ж��ٸ�ʿ��
		while(size>=6){
			x = (short)((x+1)%8);
			size = getYSize(x);
		}
		army.setX(x);
		army.setY(size);
		if (army.getTemplate().level == 0) {
			return;
		}
		//检查是否能容纳高级士兵
		boolean match = isMatch(army);
		if (!match) {
			short tempSize = 0;
			for(short i=0;i<8;i++){
				tempSize = getYSize(i);
				army.setX(i);
				army.setY(tempSize);
				match = isMatch(army);
				if (match) {
					break;
				}
			}
		}
		if (!match) {//依然不能容纳 ，把高级士兵缓存小兵……
			army.setX(x);
			army.setY(size);
			int position = random.nextInt(3);
			int armyTempId = player.getRacist().getPosition(position);
			army.setTemplateId(armyTempId);
			army.setTemplate(World.getInstance().getArmyModel().getArmyTemplate(armyTempId));
			army.setColor(position);
		}
		
	}
	
	/**
	 * 是否可以容纳
	 * @param army
	 * @return
	 */
	private boolean isMatch(Army army){
		boolean match = true;
		if (army.getTemplate().level == 1) {
			if (army.getY() >= 5) {
				match = false;
			}
		}
		if (army.getTemplate().level  == 2) {
			if (army.getY() >= 5 || army.getX()>=7 || (y_Armies[army.getY()][army.getX()+1]!=null)) {
				match = false;
			}
		}
		return match;
	}
	
	/**
	 * 某一列有多少个士兵
	 * @param x
	 * @return
	 */
	private short getYSize(short x){
		short size = 0;
		Army[] armys = x_Armies[x];
		for(Army army : armys){
			if (army == null) {
				break;
			}
			size++;
		}
		return size;
	}
	
	/**
	 * 获取魔力值
	 * @return
	 */
	public int getMagic(){
		return this.magic;
	}
	
	public static void main(String[] args) throws BiffException, IOException{
		World.getInstance().buildModles();
		Player player = new Player();
		Random random = new Random();
		Racist racist = new Racist();
		for(int i=0;i<1000;i++){
			System.out.println("number:"+i);
			racist.setPosition0(0);
			racist.setPosition1(1);
			racist.setPosition2(2);
			racist.setPosition3(random.nextBoolean()?3:4);
			racist.setPosition4(random.nextBoolean()?5:6);
			player.setRacist(racist);
			Square square = new Square(player);
			long start = System.currentTimeMillis();
			square.initArmys();
			System.out.println(System.currentTimeMillis() - start);
			for(Army[] arms: square.x_Armies){
				for(Army army : arms){
					if (army == null) {
						System.out.print("null;    ");
					}else{
						System.out.print(army.getInstanceId() +","+army.getTemplateId()+","+army.getColor()+","+army.getTemplate().armyName+";    ");
					}
				}
				System.out.println("");
			}
		}
	}
	
	
	public List<BattleArmy> buildBattleArmys(){
		List<BattleArmy> battleArmys = new ArrayList<>();
		for(Army[] armys : y_Armies){
			for(Army army : armys){
				if (army == null) {
					continue;
				}
				BattleArmy.Builder builder = BattleArmy.newBuilder();
				builder.setX(army.getX());
				builder.setY(army.getY());
				builder.setIcon(army.getTemplate().icon);
				builder.setArmyColor(army.getColor());
				builder.setInstanceId(army.getInstanceId());
				battleArmys.add(builder.build());
			}
		}
		return battleArmys;
	}
}
