package world.schedule;

import java.lang.reflect.Constructor;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 任务行为
 * @author yunlong.xu
 */
public class Action implements Runnable{
	
	int id;
	
	/**
	 * 该行为关联的Future对象，用于行为调度（例如：行为是否结束、是否取消、行为取消等操作）
	 */
	Future<?> future;
	 
	/**
	 * 实现类名
	 */
	String implClass;
	
	public String getImplClass() {
		return implClass;
	}

	public void setImplClass(String implClass) {
		this.implClass = implClass;
	}
	
	/**
	 * 实现类对象
	 */
	ActionImpl implInstance;
	
	/**
	 * 取得实现类。
	 */
	public ActionImpl getImpl() {
		if (implInstance == null) {
			try {
				Class<?> cls = Class.forName(implClass);
				Constructor<?> c = cls.getConstructor(Action.class);
				implInstance = (ActionImpl)c.newInstance(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return implInstance;
	}
	
	/**
	 * 行为状态
	 */
	int state = VIRGIN;
	static final int VIRGIN = 0;
	static final int SCHEDULED = 1;
	static final int CANCELLED = 2;
	static final int OVER = 3;
	    
	/**
     * 开始时间
     */
	long beginTime;
	
	/**
	 * 固定周期
	 */
	long period;
	
	/**
	 * 固定时刻
	 */
	int hourOfDay, minute, second;
	
	/**
	 * 希望被执行的次数
	 */
	int wantToExecCnt;
	
	/**
	 * 已经被执行的次数
	 */
	int hasExecCnt;
	
	/**
	 * 最近一次执行时间
	 */
	long lastExecTime;
	
	final Lock lock = new ReentrantLock();
	

	boolean stop;
	
	@Override
	public final void run() {
		try {
			lock.lock();
			if (stop) {
				return;
			}
			// 不限执行次数
			if (wantToExecCnt == 0) {
				try {
					lastExecTime = System.currentTimeMillis();
					getImpl().exec();
				} catch (Exception e) {
					e.printStackTrace();
					// 如果出错则停止继续执行
					try {
						getImpl().over();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					if (future != null && !future.isCancelled()) {
						future.cancel(true);
					}
					state = OVER;
				}
				return ;
			}
			// 限制执行次数
			if (wantToExecCnt > 0 && hasExecCnt < wantToExecCnt) {
				try {
					lastExecTime = System.currentTimeMillis();
					getImpl().exec();
				} catch (Exception e) {
					e.printStackTrace();
				}
				hasExecCnt++;
				if (hasExecCnt == wantToExecCnt) {
					try {
						getImpl().over();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (future != null && !future.isCancelled()) {
						future.cancel(true);
					}
					state = OVER;
				}
				return ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	
	//停止行为未改变其状态
	public void stop() {
		lock.lock();
		try {
			stop = true;
			if (future != null && !future.isCancelled()) {
				future.cancel(true);
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void stop2(){
		stop = true;
		if (future != null && !future.isCancelled()) {
			future.cancel(true);
		}
	}
	
	public boolean cancel() {
		stop2();
		if (state == SCHEDULED) {
			state = CANCELLED;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCanceled() {
		return state == CANCELLED ? true : false;
	}
}
