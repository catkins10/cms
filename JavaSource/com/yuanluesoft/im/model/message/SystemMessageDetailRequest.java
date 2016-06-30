package com.yuanluesoft.im.model.message;

/**
 * 请求获取系统消息详细信息,如:待办事项通知
 * @author linchuan
 *
 */
public class SystemMessageDetailRequest extends Message {
	private long systemMessageId; //系统消息ID

	public SystemMessageDetailRequest() {
		super();
		setCommand(CMD_SYSTEM_MESSAGE_DETAIL_REQUEST);
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