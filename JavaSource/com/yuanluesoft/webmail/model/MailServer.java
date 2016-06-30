package com.yuanluesoft.webmail.model;

import com.yuanluesoft.webmail.service.mailservice.MailService;

/**
 * 邮件服务器
 * @author linchuan
 *
 */
public class MailServer {
	private MailService mailService; //使用的邮件服务
	private String mailDomain; //邮件域名
    private String serverHost; //邮件服务器主机名
    private int sendPort; //邮件服务器发送端口
    private int receivePort; //邮件服务器接收端口
    private int managePort; //邮件服务器管理端口
    private String managerName; //管理员帐号
    private String managerPassword; //管理员密码
    
	/**
	 * @return the managerPassword
	 */
	public String getManagerPassword() {
		return managerPassword;
	}
	/**
	 * @param managerPassword the managerPassword to set
	 */
	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}
	/**
	 * @return the mailDomain
	 */
	public String getMailDomain() {
		return mailDomain;
	}
	/**
	 * @param mailDomain the mailDomain to set
	 */
	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}
	/**
	 * @return the managePort
	 */
	public int getManagePort() {
		return managePort;
	}
	/**
	 * @param managePort the managePort to set
	 */
	public void setManagePort(int managePort) {
		this.managePort = managePort;
	}
	/**
	 * @return the managerName
	 */
	public String getManagerName() {
		return managerName;
	}
	/**
	 * @param managerName the managerName to set
	 */
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	/**
	 * @return the receivePort
	 */
	public int getReceivePort() {
		return receivePort;
	}
	/**
	 * @param receivePort the receivePort to set
	 */
	public void setReceivePort(int receivePort) {
		this.receivePort = receivePort;
	}
	/**
	 * @return the sendPort
	 */
	public int getSendPort() {
		return sendPort;
	}
	/**
	 * @param sendPort the sendPort to set
	 */
	public void setSendPort(int sendPort) {
		this.sendPort = sendPort;
	}
	/**
	 * @return the serverHost
	 */
	public String getServerHost() {
		return serverHost;
	}
	/**
	 * @param serverHost the serverHost to set
	 */
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	/**
	 * @return the mailService
	 */
	public MailService getMailService() {
		return mailService;
	}
	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
}
