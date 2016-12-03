package com.yim.pix.world.model.army;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.yim.pix.world.model.RacistType;

public class ArmyModel {

	private Map<RacistType, LinkedHashMap<Integer, ArmyTemplate>> armyTempsMap = new HashMap<>();
	
	private Map<Integer, ArmyTemplate> armyTemps = new HashMap<>();
	
	public ArmyModel() {
	}
	
	public void addArmyTemp(ArmyTemplate armyTemplate){
		this.armyTemps.put(armyTemplate.id, armyTemplate);
		if (this.armyTempsMap.containsKey(armyTemplate.racist)) {
			LinkedHashMap<Integer, ArmyTemplate> templates = this.armyTempsMap.get(armyTemplate.racist);
			templates.put(armyTemplate.armyType, armyTemplate);
		}else{
			LinkedHashMap<Integer, ArmyTemplate> templates = new LinkedHashMap<>();
			templates.put(armyTemplate.armyType, armyTemplate);
			this.armyTempsMap.put(armyTemplate.racist, templates);
		}
	}
	
	public ArmyTemplate getArmyTemplate(int id){
		return this.armyTemps.get(id);
	}
	/**
	 * 获取兵种模板
	 * @param racist 种族
	 * @param armyType 兵种
	 * @return
	 */
	public ArmyTemplate getArmyTemplate(RacistType racist,int armyType){
		return this.armyTempsMap.get(racist).get(armyType);
	}
	
	/**
	 * 获取某个种族所有的兵种
	 * @param racist
	 * @return
	 */
	public Collection<ArmyTemplate> getArmyTemplates(RacistType racist){
		return this.armyTempsMap.get(racist).values();
	}
	
}
