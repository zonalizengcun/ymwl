package net.handler;

import net.session.Packet;

public interface PacketHandler {

	public void handle(Packet packet);
	
	public int[] getOpcodes();
}
