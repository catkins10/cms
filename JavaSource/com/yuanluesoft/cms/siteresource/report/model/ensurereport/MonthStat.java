package com.yuanluesoft.cms.siteresource.report.model.ensurereport;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class MonthStat {
	private int issueStat; //信息量
	private int score; //分数
	private List columnStats; //栏目统计列表
	
	/**
	 * @return the articleNumber
	 */
	public int getIssueStat() {
		return issueStat;
	}
	/**
	 * @param articleNumber the articleNumber to set
	 */
	public void setIssueStat(int articleNumber) {
		this.issueStat = articleNumber;
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
	 * @return the columnStats
	 */
	public List getColumnStats() {
		return columnStats;
	}
	/**
	 * @param columnStats the columnStats to set
	 */
	public void setColumnStats(List columnStats) {
		this.columnStats = columnStats;
	}
}