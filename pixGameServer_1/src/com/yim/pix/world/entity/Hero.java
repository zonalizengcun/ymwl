package com.yim.pix.world.entity;

import com.yim.io.Stream;

/**
 * гЂал
 * @author lizengcun
 *
 */
public class Hero {
	
	private int instanceId;
	private int templateId;
	
	public Hero() {
	}
	
	public Hero(int instanceId,int templateId){
		this.instanceId = instanceId;
		this.templateId = templateId;
	}
	
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	
	public void writeToStream(Stream stream){
		stream.writeShort(0);
		stream.writeInt(instanceId);
		stream.writeInt(templateId);
	}
	
	public static Hero readFromStream(Stream stream){
		int version = stream.readInt();
		Hero hero = new Hero();
		if (version>=0) {
			hero.instanceId = stream.readInt();
			hero.templateId = stream.readInt();
		}
		return hero;
	}
}
