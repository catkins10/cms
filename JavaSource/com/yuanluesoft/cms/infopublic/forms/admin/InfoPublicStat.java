package com.yuanluesoft.cms.infopublic.forms.admin;

import java.sql.Date;
import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 信息公开统计
 * @author linchuan
 *
 */
public class InfoPublicStat extends ActionForm {
	private Date beginDate; //统计开始时间
	private Date endDate; //统计结束时间
	private String directoryName; //目录
	private long directoryId; //目录ID
	private List infoStats; //第一一级目录信息量和比例
	private List infoCategoryStats; //按分类统计
	private List requestStats; //各种申请形式的数量和比例
	
	/**
	 * @return the infoStats
	 */
	public List getInfoStats() {
		return infoStats;
	}
	/**
	 * @param infoStats the infoStats to set
	 */
	public void setInfoStats(List infoStats) {
		this.infoStats = infoStats;
	}
	/**
	 * @return the requestStats
	 */
	public List getRequestStats() {
		return requestStats;
	}
	/**
	 * @param requestStats the requestStats to set
	 */
	public void setRequestStats(List requestStats) {
		this.requestStats = requestStats;
	}
	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return the directoryName
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * @param directoryName the directoryName to set
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	/**
	 * @return the infoCategoryStats
	 */
	public List getInfoCategoryStats() {
		return infoCategoryStats;
	}
	/**
	 * @param infoCategoryStats the infoCategoryStats to set
	 */
	public void setInfoCategoryStats(List infoCategoryStats) {
		this.infoCategoryStats = infoCategoryStats;
	}
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
}