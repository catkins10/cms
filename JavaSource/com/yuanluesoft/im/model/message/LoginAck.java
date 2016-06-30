package com.yuanluesoft.im.model.message;

import com.yuanluesoft.im.service.IMService;

/**
 * 
 * @author linchuan
 *
 */
public class LoginAck extends Message {
	private long personId; //用户ID,不输出到客户端
	private byte status; //用户状态
	private String offlineChats; //有留言的对话列表,[chatId]$$[chatPersonNames]$$[isGroupChat]$$[isCustomerService]##...

	public LoginAck() {
		super();
		setCommand(CMD_LOGIN_ACK);
	}
	
	/**
	 * 获取用户状态说明
	 * @return
	 */
	public String getStatusText() {
		try {
			return IMService.IM_PERSON_STATUS_TEXTS[status - IMService.IM_PERSON_STATUS_OFFLINE];
		}
		catch(Exception e) {
			return null;
		}
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

	/**
	 * @return the offlineChats
	 */
	public String getOfflineChats() {
		return offlineChats;
	}

	/**
	 * @param offlineChats the offlineChats to set
	 */
	public void setOfflineChats(String offlineChats) {
		this.offlineChats = offlineChats;
	}

	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}

	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
}