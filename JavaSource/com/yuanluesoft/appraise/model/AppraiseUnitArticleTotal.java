package com.yuanluesoft.appraise.model;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 参评单位文章发布统计
 * @author linchuan
 *
 */
public class AppraiseUnitArticleTotal extends Record {
	private long unitId; //单位ID
	private String unitName; //单位名称
	private String category; //分类
	private int createTotal; //上报数量
	private int issueTotal; //采用数量
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the createTotal
	 */
	public int getCreateTotal() {
		return createTotal;
	}
	/**
	 * @param createTotal the createTotal to set
	 */
	public void setCreateTotal(int createTotal) {
		this.createTotal = createTotal;
	}
	/**
	 * @return the issueTotal
	 */
	public int getIssueTotal() {
		return issueTotal;
	}
	/**
	 * @param issueTotal the issueTotal to set
	 */
	public void setIssueTotal(int issueTotal) {
		this.issueTotal = issueTotal;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
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
}