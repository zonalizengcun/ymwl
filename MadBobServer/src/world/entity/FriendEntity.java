package world.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 好友列表
 * @author admin
 *
 */
public class FriendEntity {
	
	private int roleId;

	private Set<Integer> friends;
	
	private Set<Integer> friendApplys;
	
	private Set<Integer> giftFriends;
	
	//当日送礼数量
	private int giftCnt;
	//上次送礼时间
	private int giftTime;
	
	public FriendEntity(){
		this.setFriends(new HashSet<Integer>());
		this.friendApplys = new HashSet<>();
		this.giftFriends = new HashSet<>();
	}

	public Set<Integer> getFriends() {
		return friends;
	}

	public void setFriends(Set<Integer> friends) {
		this.friends = friends;
	}

	public Set<Integer> getFriendApplys() {
		return friendApplys;
	}

	public void setFriendApplys(Set<Integer> friendApplys) {
		this.friendApplys = friendApplys;
	}
	
	public void addFriendApply(int roleId){
		this.friendApplys.add(roleId);
	}
	
	public void addFriend(int roleId){
		this.friends.add(roleId);
	}

	public Set<Integer> getGiftFriends() {
		return giftFriends;
	}

	public void setGiftFriends(Set<Integer> giftFriends) {
		this.giftFriends = giftFriends;
	}

	public int getGiftCnt() {
		return giftCnt;
	}

	public void setGiftCnt(int giftCnt) {
		this.giftCnt = giftCnt;
	}

	public int getGiftTime() {
		return giftTime;
	}

	public void setGiftTime(int giftTime) {
		this.giftTime = giftTime;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	
	
}
