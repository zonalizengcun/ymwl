package world.entity;

//占领模式 所有关卡信息
public class OccupyEntity {
	
	private int sectionId;
	
	private int playerId;
	
	private int award;
	
	private int score;
	
	public OccupyEntity(){
		this.playerId = -1;
		this.award = 20;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getAward() {
		return award;
	}

	public void setAward(int award) {
		this.award = award;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
}

