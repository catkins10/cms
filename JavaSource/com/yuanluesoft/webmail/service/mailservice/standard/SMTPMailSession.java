package com.yuanluesoft.webmail.service.mailservice.standard;

import org.apache.commons.net.smtp.SMTPClient;

import com.yuanluesoft.webmail.model.MailSession;

/**
 * 
 * @author linchuan
 *
 */
public class SMTPMailSession extends MailSession {
	private SMTPClient smtpClient; //smtp客户端


	public SMTPMailSession(String mailAddress, String serverHost, int servicePort, String loginName, String password, SMTPClient smtpClient) {
		super(mailAddress, serverHost, servicePort, loginName, password);
		this.smtpClient = smtpClient;
	}

	/**
	 * @return the smtpClient
	 */
	public SMTPClient getSmtpClient() {
		return smtpClient;
	}

	/**
	 * @param smtpClient the smtpClient to set
	 */
	public void setSmtpClient(SMTPClient smtpClient) {
		this.smtpClient = smtpClient;
	}
}