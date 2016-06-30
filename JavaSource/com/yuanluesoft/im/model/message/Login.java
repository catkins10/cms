package com.yuanluesoft.im.model.message;

/**
 * 客户端登录
 * @author linchuan
 *
 */
public class Login extends Message {
	private String ticket; //钥匙
	private byte status; //状态

	public Login() {
		setCommand(CMD_LOGIN);
	}

	/**
	 * @return the ticket
	 */
	public String getTicket() {
		return ticket;
	}

	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}
}