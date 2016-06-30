package com.yuanluesoft.dpc.keyproject.pojo;

/**
 * 形象进度(keyproject_progress)
 * @author linchuan
 *
 */
public class KeyProjectProgress extends KeyProjectComponent {
	private int progressYear; //年份
	private int progressMonth; //月份
	private char progressTenDay = '1'; //旬,上旬/1、中旬/2、下旬/3
	private String plan; //安排
	private char completed = '0'; //是否已汇报项目进度
	private String progress; //进度
	
	/**
	 * 获取月份
	 * @return
	 */
	public String getProgressMonthTitle() {
		return progressMonth<=0 ? null : progressMonth + "月" + (progressTenDay>'0' ? new String[]{"上旬", "中旬", "下旬"}[progressTenDay-'1'] : "");
	}
	
	/**
	 * @return the progress
	 */
	public String getProgress() {
		return progress;
	}
	/**
	 * @param progress the progress to set
	 */
	public void setProgress(String progress) {
		this.progress = progress;
	}
	/**
	 * @return the progressMonth
	 */
	public int getProgressMonth() {
		return progressMonth;
	}
	/**
	 * @param progressMonth the progressMonth to set
	 */
	public void setProgressMonth(int progressMonth) {
		this.progressMonth = progressMonth;
	}
	/**
	 * @return the progressYear
	 */
	public int getProgressYear() {
		return progressYear;
	}
	/**
	 * @param progressYear the progressYear to set
	 */
	public void setProgressYear(int progressYear) {
		this.progressYear = progressYear;
	}
	/**
	 * @return the plan
	 */
	public String getPlan() {
		return plan;
	}
	/**
	 * @param plan the plan to set
	 */
	public void setPlan(String plan) {
		this.plan = plan;
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
	 * @return the progressTenDay
	 */
	public char getProgressTenDay() {
		return progressTenDay;
	}
	/**
	 * @param progressTenDay the progressTenDay to set
	 */
	public void setProgressTenDay(char progressTenDay) {
		this.progressTenDay = progressTenDay;
	}
}