/*
 * Created on 2005-9-21
 *
 */
package com.yuanluesoft.j2oa.document.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 公文配置:选项配置(document_option)
 * @author linchuan
 *
 */
public class DocumentOption extends Record {
	private String secureLevel; //秘密等级
	private String secureTerm; //保密期限
	private String priority; //紧急程度
	private String docType; //公文种类

	/**
	 * @return Returns the priority.
	 */
	public java.lang.String getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public void setPriority(java.lang.String priority) {
		this.priority = priority;
	}
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
	 * @return Returns the secureTerm.
	 */
	public java.lang.String getSecureTerm() {
		return secureTerm;
	}
	/**
	 * @param secureTerm The secureTerm to set.
	 */
	public void setSecureTerm(java.lang.String secureTerm) {
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
