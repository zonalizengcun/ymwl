package world.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天内容
 * @author admin
 *
 */
public class ChatEntity {
	
	//消息id
	private int chatId;
	//发送者id
	private int roleId;
	//消息内容
	private String content;
	//钻石1000 经验2000
	private Map<Integer, Integer> attach = new HashMap<>(2);
	//邮件是否已经领取 如果是好友申请 标识好友申请是否已处理
	private boolean attachRecive = false;
	
	private int time;
	
	private int type;//0私聊	1系统奖励	2好友请求

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Map<Integer, Integer> getAttach() {
		return attach;
	}

	public void setAttach(Map<Integer, Integer> attach) {
		this.attach = attach;
	}

	public boolean isAttachRecive() {
		return attachRecive;
	}

	public void setAttachRecive(boolean attachRecive) {
		this.attachRecive = attachRecive;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getChatId() {
		return chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}
	
	
	
}
