package com.yuanluesoft.im.model;

import com.yuanluesoft.im.service.IMService;

/**
 * IM用户
 * @author linchuan
 *
 */
public class IMPerson {
	private long personId; //用户ID
	private byte status; //用户状态
	private String personName; //用户名
	private String portraitURL; //头像
	private long chatId; //对话ID
	
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
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}

	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}

	/**
	 * @return the portraitURL
	 */
	public String getPortraitURL() {
		return portraitURL;
	}

	/**
	 * @param portraitURL the portraitURL to set
	 */
	public void setPortraitURL(String portraitURL) {
		this.portraitURL = portraitURL;
	}

	/**
	 * @return the chatId
	 */
	public long getChatId() {
		return chatId;
	}

	/**
	 * @param chatId the chatId to set
	 */
	public void setChatId(long chatId) {
		this.chatId = chatId;
	}
}