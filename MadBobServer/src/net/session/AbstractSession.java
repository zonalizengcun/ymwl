package net.session;

import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

public abstract class AbstractSession {
	
	private static final Logger logger = Logger.getLogger(AbstractSession.class);

	public AbstractSession() {

	}

	public abstract Packet getPacket();

	public abstract AbstractClient getClient();

	public void packetProcess(Executor executor,Packet packet) {
		AbstractClient client = getClient();
		if (client != null) {
			client.putPacket(packet);
			if (client.isProcessing()) {
				return;
			}else{
				executor.execute(new ClientTask(client));
			}			
		}else{
			packet.sendError("Account exception");
			logger.error("token is error");
		}
	}
}
