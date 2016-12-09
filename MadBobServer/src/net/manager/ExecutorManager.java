package net.manager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * 线程池管理
 * @author lizengcun
 *
 */
public class ExecutorManager {
	
	private Executor netExecutor;
	
	public ExecutorManager(){
		this.netExecutor = new ThreadPoolExecutor(8, 16, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(512));
	}
	
	public Executor getNetExecutor(){
		return this.netExecutor;
	}
	
	private static ExecutorManager manager = new ExecutorManager();
	
	public static ExecutorManager getDefault(){
		return manager;
	}
}
