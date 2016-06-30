package com.yuanluesoft.cms.sitemanage.model;

/**
 * 发布统计
 * @author linchuan
 *
 */
public class IssueTotal {
	private String unitName; //单位名称
	private int issueCount; //发布数量
	
	/**
	 * @return the issueCount
	 */
	public int getIssueCount() {
		return issueCount;
	}
	/**
	 * @param issueCount the issueCount to set
	 */
	public void setIssueCount(int issueCount) {
		this.issueCount = issueCount;
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