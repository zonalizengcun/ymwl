package net.session.clients;

import net.session.AbstractClient;
import net.session.AbstractSession;
import net.session.Packet;
import world.entity.PlayerClient;

public class RegistSession extends AbstractSession {

	private Packet packet;
	
	public RegistSession(Packet packet) {
		this.packet = packet;
	}
	
	@Override
	public Packet getPacket() {
		return this.packet;
	}

	@Override
	public AbstractClient getClient() {
		PlayerClient client = new PlayerClient("");
		return client;
	}
	
	
	
	

}
