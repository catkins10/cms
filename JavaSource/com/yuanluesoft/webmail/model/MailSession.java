package com.yuanluesoft.webmail.model;

/**
 * 邮件会话
 * @author linchuan
 *
 */
public class MailSession {
	private String mailAddress; //邮件地址,如linjinyu@yuanluesoft.com
	private String serverHost; //主机名
	private int servicePort; //端口
	private String loginName; //登录用户名
	private String password; //密码
	
	public MailSession(String mailAddress, String serverHost, int servicePort, String loginName, String password) {
		super();
		this.mailAddress = mailAddress;
		this.serverHost = serverHost;
		this.servicePort = servicePort;
		this.loginName = loginName;
		this.password = password;
	}
	
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the servicePort
	 */
	public int getServicePort() {
		return servicePort;
	}
	/**
	 * @param servicePort the servicePort to set
	 */
	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}
}
