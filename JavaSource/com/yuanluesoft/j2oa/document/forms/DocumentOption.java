/*
 * Created on 2005-9-21
 *
 */
package com.yuanluesoft.j2oa.document.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class DocumentOption extends ActionForm {
	private String secureLevel; //秘密等级
	private String secureTerm; //保密期限
	private String priority; //紧急程度
	private String docType; //公文种类
	
	/**
	 * @return Returns the priority.
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	/**
	 * @return Returns the secureLevel.
	 */
	public String getSecureLevel() {
		return secureLevel;
	}
	/**
	 * @param secureLevel The secureLevel to set.
	 */
	public void setSecureLevel(String secureLevel) {
		this.secureLevel = secureLevel;
	}
	/**
	 * @return Returns the secureTerm.
	 */
	public String getSecureTerm() {
		return secureTerm;
	}
	/**
	 * @param secureTerm The secureTerm to set.
	 */
	public void setSecureTerm(String secureTerm) {
		this.secureTerm = secureTerm;
	}
	/**
	 * @return the docType
	 */
	public String getDocType() {
		return docType;
	}
	/**
	 * @param docType the docType to set
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}
}
