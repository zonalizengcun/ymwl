package world.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import world.schedule.impl.RefreshAt24;
import world.service.Iservice;

/**
 *  计划行为Action
 *
 */
public class Scheduler implements Iservice{
	
	public List<Action> actionNews;
	
    public ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

	private Scheduler() {
		this.actionNews = new ArrayList<>();
	}

	private static Scheduler scheduler = new Scheduler();
	
	public static Scheduler getInstance() {
		return scheduler;
	}
	
	
	@Override
	public void start() {
		Action action = new Action();
		action.setImplClass(RefreshAt24.class.getName());
		Scheduler.getInstance().scheduleAtFixedTime(action, 24, 0, 10, 0);
	}


	@Override
	public void shutdown() {
		
	}

	
	/**
	 * 获得指定ID的Action
	 * @param id
	 * @return
	 */
	public Action getAction(int id) {
		synchronized (this) {
			if (actionNews != null && actionNews.size() > 0) {
				for (Action actionNew : actionNews.toArray(new Action[] {})) {
					if (actionNew.id == id) {
						return actionNew;
					}
				}
			}
			return null;
		}
	}
	
	public List<Action> getActionNews() {
		return actionNews;
	}
	
	/**
	 * 指定时间延迟后执行Action
	 * @param action
	 * @param delay
	 * @param unit
	 */
	public void schedule(Action action, long delay, TimeUnit unit) {
		synchronized (this) {
			if (action.state != Action.VIRGIN) {
				return;
			}
			action.beginTime = System.currentTimeMillis() + unit.toMillis(delay);//第一次开始运行的时间
			action.period = unit.toMillis(delay);
			action.wantToExecCnt = 1;
			action.state = Action.SCHEDULED;
			Future<?> future = scheduledExecutorService.schedule(action, delay, unit);
			action.future = future;
			actionNews.add(action);
		}
	}

	/**
	 * 固定周期执行指定次数Action
	 * @param action
	 * @param initialDelay
	 * @param period
	 * @param unit
	 * @param cnt
	 */
	public void scheduleAtFixedRate(Action action, long initialDelay, long period, TimeUnit unit, int cnt) {
		synchronized (this) {
			if (action.state != Action.VIRGIN) {
				return;
			}
			action.beginTime = System.currentTimeMillis() + unit.toMillis(initialDelay);
			action.period = unit.toMillis(period);
			action.wantToExecCnt = cnt;
			action.state = Action.SCHEDULED;
			Future<?> future = scheduledExecutorService.scheduleAtFixedRate(action, initialDelay, period, unit);
			action.future = future;
			actionNews.add(action);
		}
	}
	
	/**
	 * 指定时刻执行Action
	 * @param action
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 */
	public void scheduleAtTime(Action action, int hourOfDay, int minute, int second) {
		synchronized (this) {
			if (action.state != Action.VIRGIN) {
				return;
			}
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			calendar.set(Calendar.MILLISECOND, 0);
			long delay = 0;
			if (calendar.getTime().after(date)) {
				delay = calendar.getTimeInMillis() - date.getTime();
			}else{
				calendar.add(Calendar.DATE, 1);
				delay = calendar.getTimeInMillis() - date.getTime();
			}
			action.beginTime = calendar.getTimeInMillis();
			action.hourOfDay = hourOfDay;
			action.minute = minute;
			action.second = second;
			action.wantToExecCnt = 1;
			action.state = Action.SCHEDULED;
			Future<?> future = scheduledExecutorService.schedule(action, delay, TimeUnit.MILLISECONDS);
			action.future = future;
			
			actionNews.add(action);
		}
	}
	
	/**
	 * 固定时刻执行指定次数Action
	 * @param action
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 * @param cnt
	 */
	public void scheduleAtFixedTime(Action action, int hourOfDay, int minute, int second, int cnt) {
		synchronized (this) {
			if (action.state != Action.VIRGIN) {
				return;
			}
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			calendar.set(Calendar.MILLISECOND, 0);
			long delay = 0;
			if (calendar.getTime().after(date)) {
				delay = calendar.getTimeInMillis() - date.getTime();
			}else{
				calendar.add(Calendar.DATE, 1);
				delay = calendar.getTimeInMillis() - date.getTime();
			}
			action.beginTime = calendar.getTimeInMillis();
			action.hourOfDay = hourOfDay;
			action.minute = minute;
			action.second = second;
			action.wantToExecCnt = cnt;
			action.state = Action.SCHEDULED;
			Future<?> future = scheduledExecutorService.scheduleAtFixedRate(action, delay, 24L * 60L * 60L * 1000L, TimeUnit.MILLISECONDS);
			action.future = future;
			actionNews.add(action);
		}
	}
	
	/**
	 * 加速指定的Action
	 * @param id
	 * @param accTime
	 * @param unit
	 */
	public void accelerate(int id, long accTime, TimeUnit unit) {
		Action actionNew = getAction(id);
		if (actionNew != null && actionNew.wantToExecCnt == 1) {
			actionNew.stop();
			if (actionNew.state == Action.SCHEDULED) {
				long delay = actionNew.beginTime - (System.currentTimeMillis() + unit.toMillis(accTime));
				if (delay < 0) {
					delay = 0;
				}
				actionNew.stop = false;
				Future<?> future = scheduledExecutorService.schedule(actionNew, delay, TimeUnit.MILLISECONDS);
				actionNew.future = future;
			}
		}
	}







}
