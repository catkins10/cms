package com.yuanluesoft.job.company.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class MailTemplate extends ActionForm {
	private long companyId; //企业ID
	private String invitationMailSubject; //面试邀请函邮件标题
	private String invitationMailTemplate; //面试邀请函邮件模板
	private String offerMailSubject; //录用通知书邮件标题
	private String offerMailTemplate; //录用通知书邮件模板
	private String pushMailSubject; //职位推送邮件标题
	private String pushMailTemplate; //职位推送邮件模板
	
	/**
	 * @return the companyId
	 */
	public long getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	/**
	 * @return the invitationMailSubject
	 */
	public String getInvitationMailSubject() {
		return invitationMailSubject;
	}
	/**
	 * @param invitationMailSubject the invitationMailSubject to set
	 */
	public void setInvitationMailSubject(String invitationMailSubject) {
		this.invitationMailSubject = invitationMailSubject;
	}
	/**
	 * @return the invitationMailTemplate
	 */
	public String getInvitationMailTemplate() {
		return invitationMailTemplate;
	}
	/**
	 * @param invitationMailTemplate the invitationMailTemplate to set
	 */
	public void setInvitationMailTemplate(String invitationMailTemplate) {
		this.invitationMailTemplate = invitationMailTemplate;
	}
	/**
	 * @return the offerMailSubject
	 */
	public String getOfferMailSubject() {
		return offerMailSubject;
	}
	/**
	 * @param offerMailSubject the offerMailSubject to set
	 */
	public void setOfferMailSubject(String offerMailSubject) {
		this.offerMailSubject = offerMailSubject;
	}
	/**
	 * @return the offerMailTemplate
	 */
	public String getOfferMailTemplate() {
		return offerMailTemplate;
	}
	/**
	 * @param offerMailTemplate the offerMailTemplate to set
	 */
	public void setOfferMailTemplate(String offerMailTemplate) {
		this.offerMailTemplate = offerMailTemplate;
	}
	/**
	 * @return the pushMailSubject
	 */
	public String getPushMailSubject() {
		return pushMailSubject;
	}
	/**
	 * @param pushMailSubject the pushMailSubject to set
	 */
	public void setPushMailSubject(String pushMailSubject) {
		this.pushMailSubject = pushMailSubject;
	}
	/**
	 * @return the pushMailTemplate
	 */
	public String getPushMailTemplate() {
		return pushMailTemplate;
	}
	/**
	 * @param pushMailTemplate the pushMailTemplate to set
	 */
	public void setPushMailTemplate(String pushMailTemplate) {
		this.pushMailTemplate = pushMailTemplate;
	}
}