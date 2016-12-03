package com.yim.pix.world.entity.battle;

import java.util.HashMap;
import java.util.Map;

/**
 * 战斗中的阵营
 * 
 * @author lizengcun
 *
 */
public class Square {

	private Map<Integer, Army> armyMap;

	public Square() {
		this.armyMap = new HashMap<>();
	}
	
	public Map<Integer, Army> getArmyMap(){
		return this.armyMap;
	}
	
	public void init(){
		for(int i=0;i<32;i++){
			Army army = new Army();
			
			
		}
		for (short x = 0; x < 8; x++) {
			for (short y = 0; y < 6; y++) {
		
			}
		}
	}
	
	private Army creatArmy(){
		
	}

}
