package com.yuanluesoft.im.model.message;


/**
 * 系统消息通知,如:待办事项通知
 * @author linchuan
 *
 */
public class SystemMessage extends Message {
	private long systemMessageId; //系统消息ID

	public SystemMessage() {
		super();
		setCommand(CMD_SYSTEM_MESSAGE);
	}
	
	public SystemMessage(long systemMessageId) {
		super();
		setCommand(CMD_SYSTEM_MESSAGE);
		this.systemMessageId = systemMessageId;
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