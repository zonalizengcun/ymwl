package com.yim.pix.world.entity.battle;

import com.yim.pix.world.World;
import com.yim.pix.world.model.army.ArmyTemplate;

import antlr.TokenStreamHiddenTokenFilter;

public class AtkQueue {

	/**
	 * 队列实例id
	 */
	private int instanceId;
	
	/**
	 * 攻击队列中所有士兵实例id
	 */
	private int[] armyIds;
	
	/**
	 * 兵种模板id
	 */
	private int templateId;
	
	/**
	 * 当前攻击力
	 */
	private int atk;
	
	/**
	 * 剩余回合数
	 */
	private int round;
	
	private ArmyTemplate armyTemplate;
	
	private short x;
	
	
	
	public AtkQueue(int instanceId,int templateId,short x) {
		this.instanceId = instanceId;
		this.armyTemplate = World.getInstance().getArmyModel().getArmyTemplate(templateId);
		this.atk = armyTemplate.queueAtk;
		this.round = armyTemplate.round;
		this.setX(x);
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public int[] getArmyIds() {
		return armyIds;
	}

	public void setArmyIds(int[] armyIds) {
		this.armyIds = armyIds;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public ArmyTemplate getArmyTemplate() {
		if (armyTemplate == null) {
			armyTemplate = World.getInstance().getArmyModel().getArmyTemplate(templateId);
		}
		return armyTemplate;
	}

	public void setArmyTemplate(ArmyTemplate armyTemplate) {
		this.armyTemplate = armyTemplate;
	}

	public short getX() {
		return x;
	}

	public void setX(short x) {
		this.x = x;
	}
	
	
	
}
