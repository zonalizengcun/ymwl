package world.entity;

public class PlatEntity {
	private String platName;//平台名称
	
	private String platToken;//平台token
	
	private String token;//游戏中token
	
	public PlatEntity(){}

	public String getPlatName() {
		return platName;
	}

	public void setPlatName(String platName) {
		this.platName = platName;
	}

	public String getPlatToken() {
		return platToken;
	}

	public void setPlatToken(String platToken) {
		this.platToken = platToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getKey(){
		return this.platName + "###" + this.platToken;
	}
	
	
}
