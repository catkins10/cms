package com.yuanluesoft.cms.siteresource.report.model.ensurereport;

import com.yuanluesoft.cms.siteresource.report.pojo.EnsureColumnConfig;

/**
 * 
 * @author linchuan
 *
 */
public class ColumnStat {
	private EnsureColumnConfig ensureColumnConfig; //栏目配置
	private int issueStat; //发布统计,按信息量计分时有效
	private double average; //同类单位平均值,按信息量计分时有效
	private int captureStat; //抓取统计,按信息量计分时有效
	private double captureAverage; //同类单位抓取平均值,按信息量计分时有效
	private int issueColumnNumber; //有发布信息的栏目数,按维护栏目数计分时有效
	private int ensureColumnNumber; //需要保障的栏目数,按维护栏目数计分时有效
	private double score; //得分
	
	/**
	 * @return the average
	 */
	public double getAverage() {
		return average;
	}
	/**
	 * @param average the average to set
	 */
	public void setAverage(double average) {
		this.average = average;
	}
	/**
	 * @return the ensureColumnConfig
	 */
	public EnsureColumnConfig getEnsureColumnConfig() {
		return ensureColumnConfig;
	}
	/**
	 * @param ensureColumnConfig the ensureColumnConfig to set
	 */
	public void setEnsureColumnConfig(EnsureColumnConfig ensureColumnConfig) {
		this.ensureColumnConfig = ensureColumnConfig;
	}
	/**
	 * @return the ensureColumnNumber
	 */
	public int getEnsureColumnNumber() {
		return ensureColumnNumber;
	}
	/**
	 * @param ensureColumnNumber the ensureColumnNumber to set
	 */
	public void setEnsureColumnNumber(int ensureColumnNumber) {
		this.ensureColumnNumber = ensureColumnNumber;
	}
	/**
	 * @return the issueColumnNumber
	 */
	public int getIssueColumnNumber() {
		return issueColumnNumber;
	}
	/**
	 * @param issueColumnNumber the issueColumnNumber to set
	 */
	public void setIssueColumnNumber(int issueColumnNumber) {
		this.issueColumnNumber = issueColumnNumber;
	}
	/**
	 * @return the issueStat
	 */
	public int getIssueStat() {
		return issueStat;
	}
	/**
	 * @param issueStat the issueStat to set
	 */
	public void setIssueStat(int issueStat) {
		this.issueStat = issueStat;
	}
	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}
	/**
	 * @return the captureAverage
	 */
	public double getCaptureAverage() {
		return captureAverage;
	}
	/**
	 * @param captureAverage the captureAverage to set
	 */
	public void setCaptureAverage(double captureAverage) {
		this.captureAverage = captureAverage;
	}
	/**
	 * @return the captureStat
	 */
	public int getCaptureStat() {
		return captureStat;
	}
	/**
	 * @param captureStat the captureStat to set
	 */
	public void setCaptureStat(int captureStat) {
		this.captureStat = captureStat;
	}
}