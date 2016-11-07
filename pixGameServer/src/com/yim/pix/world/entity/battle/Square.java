package com.yim.pix.world.entity.battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.criteria.CriteriaBuilder.Case;

import org.hibernate.sql.Template;

import com.yim.pix.world.World;
import com.yim.pix.world.entity.Player;
import com.yim.pix.world.entity.Racist;
import com.yim.pix.world.model.army.ArmyModel;
import com.yim.pix.world.model.army.ArmyTemplate;
import com.yim.util.ProbabilityUtil;

/**
 * 战斗中的阵营
 * 
 * @author lizengcun
 *
 */
public class Square {

	private Map<Integer, Army> armyMap;
	
	private Player player;

	public Square(Player player) {
		this.armyMap = new HashMap<>();
		this.player = player;
	}
	
	public Map<Integer, Army> getArmyMap(){
		return this.armyMap;
	}
	
	public void init(){
		Racist racist = player.getRacist();
		Random random = new Random();
		List<Army> armys = new ArrayList<>();
		//先刷27个低级兵
		for(int i=0;i<27;i++){
			int position = random.nextInt(3);
			int armyTempId = racist.getPosition(position);
			Army army = new Army();
			army.setTemplateId(armyTempId);
			army.setColor(position);
			armys.add(army);
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
			army.setTemplateId(armyTempId);
			army.setColor(position);
			armys.add(army);
		}
		//设置兵的位置
		this.initXY(armys);
	}
	/**
	 * 给士兵设置xy坐标
	 */
	private void initXY(List<Army> armys){
		for(int x=0;x<8;x++){
			for(int y=0;y<6;y++){
				
			}
		}
	}
	
	private boolean checkColorXY(int color,int x,int y){
		for(int y1=0){
			
		}
	}
	
}
