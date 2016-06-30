package com.yuanluesoft.exchange.client.packet;

/**
 * 登录
 * @author linchuan
 *
 */
public class Login extends ExchangePacket {
	private String senderName; //发送方名称

	public Login(String senderName) {
		super();
		this.senderName = senderName;
	}

	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
}