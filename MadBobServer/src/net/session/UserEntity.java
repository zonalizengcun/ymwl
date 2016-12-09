package net.session;

public class UserEntity {

	private int id;
	
	private String accountName;

	private String passWord;
	
	private String token;
	
	private String name;
	
	private int headId;
	
	private int money;
	//pve最高关卡
	private int maxPveSection;
	
	//世界聊天次数
	private int chatCnt;
	
	//上次世界聊天时间
	private int chatTime;
	
	//游戏总排行榜分数
	private int gameScore;
	
	public UserEntity(){}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHeadId() {
		return headId;
	}

	public void setHeadId(int headId) {
		this.headId = headId;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getMaxPveSection() {
		return maxPveSection;
	}

	public void setMaxPveSection(int maxPveSection) {
		this.maxPveSection = maxPveSection;
	}

	public int getChatCnt() {
		return chatCnt;
	}

	public void setChatCnt(int chatCnt) {
		this.chatCnt = chatCnt;
	}

	public int getChatTime() {
		return chatTime;
	}

	public void setChatTime(int chatTime) {
		this.chatTime = chatTime;
	}

	public int getGameScore() {
		return gameScore;
	}

	public void setGameScore(int gameScore) {
		this.gameScore = gameScore;
	}

	
}
