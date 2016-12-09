package com.yim.pix.world.entity.battle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.yim.message.pix.game.MessagePtoto.AtkQueueMessage;
import com.yim.message.pix.game.MessagePtoto.BattleArmy;
import com.yim.message.pix.game.MessagePtoto.BattleStepMessage;
import com.yim.message.pix.game.MessagePtoto.WallMergeMessage;
import com.yim.message.pix.game.MessagePtoto.WallQueueMessage;
import com.yim.pix.world.World;
import com.yim.pix.world.entity.Player;
import com.yim.pix.world.entity.Racist;
import com.yim.pix.world.exception.BattleException;

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
	
	/**
	 * 所有攻击队列
	 */
	private Map<Integer,AtkQueue> atkQueues = new HashMap<>();
	

	public Square(Player player) {
		this.armyMap = new HashMap<>();
		this.player = player;
	}
	/**
	 * 初始化阵型信息
	 */
	public void initSquare(){
		do {
			this.initArmys();//初始化兵种
		} while (this.checkColor());//检测是否撞色如果撞色重新初始化
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
		this.armyMap.clear();
		this.x_Armies = new Army[8][6];
		this.y_Armies = new Army[6][8];
		
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
			int position = random.nextInt(5);//
			int armyTempId = racist.getPosition(position);
			if (armyTempId == -1) {
				position = random.nextInt(3);
				armyTempId = racist.getPosition(position);
			}
			Army army = new Army();
			army.setInstanceId(poolInstance.getAndIncrement());
			army.setTemplateId(armyTempId);
			if (army.getTemplate().level > 0) {
				army.setColor(random.nextInt(3));
			}else{
				army.setColor(position);
			}
			armys.add(army);
			this.armyMap.put(army.getInstanceId(), army);
		}
		Collections.shuffle(armys);
		
		this.initArmyXY(random,armys);
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
	private boolean checkColor(){
		int color1 = -1;
		int color2 = -1;
		for(Army[] armys : x_Armies){
			color1 = -1;
			color2 = -1;
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
					return true;
				}
				color1 = color2;
				color2 = army.getColor();
			}
		}
		for(Army[] armys : y_Armies){
			color1 = -1;
			color2 = -1;
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
					return true;
				}
				color1 = color2;
				color2 = army.getColor();
			}
		}
		return false;
	}
	
	/**
	 * 将士兵添加到数组
	 * @param army
	 */
	private void cacheXyArmy(Army army){
		x_Armies[army.getX()][army.getY()] = army;
		y_Armies[army.getY()][army.getX()] = army;
	}
	
	private void removeCacheXy(int x,int y){
		x_Armies[x][y] = null;
		y_Armies[y][x] = null;
	}
	/**
	 * 设置士兵xy坐标
	 * @param random
	 * @param army
	 */
	private void armySetXY(Random random ,Army army){
		short x = (short)random.nextInt(8);//随机一列
		short size = getYSize(x);
		while(size>=6){
			x = (short)((x+1)%8);
			size = getYSize(x);
		}
		army.setX(x);
		army.setY(size);
		if (army.getTemplate().level == 0) {
			return;
		}
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
		for(int i=0;i<1000;i++){
			Player player = new Player();
			Racist racist = new Racist();
			System.out.println("number:"+i);
			racist.setPosition0(0);
			racist.setPosition1(1);
			racist.setPosition2(2);
			racist.setPosition3(3);
			racist.setPosition4(4);
			player.setRacist(racist);
			Square square = new Square(player);
			long start = System.currentTimeMillis();
			square.initSquare();
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
	
	/**
	 * 检查队形中所有队列
	 */
	public void checkQueue(){
		for(AtkQueue queue : atkQueues.values()){
			if (queue.getRound() != 0) {
				queue.setRound(queue.getRound()-1);
				queue.setAtk(queue.getAtk()+queue.getArmyTemplate().queueAddAtk);
			}
		}
	}
	
	/**
	 * 玩家移动后检查是否有新的攻击队列
	 */
	public BattleStepMessage checkAtkqueue(){
		//攻击队列检测
		for(Army[] armys : x_Armies){
			int color1 = -1;
			int color2 = -1;
			for(int i=0;i<armys.length;i++){
				Army army = armys[i];
				if (army == null || army.getQueueId() != 0 || army.isWall()) {
					color1 = -1;
					color2 = -1;
					continue;
				}
				if (army.getTemplate().level != 0) {
					color1 = -1;
					color2 = army.getColor();
					continue;
				}
				if (army.getColor()==color1 && army.getColor() == color2) {
					if (armys[i-2].getTemplate().level == 0) {
						AtkQueue atkQueue = new AtkQueue(this.poolInstance.getAndIncrement(),army.getTemplateId(),army.getX());
						atkQueue.setArmyIds(new int[]{armys[i-2].getInstanceId(),armys[i-1].getInstanceId(),armys[i].getInstanceId()});
						return this.addNewAtkQueue(atkQueue);
					}else{
						AtkQueue atkQueue = new AtkQueue(this.poolInstance.getAndIncrement(),army.getTemplateId(),army.getX());
						atkQueue.setArmyIds(new int[]{armys[i-2].getInstanceId()});
						this.armyMap.remove(armys[i-1].getInstanceId());
						this.armyMap.remove(armys[i].getInstanceId());
						this.removeCacheXy(armys[i-1].getX(), armys[i-1].getY());
						this.removeCacheXy(armys[i].getX(), armys[i].getY());
						return this.addNewAtkQueue(atkQueue);
					}
				}
				color1 = color2;
				color2 = army.getColor();
			}
		}
		
		return null;
	}
	/**
	 * 添加一个攻击队列
	 * @param atkQueue
	 */
	private BattleStepMessage addNewAtkQueue(AtkQueue atkQueue){
		BattleStepMessage.Builder stepBuilder = BattleStepMessage.newBuilder();
		AtkQueueMessage.Builder atkBuilder = AtkQueueMessage.newBuilder();//攻击队列协议
		this.atkQueues.put(atkQueue.getInstanceId(), atkQueue);
		Army[] armys = this.x_Armies[atkQueue.getX()];
		short startY = this.getYStart(atkQueue.getX());
		for(short i=0;i<atkQueue.getArmyIds().length;i++){
			Army army = this.armyMap.get(atkQueue.getArmyIds()[i]);
			army.setY((short)(startY+i));
			armys[startY+i].setY((short)(startY+atkQueue.getArmyIds().length+i));
			atkBuilder.addArmyId(army.getInstanceId());
			atkBuilder.addTargetY(army.getY());
		}
		for(Army army : armys){
			army.setQueueId(atkQueue.getInstanceId());
			cacheXyArmy(army);
		}
		stepBuilder.setType(2);
		stepBuilder.setAtkqueue(atkBuilder.build());
		return stepBuilder.build();
	}
	

	/**
	 * 检查防御队列
	 */
	public BattleStepMessage checkDefQueue() {
		// 防御队列检测
		for (Army[] armys : y_Armies) {
			int color = -1;
			int sameCount = 1;
			for (int i = 0; i < armys.length; i++) {
				Army army = armys[i];
				if (army == null || army.getQueueId() != -1 || army.getTemplate().level != 0 || army.isWall()) {
					color = -1;
					sameCount = 0;
					continue;
				}
				if (army.getColor() == color) {
					sameCount++;
				} else {
					if (sameCount >= 3) {
						BattleStepMessage.Builder stepBuilder = BattleStepMessage.newBuilder();
						WallQueueMessage.Builder wallBuilder = WallQueueMessage.newBuilder();
						for (int j = 1; j <= sameCount; j++) {
							Army wallAramy = armys[i - j];
							wallAramy.setWall(true);
							wallAramy.setColor(-1);
							this.moveWall(wallAramy);
							wallBuilder.addArmyId(wallAramy.getInstanceId());
							wallBuilder.addTargetX(wallAramy.getX());
							wallBuilder.addTargetY(wallAramy.getY());
						}
						stepBuilder.setType(3);
						stepBuilder.setWallQueue(wallBuilder.build());
						return stepBuilder.build();
					}
					sameCount = 1;
					color = army.getColor();
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 移动一个防御城墙
	 * @param army
	 */
	private void moveWall(Army army){
		Army[] armys = x_Armies[army.getX()];
		for(int i=army.getY()-1;i>=0;i--){//向上冒泡
			Army army2 = armys[i];
			if (army2.isWall()) {
				short tempY = army.getY();
				army.setY(army2.getY());
				army2.setY(tempY);
				break;
			}
		}
	}
	
	/**
	 * 检查是否有城墙合成
	 */
	public BattleStepMessage checkWallMerge(){
		BattleStepMessage.Builder stepBuilder = BattleStepMessage.newBuilder();
		WallMergeMessage.Builder mergeBuilder = WallMergeMessage.newBuilder();
		List<Army> armys = new ArrayList<>();
		for(Army army : this.armyMap.values()){
			if (army.isWall() && army.getWallLevel() == 0 && army.getY()<5) {
				Army next = x_Armies[army.getX()][army.getY()+1];
				if (next!=null && next.isWall() && next.getWallLevel() == 0 ) {
					army.setWallLevel(1);
					armys.add(next);
					mergeBuilder.addFromId(next.getInstanceId());
					mergeBuilder.addToId(army.getInstanceId());
				}
			}
		}
		if (armys.size() > 0) {
			for(Army army : armys){
				this.armyMap.remove(army);
				int endy = 0;//当列最后一个士兵的y坐标
				for(int i=army.getY()+1;i<=5;i++){
					Army moveArmy = x_Armies[army.getX()][i];
					if (moveArmy == null) {
						endy = i-1;
						break;
					}
					moveArmy.setY((short)(moveArmy.getY()-1));
					cacheXyArmy(moveArmy);
				}
				this.removeCacheXy(army.getX(), endy);
			}
			stepBuilder.setType(4);
			stepBuilder.setWallMerge(mergeBuilder.build());
			return stepBuilder.build();
		}
		return null;
	}
	
	public Army moveArmy(int fromx,int tox)throws BattleException{
		
		Army[] armys = x_Armies[fromx];
		Army army = armys[armys.length-1];
		int toY = 0;
		int xlenth = getXArmyLength(tox);
		if (xlenth >= armys.length) {
			throw new BattleException("目标队列已满");
		}
		toY = xlenth-1;
		//将士兵添加到新的位置
		x_Armies[tox][toY] = army;
		y_Armies[toY][tox ] = army;
		//原来的位置干掉
		x_Armies[fromx][armys.length-1] = null;
		y_Armies[army.getY()][army.getX()] = null;
		army.setX((short)tox);
		army.setY((short)toY);
		return army;
	}
	
	//某一列当前有多少个士兵
	private int getXArmyLength(int x){
		Army[] armys = x_Armies[x];
		for(int i=armys.length-1;i>=0;i--){
			if (armys[i] != null) {
				return i+1;
			}
		}
		return armys.length;
	}
	
	/**
	 * 当普通攻击队形向上移动的时候，检测该移动到哪个位置
	 * @return
	 */
	private short getYStart(int x){
		Army[] armys = x_Armies[x];
		for(short i=0;i<6;i++){
			if (armys[i].isWall()) {
				continue;
			}
			if (armys[i]==null || armys[i].getQueueId()==0) {
				return i;
			}
		}
		return 5;
	}
}
