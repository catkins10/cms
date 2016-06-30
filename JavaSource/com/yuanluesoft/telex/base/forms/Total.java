package com.yuanluesoft.telex.base.forms;

import java.sql.Date;
import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Total extends ActionForm {
	private Date beginDate;
	private Date endDate;
	private long fromUnitId; //发电单位ID
	private String fromUnitName; //发电单位名称
	private String[] selectedTelegramLevels; //等级
	private String[] selectedSecurityLevels; //密级

	private List totals; //统计数据列表
	
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
	 * @return the selectedSecurityLevels
	 */
	public String[] getSelectedSecurityLevels() {
		return selectedSecurityLevels;
	}
	/**
	 * @param selectedSecurityLevels the selectedSecurityLevels to set
	 */
	public void setSelectedSecurityLevels(String[] selectedSecurityLevels) {
		this.selectedSecurityLevels = selectedSecurityLevels;
	}
	/**
	 * @return the selectedTelegramLevels
	 */
	public String[] getSelectedTelegramLevels() {
		return selectedTelegramLevels;
	}
	/**
	 * @param selectedTelegramLevels the selectedTelegramLevels to set
	 */
	public void setSelectedTelegramLevels(String[] selectedTelegramLevels) {
		this.selectedTelegramLevels = selectedTelegramLevels;
	}
	/**
	 * @return the totals
	 */
	public List getTotals() {
		return totals;
	}
	/**
	 * @param totals the totals to set
	 */
	public void setTotals(List totals) {
		this.totals = totals;
	}
	/**
	 * @return the fromUnitId
	 */
	public long getFromUnitId() {
		return fromUnitId;
	}
	/**
	 * @param fromUnitId the fromUnitId to set
	 */
	public void setFromUnitId(long fromUnitId) {
		this.fromUnitId = fromUnitId;
	}
	/**
	 * @return the fromUnitName
	 */
	public String getFromUnitName() {
		return fromUnitName;
	}
	/**
	 * @param fromUnitName the fromUnitName to set
	 */
	public void setFromUnitName(String fromUnitName) {
		this.fromUnitName = fromUnitName;
	}
}