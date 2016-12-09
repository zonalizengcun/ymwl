package com.yim.pix.world.entity.battle;

import com.yim.pix.world.World;
import com.yim.pix.world.model.army.ArmyTemplate;

/**
 * һ��ʿ��
 * @author admin
 *
 */
public class Army {

	//ʵ��id
	private int instanceId;
	//ģ��id
	private int templateId;
	
	private short x;
	
	private short y;
	//�������ģ��
	private ArmyTemplate armyTemplate;
	//颜色，对应阵型坐标012
	private int color;
	
	
	/**
	 * 对应的攻击队列id
	 */
	private int queueId;
	
	/**
	 * 是否是城墙
	 */
	private boolean isWall;
	
	//城墙等级
	private int wallLevel;
	
	public Army() {
	}
	

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
		this.armyTemplate = World.getInstance().getArmyModel().getArmyTemplate(templateId);
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	
	public ArmyTemplate getTemplate(){
		if (armyTemplate == null) {
			armyTemplate = World.getInstance().getArmyModel().getArmyTemplate(templateId);
		}
		return armyTemplate;
	}
	

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public short getX() {
		return x;
	}

	public void setX(short x) {
		this.x = x;
	}

	public short getY() {
		return y;
	}

	public void setY(short y) {
		this.y = y;
	}


	public int getQueueId() {
		return queueId;
	}


	public void setQueueId(int queueId) {
		this.queueId = queueId;
	}


	public boolean isWall() {
		return isWall;
	}


	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}


	public int getWallLevel() {
		return wallLevel;
	}


	public void setWallLevel(int wallLevel) {
		this.wallLevel = wallLevel;
	}

}
