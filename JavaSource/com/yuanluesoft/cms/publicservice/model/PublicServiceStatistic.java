package com.yuanluesoft.cms.publicservice.model;

/**
 * 公众服务统计
 * @author linchuan
 *
 */
public class PublicServiceStatistic {
	private int total; //累计受理
	private int completeTotal; //累计办结
	private int monthTotal; //本月累计受理
	private int monthCompleteTotal; //本月累计办结
	private int yesterdayTotal; //昨日累计受理
	private int yesterdayCompleteTotal; //昨日累计办结
	private int todayTotal; //今日累计受理
	private int todayCompleteTotal; //今日累计办结
	
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
	 * @return the monthTotal
	 */
	public int getMonthTotal() {
		return monthTotal;
	}
	/**
	 * @param monthTotal the monthTotal to set
	 */
	public void setMonthTotal(int monthTotal) {
		this.monthTotal = monthTotal;
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
	 * @return the todayTotal
	 */
	public int getTodayTotal() {
		return todayTotal;
	}
	/**
	 * @param todayTotal the todayTotal to set
	 */
	public void setTodayTotal(int todayTotal) {
		this.todayTotal = todayTotal;
	}
	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
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
	/**
	 * @return the yesterdayTotal
	 */
	public int getYesterdayTotal() {
		return yesterdayTotal;
	}
	/**
	 * @param yesterdayTotal the yesterdayTotal to set
	 */
	public void setYesterdayTotal(int yesterdayTotal) {
		this.yesterdayTotal = yesterdayTotal;
	}
}