package world.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 占领模式 玩家关卡分数信息
 * @author admin
 *
 */
public class SectionEntity {
	
	private int userId;
	
	/**
	 * key:sectionId
	 * value:[0]last score [1]maxscore
	 */
	private Map<Integer, int[]> scoreMap = new HashMap<>();
	
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Map<Integer, int[]> getScoreMap() {
		return scoreMap;
	}

	public void setScoreMap(Map<Integer, int[]> scoreMap) {
		this.scoreMap = scoreMap;
	}

	
	
}
