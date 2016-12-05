package com.yim.pix.world.entity.battle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.yim.pix.world.World;
import com.yim.pix.world.entity.Player;
import com.yim.pix.world.entity.Racist;

import jxl.read.biff.BiffException;

/**
 * 战斗中的阵营
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

	public Square(Player player) {
		this.armyMap = new HashMap<>();
		this.player = player;
	}
	
	public Map<Integer, Army> getArmyMap(){
		return this.armyMap;
	}
	
	/**
	 * 初始化士兵 
	 */
	public void initArmys(){
		Racist racist = player.getRacist();
		Random random = new Random();
		List<Army> armys = new ArrayList<>();
		//先刷27个低级兵
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

		//从高级兵和低级兵里随机5个兵
		for (int i = 0; i < 5; i++) {
			int position = random.nextInt(5);//阵型中的位置
			int armyTempId = racist.getPosition(position);
			if (armyTempId == -1) {
				position = random.nextInt(3);
				armyTempId = racist.getPosition(position);
			}
			Army army = new Army();
			army.setInstanceId(poolInstance.getAndIncrement());
			army.setTemplateId(armyTempId);
			if (army.getTemplate().level > 0) {//如果是高级兵 随机一种颜色
				army.setColor(random.nextInt(3));
			}
			armys.add(army);
			this.armyMap.put(army.getInstanceId(), army);
		}
		Collections.shuffle(armys);
		//设置兵的位置
		this.initArmyXY(random,armys);
		
	}
	/**
	 * 给士兵设置xy坐标
	 */
	private void initArmyXY(Random random,List<Army> armys){
		for(Army army : armys){
			armySetXY(random, army);
			cacheXyArmy(army);
		}
	}
	
	/**
	 * 将xy填充到数组中
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
	 * 设置xy坐标
	 * @param random
	 * @param army
	 */
	private void armySetXY(Random random ,Army army){
		short x = (short)random.nextInt(8);//士兵的x坐标
		short size = getYSize(x);//x对应的一列有多少个士兵
		while(size>=6){
			x = (short)((x+1)%8);
			size = getYSize(x);
		}
		army.setX(x);
		army.setY(size);
		if (army.getTemplate().level == 0) {
			return;
		}
		//如果是高级兵 需要检查当前位置是否合适
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
		if (!match) {//如果还找不到位置，放弃……换个普通兵
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
	 * 判断一个高级士兵的位置是否合适，是否能够容下
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
	 * 根据x轴获取y的个数
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
	
}
