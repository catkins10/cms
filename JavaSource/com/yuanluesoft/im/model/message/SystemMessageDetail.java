package com.yuanluesoft.im.model.message;

import java.sql.Timestamp;


/**
 * 系统消息通知详细信息,如:待办事项通知
 * @author linchuan
 *
 */
public class SystemMessageDetail extends Message {
	private long systemMessageId; //系统消息ID
	private String senderName; //发送人
	private Timestamp sendTime; //通知时间
	private String content; //内容
	private String webLink; //HTTP链接
	
	public SystemMessageDetail() {
		super();
		setCommand(CMD_SYSTEM_MESSAGE_DETAIL);
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

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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

	/**
	 * @return the sendTime
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the webLink
	 */
	public String getWebLink() {
		return webLink==null || webLink.isEmpty() ? null : "{FINAL}" + webLink;
	}

	/**
	 * @param webLink the webLink to set
	 */
	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}
}