package com.yuanluesoft.im.model.message;

/**
 * 系统消息应答
 * @author linchuan
 *
 */
public class SystemMessageFeedback extends Message {
	private long systemMessageId; //系统消息ID

	public SystemMessageFeedback() {
		super();
		setCommand(CMD_SYSTEM_MESSAGE_FEEDBACK);
	}

	/**
	 * @return the systemMessageId
	 */
	public long getSystemMessageId() {
		return systemMessageId;
	}

	/**
	 * @param systemMessageId the systemMessageId to set
	 */
	public void setSystemMessageId(long systemMessageId) {
		this.systemMessageId = systemMessageId;
	}
}