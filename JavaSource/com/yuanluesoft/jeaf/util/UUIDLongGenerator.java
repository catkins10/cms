/*
 * Created on 2006-3-9
 *
 */
package com.yuanluesoft.jeaf.util;

/**
 *
 * @author LinChuan
 * 每10毫秒内10000个以内的ID不重复,200年内不重复,长度18位
 *
 */
public class UUIDLongGenerator {
	private static long baseTime = 109409580000L; //基准时间(2005年7月)
	private static long systemTime = (long)(System.currentTimeMillis()/1000)-baseTime; //当前时间
	private static int count = 0;
	private static long ip = ((long)Math.round(Math.pow(10, 16))) * (Integer.parseInt(NetworkUtils.getLocalHostIP(null).substring(NetworkUtils.getLocalHostIP(null).lastIndexOf('.') + 1)) % 100); 
	
	/**
	 * 生成ID
	 */
	public static long generateId() {
		synchronized(UUIDLongGenerator.class) {
			long time = (long)(System.currentTimeMillis()/10) - baseTime;
			if(time!=systemTime) {
				count = 0;
				systemTime = time;
			}
			return ip + systemTime * 10000 + (count++);
		}
	}
}
