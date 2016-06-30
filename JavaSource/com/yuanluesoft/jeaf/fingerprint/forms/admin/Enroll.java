package com.yuanluesoft.jeaf.fingerprint.forms.admin;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Enroll extends ActionForm {
	private long personId; //用户ID
	private String personName; //用户名
	private String finger; //手指
	private String template; //指纹数据
	private Timestamp created; //采集时间
	
	//扩展属性
	private boolean selfEnroll; //是否采集自己的指纹
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the finger
	 */
	public String getFinger() {
		return finger;
	}
	/**
	 * @param finger the finger to set
	 */
	public void setFinger(String finger) {
		this.finger = finger;
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
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}
	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
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
	/**
	 * @return the selfEnroll
	 */
	public boolean isSelfEnroll() {
		return selfEnroll;
	}
	/**
	 * @param selfEnroll the selfEnroll to set
	 */
	public void setSelfEnroll(boolean selfEnroll) {
		this.selfEnroll = selfEnroll;
	}
}