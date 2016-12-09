package com.yim.pix.world.model.army;

import com.yim.pix.world.model.RacistType;

/**
 * ����ģ��
 * @author admin
 *
 */
public class ArmyTemplate {
	
	public int id;
	
	public RacistType racist;
	
	public int armyType;
	
	public String armyName;
	
	/**
	 * 攻击力
	 */
	public int atk;
	
	/**
	 * 防御力
	 */
	public int def;
	
	/**
	 * 回合数
	 */
	public int round;
	
	
	/**
	 * 等级
	 */
	public int level;
	
	/**
	 * 技能
	 */
	public int skill;
	
	/**
	 * 士兵图标
	 */
	public String icon;
	
	/**
	 * 描述
	 */
	public String describe;
	
	
	/**
	 * 上限
	 */
	public int productLimit;
	
	/**
	 * 队列基础攻击力
	 */
	public int queueAtk;
	/**
	 * 每回合增增长攻击力
	 */
	public int queueAddAtk;
}
