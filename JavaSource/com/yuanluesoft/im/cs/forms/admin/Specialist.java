package com.yuanluesoft.im.cs.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Specialist extends ActionForm {
	private long siteId; //站点ID
	private long personId; //用户ID
	private String personName; //用户名
	private String externalName; //对外显示的用户名
	private int maxChat; //最大并发对话数量,超时对话不计算在内
	
	/**
	 * @return the externalName
	 */
	public String getExternalName() {
		return externalName;
	}
	/**
	 * @param externalName the externalName to set
	 */
	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}
	/**
	 * @return the maxChat
	 */
	public int getMaxChat() {
		return maxChat;
	}
	/**
	 * @param maxChat the maxChat to set
	 */
	public void setMaxChat(int maxChat) {
		this.maxChat = maxChat;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
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
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
}