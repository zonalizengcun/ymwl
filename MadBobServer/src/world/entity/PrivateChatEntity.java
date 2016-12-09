package world.entity;

import java.util.LinkedList;
import java.util.List;

import world.GameNumber;

public class PrivateChatEntity {
	
	//上次读取时间
	private int readTime;
	
	private LinkedList<ChatEntity> chats = new LinkedList<ChatEntity>();
	
	public void addChats(ChatEntity chatEntity){
		chats.add(chatEntity);
	}
	
	public List<ChatEntity> getChats(){
		return chats;
	}
	
	public void addChat(ChatEntity chatEntity){
		if (chats.size() >= GameNumber.privateChatLimit) {
			chats.removeLast();
		}
		chats.push(chatEntity);
	}
	
	public void setChats(LinkedList<ChatEntity> chatList){
		this.chats = chatList;
	}

	public int getReadTime() {
		return readTime;
	}

	public void setReadTime(int readTime) {
		this.readTime = readTime;
	}
	
	
}
