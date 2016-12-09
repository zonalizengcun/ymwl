package world;

import java.io.IOException;

import jxl.read.biff.BiffException;
import net.handler.PacketHandlerManager;
import world.model.AwardModel;
import world.model.AwardModelBuilder;

public class World {

	private static World instance = new World();

	private Config config;

	private PacketHandlerManager handlerManager;
	
	private AwardModel awardModel;

	private World() {
		this.handlerManager = new PacketHandlerManager();
		GameNumber.load("gameNumber.properties");
	}

	public static World getDefault() {
		return instance;
	}
	
	public void initModel() throws BiffException, IOException{
		AwardModelBuilder awardModelBuilder = new AwardModelBuilder();
		awardModelBuilder.build();
		this.awardModel = awardModelBuilder.getAwardModel();
	}

	public PacketHandlerManager getHandlerManager() {
		return this.handlerManager;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
	public AwardModel getAwardModel(){
		return this.awardModel;
	}

}
