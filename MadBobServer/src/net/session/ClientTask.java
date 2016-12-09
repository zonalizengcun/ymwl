package net.session;

public class ClientTask implements Runnable{
	
	private AbstractClient client;
	
	public ClientTask(AbstractClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		client.process();	
	}
	
}
