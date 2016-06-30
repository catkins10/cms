package com.yuanluesoft.archives.forms;

import com.yuanluesoft.jeaf.form.ActionForm;


/**
 * 
 * @author linchuan
 *
 */
public class ArchivesRotentionPeriod extends ActionForm {
	private String rotentionPeriodCode; //期限编号
	private String rotentionPeriod; //保管期限

	/**
	 * @return Returns the rotentionPeriod.
	 */
	public java.lang.String getRotentionPeriod() {
		return rotentionPeriod;
	}
	/**
	 * @param rotentionPeriod The rotentionPeriod to set.
	 */
	public void setRotentionPeriod(java.lang.String rotentionPeriod) {
		this.rotentionPeriod = rotentionPeriod;
	}
	/**
	 * @return Returns the rotentionPeriodCode.
	 */
	public java.lang.String getRotentionPeriodCode() {
		return rotentionPeriodCode;
	}
	/**
	 * @param rotentionPeriodCode The rotentionPeriodCode to set.
	 */
	public void setRotentionPeriodCode(java.lang.String rotentionPeriodCode) {
		this.rotentionPeriodCode = rotentionPeriodCode;
	}
}