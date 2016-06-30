package com.yuanluesoft.cms.siteresource.report.model.ensurereport;

import java.util.List;

/**
 * 单位统计
 * @author linchuan
 *
 */
public class UnitStat {
	private String unitName; //单位名称
	private List monthStats; //月统计
	private int issueStat; //总的信息量
	private int score; //总分
	
	/**
	 * @return the monthStats
	 */
	public List getMonthStats() {
		return monthStats;
	}
	/**
	 * @param monthStats the monthStats to set
	 */
	public void setMonthStats(List monthStats) {
		this.monthStats = monthStats;
	}
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
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
}