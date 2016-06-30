package com.yuanluesoft.cms.siteresource.report.forms;

import java.sql.Date;
import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ColumnStat extends ActionForm {
	private Date beginDate; //开始日期
	private Date endDate; //结束日期
	private long siteId; //站点ID
	private String siteName; //站点名称
	private String unitIds; //单位ID列表
	private String unitNames; //单位名称列表
	
	private List unitStats; //单位统计列表
	
	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}
	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	/**
	 * @return the unitStats
	 */
	public List getUnitStats() {
		return unitStats;
	}
	/**
	 * @param unitStats the unitStats to set
	 */
	public void setUnitStats(List unitStats) {
		this.unitStats = unitStats;
	}
	/**
	 * @return the unitIds
	 */
	public String getUnitIds() {
		return unitIds;
	}
	/**
	 * @param unitIds the unitIds to set
	 */
	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}
	/**
	 * @return the unitNames
	 */
	public String getUnitNames() {
		return unitNames;
	}
	/**
	 * @param unitNames the unitNames to set
	 */
	public void setUnitNames(String unitNames) {
		this.unitNames = unitNames;
	}
}