package world.entity;

import java.util.ArrayList;
import java.util.List;

public class HeroEntity {
	
	private int heroId;
	
	private boolean enable = false;
	
	private int level;
	
	private int exp;
	
	private List<Integer> pieces = new ArrayList<>();

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public List<Integer> getPieces() {
		return pieces;
	}

	public void setPieces(List<Integer> pieces) {
		this.pieces = pieces;
	}
	
	
}
