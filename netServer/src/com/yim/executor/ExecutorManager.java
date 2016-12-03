package com.yim.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * 线程池管理
 * @author lizengcun
 *
 */
public class ExecutorManager {
	
	private Executor netExecutor;
	
	private Executor authLogicExecutor;
	
	protected ScheduledExecutorService normalScheduled;
	
	private static final int WORKERTHREADNUMBER = Runtime.getRuntime().availableProcessors();
	
	public ExecutorManager(){
		this.netExecutor = new ThreadPoolExecutor(WORKERTHREADNUMBER, WORKERTHREADNUMBER*2, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(512),new NamedThreadFactory("logic"));
		this.authLogicExecutor =  new ThreadPoolExecutor(WORKERTHREADNUMBER, WORKERTHREADNUMBER, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(512),new NamedThreadFactory("authlogic"));
		this.normalScheduled  = Executors.newScheduledThreadPool(1,new NamedThreadFactory("normal"));
	}
	
	/**
	 * 逻辑处理线程池
	 * @return
	 */
	public Executor getNetExecutor(){
		return this.netExecutor;
	}
	
	public Executor getAuthLogicExecutor(){
		return this.authLogicExecutor;
	}
	public ScheduledExecutorService getNormalScheduled(){
		return this.normalScheduled;
	}
	
	private static ExecutorManager manager = new ExecutorManager();
	
	public static ExecutorManager getDefault(){
		return manager;
	}
	
}
