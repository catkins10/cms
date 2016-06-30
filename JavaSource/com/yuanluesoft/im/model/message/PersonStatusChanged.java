package com.yuanluesoft.im.model.message;

/**
 * 用户状态变更通知
 * @author linchuan
 *
 */
public class PersonStatusChanged extends Message {
	private long personId; //用户ID
	private String personName; //用户名
	private byte status; //用户状态
	private String portraitURL; //用户头像
	
	public PersonStatusChanged() {
		super();
		setCommand(CMD_PERSON_STATUS_CHANGED);
	}
	
	public PersonStatusChanged(long personId, byte status) {
		super();
		setCommand(CMD_PERSON_STATUS_CHANGED);
		this.personId = personId;
		this.status = status;
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
	 * @param statuc the statuc to set
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
}