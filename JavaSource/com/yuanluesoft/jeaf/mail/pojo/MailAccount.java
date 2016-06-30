package com.yuanluesoft.jeaf.mail.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 邮件帐号(mail_account)
 * @author linchuan
 *
 */
public class MailAccount extends Record {
	private long orgId; //单位ID
	private String orgName; //单位名称
	private String mailAddress; //邮箱
	private String name; //名称
	private String smtpHost; //SMTP主机
	private int smtpPort; //SMTP端口
	private String smtpUserName; //SMTP用户名
	private String smtpPassword; //SMTP密码
	
	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}
	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the smtpHost
	 */
	public String getSmtpHost() {
		return smtpHost;
	}
	/**
	 * @param smtpHost the smtpHost to set
	 */
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	/**
	 * @return the smtpPassword
	 */
	public String getSmtpPassword() {
		return smtpPassword;
	}
	/**
	 * @param smtpPassword the smtpPassword to set
	 */
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}
	/**
	 * @return the smtpPort
	 */
	public int getSmtpPort() {
		return smtpPort;
	}
	/**
	 * @param smtpPort the smtpPort to set
	 */
	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}
	/**
	 * @return the smtpUserName
	 */
	public String getSmtpUserName() {
		return smtpUserName;
	}
	/**
	 * @param smtpUserName the smtpUserName to set
	 */
	public void setSmtpUserName(String smtpUserName) {
		this.smtpUserName = smtpUserName;
	}
}