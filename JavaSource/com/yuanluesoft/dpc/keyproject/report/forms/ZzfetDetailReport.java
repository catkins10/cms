package com.yuanluesoft.dpc.keyproject.report.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ZzfetDetailReport extends ActionForm {
	private long projectId; //项目ID
	private int year;
	private int month;
	private int keyMonth = 8; //大干150天的起始月份
	private String reportType; //报表类型,项目前期工作台帐/在建、新开工台账
	
	/**
	 * @return the keyMonth
	 */
	public int getKeyMonth() {
		return keyMonth;
	}
	/**
	 * @param keyMonth the keyMonth to set
	 */
	public void setKeyMonth(int keyMonth) {
		this.keyMonth = keyMonth;
	}
	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}
	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
}