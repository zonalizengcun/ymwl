package com.yim.pix.world.entity.battle;

import com.yim.pix.world.World;
import com.yim.pix.world.model.army.ArmyTemplate;

/**
 * 一个士兵
 * @author admin
 *
 */
public class Army {

	//实例id
	private int instanceId;
	//模板id
	private int templateId;
	
	private int x;
	
	private int y;
	//缓存兵种模板
	private ArmyTemplate armyTemplate;
	//士兵颜色，跟阵型位置有关 0 1 2
	private int color;
	
	public Army() {
	}
	

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
