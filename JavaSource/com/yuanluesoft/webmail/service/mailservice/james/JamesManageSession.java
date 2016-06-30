package com.yuanluesoft.webmail.service.mailservice.james;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.yuanluesoft.webmail.model.MailSession;

/**
 * 
 * @author linchuan
 *
 */
public class JamesManageSession extends MailSession {
	private Socket telnetClient; //pop3客户端
	private InputStream inputStream; //输入流,接收服务器返回信息
	private OutputStream outputStream; //输出流,发送命令给服务器

	public JamesManageSession(String mailAddress, String serverHost, int servicePort, String loginName, String password, Socket telnetClient, InputStream inputStream, OutputStream outputStream) {
		super(mailAddress, serverHost, servicePort, loginName, password);
		this.telnetClient = telnetClient;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	/**
	 * @return the telnetClient
	 */
	public Socket getTelnetClient() {
		return telnetClient;
	}

	/**
	 * @param telnetClient the telnetClient to set
	 */
	public void setTelnetClient(Socket telnetClient) {
		this.telnetClient = telnetClient;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * @return the outputStream
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * @param outputStream the outputStream to set
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
}
