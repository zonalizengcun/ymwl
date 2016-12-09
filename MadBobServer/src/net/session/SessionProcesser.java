package net.session;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

public class SessionProcesser implements Runnable{
	
	private Logger logger = Logger.getLogger(SessionProcesser.class);
	
	private Executor executor;
	
	private ArrayBlockingQueue<AbstractSession> sessions = new ArrayBlockingQueue<AbstractSession>(1024);
	
	public SessionProcesser(Executor executor){
		this.executor = executor;
		this.start();
	}
	
	public void insertSession(AbstractSession session){
		try {
			this.sessions.put(session);
		} catch (InterruptedException e) {
			logger.error("[SESSIONPUT]FAIL[线程中断]");
			e.printStackTrace();
		}
	}

	
	public void start(){
		Thread thread = new Thread(this);
		thread.start();
	}


	@Override
	public void run() {
		while (true) {
			try {
				AbstractSession session = this.sessions.take();
				session.packetProcess(executor,session.getPacket());
				//logger.info("packet execute");
			} catch (InterruptedException e) {
				logger.error("[SESSIONTAKE]FAIL[线程中断]");
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
}
