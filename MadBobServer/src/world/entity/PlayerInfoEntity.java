package world.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家附加信息
 * @author admin
 *
 */
public class PlayerInfoEntity {
	
	private int roleId;
	
	private List<Boolean> cinemas = new ArrayList<>();//剧情
	
	private List<Integer> props = new ArrayList<>();//道具数据
	
	private List<Integer> propPieces = new ArrayList<>();//道具碎片数据
	
	private List<Integer> storyPieces = new ArrayList<>();//故事碎片
	
	private List<HeroEntity> heros = new ArrayList<>();//英雄信息
	
	

	public List<Boolean> getCinemas() {
		return cinemas;
	}

	public void setCinemas(List<Boolean> cinemas) {
		this.cinemas = cinemas;
	}

	public List<Integer> getProps() {
		return props;
	}

	public void setProps(List<Integer> props) {
		this.props = props;
	}

	public List<Integer> getPropPieces() {
		return propPieces;
	}

	public void setPropPieces(List<Integer> propPieces) {
		this.propPieces = propPieces;
	}

	public List<Integer> getStoryPieces() {
		return storyPieces;
	}

	public void setStoryPieces(List<Integer> storyPieces) {
		this.storyPieces = storyPieces;
	}

	public List<HeroEntity> getHeros() {
		return heros;
	}

	public void setHeros(List<HeroEntity> heros) {
		this.heros = heros;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	
}
