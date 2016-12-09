package net.session;

import java.util.concurrent.ArrayBlockingQueue;

import world.World;

public abstract class AbstractClient {

	private volatile boolean isProcessing;
	
	private String token;

	private ArrayBlockingQueue<Packet> queue = new ArrayBlockingQueue<Packet>(512);
	
	public AbstractClient(String token){
		this.token = token;
	}

	public boolean isProcessing() {
		return isProcessing;
	}

	public void setProcessing(boolean isProcessing) {
		this.isProcessing = isProcessing;
	}

	public void putPacket(Packet packet) {
		try {
			this.queue.put(packet);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void process() {
		this.setProcessing(true);
		Packet packet;
		while (queue.size() > 0) {
			packet = queue.poll();
			if (packet != null) {
				try {
					World.getDefault().getHandlerManager().handle(packet);
				} catch (Exception e) {
					e.printStackTrace();
					this.setProcessing(false);
				}
			}
		}
		this.setProcessing(false);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
