package com.yuanluesoft.bidding.project.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 投标企业帐户信息完善
 * @author linchuan
 *
 */
public class AccountComplement extends ActionForm {
	private long projectId; //项目ID
	private long signUpId; //报名ID
	private String enterpriseName; //企业名称
	private String enterpriseBank; //企业开户银行
	private String enterpriseAccount; //企业账户
	
	/**
	 * @return the enterpriseAccount
	 */
	public String getEnterpriseAccount() {
		return enterpriseAccount;
	}
	/**
	 * @param enterpriseAccount the enterpriseAccount to set
	 */
	public void setEnterpriseAccount(String enterpriseAccount) {
		this.enterpriseAccount = enterpriseAccount;
	}
	/**
	 * @return the enterpriseBank
	 */
	public String getEnterpriseBank() {
		return enterpriseBank;
	}
	/**
	 * @param enterpriseBank the enterpriseBank to set
	 */
	public void setEnterpriseBank(String enterpriseBank) {
		this.enterpriseBank = enterpriseBank;
	}
	/**
	 * @return the enterpriseName
	 */
	public String getEnterpriseName() {
		return enterpriseName;
	}
	/**
	 * @param enterpriseName the enterpriseName to set
	 */
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	/**
	 * @return the signUpId
	 */
	public long getSignUpId() {
		return signUpId;
	}
	/**
	 * @param signUpId the signUpId to set
	 */
	public void setSignUpId(long signUpId) {
		this.signUpId = signUpId;
	}
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
}