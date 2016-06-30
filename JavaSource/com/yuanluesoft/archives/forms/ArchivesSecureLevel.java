package com.yuanluesoft.archives.forms;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;


/**
 * 
 * @author linchuan
 *
 */
public class ArchivesSecureLevel extends ActionForm {
	private String secureLevelCode; //密级编号
	private String secureLevel; //密级
	//对应的访问者列表
	private RecordVisitorList visitors = new RecordVisitorList();


	/**
	 * @return Returns the secureLevel.
	 */
	public java.lang.String getSecureLevel() {
		return secureLevel;
	}
	/**
	 * @param secureLevel The secureLevel to set.
	 */
	public void setSecureLevel(java.lang.String secureLevel) {
		this.secureLevel = secureLevel;
	}
	/**
	 * @return Returns the secureLevelCode.
	 */
	public java.lang.String getSecureLevelCode() {
		return secureLevelCode;
	}
	/**
	 * @param secureLevelCode The secureLevelCode to set.
	 */
	public void setSecureLevelCode(java.lang.String secureLevelCode) {
		this.secureLevelCode = secureLevelCode;
	}
	/**
	 * @return Returns the visitors.
	 */
	public RecordVisitorList getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors The visitors to set.
	 */
	public void setVisitors(RecordVisitorList visitors) {
		this.visitors = visitors;
	}
}