package com.yuanluesoft.cms.onlineservice.interactive.accept.model;

/**
 * 办件统计
 * @author linchuan
 *
 */
public class AcceptStatistic {
	private int acceptTotal; //累计受理
	private int completeTotal; //累计办结
	private int monthAcceptTotal; //本月累计受理
	private int monthCompleteTotal; //本月累计办结
	private int yesterdayAcceptTotal; //昨日累计受理
	private int yesterdayCompleteTotal; //昨日累计办结
	private int todayAcceptTotal; //今日累计受理
	private int todayCompleteTotal; //今日累计办结
	
	/**
	 * @return the acceptTotal
	 */
	public int getAcceptTotal() {
		return acceptTotal;
	}
	/**
	 * @param acceptTotal the acceptTotal to set
	 */
	public void setAcceptTotal(int acceptTotal) {
		this.acceptTotal = acceptTotal;
	}
	/**
	 * @return the completeTotal
	 */
	public int getCompleteTotal() {
		return completeTotal;
	}
	/**
	 * @param completeTotal the completeTotal to set
	 */
	public void setCompleteTotal(int completeTotal) {
		this.completeTotal = completeTotal;
	}
	/**
	 * @return the monthAcceptTotal
	 */
	public int getMonthAcceptTotal() {
		return monthAcceptTotal;
	}
	/**
	 * @param monthAcceptTotal the monthAcceptTotal to set
	 */
	public void setMonthAcceptTotal(int monthAcceptTotal) {
		this.monthAcceptTotal = monthAcceptTotal;
	}
	/**
	 * @return the monthCompleteTotal
	 */
	public int getMonthCompleteTotal() {
		return monthCompleteTotal;
	}
	/**
	 * @param monthCompleteTotal the monthCompleteTotal to set
	 */
	public void setMonthCompleteTotal(int monthCompleteTotal) {
		this.monthCompleteTotal = monthCompleteTotal;
	}
	/**
	 * @return the todayAcceptTotal
	 */
	public int getTodayAcceptTotal() {
		return todayAcceptTotal;
	}
	/**
	 * @param todayAcceptTotal the todayAcceptTotal to set
	 */
	public void setTodayAcceptTotal(int todayAcceptTotal) {
		this.todayAcceptTotal = todayAcceptTotal;
	}
	/**
	 * @return the todayCompleteTotal
	 */
	public int getTodayCompleteTotal() {
		return todayCompleteTotal;
	}
	/**
	 * @param todayCompleteTotal the todayCompleteTotal to set
	 */
	public void setTodayCompleteTotal(int todayCompleteTotal) {
		this.todayCompleteTotal = todayCompleteTotal;
	}
	/**
	 * @return the yesterdayAcceptTotal
	 */
	public int getYesterdayAcceptTotal() {
		return yesterdayAcceptTotal;
	}
	/**
	 * @param yesterdayAcceptTotal the yesterdayAcceptTotal to set
	 */
	public void setYesterdayAcceptTotal(int yesterdayAcceptTotal) {
		this.yesterdayAcceptTotal = yesterdayAcceptTotal;
	}
	/**
	 * @return the yesterdayCompleteTotal
	 */
	public int getYesterdayCompleteTotal() {
		return yesterdayCompleteTotal;
	}
	/**
	 * @param yesterdayCompleteTotal the yesterdayCompleteTotal to set
	 */
	public void setYesterdayCompleteTotal(int yesterdayCompleteTotal) {
		this.yesterdayCompleteTotal = yesterdayCompleteTotal;
	}
	
}