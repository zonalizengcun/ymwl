package net.session.clients;

import net.manager.ClientManager;
import net.session.AbstractClient;
import net.session.AbstractSession;
import net.session.Packet;

public class TokenSession extends AbstractSession{

	private Packet packet;
	
	private String token;
	
	public TokenSession(Packet packet,String token) {
		super();
		this.packet = packet;
		this.token = token;
	}

	@Override
	public Packet getPacket() {
		return this.packet;
	}

	@Override
	public AbstractClient getClient() {
		return ClientManager.getDefault().getClient(this.token);
	}
	
}
