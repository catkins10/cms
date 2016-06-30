/*
 * Created on 2005-9-14
 *
 */
package com.yuanluesoft.jeaf.opinionmanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 用户常用意见(opinion_often_use)
 * @author linchuan
 *
 */
public class OftenUseOpinion extends Record {
	private long personId; //填写人ID
	private String applicationName; //应用名称
	private String opinion; //意见内容
	
	/**
	 * @return Returns the opinion.
	 */
	public java.lang.String getOpinion() {
		return opinion;
	}
	/**
	 * @param opinion The opinion to set.
	 */
	public void setOpinion(java.lang.String opinion) {
		this.opinion = opinion;
	}
	/**
	 * @return Returns the personId.
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return Returns the applicationName.
	 */
	public java.lang.String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName The applicationName to set.
	 */
	public void setApplicationName(java.lang.String applicationName) {
		this.applicationName = applicationName;
	}
}
