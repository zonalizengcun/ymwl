package com.yim.pix.world.model.army;

import com.yim.pix.world.model.RacistType;

/**
 * 兵种模板
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
	 * 占格数
	 */
	public int space;
	
	/**
	 * 兵种等级
	 */
	public int level;
	
	/**
	 * 特殊技能
	 */
	public int skill;
	
	/**
	 * 兵种图标
	 */
	public String icon;
	
	/**
	 * 描述
	 */
	public String describe;
	
	
	/**
	 * 产出上限
	 */
	public int productLimit;
}
