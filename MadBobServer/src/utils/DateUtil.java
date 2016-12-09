package utils;

import java.util.Calendar;

public class DateUtil {
	
	
	public static int getSecondStamp(){
		return (int)(System.currentTimeMillis()/1000L);
	}

	/**
	 * 判断两个时间是否是同一周
	 * @param time
	 * @param time1
	 * @return
	 */
	public static boolean isSameWeek(long time,long time1){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.setTimeInMillis(time1);
		int week1 = calendar.get(Calendar.WEEK_OF_YEAR);
		if (week1 == week && Math.abs(time - time1) < 2592000000L) {
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isSameMonth(long time,long time1){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int month = calendar.get(Calendar.MONTH);
		calendar.setTimeInMillis(time1);
		int month1 = calendar.get(Calendar.MONTH);
		if (month1 == month && Math.abs(time - time1) < 2592000000L*5) {
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断两个日期是否是同一天
	 * @param time
	 * @param time1
	 * @return
	 */
	public static boolean isSameDay(long time,long time1){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.setTimeInMillis(time1);
		int day1 = calendar.get(Calendar.DAY_OF_YEAR);
		if (day == day1 && Math.abs(time - time1) < 2592000000L) {
			return true;
		}
		return false;
	}
	
	public static int getNextTime(long time,int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int nowDay = calendar.get(Calendar.DAY_OF_WEEK);
		if (nowDay<day) {
			calendar.set(Calendar.DAY_OF_WEEK, day);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 10);
			return (int)((calendar.getTimeInMillis() - time)/1000L);
		}else{
			calendar.set(Calendar.DAY_OF_WEEK, day);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 10);
			return (int)((calendar.getTimeInMillis() - time)/1000L + 7*24*3600);
		}
		
	}
	
	public static void main(String[] args){
		System.out.println(getNextTime(System.currentTimeMillis(), 4));
	}
	
}
