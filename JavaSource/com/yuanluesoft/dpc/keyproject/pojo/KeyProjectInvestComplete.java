package com.yuanluesoft.dpc.keyproject.pojo;

/**
 * 项目投资完成情况(keyproject_invest_complete)
 * @author linchuan
 *
 */
public class KeyProjectInvestComplete extends KeyProjectComponent {
	private int completeYear; //年份
	private int completeMonth; //月份
	private char completeTenDay = '1'; //旬,上旬/1、中旬/2、下旬/3
	private double investPlan; //计划完成投资（万元）
	private char completed = '0'; //是否已提交完成情况
	private double completeInvest; //完成投资（万元）
	private double yearInvest; //年初至报告期累计完成投资（万元）
	private double percentage; //占年计划（%）
	private double totalComplete; //开工至报告期累计完成投资（万元）
	private double completePercentage; //占总投资（%）
	
	/**
	 * 获取月份
	 * @return
	 */
	public String getCompleteMonthTitle() {
		return completeMonth<=0 ? null : completeMonth + "月" + (completeTenDay>'0' ? new String[]{"上旬", "中旬", "下旬"}[completeTenDay-'1'] : "");
	}
	
	/**
	 * @return the completeInvest
	 */
	public double getCompleteInvest() {
		return completeInvest;
	}
	/**
	 * @param completeInvest the completeInvest to set
	 */
	public void setCompleteInvest(double completeInvest) {
		this.completeInvest = completeInvest;
	}
	/**
	 * @return the completeMonth
	 */
	public int getCompleteMonth() {
		return completeMonth;
	}
	/**
	 * @param completeMonth the completeMonth to set
	 */
	public void setCompleteMonth(int completeMonth) {
		this.completeMonth = completeMonth;
	}
	/**
	 * @return the completePercentage
	 */
	public double getCompletePercentage() {
		return completePercentage;
	}
	/**
	 * @param completePercentage the completePercentage to set
	 */
	public void setCompletePercentage(double completePercentage) {
		this.completePercentage = completePercentage;
	}
	/**
	 * @return the completeYear
	 */
	public int getCompleteYear() {
		return completeYear;
	}
	/**
	 * @param completeYear the completeYear to set
	 */
	public void setCompleteYear(int completeYear) {
		this.completeYear = completeYear;
	}
	/**
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	/**
	 * @return the totalComplete
	 */
	public double getTotalComplete() {
		return totalComplete;
	}
	/**
	 * @param totalComplete the totalComplete to set
	 */
	public void setTotalComplete(double totalComplete) {
		this.totalComplete = totalComplete;
	}
	/**
	 * @return the yearInvest
	 */
	public double getYearInvest() {
		return yearInvest;
	}
	/**
	 * @param yearInvest the yearInvest to set
	 */
	public void setYearInvest(double yearInvest) {
		this.yearInvest = yearInvest;
	}
	/**
	 * @return the completeTenDay
	 */
	public char getCompleteTenDay() {
		return completeTenDay;
	}
	/**
	 * @param completeTenDay the completeTenDay to set
	 */
	public void setCompleteTenDay(char completeTenDay) {
		this.completeTenDay = completeTenDay;
	}

	/**
	 * @return the completed
	 */
	public char getCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(char completed) {
		this.completed = completed;
	}

	/**
	 * @return the investPlan
	 */
	public double getInvestPlan() {
		return investPlan;
	}

	/**
	 * @param investPlan the investPlan to set
	 */
	public void setInvestPlan(double investPlan) {
		this.investPlan = investPlan;
	}
}