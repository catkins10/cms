package com.yuanluesoft.jeaf.stat.model;

/**
 * 
 * @author linchuan
 *
 */
public class Statistics {
	private int totalAccess;
	private int currentMonthAccess;
	private int previousMonthAccess;
	private int todayAccess;
	private int yesterdayAccess;
	private int averageAccess; //平均每天访问量
	private int infoPublicAccess; //信息公开访问统计
	
	/**
	 * @return the averageAccess
	 */
	public int getAverageAccess() {
		return averageAccess;
	}
	/**
	 * @param averageAccess the averageAccess to set
	 */
	public void setAverageAccess(int averageAccess) {
		this.averageAccess = averageAccess;
	}
	/**
	 * @return the currentMonthAccess
	 */
	public int getCurrentMonthAccess() {
		return currentMonthAccess;
	}
	/**
	 * @param currentMonthAccess the currentMonthAccess to set
	 */
	public void setCurrentMonthAccess(int currentMonthAccess) {
		this.currentMonthAccess = currentMonthAccess;
	}
	/**
	 * @return the previousMonthAccess
	 */
	public int getPreviousMonthAccess() {
		return previousMonthAccess;
	}
	/**
	 * @param previousMonthAccess the previousMonthAccess to set
	 */
	public void setPreviousMonthAccess(int previousMonthAccess) {
		this.previousMonthAccess = previousMonthAccess;
	}
	/**
	 * @return the todayAccess
	 */
	public int getTodayAccess() {
		return todayAccess;
	}
	/**
	 * @param todayAccess the todayAccess to set
	 */
	public void setTodayAccess(int todayAccess) {
		this.todayAccess = todayAccess;
	}
	/**
	 * @return the totalAccess
	 */
	public int getTotalAccess() {
		return totalAccess;
	}
	/**
	 * @param totalAccess the totalAccess to set
	 */
	public void setTotalAccess(int totalAccess) {
		this.totalAccess = totalAccess;
	}
	/**
	 * @return the yesterdayAccess
	 */
	public int getYesterdayAccess() {
		return yesterdayAccess;
	}
	/**
	 * @param yesterdayAccess the yesterdayAccess to set
	 */
	public void setYesterdayAccess(int yesterdayAccess) {
		this.yesterdayAccess = yesterdayAccess;
	}
	/**
	 * @return the infoPublicAccess
	 */
	public int getInfoPublicAccess() {
		return infoPublicAccess;
	}
	/**
	 * @param infoPublicAccess the infoPublicAccess to set
	 */
	public void setInfoPublicAccess(int infoPublicAccess) {
		this.infoPublicAccess = infoPublicAccess;
	}
}
