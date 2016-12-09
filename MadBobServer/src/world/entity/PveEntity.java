package world.entity;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class PveEntity {
	
	private int playerId;
	
	private Map<String,PveSectionEntity> sectionMap = new HashMap<String, PveSectionEntity>();

	public Map<String,PveSectionEntity> getSectionMap() {
		return sectionMap;
	}

	public void setSectionMap(Map<String,PveSectionEntity> sectionMap) {
		this.sectionMap = sectionMap;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	public String toJsonStr(){
		return JSONObject.fromObject(sectionMap).element("playerId", this.playerId).toString();
	}
	
	public static PveEntity parseFromJson(String str){
		PveEntity pveEntity = new PveEntity();
		JSONObject jsonObject = JSONObject.fromObject(str);
		pveEntity.setPlayerId(jsonObject.getInt("playerId"));
		jsonObject.remove("playerId");
		Map<String, PveSectionEntity> map = new HashMap<>();
		for(Object object : jsonObject.values()){
			JSONObject json = (JSONObject)object;
			PveSectionEntity sectionEntity = (PveSectionEntity)JSONObject.toBean(json, PveSectionEntity.class);
			map.put(sectionEntity.getSectionId()+"", sectionEntity);
		}
		pveEntity.setSectionMap(map);
		return pveEntity;
	}
	
	
}

