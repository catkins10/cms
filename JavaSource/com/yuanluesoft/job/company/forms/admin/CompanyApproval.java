package com.yuanluesoft.job.company.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 企业审核设置(job_company_approval)
 * @author linchuan
 *
 */
public class CompanyApproval extends ActionForm {
	private int approvalRequired = 1; //是否需要审核
	private String passMailSubject; //审核通过邮件标题
	private String passMailTemplate; //审核通过邮件模板
	private String failedMailSubject; //审核未通过邮件标题
	private String failedMailTemplate; //审核未通过邮件模板
	
	/**
	 * @return the approvalRequired
	 */
	public int getApprovalRequired() {
		return approvalRequired;
	}
	/**
	 * @param approvalRequired the approvalRequired to set
	 */
	public void setApprovalRequired(int approvalRequired) {
		this.approvalRequired = approvalRequired;
	}
	/**
	 * @return the failedMailTemplate
	 */
	public String getFailedMailTemplate() {
		return failedMailTemplate;
	}
	/**
	 * @param failedMailTemplate the failedMailTemplate to set
	 */
	public void setFailedMailTemplate(String failedMailTemplate) {
		this.failedMailTemplate = failedMailTemplate;
	}
	/**
	 * @return the passMailTemplate
	 */
	public String getPassMailTemplate() {
		return passMailTemplate;
	}
	/**
	 * @param passMailTemplate the passMailTemplate to set
	 */
	public void setPassMailTemplate(String passMailTemplate) {
		this.passMailTemplate = passMailTemplate;
	}
	/**
	 * @return the failedMailSubject
	 */
	public String getFailedMailSubject() {
		return failedMailSubject;
	}
	/**
	 * @param failedMailSubject the failedMailSubject to set
	 */
	public void setFailedMailSubject(String failedMailSubject) {
		this.failedMailSubject = failedMailSubject;
	}
	/**
	 * @return the passMailSubject
	 */
	public String getPassMailSubject() {
		return passMailSubject;
	}
	/**
	 * @param passMailSubject the passMailSubject to set
	 */
	public void setPassMailSubject(String passMailSubject) {
		this.passMailSubject = passMailSubject;
	}
}