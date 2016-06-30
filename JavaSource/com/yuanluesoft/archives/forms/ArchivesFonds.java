package com.yuanluesoft.archives.forms;

import com.yuanluesoft.jeaf.form.ActionForm;


/**
 * 
 * @author linchuan
 *
 */
public class ArchivesFonds extends ActionForm {
	private String fondsName; //全宗名称,单位名称
	private String fondsCode; //全宗号
	
	/**
	 * @return Returns the fondsCode.
	 */
	public java.lang.String getFondsCode() {
		return fondsCode;
	}
	/**
	 * @param fondsCode The fondsCode to set.
	 */
	public void setFondsCode(java.lang.String fondsCode) {
		this.fondsCode = fondsCode;
	}
	/**
	 * @return Returns the fondsName.
	 */
	public java.lang.String getFondsName() {
		return fondsName;
	}
	/**
	 * @param fondsName The fondsName to set.
	 */
	public void setFondsName(java.lang.String fondsName) {
		this.fondsName = fondsName;
	}
}