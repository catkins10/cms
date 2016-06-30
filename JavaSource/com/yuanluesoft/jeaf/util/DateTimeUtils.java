/*
 * Created on 2006-3-20
 *
 */
package com.yuanluesoft.jeaf.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.mail.MailException;

/**
 * 
 * @author linchuan
 *
 */
public class DateTimeUtils {
	
	/**
	 * 时间转换为日历
	 * @param time
	 * @return
	 */
	public static Calendar timestampToCalendar(Timestamp time) {
		if(time==null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time.getTime());
		return calendar;
	}
	
	/**
	 * 日历转换为时间
	 * @param calendar
	 * @return
	 */
	public static Timestamp calendarToTimestamp(Calendar calendar) {
		return calendar==null ? null : new Timestamp(calendar.getTimeInMillis());
	}
	
	/**
	 * 日期转换为日历
	 * @param time
	 * @return
	 */
	public static Calendar dateToCalendar(Date date) {
		if(date==null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		return calendar;
	}
	
	/**
	 * 日历转换为日期
	 * @param calendar
	 * @return
	 */
	public static Date calendarToDate(Calendar calendar) {
		return calendar==null ? null : new Date(calendar.getTimeInMillis());
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 获取当前日期
	 * @return
	 */
	public static Date date() {
		Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
		return new Date(date.getTimeInMillis());
	}
	
	/**
	 * 生成日期
	 * @param millis
	 * @return
	 */
	public static Date date(long millis) {
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(millis);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
		return new Date(date.getTimeInMillis());
	}
	
	/**
	 * 生成日期
	 * @param time
	 * @return
	 */
	public static Date date(Timestamp time) {
		return date(time.getTime());
	}
	
	/**
	 * 获取本月第一天
	 * @return
	 */
	public static Date getMonthBegin() {
		return set(date(), Calendar.DAY_OF_MONTH, 1);
	}
	
	/**
	 * 获取本月最后一天
	 * @return
	 */
	public static Date getMonthEnd() {
		return add(add(set(date(), Calendar.DAY_OF_MONTH, 1), Calendar.MONTH, 1), Calendar.DAY_OF_MONTH, -1);
	}
	
	/**
	 * 获取上月第一天
	 * @return
	 */
	public static Date getLastMonthBegin() {
		return add(set(date(), Calendar.DAY_OF_MONTH, 1), Calendar.MONTH, -1);
	}

	/**
	 * 获取上月最后一天
	 * @return
	 */
	public static Date getLastMonthEnd() {
		return add(set(date(), Calendar.DAY_OF_MONTH, 1), Calendar.DAY_OF_MONTH, -1);
	}
	
	/**
	 * 获取下月第一天
	 * @return
	 */
	public static Date getNextMonthBegin() {
		return add(set(date(), Calendar.DAY_OF_MONTH, 1), Calendar.MONTH, 1);
	}

	/**
	 * 获取下月最后一天
	 * @return
	 */
	public static Date getNextMonthEnd() {
		return add(add(set(date(), Calendar.DAY_OF_MONTH, 1), Calendar.MONTH, 2), Calendar.DAY_OF_MONTH, -1);
	}

	/**
	 * 获取本季度第一天
	 * @return
	 */
	public static Date getQuarterBegin() {
		Date today = date();
		return set(set(today, Calendar.DAY_OF_MONTH, 1), Calendar.MONTH, getMonth(today)/3*3);
	}
	
	/**
	 * 获取本季度最后一天
	 * @return
	 */
	public static Date getQuarterEnd() {
		Date today = date();
		return add(set(set(today, Calendar.DAY_OF_MONTH, 1), Calendar.MONTH, getMonth(today)/3*3+3), Calendar.DAY_OF_MONTH, -1);
	}
	
	/**
	 * 获取上季度第一天
	 * @return
	 */
	public static Date getLastQuarterBegin() {
		return add(getQuarterBegin(), Calendar.MONTH, -3);
	}
	
	/**
	 * 获取上季度最后一天
	 * @return
	 */
	public static Date getLastQuarterEnd() {
		return add(getQuarterBegin(), Calendar.DAY_OF_MONTH, -1);
	}
	
	/**
	 * 获取下季度第一天
	 * @return
	 */
	public static Date getNextQuarterBegin() {
		return add(getQuarterBegin(), Calendar.MONTH, 3);
	}
	
	/**
	 * 获取下季度最后一天
	 * @return
	 */
	public static Date getNextQuarterEnd() {
		return add(add(getQuarterBegin(), Calendar.MONTH, 4), Calendar.DAY_OF_MONTH, -1);
	}
	
	/**
	 * 获取本年第一天
	 * @return
	 */
	public static Date getYearBegin() {
		return set(set(date(), Calendar.DAY_OF_MONTH, 1), Calendar.MONTH, 0);
	}
	
	/**
	 * 获取本年最后一天
	 * @return
	 */
	public static Date getYearEnd() {
		return add(add(set(set(date(), Calendar.DAY_OF_MONTH, 1), Calendar.MONTH, 0), Calendar.YEAR, 1), Calendar.DAY_OF_MONTH, -1);
	}
	
	/**
	 * 获取去年第一天
	 * @return
	 */
	public static Date getLastYearBegin() {
		return add(getYearBegin(), Calendar.YEAR, -1);
	}
	
	/**
	 * 获取去年最后一天
	 * @return
	 */
	public static Date getLastYearEnd() {
		return add(getYearBegin(), Calendar.DAY_OF_MONTH, -1);
	}
	
	/**
	 * 获取明年第一天
	 * @return
	 */
	public static Date getNextYearBegin() {
		return add(getYearBegin(), Calendar.YEAR, 1);
	}
	
	/**
	 * 获取明年最后一天
	 * @return
	 */
	public static Date getNextYearEnd() {
		return add(add(getYearBegin(), Calendar.YEAR, 2), Calendar.DAY_OF_MONTH, -1);
	}
	
	/**
	 * 获取年
	 * @param date
	 * @return
	 */
	public static int getYear(java.util.Date date) {
		return getValue(date, Calendar.YEAR);
	}
	
	/**
	 * 获取月,一月返回0
	 * @param date
	 * @return
	 */
	public static int getMonth(java.util.Date date) {
		return getValue(date, Calendar.MONTH);
	}
	
	/**
	 * 获取日
	 * @param date
	 * @return
	 */
	public static int getDay(java.util.Date date) {
		return getValue(date, Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取星期
	 * @param date
	 * @return
	 */
	public static int getWeek(java.util.Date date) {
		return getValue(date, Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 获取日期在所在年度的周数
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(java.util.Date date) {
		return getValue(date, Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * 获取小时
	 * @param date
	 * @return
	 */
	public static int getHour(java.util.Date date) {
		return getValue(date, Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 获取分钟
	 * @param date
	 * @return
	 */
	public static int getMinute(java.util.Date date) {
		return getValue(date, Calendar.MINUTE);
	}
	
	/**
	 * 获取分钟
	 * @param date
	 * @return
	 */
	public static int getSecond(java.util.Date date) {
		return getValue(date, Calendar.SECOND);
	}
	
	/**
	 * 
	 * @param date
	 * @param field
	 * @return
	 */
	private static int getValue(java.util.Date date, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		return calendar.get(field);
	}
	
	/**
	 * 解析时间
	 * @param timeStr
	 * @param format 空时为yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws BaseParseException
	 */
	public static Timestamp parseTimestamp(String timeStr, String format) throws ParseException {
		if(format==null) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		else {
			if(format.indexOf('d')==-1) { //没有指日
				format = "dd " + format;
				timeStr = getDay(now()) + " " + timeStr;
			}
			if(format.indexOf('M')==-1) { //没有指定月
				format = "MM-" + format;
				timeStr = (getMonth(now()) + 1) + "-" + timeStr;
			}
			if(format.indexOf('y')==-1) { //没有指定年
				format = "yyyy-" + format;
				timeStr = getYear(now()) + "-" + timeStr;
			}
		}
		return new Timestamp(new SimpleDateFormat(format).parse(timeStr).getTime());
	}
	
	/**
	 * 解析日期
	 * @param dateStr
	 * @param format 空时为yyyy-M-d
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr, String format) throws ParseException {
		if(format==null) {
			format = "yyyy-M-d";
		}
		else {
			if(format.indexOf('M')==-1) { //没有指定月
				format = "MM-" + format;
				dateStr = (getMonth(now()) + 1) + "-" + dateStr;
			}
			if(format.indexOf('y')==-1) { //没有指定年
				format = "yyyy-" + format;
				dateStr = getYear(now()) + "-" + dateStr;
			}
		}
		return new Date(new SimpleDateFormat(format).parse(dateStr).getTime());
	}
	
	/**
	 * 解析COOKIE时间, 如: Mon, 31 Dec 1999 23:59:59 UTC
	 * @param strDate
	 * @return
	 * @throws MailException
	 */
	public static Timestamp parseCookieTimestamp(String timeStr) throws ParseException {
		return new Timestamp(getCookerFormatter().parse(timeStr).getTime());
	}
	
	/**
	 * 格式化COOKIE时间
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static String formatCookieTimestamp(Timestamp time) {
		return getCookerFormatter().format(time);
	}
	
	/**
	 * 获取COOKIE时间解析器
	 * @return
	 */
	private static SimpleDateFormat getCookerFormatter() {
		//Mon, 31 Dec 1999 23:59:59 UTC
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formatter;
	}
	
	/**
	 * 给日期增加值
	 * @param date
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Date add(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		calendar.add(field, amount);
		return new Date(calendar.getTimeInMillis());
	}
	
	/**
	 * 给时间增加值
	 * @param timestamp
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Timestamp add(Timestamp timestamp, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp.getTime());
		calendar.add(field, amount);
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	/**
	 * 设置日期
	 * @param date
	 * @param field
	 * @param value
	 * @return
	 */
	public static Date set(Date date, int field, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		calendar.set(field, value);
		return new Date(calendar.getTimeInMillis());
	}
	
	/**
	 * 设置时间
	 * @param timestamp
	 * @param field
	 * @param value
	 * @return
	 */
	public static Timestamp set(Timestamp timestamp, int field, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp.getTime());
		calendar.set(field, value);
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	/**
	 * 格式化输出日期
	 * @param date
	 * @param format 空时为yyyy-MM-dd
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		if(date==null) {
			return null;
		}
		return formatLunar(date, new SimpleDateFormat(format==null || format.equals("") ? "yyyy-MM-dd" : format).format(date));
	}
	
	/**
	 * 格式化输出时间
	 * @param timestamp
	 * @param format 空时为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatTimestamp(Timestamp timestamp, String format) {
		if(timestamp==null) {
			return null;
		}
		return formatLunar(timestamp, new SimpleDateFormat(format==null || format.equals("") ? "yyyy-MM-dd HH:mm:ss" : format).format(timestamp));
	}
	
	/**
	 * 格式化农历
	 * @param date
	 * @param format
	 * @return
	 */
	private static String formatLunar(java.util.Date date, String format) {
		return format.replaceAll("<农历年>", Lunar.getLunarYear(date))
					 .replaceAll("<农历月>", Lunar.getLunarMonth(date))
					 .replaceAll("<农历日>", Lunar.getLunarDay(date))
					 .replaceAll("<生肖>", Lunar.getLunarAnimal(date));
	}
}