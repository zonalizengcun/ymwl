package world;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {
	
	private Properties properties;
	
	private String path;
	
	public String jedisIp;
	
	public int jedisPort;
	
	public String jedisPass;

	public int gamePort;
	
	public Config(String path){
		this.properties = new Properties();
		this.path = path;
	}
	

	public void init()throws Exception{
		FileInputStream fileInputStream = new FileInputStream(path);
		this.properties.load(fileInputStream);
		fileInputStream.close();
		this.jedisIp = this.properties.getProperty("jedisIp");
		this.jedisPort = Integer.parseInt(this.properties.getProperty("jedisPort"));
		this.jedisPass = this.properties.getProperty("jedisPass");
		this.gamePort = Integer.parseInt(this.properties.getProperty("gamePort"));
	}
	
	
	
	
}
