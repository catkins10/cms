package com.yuanluesoft.webmail.service.mailservice.standard;

import org.apache.commons.net.pop3.POP3Client;

import com.yuanluesoft.webmail.model.MailSession;

/**
 * 
 * @author linchuan
 *
 */
public class POP3MailSession extends MailSession {
	private POP3Client pop3Client; //pop3客户端

	public POP3MailSession(String mailAddress, String serverHost, int servicePort, String loginName, String password, POP3Client pop3Client) {
		super(mailAddress, serverHost, servicePort, loginName, password);
		this.pop3Client = pop3Client;
	}

	/**
	 * @return the pop3Client
	 */
	public POP3Client getPop3Client() {
		return pop3Client;
	}

	/**
	 * @param pop3Client the pop3Client to set
	 */
	public void setPop3Client(POP3Client pop3Client) {
		this.pop3Client = pop3Client;
	}
}