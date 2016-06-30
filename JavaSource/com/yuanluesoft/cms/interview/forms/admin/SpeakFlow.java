package com.yuanluesoft.cms.interview.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class SpeakFlow extends ActionForm {
	private long siteId; //站点ID
	private String speakFlow; //发言审核顺序
	private String compereSpeakFlow; //主持人发言审核顺序
	private String guestsSpeakFlow; //嘉宾发言审核顺序
	private String roles; //审核人角色以及主持人和嘉宾
	
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
	 * @return the speakFlow
	 */
	public String getSpeakFlow() {
		return speakFlow;
	}
	/**
	 * @param speakFlow the speakFlow to set
	 */
	public void setSpeakFlow(String speakFlow) {
		this.speakFlow = speakFlow;
	}
	/**
	 * @return the roles
	 */
	public String getRoles() {
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(String roles) {
		this.roles = roles;
	}
	/**
	 * @return the compereSpeakFlow
	 */
	public String getCompereSpeakFlow() {
		return compereSpeakFlow;
	}
	/**
	 * @param compereSpeakFlow the compereSpeakFlow to set
	 */
	public void setCompereSpeakFlow(String compereSpeakFlow) {
		this.compereSpeakFlow = compereSpeakFlow;
	}
	/**
	 * @return the guestsSpeakFlow
	 */
	public String getGuestsSpeakFlow() {
		return guestsSpeakFlow;
	}
	/**
	 * @param guestsSpeakFlow the guestsSpeakFlow to set
	 */
	public void setGuestsSpeakFlow(String guestsSpeakFlow) {
		this.guestsSpeakFlow = guestsSpeakFlow;
	}
}